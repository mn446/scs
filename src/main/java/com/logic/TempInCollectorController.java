package com.logic;

public class TempInCollectorController extends Controller
{
    private ProcessPoint processPoint;

    public TempInCollectorController(ProcessPoint processPoint, double inferiorLimit, double superiorLimit, boolean active) 
    {
        super(inferiorLimit, superiorLimit, active);
        this.processPoint = processPoint;
    }
    
    // Methods --------------------------------------------------------------------------------------------------------//
    
    @Override
    public <T extends Object> T equation1() 
    {
        Double out = processPoint.getTempOutCollector();
        return (T)out;
    }

    @Override
    public <T extends Object> T equation2() 
    {
        Double out = processPoint.getTempOutAccumulator();
        return (T)out;
    }
    
    @Override
    public <T extends Object> T equation3() 
    {
        Double out = processPoint.getTempOutAccumulator();
        return (T)out;
    }
}
