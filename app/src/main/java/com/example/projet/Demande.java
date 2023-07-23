package com.example.projet;

public class Demande {


    private String name;
    private String status;

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String raison;
    private String userId;

    public Demande(String name, String status, String userId,String raison) {
        this.name = name;
        this.status = status;
        this.userId = userId;
        this.raison = raison;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
