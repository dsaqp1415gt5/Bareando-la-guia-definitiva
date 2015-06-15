package edu.upc.eetac.dsa.acouceiro.bareando.api;

public class AppException extends Exception {
    public AppException() {
        super();
    }

    public AppException(String detailMessage) {
        super(detailMessage);
    }
}
