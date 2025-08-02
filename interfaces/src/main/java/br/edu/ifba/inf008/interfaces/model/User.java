package br.edu.ifba.inf008.interfaces.model;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private String email;
    private Date registeredAt;

    public User() {}
    public User(int id, String name, String email, Date registeredAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Date registeredAt) { this.registeredAt = registeredAt; }
}
