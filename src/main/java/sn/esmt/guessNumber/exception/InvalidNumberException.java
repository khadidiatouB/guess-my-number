package sn.esmt.guessNumber.exception;

public class InvalidNumberException extends RuntimeException{
    public InvalidNumberException() {
        super("Le nombre doit être entre 1 et 100.");
    }
}
