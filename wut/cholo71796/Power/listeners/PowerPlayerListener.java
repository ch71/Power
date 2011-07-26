/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.Power.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import wut.cholo71796.Power.Power;
import wut.cholo71796.Power.PowerConfig;
import wut.cholo71796.Power.PowerHandler;

/**
 *
 * @author Cole Erickson
 */
public class PowerPlayerListener extends PlayerListener {
    public static Power plugin;
    public PowerPlayerListener(Power instance) {
        plugin = instance;
    }
    
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        PowerHandler.setPower(event.getPlayer().getName(), PowerConfig.getInitialAmount());
    }
}
