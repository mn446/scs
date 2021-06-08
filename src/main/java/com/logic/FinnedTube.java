package com.logic;

public class FinnedTube 
{
    private double heatLossParamU;
    private double diameter;
    private double longTube;
    private double transferSurface;
    private double coefUA;
    private boolean active;

    public FinnedTube(double heatLossParamU, double diameter, double longTube) 
    {
        this.heatLossParamU = heatLossParamU;
        this.diameter = diameter;
        this.longTube = longTube;
        this.transferSurface = 1.92*diameter*longTube*Math.PI;
        this.coefUA = this.transferSurface*heatLossParamU;
    }

    // Methods --------------------------------------------------------------------------------------------------------//
    
    public double tubeHeat(double TempOutCol, double RoomTemperature)
    {
        if (active)
            return coefUA*(TempOutCol-RoomTemperature);
        else
            return 0;
    }
    
    // Getters --------------------------------------------------------------------------------------------------------//

    public boolean getState() 
    {
        return active;
    }

    // Setters --------------------------------------------------------------------------------------------------------//
    
    public void setState(boolean active) 
    {
        this.active = active;
    }          
}
