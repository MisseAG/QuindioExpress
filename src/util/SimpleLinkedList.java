package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleLinkedList<T> implements Coleccion<T>, Iterable<T> {
    private int size;
    private SimpleNode<T> firstNode;

    public SimpleLinkedList() {
        this.firstNode = null;
        this.size = 0;
    }

    public boolean indiceValido(int index) {
        return index >= 0 && index < size;
    }

    public void addFirst(T data) {
        SimpleNode<T> newNode = new SimpleNode<>(data);
        newNode.setNext(firstNode);
        firstNode = newNode;
        size++;
    }

    public void addFinal(T data) {
        SimpleNode<T> newNode = new SimpleNode<>(data);
        if (estaVacia()) {
            firstNode = newNode;
        } else {
            SimpleNode<T> current = firstNode;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
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
            if (current.getData().equals(data)) { // Usamos .equals para objetos genéricos
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    public void deleteFirst() {
        if (!estaVacia()) {
            firstNode = firstNode.getNext();
            size--;
        }
    }

    public void deleteLast() {
        if (estaVacia()) return;
        if (firstNode.getNext() == null) {
            firstNode = null;
        } else {
            SimpleNode<T> current = firstNode;
            while (current.getNext().getNext() != null) {
                current = current.getNext();
            }
            current.setNext(null);
        }
        size--;
    }

    public void eliminarPorValor(T data) {
        if (estaVacia()) return;
        if (firstNode.getData().equals(data)) {
            deleteFirst();
            return;
        }
        SimpleNode<T> current = firstNode;
        while (current.getNext() != null && !current.getNext().getData().equals(data)) {
            current = current.getNext();
        }
        if (current.getNext() != null) {
            current.setNext(current.getNext().getNext());
            size--;
        }
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
        size = 0;
    }

    public int getSize() {
        return size;}

    // implementación de de la interface colección
    @Override
    public void agregar(T elemento) {
        addFinal(elemento); // Usamos tu método addFinal existente
    }

    @Override
    public boolean eliminar(T elemento) {
        if (estaVacia()) return false;
        if (firstNode.getData().equals(elemento)) {
            deleteFirst();
            return true;
        }
        SimpleNode<T> current = firstNode;
        while (current.getNext() != null && !current.getNext().getData().equals(elemento)) {
            current = current.getNext();
        }
        if (current.getNext() != null) {
            current.setNext(current.getNext().getNext());
            size--;
            return true;
        }
        return false;
    }

    @Override
    public T obtener(int posicion) {
        return getValorNodo(posicion); // Reutilizamos tu método getValorNodo
    }

    @Override
    public int tamano() {
        return size;
    }

    @Override
    public boolean estaVacia() {
        return firstNode == null;
    }

    // IMplementación de iterable E iterator (Requerido por el PDF)
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
        for (T data : this) { // ¡Gracias a Iterable ya puedes usar for-each!
            System.out.print("[" + data + "] -> ");
        }
        System.out.println("null");
    }
}