/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.Power.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wut.cholo71796.Power.PowerHandler;

/**
 *
 * @author Cole Erickson
 */
public class PowerCommand implements CommandExecutor {
    
    private Player player;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("That command must be ran by a player.");
            return false;
        } else
            player = (Player) sender;
        
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "You have " + ChatColor.WHITE + PowerHandler.getPower(player.getName()) + " power" + ChatColor.GOLD + ".");
            return true;
        }
//        } else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("grant") || args[0].equalsIgnoreCase("add")) {
//            //T
//        }
//        
        return false;
    }
}
