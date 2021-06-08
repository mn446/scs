package com.db;

public class Query 
{
    public String queryHorizontalIrradiation(int hourYear)
    {
        return "select horizontal_irradiation from Meteorological_data where ID = " + hourYear;
    }
    
    public String queryRoomTemperature(int hourYear)
    {
        return "select room_temperature from Meteorological_data where ID = " + hourYear;
    }
        
    public String queryShade(int hourYear)
    {
        return "select shades from Meteorological_data where ID = " + hourYear;
    }
}
