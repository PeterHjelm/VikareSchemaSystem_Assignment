package se.yrgo.schedule.domain;

public class School {

    private String name;
    private String address;

    public School(String name, String address) {
        this.name = name;
        this.address = address;
    }
    public String name(){
        return this.name;
    }
    public String address(){
        return this.address;
    }
}
