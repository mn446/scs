package com.types;

public class DtAccumulator 
{
    // Accumulator parameters
    private double height;         // (m)
    private double diameter;       // (m)
    private double volume;         // (m2)
    private double heatLossParamU;
    private double waterMass;
    private double externalArea;

    public DtAccumulator(double height, double collectorArea, double heatLossParamU, double densityFluid) 
    {
        this.volume = 75*collectorArea/1000;
        this.height = height;
        this.diameter = Math.sqrt(4*volume/(height*Math.PI));
        this.heatLossParamU = heatLossParamU;
        this.waterMass = densityFluid*volume*1000;
        this.externalArea = 2*Math.PI*Math.pow(diameter,2)/4 + Math.PI*diameter*height;
    }
    
    // Getters --------------------------------------------------------------------------------------------------------//

    public double getHeight() 
    {
        return height;
    }

    public double getDiameter() 
    {
        return diameter;
    }

    public double getVolume() 
    {
        return volume;
    }

    public double getHeatLossParamU() 
    {
        return heatLossParamU;
    }

    public double getWaterMass() 
    {
        return waterMass;
    }

    public double getExternalArea() 
    {
        return externalArea;
    }
    
    
    
}
