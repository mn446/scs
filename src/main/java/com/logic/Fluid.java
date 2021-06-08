package com.logic;

public class Fluid 
{
    private double cp;
    private double density;

    public Fluid(double cp, double density) 
    {
        this.cp = cp;
        this.density = density;
    }

    // Getters --------------------------------------------------------------------------------------------------------//
    
    public double getCp() 
    {
        return cp;
    }

    public double getDensity() {
        return density;
    }
}
