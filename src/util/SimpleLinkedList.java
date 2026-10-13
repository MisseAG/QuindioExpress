package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SimpleLinkedList<T> implements Coleccion<T>, Iterable<T> {
    private int size;
    private SimpleNode<T> firstNode;
    private SimpleNode<T> lastNode;

    public SimpleLinkedList() {
        this.firstNode = null;
        this.lastNode = null;
        this.size = 0;
    }

    public boolean indiceValido(int index) {
        return index >= 0 && index < size;
    }

    public void addFirst(T data) {
        SimpleNode<T> newNode = new SimpleNode<>(data);
        newNode.setNext(firstNode);
        firstNode = newNode;
        if (lastNode == null) {
            lastNode = newNode;
        }
        size++;
    }

    public void addFinal(T data) {
        SimpleNode<T> newNode = new SimpleNode<>(data);
        if (estaVacia()) {
            firstNode = newNode;
        } else {
            lastNode.setNext(newNode); // O(1) operación que no recorre la lista
        }
        lastNode = newNode;
        size++;
    }

    public void addPosicion(int index, T data) {
        if (index == 0) {
            addFirst(data);
        } else if (index == size) {
            addFinal(data);
        } else if (indiceValido(index)) {
            SimpleNode<T> newNode = new SimpleNode<>(data);
            SimpleNode<T> current = firstNode;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
            size++;
        } else {
            System.out.println("Índice no válido.");
        }
    }

    public T getValorNodo(int index) {
        if (indiceValido(index)) {
            SimpleNode<T> current = firstNode;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            return current.getData();
        }
        throw new IndexOutOfBoundsException("Índice inválido: " + index);
    }

    public int getPosicionNodo(T data) {
        SimpleNode<T> current = firstNode;
        int index = 0;
        while (current != null) {
            if (Objects.equals(current.getData(), data)) { // Usamos .equals para objetos genéricos
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    public void deleteFirst() {
        if (estaVacia())return;
        firstNode = firstNode.getNext();
        if (firstNode == null) { // vacía la lista, actualizamos lastNode también
            lastNode = null;
        }
        size--;
    }

    public void deleteLast() {
        if (estaVacia()) return;
        if (firstNode == lastNode) { // Solo un nodo en la lista
            firstNode = null;
            lastNode = null;
        } else {
            SimpleNode<T> current = firstNode;
            while (current.getNext() != lastNode) {
                current = current.getNext();
            }
            current.setNext(null);
            lastNode = current;
        }
        size--;
    }

    public void eliminarPorValor(T data) {
        eliminar(data);
    }

    public void setNodo(int index, T newData) {
        if (indiceValido(index)) {
            SimpleNode<T> current = firstNode;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            current.setData(newData);
        } else {
            System.out.println("Índice no válido para modificar.");
        }
    }

    public void deleteList() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    public int getSize() {
        return size;}

    // implementación de de la interface colección
    @Override
    public void agregar(T elemento) {
        addFinal(elemento);
    }

    // elimina la primera aparición del elemento en la lista
    @Override
    public boolean eliminar(T elemento) {
        if (estaVacia()) return false;
        if (Objects.equals(firstNode.getData(), elemento)) {
            deleteFirst();
            return true;
        }
        SimpleNode<T> previo = firstNode;
        while (previo.getNext() != null && !Objects.equals(previo.getNext().getData(), elemento)) {
            previo = previo.getNext();
        }
        if (previo.getNext() == null) {
            return false;                // elemento inexistente
        }
        SimpleNode<T> eliminado = previo.getNext();
        previo.setNext(eliminado.getNext());
        if (eliminado == lastNode) {     // se eliminó el último
            lastNode = previo;
        }
        size--;
        return true;
    }

    @Override
    public T obtener(int posicion) {
        return getValorNodo(posicion);
    }

    @Override
    public int tamano() {
        return size;
    }

    @Override
    public boolean estaVacia() {
        return firstNode == null;
    }

    // Implementación de iterable E iterator
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private SimpleNode<T> current = firstNode;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

    public void print() {
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return;
        }
        System.out.print("null <- ");
        for (T data : this) {
            System.out.print("[" + data + "] -> ");
        }
        System.out.println("null");
    }
}