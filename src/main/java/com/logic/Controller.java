package com.logic;

public abstract class Controller 
{
    
    private double inferiorLimit;
    private double superiorLimit;
    private boolean active;
    private boolean state;
    
    public Controller(double inferiorLimit, double superiorLimit, boolean active) 
    {
        this.inferiorLimit = inferiorLimit;
        this.superiorLimit = superiorLimit;
        this.active = active;
        this.state = false;
    }
    
    // Methods --------------------------------------------------------------------------------------------------------//
    
    public <T extends Object> T control(double parametroMedido, boolean stateAnterior)
    {
        T out = null;
        if (active)
        {
            if ((parametroMedido < inferiorLimit) || ((parametroMedido <= superiorLimit) && (!stateAnterior)))
            {
                this.state = false;
                out = equation1();
            }
            else if ((parametroMedido > superiorLimit) || ((parametroMedido >= inferiorLimit) && (stateAnterior)))
            {
                this.state = true;
                out = equation2();
            }
        }
        else
        {
            this.state = false;
            out = equation3();
        }
        return out;
    }
    
    public abstract <T extends Object> T equation1();
    
    public abstract <T extends Object> T equation2();
    
    public abstract <T extends Object> T equation3();
            
    // Getters --------------------------------------------------------------------------------------------------------//

    public boolean Active() 
    {
        return active;
    }

    public boolean State()
    {
        return state;
    }
}
