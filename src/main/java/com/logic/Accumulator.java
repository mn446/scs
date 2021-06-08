package com.logic;
import com.types.DtAccumulator;

public class Accumulator 
{
    // Accumulator parameters
    private double height;
    private double diameter;
    private double volume;
    private double waterMass;
    private double externalArea;
    private double heatLossParamU;
    
    // System parameters
    private double TempInTK2;
    private double TempOutTK2;
    private double TempInTK3;
    private double TempOutTK3;
    private double capturedHeat;
    
    // Fluids
    private Fluid fluid2;
    
    // Pumps
    private Pump pump2, pump3;

    public Accumulator(MeteorologicalData meteorologicalData, DtAccumulator DtAcum, Fluid fluid2, Pump pump2, Pump pump3) 
    {
        this.height = DtAcum.getHeight();
        this.diameter = DtAcum.getDiameter();
        this.volume = DtAcum.getVolume();
        this.waterMass = DtAcum.getWaterMass();
        this.externalArea = DtAcum.getExternalArea();
        this.heatLossParamU = DtAcum.getHeatLossParamU();
        
        this.TempInTK3 = meteorologicalData.getRoomTemperature();
        this.fluid2 = fluid2;
        this.pump2 = pump2;
        this.pump3 = pump3;
    }
    
    // Methods --------------------------------------------------------------------------------------------------------//

    public void capturedHeat(double TempOutTK2ant)
    {
        double dt = 60*60;
        this.capturedHeat =  waterMass*fluid2.getCp()*(TempOutTK2-TempOutTK2ant)/dt + heatLossParamU*externalArea*(TempOutTK2 - TempInTK3) + pump3.getFlowRate()*fluid2.getCp()*(TempOutTK2 - TempInTK3);
    }
    
    public double CalcTempOutAccumulator(double TempInAccumulator, double TempOutAccumulatorAnt)
    {
        int dt = 3600;
        double UA = heatLossParamU*externalArea;
        return (waterMass*TempOutAccumulatorAnt + dt*(pump2.getFlowRate()*TempInAccumulator + (pump3.getFlowRate() + UA/fluid2.getCp())*TempInTK3) ) / (waterMass + dt*(pump2.getFlowRate() + UA/fluid2.getCp() + pump3.getFlowRate()) ) ;
    }

    // Getters --------------------------------------------------------------------------------------------------------//

    public double getcapturedHeat() 
    {
        return capturedHeat;
    }
    
    public Pump getPump2()
    {
        return pump2;
    }
    
    public Pump getPump3()
    {
        return pump3;
    }
}
