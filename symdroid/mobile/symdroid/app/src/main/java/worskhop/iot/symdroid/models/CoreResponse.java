package worskhop.iot.symdroid.models;

import java.io.Serializable;

public class CoreResponse<T> implements Serializable {
    private int status;
    private String message;
    private T body;

    public CoreResponse() {
    }

    public CoreResponse(int status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }
}