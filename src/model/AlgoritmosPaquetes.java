package model;

import java.util.List;

//Clase utilitaria para algoritmos especificos sobre paquetes

public class AlgoritmosPaquetes {

    private AlgoritmosPaquetes() {
    }

    /**
     * Suma el peso de los paquetes cuyo destino es el municipio. Recursividad lineal, sin ciclos.
     *
     * Estructura esperada: List<Paquete> sin orden requerido (ArrayList).
     * Sirve cualquier lista: new ArrayList<>(registro.values()) para toda la jornada,
     * o porMunicipio.get(m) si solo interesan los pendientes.
     *
     * Caso base: i == tamaño de la lista, retorna 0.0
     * Caso recursivo: aporte del paquete i + pesoTotal desde i + 1
     * Avance: i + 1 en cada llamada, hasta alcanzar el caso base
     * Tiempo O(n); espacio O(n) por la pila (n llamadas pendientes)
     */
    public static double pesoTotalMunicipio(List<Paquete> paquetes, String municipio) {
        if (paquetes == null || municipio == null) {
            return 0.0;
        }
        return pesoTotalMunicipio(paquetes, municipio, 0);
    }

    private static double pesoTotalMunicipio(List<Paquete> paquetes, String municipio, int i) {
        if (i >= paquetes.size()) {
            return 0.0;
        }
        Paquete actual = paquetes.get(i);
        double aporte = actual.getDestino().equals(municipio) ? actual.getPeso() : 0.0;
        return aporte + pesoTotalMunicipio(paquetes, municipio, i + 1); // Caso recursivo
    }

    /**
     * Cuenta los paquetes con prioridad >= k. Recursividad lineal, sin ciclos.
     *
     * Estructura esperada: List<Paquete> sin orden requerido (ArrayList).
     *
     * Caso base: i == tamaño de la lista, retorna 0
     * Caso recursivo: (1 si prioridad del paquete i >= k, si no 0) + conteo desde i + 1
     * Tiempo O(n); espacio O(n) por la pila
     */
    public static int contarPrioridadMinima(List<Paquete> paquetes, byte k) {
        if (paquetes == null) {
            return 0;
        }
        return contarPrioridadMinima(paquetes, k, 0);
    }

    private static int contarPrioridadMinima(List<Paquete> paquetes, byte k, int i) {
        if (i >= paquetes.size()) {                          // caso base
            return 0;
        }
        int cuenta = paquetes.get(i).getPrioridad() >= k ? 1 : 0;
        return cuenta + contarPrioridadMinima(paquetes, k, i + 1);        // caso recursivo
    }

    /**
     * Busca un paquete por código. Retorna su posición o -1 si no existe.
     *
     * Estructura esperada: List<Paquete> (ArrayList) ORDENADA por código ascendente,
     * es decir, por el orden natural (compareTo). Esa es la precondición.
     * Se prepara con: List<Paquete> ordenados = new ArrayList<>(registro.values());
     *                 Collections.sort(ordenados);   // O(n log n), una sola vez
     * registro.values() viene en orden de REGISTRO, no de código: no sirve sin ordenar.
     * Verificar el orden costaría O(n), que anula la ventaja; se asume y se documenta.
     *
     * Cada vuelta descarta la mitad del rango. Versión iterativa: espacio O(1).
     * Tiempo O(log n)
     */
    public static int busquedaBinaria(List<Paquete> paquetes, String codigo) {
        if (paquetes == null || codigo == null) {
            return -1;
        }
        int inicio = 0;
        int fin = paquetes.size() - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;     // evita desbordamiento de (inicio+fin)
            int cmp = paquetes.get(medio).getCodigo().compareTo(codigo);
            if (cmp == 0) {
                return medio;
            } else if (cmp < 0) {
                inicio = medio + 1;                      // el código está a la derecha
            } else {
                fin = medio - 1;                         // el código está a la izquierda
            }
        }
        return -1;
    }

    /**
     * Retorna el paquete de mayor peso. Divide y vencerás, sin ciclos.
     *
     * Estructura esperada: List<Paquete> sin orden requerido (ArrayList).
     * Si hay empate de peso, gana el de índice menor (el registrado primero).
     *
     * Caso base: rango de un solo elemento (inicio == fin), retorna ese paquete
     * División: medio = inicio + (fin - inicio) / 2
     * Subproblema izquierdo: [inicio, medio]; subproblema derecho: [medio + 1, fin]
     * Combinación: se queda con el de mayor peso entre los dos resultados
     * Resultado final: lo que retorna la primera llamada, sobre [0, n - 1]
     * Tiempo O(n): T(n) = 2T(n/2) + O(1); espacio O(log n) por la profundidad de pila
     */
    public static Paquete mayorPeso(List<Paquete> paquetes) {
        if (paquetes == null || paquetes.isEmpty()) {
            return null;
        }
        return mayorPeso(paquetes, 0, paquetes.size() - 1);
    }

    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin) {
        if (inicio == fin) {                                   // caso base
            return paquetes.get(inicio);
        }
        int medio = inicio + (fin - inicio) / 2;               // división
        Paquete izquierdo = mayorPeso(paquetes, inicio, medio);        // subproblema izquierdo
        Paquete derecho = mayorPeso(paquetes, medio + 1, fin);         // subproblema derecho
        return derecho.getPeso() > izquierdo.getPeso() ? derecho : izquierdo;   // combinación
    }
}