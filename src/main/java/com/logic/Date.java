package com.logic;

public class Date 
{
    private int hour;
    private int day;
    
    private int monthNumber;
    private String monthName;

    public Date(int hourYear) 
    {
        int day = (hourYear)/24 + 1;
        
        if (day <= 31)  
        {
            this.monthNumber = 1;
            this.monthName = "January";
            this.day = day;
        }
        else if ((day >= 32) && (day < 60))
        {
            this.monthNumber = 2;
            this.monthName = "February";
            this.day = day - 32 + 1;
        }      
        else if ((day >= 60) && (day < 91))
        {
            this.monthNumber = 3;
            this.monthName = "March";
            this.day = day - 60 + 1;
        }    
        else if ((day >= 91) && (day < 121))
        {
            this.monthNumber = 4;
            this.monthName = "April";
            this.day = day - 91 + 1;
        }    
        else if ((day >= 121) && (day < 152))
        {
            this.monthNumber = 5;
            this.monthName = "May";
            this.day = day - 121 + 1;
         }   
        else if ((day >= 152) && (day < 182))
        {    
            this.monthNumber = 6;
            this.monthName = "June";
            this.day = day - 152 + 1;
        }    
        else if ((day >= 182) && (day < 213))
        {
            this.monthNumber = 7;
            this.monthName = "July";
            this.day = day - 182 + 1;
        }        
        else if ((day >= 213) && (day < 244))
        {
            this.monthNumber = 8;
            this.monthName = "August";
            this.day = day - 213 + 1;
        }     
        else if ((day >= 244) && (day < 274))
        {
            this.monthNumber = 9;
            this.monthName = "September";
            this.day = day - 244 + 1;
        }        
        else if ((day >= 274) && (day < 305))
        {
        this.monthNumber = 10;
        this.monthName = "October";
        this.day = day - 274 + 1;
        }          
        else if ((day >= 305) && (day < 335))
        {
            this.monthNumber = 11;
            this.monthName = "November";
            this.day = day - 305 + 1;
        }        
            else if ((day >= 335) && (day < 365))
        {
            this.monthNumber = 12;
            this.monthName = "December";
            this.day = day - 335 + 1;
        }   
        
        this.hour = (hourYear)%24;
    }
    
    // Getters --------------------------------------------------------------------------------------------------------//

    public int getDay() 
    {
        return day;
    }

    public String getMonthName() 
    {
        return monthName;
    }
}
