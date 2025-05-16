package io.github.nicopolazzi.keepmygrind.model;

import java.util.Objects;

public class GrindProfile {

    private String id;
    private Coffee coffee;
    private String brew;
    private double beanGrams;
    private double waterMilliliters;
    private int clicks;

    public GrindProfile() {
    }

    public GrindProfile(String id, Coffee coffee, String brew, double beanGrams, double waterMilliliters, int clicks) {
        this.id = id;
        this.coffee = coffee;
        this.brew = brew;
        this.beanGrams = beanGrams;
        this.waterMilliliters = waterMilliliters;
        this.clicks = clicks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coffee getCoffee() {
        return coffee;
    }

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getBrew() {
        return brew;
    }

    public void setBrew(String brew) {
        this.brew = brew;
    }

    public double getBeanGrams() {
        return beanGrams;
    }

    public void setBeanGrams(double beanGrams) {
        this.beanGrams = beanGrams;
    }

    public double getWaterMilliliters() {
        return waterMilliliters;
    }

    public void setWaterMilliliters(double waterMilliliters) {
        this.waterMilliliters = waterMilliliters;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanGrams, brew, clicks, coffee, id, waterMilliliters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GrindProfile other = (GrindProfile) obj;
        return Double.doubleToLongBits(beanGrams) == Double.doubleToLongBits(other.beanGrams)
                && Objects.equals(brew, other.brew) && clicks == other.clicks && Objects.equals(coffee, other.coffee)
                && Objects.equals(id, other.id)
                && Double.doubleToLongBits(waterMilliliters) == Double.doubleToLongBits(other.waterMilliliters);
    }

    @Override
    public String toString() {
        return "GrindProfile [id=" + id + ", coffee=" + coffee + ", brew=" + brew + ", beanGrams=" + beanGrams
                + ", waterMilliliters=" + waterMilliliters + ", clicks=" + clicks + "]";
    }

}
