package io.github.nicopolazzi.keepmygrind.model;

import java.util.Objects;

import org.bson.codecs.pojo.annotations.BsonId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Coffee {

    /**
     * required for MongoDB mapping with _id
     */
    @BsonId
    @Id
    private String id;
    private String origin;
    private String process;

    public Coffee() {
    }

    public Coffee(String id, String origin, String process) {
        this.id = id;
        this.origin = origin;
        this.process = process;
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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, process);
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
                && Objects.equals(process, other.process);
    }

    @Override
    public String toString() {
        return "Coffee [id=" + id + ", origin=" + origin + ", process=" + process + "]";
    }

}
