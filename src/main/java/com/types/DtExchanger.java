package com.types;

public class DtExchanger 
{
    // Exchanger parameters
    private double transferCoef;
    private double exchangeArea;
    
    public DtExchanger(double transferCoef, double exchangeArea) 
    {
        this.transferCoef = transferCoef;
        this.exchangeArea = exchangeArea;
    }

    // Getters --------------------------------------------------------------------------------------------------------//
    
    public double getTransferCoef() 
    {
        return transferCoef;
    }

    public double getExchangeSurface() 
    {
        return exchangeArea;
    }
      
}
