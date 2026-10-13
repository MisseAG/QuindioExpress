package model;

import java.util.*;
import util.SimpleLinkedList;

/**
 * Centro de distribución de QuindíoExpress. Único punto de escritura del sistema:
 * cada registro, despacho, asignación y entrega pasa por aquí
 * para que las estructuras que comparten los mismos objetos Paquete
 * se mantengan sincronizadas
 * las que comparten los mismos objetos Paquete.
 * Estados de un paquete:
 *   registrado -> despachado (salió de una cola) -> asignado (tiene repartidor) -> entregado
 *  -
 *  - registro nunca borra: valida códigos únicos de toda la jornada y conserva el orden de registro.
 *  - despachados evita que un paquete salga dos veces.
 *  - porMunicipio contiene los paquetes pendientes, es decir, registrados y aún no entregados.
 */

public class CentroDistribucion {



    // --- Paquetes ---
    private final LinkedHashMap<String, Paquete> registro; // codigo -> paquete, en orden de registro
    private final Queue<Paquete> colaLlegada; // despacho por orden de llegada (FIFO)
    private final PriorityQueue<Paquete> colaPrioridad; // despacho prioritario
    private final Map<String, List<Paquete>> porMunicipio; // municipio -> pendientes (registrados y no entregados)
    private final Set<String> municipiosConPaquetes; // sin repetidos sin orden
    private final TreeSet<String> municipiosOrdenados; // sin repetidos, orden alfabético
    private final Set<String> despachados; // códigos que salieron de una cola
    private final Set<String> entregados; // códigos de ya entregados
    private final SimpleLinkedList<Paquete> historial; // orden de entrega

    // --- Repartidores ---
    private final Map<String, Repartidor> repartidores; // identificación -> repartidor
    private final Map<String, Queue<Repartidor>> disponiblesPorZona; //zona -> repartidores disponibles en esa zona
    private final Map<String,Repartidor> paquetesAsignados; // clave: codigo del paquete, valor: repartidor asignado;

    private final MunicipiosDisponibles municipiosDisponibles;

    public CentroDistribucion() {
        this.registro = new LinkedHashMap<>();
        this.colaLlegada = new LinkedList<>();
        this.colaPrioridad = new PriorityQueue<>(ComparadoresPaquete.DESPACHO);
        this.porMunicipio = new HashMap<>();
        this.municipiosConPaquetes = new HashSet<>();
        this.municipiosOrdenados = new TreeSet<>();
        this.despachados = new HashSet<>();
        this.entregados = new HashSet<>();
        this.historial = new SimpleLinkedList<>();
        this.repartidores = new HashMap<>();
        this.disponiblesPorZona = new HashMap<>();
        this.paquetesAsignados= new HashMap<>();
        this.municipiosDisponibles = new MunicipiosDisponibles();
    }


    // Registro Y Consulta

    /** Motivo por el que se rechazaría el paquete, o null si cumple todas las reglas. */
    private String motivoRechazo(Paquete p) {
        if (p == null) return "el paquete es nulo";
        if (p.getCodigo() == null || p.getCodigo().isBlank()) return "el código es nulo o vacío";
        if (registro.containsKey(p.getCodigo())) return "el código " + p.getCodigo() + " ya está registrado";
        if (p.getDestino() == null || p.getDestino().isBlank()) return "el destino es nulo o vacío";
        if (!municipiosDisponibles.verificarMunicipio(p.getDestino())) return "el destino '" + p.getDestino() + "' no es un municipio válido";
        if (!(p.getPeso() > 0)) return "el peso debe ser mayor que cero";
        if (p.getPrioridad() < 1 || p.getPrioridad() > 5) return "la prioridad debe estar entre 1 y 5";
        if (p.getTiempoEstimado() <= 0) return "el tiempo estimado debe ser mayor que cero";
        return null;
    }

    /** Valida y registra el paquete en todas las estructuras. Validación O(1); el registro completo O(log n). */
    public boolean registrarPaquete(Paquete p) {
        String motivo = motivoRechazo(p);
        if (motivo != null) {
            System.out.println("Paquete rechazado: " + motivo + ".");
            return false;
        }
        registro.put(p.getCodigo(), p);
        colaLlegada.add(p);
        colaPrioridad.add(p);
        porMunicipio.computeIfAbsent(p.getDestino(), k -> new ArrayList<>()).add(p);
        municipiosConPaquetes.add(p.getDestino());
        municipiosOrdenados.add(p.getDestino());
        return true;
    }

    /** Consulta por código en O(1). Incluye paquetes ya despachados o entregados. */
    public Paquete consultarPaquete(String codigo) {
        return codigo == null ? null : registro.get(codigo);
    }

    // ======================= REPARTIDORES =======================

    public boolean registrarRepartidor(Repartidor r) {
        if (r == null || r.getIdentificacion() == null || r.getIdentificacion().isBlank()) {
            System.out.println("Repartidor rechazado: datos inválidos.");
            return false;
        }
        if (repartidores.containsKey(r.getIdentificacion())) {
            System.out.println("Repartidor rechazado: la identificación " + r.getIdentificacion() + " ya existe.");
            return false;
        }
        if (!municipiosDisponibles.verificarMunicipio(r.getZona())) {
            System.out.println("Repartidor rechazado: la zona '" + r.getZona() + "' no es un municipio válido.");
            return false;
        }
        repartidores.put(r.getIdentificacion(), r);
        if (r.isDisponible()) {
            disponiblesPorZona.computeIfAbsent(r.getZona(), k -> new LinkedList<>()).add(r);
        }
        return true;
    }

    public Repartidor consultarRepartidor(String identificacion) {
        return identificacion == null ? null : repartidores.get(identificacion);
    }

    /**
     * Único camino para cambiar la disponibilidad de un repartidor, para que la cola de
     * disponibles de su zona nunca contradiga el booleano. Un repartidor con un paquete
     * asignado no puede marcarse disponible hasta registrar la entrega.
     */
    public boolean cambiarDisponibilidad(String identificacion, boolean disponible) {
        Repartidor r = consultarRepartidor(identificacion);
        if (r == null) {
            System.out.println("No existe el repartidor " + identificacion + ".");
            return false;
        }
        if (r.isDisponible() == disponible) {
            return true;
        }
        if (disponible) {
            if (paquetesAsignados.containsValue(r)) {      // O(p), operación administrativa poco frecuente
                System.out.println(r.getNombre() + " tiene un paquete asignado; registre la entrega primero.");
                return false;
            }
            liberar(r);
        } else {
            r.setDisponible(false);
            Queue<Repartidor> cola = disponiblesPorZona.get(r.getZona());
            if (cola != null) {
                cola.remove(r);                           // O(r de la zona), muy pequeño
            }
        }
        return true;
    }

    private void liberar(Repartidor r) {
        r.setDisponible(true);
        disponiblesPorZona.computeIfAbsent(r.getZona(), k -> new LinkedList<>()).add(r);
    }

    // ======================= DESPACHO =======================

    /** Siguiente paquete por orden de llegada. O(1) amortizado. */
    public Paquete despacharPorLlegada() {
        return extraer(colaLlegada);
    }

    /** Siguiente paquete por prioridad -> tiempo -> código. O(log n) amortizado. */
    public Paquete despacharPorPrioridad() {
        return extraer(colaPrioridad);
    }

    /**
     * Saca de la cola hasta encontrar un paquete que no haya salido ya por la otra cola.
     * despachados.add retorna false si el código ya estaba: ese es un "fantasma" y se descarta.
     */
    private Paquete extraer(Queue<Paquete> cola) {
        Paquete p;
        while ((p = cola.poll()) != null) {
            if (despachados.add(p.getCodigo())) {
                return p;
            }
        }
        System.out.println("No hay paquetes pendientes por despachar.");
        return null;
    }

    // ======================= ASIGNACIÓN Y ENTREGA =======================

    /**
     * Asigna un paquete ya despachado a un repartidor disponible de la zona igual a su destino.
     * Si no hay repartidor, el paquete sigue despachado y sin asignar: se puede reintentar.
     */
    public Repartidor asignarPaquete(Paquete p) {
        Paquete real = (p == null) ? null : registro.get(p.getCodigo());
        if (real == null) {
            System.out.println("El paquete no existe en el registro.");
            return null;
        }
        String codigo = real.getCodigo();
        if (!despachados.contains(codigo)) {
            System.out.println("El paquete " + codigo + " debe despacharse antes de asignarse.");
            return null;
        }
        if (entregados.contains(codigo) || paquetesAsignados.containsKey(codigo)) {
            System.out.println("El paquete " + codigo + " ya fue asignado o entregado.");
            return null;
        }
        Queue<Repartidor> cola = disponiblesPorZona.get(real.getDestino());
        if (cola == null || cola.isEmpty()) {
            System.out.println("No hay repartidores disponibles en la zona: " + real.getDestino());
            return null;
        }
        Repartidor r = cola.poll();
        r.setDisponible(false);
        paquetesAsignados.put(codigo, r);
        return r;
    }

    /** Registra la entrega: agrega al historial, saca de pendientes y libera al repartidor. */
    public boolean registrarEntrega(String codigo) {
        Paquete p = consultarPaquete(codigo);
        if (p == null) {
            System.out.println("El paquete " + codigo + " no existe.");
            return false;
        }
        if (entregados.contains(codigo)) {
            System.out.println("El paquete " + codigo + " ya fue entregado.");
            return false;
        }
        Repartidor r = paquetesAsignados.remove(codigo);
        if (r == null) {
            System.out.println("El paquete " + codigo + " no tiene repartidor asignado.");
            return false;
        }
        historial.addFinal(p);                              // O(1)
        entregados.add(codigo);
        List<Paquete> pendientes = porMunicipio.get(p.getDestino());
        if (pendientes != null) {
            pendientes.remove(p);                           // O(k), k = pendientes del municipio
            if (pendientes.isEmpty()) {
                porMunicipio.remove(p.getDestino());
            }
        }
        liberar(r);
        return true;
    }

    // ======================= REPORTES =======================

    /** Municipios con al menos un paquete registrado, sin repetidos (HashSet, sin orden). */
    public Set<String> municipiosConPaquetes() {
        return Collections.unmodifiableSet(municipiosConPaquetes);
    }

    /** Los mismos municipios, sin repetidos y en orden alfabético (TreeSet). */
    public Set<String> municipiosAlfabeticos() {
        return Collections.unmodifiableSet(municipiosOrdenados);
    }

    /** Paquetes pendientes (no entregados) de un municipio, en orden de llegada. */
    public List<Paquete> pendientesDeMunicipio(String municipio) {
        return List.copyOf(porMunicipio.getOrDefault(municipio, List.of()));
    }

    /** Cantidad de paquetes pendientes por municipio, en orden alfabético. */
    public Map<String, Integer> cargaPorZona() {
        Map<String, Integer> carga = new TreeMap<>();
        porMunicipio.forEach((municipio, lista) -> carga.put(municipio, lista.size()));
        return carga;
    }

    /** Copia de todos los paquetes en el orden exacto de registro (auditoría). */
    public List<Paquete> paquetesEnOrdenDeRegistro() {
        return new ArrayList<>(registro.values());
    }

    /** Copia ordenada con un criterio de ComparadoresPaquete; no altera el orden de registro. O(n log n). */
    public List<Paquete> paquetesOrdenados(Comparator<Paquete> criterio) {
        List<Paquete> copia = paquetesEnOrdenDeRegistro();
        copia.sort(criterio);
        return copia;
    }

    /** Copia ordenada por el orden natural (código ascendente). Es la precondición de la búsqueda binaria. */
    public List<Paquete> paquetesPorOrdenNatural() {
        List<Paquete> copia = paquetesEnOrdenDeRegistro();
        Collections.sort(copia);
        return copia;
    }

    /** Historial de entregas se recorre solo con Iterator; no expone la lista ni sus nodos. */
    public Iterable<Paquete> getHistorial() {
        return historial;
    }

    public int totalRegistrados() {
        return registro.size();
    }

    public int totalEntregados() {
        return historial.tamano();
    }

    public int totalPendientes() {
        return registro.size() - historial.tamano();
    }
}