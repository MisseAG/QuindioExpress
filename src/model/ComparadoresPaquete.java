package model;

import java.util.Comparator;

public class ComparadoresPaquete {

    // TODO: prioridad (mayor primero), desempate por tiempo y luego por código
    public static final Comparator<Paquete> POR_PRIORIDAD = null;

    // TODO: peso (mayor primero)
    public static final Comparator<Paquete> POR_PESO = null;

    // TODO: tiempo estimado (menor primero)
    public static final Comparator<Paquete> POR_TIEMPO = null;

    // TODO: criterio de despacho encadenado: prioridad -> tiempo -> código
    public static final Comparator<Paquete> DESPACHO = null;

    // Constructor privado para clase utilitaria
    private ComparadoresPaquete() {
    }
}