package util;

/**
 * Interfaz Coleccion que define los métodos básicos para una colección de elementos.
 *
 * @param <T> el tipo de elementos en la colección
 *
 * implementa iterable para permitir que recorra sus elementos con un bucle for-each
 */

public interface Coleccion<T> extends Iterable<T> {
    void agregar(T elemento);
    boolean eliminar(T elemento);
    T obtener(int posicion);
    int tamano();
    boolean estaVacia();
}
