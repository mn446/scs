package com.logic;

public class Pump 
{
    private double settedFlowRate;
    private double flowRate;
    private boolean on;

    public Pump(boolean on, double settedFlowRate) 
    {
        this.settedFlowRate = settedFlowRate;
        this.on = on;
        if (on)
            this.flowRate = this.settedFlowRate;
        else
            this.flowRate = 0;
    }
    
    public void turnOn() 
    {
        this.on = true;
        this.flowRate = this.settedFlowRate;
    }
    
    public void turnOff() 
    {
        this.on = false;
        this.flowRate = 0;
    }
     
    // Getters --------------------------------------------------------------------------------------------------------//

    public double getFlowRate()
    {
        return flowRate;
    }

    public boolean on()
    {
        return on;
    }
    
    // Setters --------------------------------------------------------------------------------------------------------//

    public void setFlowRate(double settedFlowRate) 
    {
        this.settedFlowRate = settedFlowRate;
    }

    public void setState(boolean on) 
    {
        this.on = on;
        if (on)
            this.flowRate = this.settedFlowRate;
        else
            this.flowRate = 0;
    }
}
