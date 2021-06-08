package com.logic;

public class ProcessPoint 
{
    // Proces point hour
    private int hourYear;
         
    private double TempInCollector;
    private double TempInCollectorEstimation;
    private double TempOutCollector;
    private double TempInAccumulator;
    private double TempOutAccumulator; 
    private double RoomTemperature;
    
    private double difTempCtrl1;
    private double collectorHeat;

    private PumpController pumpController2;
    private TempInCollectorController tempInCollectorController;
    private Collector collector;
    private Exchanger exchanger;
    private Accumulator accumulator;
    private ProcessPoint processPointAnt;

    // Initial process point contructor
    public ProcessPoint(int hourYear, double TempInCollector, double TempOutCollector, double TempInAccumulator, double TempOutAccumulator, double difTempCtrl1, PumpController pumpController2, Collector collector) 
    {
        this.hourYear = hourYear;
        this.TempInCollector = TempInCollector;
        this.TempOutCollector = TempOutCollector;
        this.TempInAccumulator = TempInAccumulator;
        this.TempOutAccumulator = TempOutAccumulator;
        this.difTempCtrl1 = difTempCtrl1;
        this.pumpController2 = pumpController2;
        this.collector = collector;
    }
       
    // Further process points contructor
    public ProcessPoint(int hourYear, Collector collector, Exchanger exchanger, Accumulator accumulator, PumpController pumpController2, TempInCollectorController tempInCollectorController, ProcessPoint processPointAnt)
    {
        this.hourYear = hourYear;
        this.collector = collector;
        this.exchanger = exchanger;
        this.accumulator = accumulator;
        this.pumpController2 = pumpController2;
        this.tempInCollectorController = tempInCollectorController;
        this.processPointAnt = processPointAnt;
        this.RoomTemperature = collector.getMeteorologicalData().getRoomTemperature();      
        
        // Temperatures calculation
        this.TempInCollector = tempInCollectorController.control(processPointAnt.getDifT(), processPointAnt.getPumpController().State());
        this.TempOutCollector = processPointAnt.getTempOutCollector();
        collector.getFinnedTube().setState(collector.getFinnedTubeController().control(processPointAnt.getTempOutCollector(), processPointAnt.getFinnedTubeController().State()));
        
        boolean verificator = false;
        while (!verificator)
           verificator = pointProcessCalculation();   
    }
    
    // Methods --------------------------------------------------------------------------------------------------------//
    
    private boolean pointProcessCalculation()
    {
        boolean goodEstimation = false;
        
        // Calculates the heat captured by the collectors
        this.collectorHeat = collector.CalcCollectorHeat(this.TempInCollector, this.TempOutCollector, this.processPointAnt);
    
        // Calculates TempOutCollector
        this.TempOutCollector = collector.CalcTempOutCol(this.TempInCollector, this.collectorHeat);
        
        // Calculates the parameter the controller is trigger with
        this.difTempCtrl1 = this.TempOutCollector - processPointAnt.getTempOutAccumulator();
        
        // Switch the pump2 state based on the control parameter
        exchanger.getPump2().setState(pumpController2.control(this.difTempCtrl1, processPointAnt.getPumpController().State()));
        
        // TempOutAccumulator initial estimation (TempInAccumulator = TempOutCollector)
        this.TempOutAccumulator = accumulator.CalcTempOutAccumulator(this.TempOutCollector, processPointAnt.getTempOutAccumulator());

        // Calculates TempInCollector estimation to iterate
        this.TempInCollectorEstimation = exchanger.CalcTempOutExchanger1(this.TempOutAccumulator, this.TempOutCollector);
        if (!exchanger.getPump2().on()) // If pump is off TempInCollector = TempOutCollector
                this.TempInCollector = TempInCollectorEstimation;
        
        // Calculates TempInAccumulator
        this.TempInAccumulator = exchanger.CalcTempOutExchanger2(this.TempOutCollector, this.TempInCollectorEstimation, this.TempOutAccumulator);

         // TempOutAccumulator estimation with TempInAccumulator = TempInAccumulator
        this.TempOutAccumulator = accumulator.CalcTempOutAccumulator(this.TempInAccumulator, processPointAnt.getTempOutAccumulator());

        if (Math.abs(this.TempInCollectorEstimation - this.TempInCollector) < 0.001)
            goodEstimation = true;
        else
            this.TempInCollector = TempInCollectorEstimation; 
      
        return goodEstimation;
    }

    // Getters --------------------------------------------------------------------------------------------------------//

    public double getTempInCollector() 
    {
        return TempInCollector;
    }

    public double getTempOutCollector() 
    {
        return TempOutCollector;
    }

    public double getTempInAccumulator() 
    {
        return TempInAccumulator;
    }

    public double getTempOutAccumulator() 
    {
        return TempOutAccumulator;
    }
    
        public double getRoomTemperature() 
    {
        return RoomTemperature;
    }

    public double getCollectorHeat() 
    {
        return collectorHeat;
    }
    
    public double getDifT() 
    {
        return difTempCtrl1;
    }
    
    public Collector getCollector() 
    {
        return collector;
    }
    
    public Exchanger getExchanger() 
    {
        return exchanger;
    }

    public PumpController getPumpController()
    {
        return pumpController2;
    }

    public FinnedTubeController getFinnedTubeController()
    {
        return collector.getFinnedTubeController();
    }
}
