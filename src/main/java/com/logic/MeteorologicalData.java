package com.logic;
import java.io.IOException;
import com.db.DBAccess;

public class MeteorologicalData 
{
    private int hourYear;
    private double hotizontalIrradiation;
    private double roomTemperature;
    private double shades;

    public MeteorologicalData(int hourYear) throws IOException, ClassNotFoundException {
        DBAccess aDB = DBAccess.getInstance();
        this.hourYear = hourYear;
        this.hotizontalIrradiation = aDB.getHotizontalIrradiation(hourYear);
        this.roomTemperature = aDB.getRoomTemperature(hourYear);
        this.shades = aDB.getShades(hourYear);
    }
    
    // Getters --------------------------------------------------------------------------------------------------------//

    public int getHourYear() {
        return hourYear;
    }

    public double getHotizontalIrradiation() {
        return hotizontalIrradiation;
    }

    public double getRoomTemperature() {
        return roomTemperature;
    }

    public double getShades() {
        return shades;
    }
}
