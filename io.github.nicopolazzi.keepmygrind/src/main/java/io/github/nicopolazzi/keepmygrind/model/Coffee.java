package io.github.nicopolazzi.keepmygrind.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "Coffee")
public class Coffee {

    @Id
    private String id;

    @OneToMany(mappedBy = "coffee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrindProfile> grindProfiles = new ArrayList<>();

    public List<GrindProfile> getGrindProfiles() {
        return grindProfiles;
    }

    public void setGrindProfiles(List<GrindProfile> grindProfiles) {
        this.grindProfiles = grindProfiles;
    }

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
        return Objects.hash(id);
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
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Coffee [id=" + id + ", origin=" + origin + ", process=" + process + "]";
    }

    public void addGrindProfile(GrindProfile grindProfile) {
        grindProfiles.add(grindProfile);
        grindProfile.setCoffee(this);
    }

}
