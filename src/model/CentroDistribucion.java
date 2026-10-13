package model;

import java.util.*;
import util.SimpleLinkedList;

public class CentroDistribucion {

    private static final CentroDistribucion INSTANCIA = new CentroDistribucion();

    private final LinkedHashMap<String, Paquete> registro;
    private final Queue<Paquete> colaLlegada;
    private final PriorityQueue<Paquete> colaPrioridad;
    private final Map<String, List<Paquete>> porMunicipio;

    private final Set<String> despachados;
    private final SimpleLinkedList<Paquete> historial;

    private final Map<String, Repartidor> repartidores;
    private final Map<String, Queue<Repartidor>> disponiblesPorZona; //la clave es la zona ¿no?
    private final Map<String,Repartidor> paquetesAsignados; //esto es para que el penultimo metodo sirva

    private final MunicipiosDisponibles municipiosDisponibles;

    private CentroDistribucion() {
        this.registro = new LinkedHashMap<>();
        this.colaLlegada = new LinkedList<>();
        this.colaPrioridad = new PriorityQueue<>();
        this.porMunicipio = new HashMap<>();
        this.despachados = new HashSet<>();
        this.historial = new SimpleLinkedList<>();
        this.repartidores = new HashMap<>();
        this.disponiblesPorZona = new HashMap<>();
        this.municipiosDisponibles = new MunicipiosDisponibles();
        this.paquetesAsignados= new HashMap<>();
    }

    public static CentroDistribucion getInstancia() {
        return INSTANCIA;
    }

    public boolean registrarPaquete(Paquete p) {
        if (p == null) {
            return false;
        }
        if (registro.containsKey(p.getCodigo()) || p.getDestino() == null || p.getDestino().isEmpty() || p.getPeso() <= 0 ||
                p.getPrioridad() < 1 || p.getPrioridad() > 5 || p.getTiempoEstimado() <= 0 ||
                !municipiosDisponibles.verificarMunicipio(p.getDestino())) {
            return false;
        }
        registro.put(p.getCodigo(), p);
        colaLlegada.add(p);
        colaPrioridad.add(p);
        // agrupación por municipio con computeIfAbsent para crear dinámicamente
        // la lista de paquetes de ese municipio si aún no existía, me tocó investigar con IA
        porMunicipio.computeIfAbsent(p.getDestino(), k -> new ArrayList<>()).add(p);
        return true;
    }

    public Paquete consultarPaquete(String codigo) {
        if(registro.isEmpty() || codigo.isEmpty()){
            System.out.println("La lista de paquetes está vacia. ");
            return null;
        }else{return registro.get(codigo);}
    }

    public void registrarRepartidor(Repartidor r) {
        if(repartidores.containsKey(r.getIdentificacion())){
            System.out.println("El repartidor ya existe en la colección");
        }else{repartidores.put(r.getIdentificacion(), r);
            if(r.isDisponible() == true){
                disponiblesPorZona.computeIfAbsent(r.getZona(), k -> new LinkedList<>()).add(r);
            }}
    }

    public Paquete despacharPorLlegada() {
        if(registro.isEmpty() || colaLlegada.isEmpty()){
            System.out.println("Las colecciones están vacias.  ");
            return null;
        }else{
            Paquete paqueteADespachar= colaLlegada.poll();
            registro.remove(paqueteADespachar.getCodigo());
            colaPrioridad.remove(paqueteADespachar);
            despachados.add(paqueteADespachar.getCodigo());
            historial.addFinal(paqueteADespachar);
            List<Paquete> listaMunicipio = porMunicipio.get(paqueteADespachar.getDestino());
            if (listaMunicipio != null) {
                listaMunicipio.remove(paqueteADespachar);
            }
            //porMunicipio.remove(paqueteADespachar.getDestino());
            return paqueteADespachar;
        }//extraer de colaLlegada (FIFO) y registrar en despachados
    }

    public Paquete despacharPorPrioridad() {
        if(registro.isEmpty() || colaPrioridad.isEmpty()){
            System.out.println("Las colecciones están vacias.  ");
            return null;
        }else{
            Paquete paqueteADespachar= colaPrioridad.poll();


            registro.remove(paqueteADespachar.getCodigo());
            colaPrioridad.remove(paqueteADespachar);
            despachados.add(paqueteADespachar.getCodigo());
            historial.addFinal(paqueteADespachar);
            if(porMunicipio.containsKey(paqueteADespachar)){
                porMunicipio.remove(paqueteADespachar);}
            return paqueteADespachar;
        }
        //extraer de colaPrioridad (según compareTo) y registrar en despachados
    }

    public boolean registrarEntrega(String codigo) {
        if (codigo == null || !despachados.contains(codigo)) {
            System.out.println("El paquete no existe o no ha sido despachado todavía.");
            return false;
        }
        // Para poder agregarlo al historial en este punto, necesitamos el objeto Paquete.
        // Como ya fue removido de "registro" al despachar, podemos buscarlo en nuestro "historial"
        Repartidor r = paquetesAsignados.get(codigo);
        if (r == null) {
            System.out.println("No se encontró el repartidor asociado a este paquete.");
            return false;
        }
        r.setDisponible(true);
        disponiblesPorZona.computeIfAbsent(r.getZona(), k -> new LinkedList<>()).add(r);
        paquetesAsignados.remove(codigo);
        System.out.println("el Repartidor " + r.getNombre() + " liberado con éxito y devuelto a la zona " + r.getZona());
        System.out.println("Entrega del paquete " + codigo + " registrada correctamente.");
        return true;
    }// hay que liberar al repartidor (volver a disponiblesPorZona)
    //validar que el paquete exista y esté despachado
    // agregar el paquete al historial

    public Repartidor asignarPaquete(Paquete p) {
        if (!registro.containsKey(p.getCodigo())) {
            System.out.println("El paquete no existe en el registro.");
            return null;
        }
        Queue<Repartidor> colaRepartidoresZona = disponiblesPorZona.get(p.getDestino());
        if (colaRepartidoresZona == null || colaRepartidoresZona.isEmpty()) {
            System.out.println("No hay repartidores disponibles en la zona: " + p.getDestino());
            return null;
        }
        while(!colaRepartidoresZona.isEmpty()){
        Repartidor repartidorAsignado = colaRepartidoresZona.poll();
        if (repartidorAsignado != null && repartidorAsignado.isDisponible()) {
            repartidorAsignado.setDisponible(false);
            System.out.println("Paquete " + p.getCodigo() + "fue asignado al repartidor " + repartidorAsignado.getNombre());
            paquetesAsignados.put(p.getCodigo(), repartidorAsignado);
            return repartidorAsignado;
        }}
        System.out.println("No se encontraron repartidores disponibles en la zona");
        return null;
    }
}