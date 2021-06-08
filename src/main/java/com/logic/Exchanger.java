package com.logic;
import com.types.DtExchanger;

public class Exchanger 
{
    // Exchanger parameters
    private double transferCoef;
    private double exchangeArea;
    private double exchangerEfficiency;
    
    // System parameters
    private double TempInExchanger1;
    private double TempOutExchanger1;
    private double TempInExchanger2;
    private double TempOutExchanger2;
    private double exchangerHeat;
    
    // Fluids
    private Fluid fluid1, fluid2;
    
    // Pumps
    private Pump pump1, pump2;
    
    public Exchanger(DtExchanger dtExchanger, Fluid fluid1, Fluid fluid2, Pump pump1, Pump pump2, double TempInExchanger1, double TempOutExchanger1, double collectorHeat)
    {
        this.transferCoef = dtExchanger.getTransferCoef();
        this.exchangeArea = dtExchanger.getExchangeSurface();
        this.TempInExchanger1 = TempInExchanger1;
        this.TempOutExchanger1 = TempOutExchanger1;
        
        this.fluid1 = fluid1;
        this.fluid2 = fluid2;
        this.pump1 = pump1;
        this.pump2 = pump2;
        calcEfficiency();
    }
    
    // Methods --------------------------------------------------------------------------------------------------------//
    
    public double CalcExchangerHeat(double collectorHeat)
    {
        this.exchangerHeat = collectorHeat;
        return this.exchangerHeat;
    }
    
    public double CalcTempInExchanger2()
    {
        double mCp1 = pump1.getFlowRate()*fluid1.getCp();
        double mCp2 = pump2.getFlowRate()*fluid2.getCp();
        double mCp = Math.min(mCp1,mCp2);
        
        this.TempInExchanger2 = exchangerHeat/(exchangerEfficiency*mCp) + TempOutExchanger1;
        return this.TempInExchanger2;
    }
    
    public double CalcTempOutExchanger1(double TempInExchanger2, double TempInExchanger1)
    {
        double mCp1 = pump1.getFlowRate()*fluid1.getCp();
        double mCp2 = pump2.getFlowRate()*fluid2.getCp();
        double mCp = Math.min(mCp1,mCp2);
       
        return TempInExchanger1-(exchangerEfficiency*mCp/mCp1)*(TempInExchanger1-TempInExchanger2);
        
    }
    
    public double CalcTempOutExchanger2(double TempInExchanger1, double TempOutExchanger1, double TempInExchanger2)
    {
        double mCp1 = pump1.getFlowRate()*fluid1.getCp();
        double mCp2 = pump2.getFlowRate()*fluid2.getCp();
        double coef;
        
        if (mCp2 == 0)
            coef = 0;
        else
            coef = mCp1/mCp2;
        
        return coef*(TempInExchanger1-TempOutExchanger1) + TempInExchanger2;
        
    }
    
    public void calcEfficiency()
    {
        double mCp1 = pump1.getFlowRate()*fluid1.getCp();
        double mCp2 = pump2.getFlowRate()*fluid2.getCp();
              
        if ((mCp1 == 0) || ( mCp2 == 0))
        {
            this.exchangerEfficiency = 0;
        }
        else
        {
            double min = Math.min(mCp1,mCp2);
            double max = Math.max(mCp1,mCp2);
            double NTU = exchangeArea*transferCoef/min;
            double C = min/max;
                
            if (C != 1)
                this.exchangerEfficiency = (1-Math.exp(-NTU*(1-C)))/(1-C*Math.exp(-NTU*(1-C)));
            else
                this.exchangerEfficiency  = NTU/(1+NTU);
        }
    }
    
    // Getters --------------------------------------------------------------------------------------------------------//

    public double getTempInExchanger2() 
    {
        return TempInExchanger2;
    }

    public double getTempOutExchanger2() 
    {
        return TempOutExchanger2;
    }

    public double getCalorExchanger() 
    {
        return exchangerHeat;
    }
    
    public Pump getPump1()
    {
        return pump1;
    }
    
    public Pump getPump2()
    {
        return pump2;
    }
    
    public double getExchangerEfficiency()
    {
        return exchangerEfficiency;
    }
}
