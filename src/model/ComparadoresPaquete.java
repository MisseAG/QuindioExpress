package model;

import java.util.Comparator;

public class ComparadoresPaquete {

    // prioridad (mayor primero), desempate por tiempo y luego por código
    public static final Comparator<Paquete> POR_PRIORIDAD =
            Comparator.comparingInt(Paquete::getPrioridad).reversed()
            .thenComparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);

    //  peso (mayor primero)
    public static final Comparator<Paquete> POR_PESO =
            Comparator.comparingDouble(Paquete::getPeso).reversed()
                    .thenComparing(Paquete::getCodigo);

    // tiempo estimado (menor primero)
    public static final Comparator<Paquete> POR_TIEMPO =
            Comparator.comparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);

    // criterio de despacho encadenado: prioridad -> tiempo -> código
    public static final Comparator<Paquete> DESPACHO =
            Comparator.comparingInt(Paquete::getPrioridad).reversed()
                    .thenComparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);

    // Constructor privado para clase utilitaria
    private ComparadoresPaquete() {
    }
}
