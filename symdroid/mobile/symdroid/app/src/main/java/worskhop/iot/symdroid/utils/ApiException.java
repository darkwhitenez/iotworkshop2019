package worskhop.iot.symdroid.utils;

public class ApiException extends RuntimeException {
    private String message;
    private Exception e;

    public ApiException(String message, Exception e) {
        this.message = message;
        this.e = e;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }
}
