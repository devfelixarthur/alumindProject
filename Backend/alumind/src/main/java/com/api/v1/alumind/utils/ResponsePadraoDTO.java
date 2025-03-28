package com.api.v1.alumind.utils;

public class ResponsePadraoDTO {

    private String status;
    private String message;

    public static ResponsePadraoDTO error(String message) {
        ResponsePadraoDTO response = new ResponsePadraoDTO();
        response.setStatus("Error");
        response.setMessage(message);
        return response;
    }

    public static ResponsePadraoDTO success(String message) {
        ResponsePadraoDTO response = new ResponsePadraoDTO();
        response.setStatus("Success");
        response.setMessage(message);
        return response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toJson() {
        return "{\"status\":\"" + status + "\", \"message\":\"" + message + "\"}";
    }
}
