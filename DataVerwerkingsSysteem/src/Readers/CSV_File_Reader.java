/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CarPositionData;
import DatabaseClasses.CarStatusEvent;
import DatabaseClasses.Database_Manager;
import DatabaseClasses.EntityClass;
import DatabaseClasses.HsdpaConnection;
import DatabaseClasses.OverallConnection;
import DatabaseClasses.TcpClientConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ronald & Elize
 */
public class CSV_File_Reader extends Thread{
    
    private static final int TICKS_PER_SECOND = 60;
    public static boolean reading = false;
    
    public CSV_File_Reader(){
        
    }

    
    protected static Date getDateByString(String string){
    
        Date dateFormatted = null; 
        try {
            dateFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" )).parse(string);
        } catch (ParseException ex) {
            System.out.println(ex);
        }finally{
        return dateFormatted;
        }
    }
    
    /**
     * Calculate the average of the given sum.
     * Used by the readAndInsertMonitoringCSV file.
     * @param sum
     * @return the average
     */
    protected static long calculateAverageByTicks(long sum){
        return sum / TICKS_PER_SECOND;
    }
    
    /**
     * @Author: someone from internet.
     * Website: http://forum.geocaching.nl/index.php?showtopic=7886
     * @param rx
     * @param ry
     * @return the longitude, latitude in a double[].
     */
    protected static long[] calculateLongAndLatFromRxAndRy(long rx, long ry){
        
        long x = rx;
        long y = ry;
        double dX = (x - 155000.0) * Math.pow(10, -5);
	double dY = (y - 463000.0) * Math.pow(10, -5);

	double somN = ((3235.65389 * dY) + (-32.58297 * Math.pow(dX, 2)) + 
                (-0.2475 * Math.pow(dY, 2)) + (-0.84978 * Math.pow(dX, 2) * dY) + 
                (-0.0655 * Math.pow(dY, 3)) + (-0.01709 * Math.pow(dX, 2) * Math.pow(dY, 2)) + 
                (-0.00738 * dX) + (0.0053 * Math.pow(dX, 4)) + 
                (-0.00039 * Math.pow(dX, 2) * Math.pow(dY, 3)) + 
                (0.00033 * Math.pow(dX, 4) * dY) + (-0.00012 * dX * dY));
	double somE = (5260.52916 * dX) + (105.94684 * dX * dY) + 
                (2.45656 * dX * Math.pow(dY, 2)) + (-0.81885 * Math.pow(dX, 3)) + 
                (0.05594 * dX * Math.pow(dY, 3)) + (-0.05607 * Math.pow(dX, 3) * dY) + 
                (0.01199 * dY) + (-0.00256 * Math.pow(dX, 3) * Math.pow(dY, 2)) + 
                (0.00128 * dX * Math.pow(dY, 4)) + (0.00022 * Math.pow(dY, 2)) + 
                (-0.00022 * Math.pow(dX, 2)) + (0.00026 * Math.pow(dX, 5));

	long	latitude = Double.doubleToLongBits(52.15517 + (somN / 3600));
	long	longitude = Double.doubleToLongBits(5.387206 + (somE / 3600));
        
        long[] latAndLongArray = {latitude, longitude};
        return latAndLongArray;
    }
    
    
}
