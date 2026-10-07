package com.example.manikantasales.exception;


import java.time.LocalDateTime;
import java.util.Map;



public class ErrorResponse {


    private int status;

    private String message;

    private LocalDateTime timestamp;

    private Map<String,String> errors;



    // =========================
    // CONSTRUCTOR
    // =========================

    public ErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp,
            Map<String,String> errors
    ){

        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;

    }



    // =========================
    // GETTERS
    // =========================


    public int getStatus(){

        return status;

    }



    public String getMessage(){

        return message;

    }



    public LocalDateTime getTimestamp(){

        return timestamp;

    }



    public Map<String,String> getErrors(){

        return errors;

    }



    // =========================
    // SETTERS
    // =========================


    public void setStatus(int status){

        this.status = status;

    }



    public void setMessage(String message){

        this.message = message;

    }



    public void setTimestamp(LocalDateTime timestamp){

        this.timestamp = timestamp;

    }



    public void setErrors(Map<String,String> errors){

        this.errors = errors;

    }


}