package guckflix.backend.exception;

public class RuntimeMovieNotFoundException extends RuntimeException{
        public RuntimeMovieNotFoundException() {
        }
        public RuntimeMovieNotFoundException(String message) {
            super(message);
        }
        public RuntimeMovieNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
        public RuntimeMovieNotFoundException(Throwable cause) {
            super(cause);
        }
    }