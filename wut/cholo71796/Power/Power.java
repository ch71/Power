/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.Power;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import wut.cholo71796.Power.commands.PowerCommand;
import wut.cholo71796.Power.listeners.PowerPlayerListener;
import wut.cholo71796.Power.utilities.Log;
import wut.cholo71796.com.alta189.sqlLibrary.SQLite.sqlCore;

/**
 *
 * @author Cole Erickson
 */
public class Power extends JavaPlugin {
    private final PowerPlayerListener playerListener = new PowerPlayerListener(this);
    
    public static Map<String, Integer> Power = new HashMap<String, Integer>();
    
    public static Plugin plugin;
    public static File dataFolder;
    
    private static PluginDescriptionFile pdfFile;
    public static sqlCore manageSQLite;
    
    @Override
    public void onDisable() {
        //TODO store data
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        pdfFile = plugin.getDescription();
        dataFolder = getDataFolder();
        
        new Log(pdfFile.getName());
        new PowerConfig();
        
        loopCheck();
        
        getCommand("power").setExecutor(new PowerCommand());
        setupSql();
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
        
        Log.info("Enabled.");
    }
    
    private static void setupSql() {
        manageSQLite = new sqlCore("power", dataFolder);
        manageSQLite.initialize();
        if (!manageSQLite.checkTable("power")) {
            Log.info("creating SQL table.");
            String query = "CREATE TABLE power (id INT AUTO_INCREMENT PRIMARY_KEY, player VARCHAR(255), amount INT);";
            manageSQLite.createTable(query); // Use sqlCore.createTable(query) to create tables
        }
    }
    
    private static void loopCheck() {
        if (PowerConfig.getDistributionInterval() == 0) {
            Log.info("Distribution disabled.");
        }
        
        plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
            Long iii = System.currentTimeMillis();
            @Override
            public void run() {
                Log.info("ticked");
                if (System.currentTimeMillis() > iii) {
                    Log.info("cycled");
                    try {
                        ResultSet results = manageSQLite.sqlQuery("SELECT * FROM power");
                        while (results.next()) {
                            Log.info("subcycled");
                            int money = results.getInt("amount");
                            String player = results.getString("player");
                            
                            if (money + PowerConfig.getAmountPerDistribution() > PowerConfig.getMaximumAmount())
                                money = PowerConfig.getMaximumAmount();
                            else
                                money += PowerConfig.getAmountPerDistribution();
                            
                            manageSQLite.updateQuery("UPDATE power SET amount=" + money + " WHERE player='" + player + "';");
                        }
                        results.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Power.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                    iii += PowerConfig.getDistributionInterval();
                }
            }
        }, 300L, 300L);
    }
}