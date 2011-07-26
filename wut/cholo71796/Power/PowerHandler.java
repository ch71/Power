/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.Power;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wut.cholo71796.Power.utilities.Log;

/**
 *
 * @author Cole Erickson
 */
public class PowerHandler {
    public static void setPower(String player, int power) {
        Power.manageSQLite.insertQuery("INSERT INTO power (player, amount) VALUES ('" + player + "', " + power + ");");
    }
    
    public static int getPower(String player) {
        try {
            return Power.manageSQLite.sqlQuery("SELECT * FROM power WHERE player = '" + player + "'").getInt("amount");
        } catch (SQLException ex) {
            Logger.getLogger(Power.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.severe("Error when retrieving data for " + player);
        return -101011101;
    }
}
