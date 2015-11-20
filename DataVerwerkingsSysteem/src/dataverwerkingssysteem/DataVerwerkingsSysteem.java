/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataverwerkingssysteem;

import DatabaseClasses.Database_Manager;
import Readers.PositionsCSVReaderThread;

/**
 *
 * @author Elize
 */
public class DataVerwerkingsSysteem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Database_Manager dbManager = new Database_Manager();
        dbManager.start();
        
        PositionsCSVReaderThread reader = new PositionsCSVReaderThread();
        reader.start();
        
    }
    
}
