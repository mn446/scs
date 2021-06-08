package com.logic;

public class Radiation 
{
     private final double latitude = -34.8453088;
     private final double longitude = -56.2337999;
     private double beta;
     private double azimut;
     private int hourYear;
     private int hourDay;
     private int day;
     private double par1;
     private double parTA;
     private double hotizontalIrradiation;
     private double tiltedIrradiation;
     private double atmosphericTransp;
     
    public Radiation(int hourYear, double beta, double azimut, MeteorologicalData meteorologicalData) 
    {
       this.hourYear = hourYear;
       this.beta = beta;
       this.azimut = azimut;
       this.day = calculateDay();
       this.hourDay = calculateHourDay();
       this.par1 = calculatePar1(hourDay);
       this.parTA = taParameter();
       this.hotizontalIrradiation = meteorologicalData.getHotizontalIrradiation();
       this.tiltedIrradiation = tiltedIrradiation();
       this.atmosphericTransp = atmosphericTransparency();
    } 

    // Methods --------------------------------------------------------------------------------------------------------//
    
    // Returns the day of the year based on the hour of the year (1 to 365 days) (it supposes days go from 0 to 23 hs)
    public int calculateDay()
    {
        return (hourYear)/24 + 1;
    }

    // Returns the hour of the day for a single day based on the hour of the year (0 to 23 hs)
    public int calculateHourDay()
    {
        return (hourYear)%24;
    }

    // Returns the parameter par1 used to calculate the declination angle and the time equation
    public double calculatePar1(int hour)
    {
        return 2*Math.PI*(day-1-((hour-1)-12)/24)/365;
    }

    // Returns the declination angle (In degrees)
    public double declinationAngle()
    {
        return  (0.006918-0.399912*Math.cos(par1)+0.070257*Math.sin(par1)-0.006758*Math.cos(2*par1)+0.000907*Math.sin(2*par1)-0.002697*Math.cos(3*par1)+0.001480*Math.sin(3*par1))*180/Math.PI;
    }

    // Returns the time equation's parameter (in degrees) 
    public double timeEquation(int parametro)
    {
        double par = calculatePar1(parametro);
        return  229.18*(0.00075+0.001868*Math.cos(par)-0.032077*Math.sin(par)-0.014615*Math.cos(2*par)-0.040849*Math.sin(2*par));
    }

    // Returns the horary angle in degrees (In the expression it is used (h-1) because it counts from 0 to 23 hs)
    public double horaryAngle()
    {
        int par;
        if (hourYear == 1)
            par = 1;
        else
            par = hourDay+24*(day-1);

        return  ((hourDay-1)-12+(longitude*24/360)+timeEquation(par)/60)*15;

    }

    // Returns Zenith angle's cosine
    public double cosZenith()
    {
        double latitudeRad = Math.toRadians(latitude);
        double declinationAngleRad = Math.toRadians(declinationAngle());
        double horaryAngleRad = Math.toRadians(horaryAngle());

        double cosZ = Math.cos(latitudeRad)*Math.cos(declinationAngleRad)*Math.cos(horaryAngleRad)+Math.sin(latitudeRad)*Math.sin(declinationAngleRad);

        if (cosZ < 0)
            cosZ=0;

        return cosZ;
    }

    // Returns the orbital factor
    public double orbitalFactor()
    {
        double par = Math.toRadians(360*day/365);
        return 1367*(1+0.033*Math.cos(par));
    }

    // Returns the extraterrestrial solar irradiation 
    public double extraterrestrialSolarRadiation()
    {
        return orbitalFactor()*cosZenith();
    }

    // Returns the atmospheric transparency
    public double atmosphericTransparency()
    {
        double HI = hotizontalIrradiation;
        double Gon = extraterrestrialSolarRadiation();
        
        if ((HI != 0) && (Gon != 0))
        {
            return HI/Gon;
        }

        else
        {
            return 0;     
        }

    }
    
    // Returns the difuse irradiation
    public double difuseIrradiation()
    {
        double costiZ = cosZenith();
        double AM;
        double HI = hotizontalIrradiation;

        if (costiZ != 0)
            AM = 1/costiZ;
        else
            AM = 0;

        double f0 = 0.992; 
        double f1 = -1.097; 
        double f2 = 3.107; 
        double f3 = -5.634; 
        double f4 = -0.133;
        double fDhra = (f0 + f1*Math.exp(-Math.exp(f2 + f3*atmosphericTransparency() + f4*AM)));
        // Model developed by the Engineering Faculty of UdelaR for the Uruguayan territory

        return fDhra*HI;
    }

    // Returns the difuse irradiation
    public double directIrradiation()
    {
        double HI = hotizontalIrradiation;
        double Id = difuseIrradiation();
        return HI - Id;
    }

    // Returns the incidence angle
    public double incidenceAngle()
    {

        double latitudeRad = Math.toRadians(latitude);
        double betaRad = Math.toRadians(beta);
        double azimutRad = Math.toRadians(azimut);
        double declinationAngleRad = Math.toRadians(declinationAngle());
        double horaryAngleRad = Math.toRadians(horaryAngle());


        double costi =Math.sin(declinationAngleRad)*Math.sin(latitudeRad)*Math.cos(betaRad)-Math.sin(declinationAngleRad)
        *Math.cos(latitudeRad)*Math.sin(betaRad)*Math.cos(azimutRad)+Math.cos(declinationAngleRad)*Math.cos(latitudeRad)
            *Math.cos(betaRad)*Math.cos(horaryAngleRad)+Math.cos(declinationAngleRad)*Math.sin(latitudeRad)*Math.sin(betaRad)
            *Math.cos(azimutRad)*Math.cos(horaryAngleRad)+Math.cos(declinationAngleRad)*Math.sin(betaRad)*Math.sin(azimutRad)
            *Math.sin(horaryAngleRad);

        if (costi < 0)
            return 0;
        else
            return costi;
    }

    // Returns the parameter to evaluate the depencency of tau-alpha (transmittance-absorbance) with the incidence angle
    public double taParameter()
    {
        double costi = incidenceAngle();

        if ((costi != 0) && ((1-0.136*((1/costi)-1)) > 0))
            return (1-0.136*((1/costi)-1));
        else
            return 0;
    }

    // Returns the RB parameter
    public double rbParameter()
    {
        double costiZ = cosZenith();
        double costi = incidenceAngle();

         if ((costiZ != 0) && (costi != 0))
            return costi/costiZ;
        else
            return 0;
    }

    // Returns the tilted irradiation
    public double tiltedIrradiation()
    {
        // Floor Reflectivity
        double rho_g = 0.2; 
        double betaRad = Math.toRadians(beta);

        // Vision factor
        double Fcs=(1+Math.cos(betaRad))/2; 
        double Fcg=(1-Math.cos(betaRad))/2;

        // Ai=Ib/Go parameter
        double Ib = directIrradiation();
        double Gon = extraterrestrialSolarRadiation();
        double Ai;

        if ((Ib != 0) && (Gon !=0))
            Ai = Ib/Gon;
        else
            Ai = 0;

        // f=(Ib/HI)^1/2 parameter
        double HI = hotizontalIrradiation;
        double f;

        if ((Ib != 0) && (HI != 0))
            f = Math.pow(Ib/HI, 0.5);
        else
            f=0;

        // Tilted irradiation
        double Id = difuseIrradiation();
        double Rb = rbParameter();

        double TI = (Ib+Id*Ai)*Rb + Id*(1-Ai)*Fcs* Math.pow(1 + f*Math.sin(Math.toRadians(beta/2)),3) + HI*rho_g*Fcg;

       // Removes negative values
        if (TI < 0)
            return 0;
        else
            return TI;
    }
    
    // Getters --------------------------------------------------------------------------------------------------------//

    public double getParTA() 
    {
        return parTA;
    }

    public double getTiltedIrradiation() 
    {
        return tiltedIrradiation;
    }
    
    public double getHotizontalIrradiation() 
    {
        return hotizontalIrradiation;
    }
    
    public int getHourDay() 
    {
        return hourDay;
    }
    
    public double getAtmosphericTransparency() 
    {
        return atmosphericTransp;
    }
}