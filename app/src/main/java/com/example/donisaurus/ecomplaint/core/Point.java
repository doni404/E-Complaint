package com.example.donisaurus.ecomplaint.core;

/**
 * Created by Donisaurus on 12/19/2016.
 */

public class Point
{
    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float x;
    public float y;

    @Override
    public String toString()
    {
        return String.format("(%.2f,%.2f)", x, y);
    }
}
