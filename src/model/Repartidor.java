package model;

import java.util.Objects;

public class Repartidor {

    private final String identificacion;
    private final String nombre;
    private final String zona;
    private boolean disponible;

    public Repartidor(String identificacion, String nombre, String zona, boolean disponible) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.zona = zona;
        this.disponible = disponible;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getZona() {
        return zona;
    }

    public boolean isDisponible() {
        return disponible;
    }

    void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repartidor that = (Repartidor) o;
        return Objects.equals(identificacion, that.identificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identificacion);
    }

    @Override

    public String toString() {
        return nombre + " (" + identificacion + ") - Zona: " + zona
                + "- Disponible: " + (disponible ? "Sí" : "No");
    }
}