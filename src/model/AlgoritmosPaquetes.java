package model;

import java.util.List;

//Clase utilitaria para algoritmos especificos sobre paquetes

public class AlgoritmosPaquetes {

    private AlgoritmosPaquetes() {
    }

    // TODO: recursividad, sumar peso de paquetes cuyo destino sea el municipio
    public static double pesoTotalMunicipio(List<Paquete> paquetes, String municipio) {
        return 0.0;
    }

    // TODO: recursividad, contar paquetes con prioridad >= k
    public static int contarPrioridadMinima(List<Paquete> paquetes, byte k) {
        return 0;
    }

    // TODO: búsqueda binaria sobre colección ordenada por código
    public static int busquedaBinaria(List<Paquete> paquetes, String codigo) {
        return -1;
    }

    // TODO: divide y vencerás, retornar el paquete de mayor peso
    public static Paquete mayorPeso(List<Paquete> paquetes) {
        return null;
    }
}