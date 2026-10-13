package model;

import java.util.Objects;

public class Paquete implements Comparable<Paquete> {

    private final String codigo;
    private final String destino;
    private final double peso;
    private final byte prioridad;
    private final int tiempoEstimado;

    public Paquete(String codigo, String destino, double peso, byte prioridad, int tiempoEstimado) {
        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    public byte getPrioridad() {
        return prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    /**
     * Orden natural: por código ascendente.
     * Precondición de la búsqueda binaria y usado por sort(lista) y TreeSet<Paquete>.
     */
    @Override
    public int compareTo(Paquete otro) {
        return this.codigo.compareTo(otro.codigo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paquete paquete = (Paquete) o;
        return Objects.equals(codigo, paquete.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }

    @Override
    public String toString() {
        return codigo + " -> " + destino + " (" +
                peso + " kg - Prioridad " + prioridad
                + " - Tiempo estimado: " + tiempoEstimado + " min)";
    }
}