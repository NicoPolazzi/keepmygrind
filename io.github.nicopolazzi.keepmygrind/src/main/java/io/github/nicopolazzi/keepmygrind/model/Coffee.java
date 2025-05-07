package io.github.nicopolazzi.keepmygrind.model;

import java.util.Objects;

public class Coffee {

    private String id;
    private String origin;
    private String processMethod;
    private String roastMethod;

    public Coffee() {
    }

    public Coffee(String id, String origin, String processMethod, String roastMethod) {
        this.id = id;
        this.origin = origin;
        this.processMethod = processMethod;
        this.roastMethod = roastMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProcessMethod() {
        return processMethod;
    }

    public void setProcessMethod(String processMethod) {
        this.processMethod = processMethod;
    }

    public String getRoastMethod() {
        return roastMethod;
    }

    public void setRoastMethod(String roastMethod) {
        this.roastMethod = roastMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, processMethod, roastMethod);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coffee other = (Coffee) obj;
        return Objects.equals(id, other.id) && Objects.equals(origin, other.origin)
                && Objects.equals(processMethod, other.processMethod) && Objects.equals(roastMethod, other.roastMethod);
    }

    @Override
    public String toString() {
        return "Coffee [id=" + id + ", origin=" + origin + ", processMethod=" + processMethod + ", roastMethod="
                + roastMethod + "]";
    }

}
