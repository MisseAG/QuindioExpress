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
    private final Map<String, Queue<Repartidor>> disponiblesPorZona;

    private final MunicipiosDisponibles municipiosDisponibles;

    private CentroDistribucion() {
        this.registro = new LinkedHashMap<>();
        this.colaLlegada = new LinkedList<>();
        this.colaPrioridad = new PriorityQueue<>(ComparadoresPaquete.DESPACHO);
        this.porMunicipio = new HashMap<>();
        this.despachados = new HashSet<>();

        this.historial = new SimpleLinkedList<>();

        this.repartidores = new HashMap<>();
        this.disponiblesPorZona = new HashMap<>();

        this.municipiosDisponibles = new MunicipiosDisponibles();
    }

    public static CentroDistribucion getInstancia() {
        return INSTANCIA;
    }

    public boolean registrarPaquete(Paquete p) {
        // TODO: validar municipio con municipiosDisponibles.verificarMunicipio(...)
        // TODO: evitar duplicados en registro
        // TODO: insertar en registro, colaLlegada, colaPrioridad y porMunicipio
        return false;
    }

    public Paquete consultarPaquete(String codigo) {
        // TODO: buscar en registro por código
        return null;
    }

    public boolean registrarRepartidor(Repartidor r) {
        // TODO: validar duplicado por identificación
        // TODO: agregar a repartidores y a disponiblesPorZona según su zona
        return false;
    }

    public Paquete despacharPorLlegada() {
        // TODO: extraer de colaLlegada (FIFO) y registrar en despachados
        return null;
    }

    public Paquete despacharPorPrioridad() {
        // TODO: extraer de colaPrioridad (según compareTo) y registrar en despachados
        return null;
    }

    public boolean registrarEntrega(String codigo) {
        // TODO: validar que el paquete exista y esté despachado
        // TODO: agregar el paquete al historial
        // TODO: liberar al repartidor (volver a disponiblesPorZona)
        return false;
    }

    public Repartidor asignarPaquete(Paquete p) {
        // TODO: buscar repartidor disponible en la zona del paquete
        // TODO: marcarlo como no disponible
        return null;
    }
}