/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.Power;

import java.io.File;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author Cole Erickson
 */
public class PowerConfig {
    
    private static Configuration config;
    private static int initialAmount;
    private static int distributionInterval;
    private static int amountPerDistribution;
    private static int maximumAmount;
    
    public PowerConfig() {
        config = new Configuration(new File(Power.dataFolder, "config.yml"));
        config.load();
        if (!Power.dataFolder.exists()) {
            Power.dataFolder.mkdirs();
            config.setProperty("initial amount", 3);
            config.setProperty("distribution interval", 1);
            config.setProperty("amount per distribution", 1);
            config.setProperty("maximum amount", 12);
            config.save();
        }
        initialAmount = config.getInt("initial amount", 3);
        distributionInterval = config.getInt("distribution interval", 1);
        amountPerDistribution = config.getInt("amount per distribution", 1);
        maximumAmount = config.getInt("maximum amount", 12);
    }

    public static int getAmountPerDistribution() {
        return amountPerDistribution;
    }

    public static Long getDistributionInterval() {
        return new Long(distributionInterval * 1000 * 60);
    }

    public static int getMaximumAmount() {
        return maximumAmount;
    }
    
    public static int getInitialAmount() {
        return initialAmount;
    }
}
