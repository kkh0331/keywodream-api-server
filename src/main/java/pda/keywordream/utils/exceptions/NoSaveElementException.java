package pda.keywordream.utils.exceptions;

public class NoSaveElementException extends RuntimeException{

    private String message;

    public NoSaveElementException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
