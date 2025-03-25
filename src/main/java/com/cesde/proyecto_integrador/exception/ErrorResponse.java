package com.cesde.proyecto_integrador.exception;

public class ErrorResponse {
    private String message;  // Human-readable message for the user
    private int code;       // HTTP status code (e.g. 404, 500)
    private String detail;  // Technical error details (optional)

    public ErrorResponse(String message, int code, String detail) {
        this.message = message;
        this.code = code;
        this.detail = detail;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
