package com.practica.controller.excepction;

public class ListEmptyException extends Exception {
    public ListEmptyException() {
    }

    public ListEmptyException(String msg) {
        super(msg);
    }
}
