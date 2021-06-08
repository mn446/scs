package com.logic;

public class FinnedTubeController extends Controller
{
    public FinnedTubeController(double inferiorLimit, double superiorLimit, boolean active) 
    {
        super(inferiorLimit, superiorLimit, active);
    }
    
    // Methods --------------------------------------------------------------------------------------------------------//
    
    @Override
    public <T extends Object> T equation1() 
    {
        Boolean out = false;
        return (T)out;
    }

    @Override
    public <T extends Object> T equation2() 
    {
        Boolean out = true;
        return (T)out;
    }
    
    @Override
    public <T extends Object> T equation3() 
    {
        Boolean out = false;
        return (T)out;
    }
}
