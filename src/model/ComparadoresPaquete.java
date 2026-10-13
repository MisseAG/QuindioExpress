package model;

import java.util.Comparator;

public class ComparadoresPaquete {

    // TODO: prioridad (mayor primero), desempate por tiempo y luego por código
    public static final Comparator<Paquete> POR_PRIORIDAD =
            Comparator.comparingInt(Paquete::getPrioridad).reversed()
            .thenComparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);

    // TODO: peso (mayor primero)
    public static final Comparator<Paquete> POR_PESO =
            Comparator.comparingDouble(Paquete::getPeso).reversed()
                    .thenComparing(Paquete::getCodigo);

    // TODO: tiempo estimado (menor primero)
    public static final Comparator<Paquete> POR_TIEMPO =
            Comparator.comparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);

    // TODO: criterio de despacho encadenado: prioridad -> tiempo -> código
    public static final Comparator<Paquete> DESPACHO =
            Comparator.comparingInt(Paquete::getPrioridad).reversed()
                    .thenComparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);

    // Constructor privado para clase utilitaria
    private ComparadoresPaquete() {
    }
}