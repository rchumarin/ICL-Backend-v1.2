package ru.icl.test.beans;

public class Messages {
    
    private String fname, clientid, message;
    private int id;
    
    public Messages() {
    }

    /*
    public Messages(String name, int id) {
        this.name = name;
        this.id = id;
    }
    */
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }
     
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
