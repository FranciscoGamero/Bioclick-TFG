package com.salesianostriana.bioclick.error;

public class CommentException extends RuntimeException {
    public CommentException(String message) {
        super(message);
    }

    public CommentException() {
        super("No se ha podido crear el comentario");
    }
}
