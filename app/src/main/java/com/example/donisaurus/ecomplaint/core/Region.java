package com.example.donisaurus.ecomplaint.core;

/**
 * Created by Donisaurus on 12/20/2016.
 */

public class Region {

    private String name;
    private Polygon polygon;

    public Region(String name, Polygon polygon) {
        this.name = name;
        this.polygon = polygon;
    }

    public String getName() {
        return name;
    }

    public Polygon getPolygon() {
        return polygon;
    }
}
