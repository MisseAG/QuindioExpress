package model;

import java.util.Objects;

public class Paquete implements Comparable<Paquete> {

    private String codigo;
    private String destino;
    private double peso;
    private byte prioridad;
    private int tiempoEstimado;

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

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public byte getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(byte prioridad) {
        this.prioridad = prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    /**
     * Orden natural: por código ascendente.
     * Precondición de la búsqueda binaria y usado por sort(lista) y TreeSet<Paquete>.
     */
    @Override
    public int compareTo(Paquete otro) {
        // TODO: comparar por codigo (String ya es Comparable)
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Paquete paquete = (Paquete) o;
        return Objects.equals(codigo, paquete.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}