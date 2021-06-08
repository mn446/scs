package com.types;

public class DtCollector 
{
    // Collector parameters
    private double heatLossParamG1;
    private double heatLossParamG2;
    private double opticalEfficiency;
    private double netArea;
    private int collectorNumber;

    public DtCollector(double heatLossParamG1, double heatLossParamG2, double opticalEfficiency, double netArea, int collectorNumber) 
    {
        this.heatLossParamG1 = heatLossParamG1;
        this.heatLossParamG2 = heatLossParamG2;
        this.opticalEfficiency = opticalEfficiency;
        this.netArea = netArea;
        this.collectorNumber = collectorNumber;
    }
    
    // Getters --------------------------------------------------------------------------------------------------------//

    public double getHeatLossParamG1() 
    {
        return heatLossParamG1;
    }

    public double getHeatLossParamG2() 
    {
        return heatLossParamG2;
    }

    public double getOpticalEfficiency() 
    {
        return opticalEfficiency;
    }

    public double getNetArea() 
    {
        return netArea;
    }

    public int getCollectorNumber() 
    {
        return collectorNumber;
    }
 
}
