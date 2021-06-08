package com.logic;
import com.types.DtCollector;

public class Collector 
{
    // Collector parameters
    private double heatLossParamG1;
    private double heatLossParamG2;
    private double opticalEfficiency;
    private double netArea;
    private int collectorNumber;
    private double totalArea;
    private MeteorologicalData meteorologicalData;

    // Radiation
    private Radiation radiation;
    
    // Finned tube controller
    private FinnedTubeController finnedTubeController;
    
    // Finned tube
    private FinnedTube finnedTube;
    
    // Fluid
    private Fluid fluid;
    
    // Pump
    private Pump pump;
    
    // System parameters
    private double TempInCol;
    private double TempOutCol;
    private double collectorHeat;

    public Collector(FinnedTubeController finnedTubeController)
    {
        this.finnedTubeController = finnedTubeController;
    }

    public Collector(MeteorologicalData meteorologicalData, Radiation radiation, Fluid fluid, Pump pump, FinnedTube finnedTube, FinnedTubeController finnedTubeController, DtCollector DtCol) 
    {
        this.heatLossParamG1 = DtCol.getHeatLossParamG1();
        this.heatLossParamG2 = DtCol.getHeatLossParamG2();
        this.opticalEfficiency = DtCol.getOpticalEfficiency();
        this.netArea = DtCol.getNetArea();
        this.collectorNumber = DtCol.getCollectorNumber();
        this.meteorologicalData = meteorologicalData;
        this.radiation = radiation;
        this.finnedTube = finnedTube;
        this.finnedTubeController = finnedTubeController;
        this.fluid = fluid;
        this.pump = pump;
        this.totalArea = DtCol.getNetArea()*DtCol.getCollectorNumber();
    }
    
    // Methods --------------------------------------------------------------------------------------------------------------------/
    
    public double CalcCollectorHeat(double TempInCol, double TempOutCol, ProcessPoint processPointAnt)
    {
        double totalAreaShades = totalArea/*meteorologicalData.getShades()*/;
        double factTauAlfa = radiation.getParTA() ;
        double TI = radiation.getTiltedIrradiation();
        double RT = meteorologicalData.getRoomTemperature();

        // Quadratic model
        this.collectorHeat = (opticalEfficiency*0.96*factTauAlfa*TI-(heatLossParamG1*(TempInCol-RT)+heatLossParamG2*Math.pow((TempInCol-RT),2)))*0.95*totalAreaShades/* - finnedTube.tubeHeat(TempOutCol, RT)*/;

        // Linear model
        // this.collectorHeat = (opticalEfficiency*0.96*factTauAlfa*TI-heatLossParamG1*(TempInCol-RT))*0.95*totalAreaShades;

        return this.collectorHeat;
    }
    
    public double CalcTempOutCol(double TempInCol, double collectorHeat)
    {
        double mCp = pump.getFlowRate()*fluid.getCp();
        
        if (mCp != 0)
            return collectorHeat/(pump.getFlowRate()*fluid.getCp()) + TempInCol;
        else
            return TempInCol;
    }
    
    public double collectorEfficiency()
    {
        double TI = radiation.getTiltedIrradiation();
        double Ef;
        
        if (TI == 0)
            Ef = 0;
        else
        {
            Ef = collectorHeat/(TI*totalArea*0.95);
            if (Ef < 0)
                Ef = 0;
        }

        return Ef;
    }
    
    
    // Getters ----------------------------------------------------------------------------------------------------------------------/
    
    public double getTempInCol() 
    {
        return TempOutCol;
    }
    
    public double getTempOutCol() 
    {
        return TempOutCol;
    }

    public double getCollectorHeat() 
    {
        return collectorHeat;
    }
    
    public double getAreaCol()
    {
        return netArea*collectorNumber;
    }
    
    public Pump getPump()
    {
        return pump;
    }
    
    public Fluid getFluid()
    {
        return fluid;
    }
    
    public Radiation getRadiation()
    {
        return radiation;
    }
    
    public MeteorologicalData getMeteorologicalData()
    {
        return meteorologicalData;
    }
    
    public FinnedTubeController getFinnedTubeController()
    {
        return finnedTubeController;
    }
    
    public FinnedTube getFinnedTube()
    {
        return finnedTube;
    }
}
