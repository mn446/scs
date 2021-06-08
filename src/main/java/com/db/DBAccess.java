package com.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBAccess 
{
    // Singleton instance
    private static DBAccess dbAccess = null;
    public Connection con;

    // private constructor
    private DBAccess() throws IOException, ClassNotFoundException
    {
        try
        {
            con = DriverManager.getConnection("jdbc:sqlite:solar_data.db");
            System.out.println("Connected to db...");
        }
        catch (SQLException e) 
        {
                System.out.println("Error while connecting " + e);
        }
    }

    // static method to create instance of Singleton class
    public static DBAccess getInstance() throws IOException, ClassNotFoundException
    {
        if (dbAccess == null)
            dbAccess = new DBAccess();

        return dbAccess;
    }
    
   
    public double getHotizontalIrradiation(int hourYear)
    {
        double HI = 0;
        Query query = new Query();
        String getHI = query.queryHorizontalIrradiation(hourYear); 

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getHI);
            if(rs.next()) { 
                HI = rs.getDouble("HORIZONTAL_IRRADIATION"); 
            }
        }
        catch (SQLException e) 
        {
                e.printStackTrace();
        } 
        return HI;
    }
    
    public double getRoomTemperature(int hourYear)
    {
        double RT = 0;
        Query query = new Query();
        String getRT = query.queryRoomTemperature(hourYear); 

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getRT);
            if(rs.next()) { 
                RT = rs.getDouble("ROOM_TEMPERATURE"); 
            }
        }
        catch (SQLException e) 
        {
                e.printStackTrace();
        } 
        return RT;
    }
    
    public double getShades(int hourYear)
    {
        double Shade = 0;
        Query query = new Query();
        String getShade = query.queryShade(hourYear); 

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getShade);
            if(rs.next()) { 
                Shade = rs.getDouble("SHADES"); 
            }
        }
        catch (SQLException e) 
        {
                e.printStackTrace();
        } 
        return Shade;
    }

 }


