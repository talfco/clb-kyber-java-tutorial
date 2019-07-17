package net.cloudburo.kyber.tutorial.protocol;

public class Param {

    public static final String OPS_GET = "GET";
    public static final String OPS_POST = "POST";
    public static final String OPS_PATCH = "PATCH";
    public static final String OPS_DELETE = "DELETE";

    private String name;
    private String value;

    public Param(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return name+"="+value;
    }
}
