package io.github.lordfusion.fusionutilities.commands;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.lordfusion.fusionutilities.DataManager;
import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kickout implements CommandExecutor
{
    private DataManager dataManager;
    
    private static TextComponent HELP;
    private static TextComponent MSG_CONSOLE_UNAVAILABLE, MSG_TOWNY_DISABLED, MSG_NOT_RESIDENT, MSG_NO_TOWN;
    
    public Kickout()
    {
        this.dataManager = FusionUtilities.getInstance().getDataManager();
    }
    
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // Ensure that the command is sent by a player
        if (!(sender instanceof Player)) {
            FusionUtilities.sendUserMessage(sender, MSG_CONSOLE_UNAVAILABLE);
            return true;
        }
        // Ensure that Towny is available
        if (!this.dataManager.isTownyEnabled()) {
            FusionUtilities.sendUserMessage(sender, MSG_TOWNY_DISABLED);
            FusionUtilities.sendConsoleWarn("Tried to use /kickout while Towny is missing!");
            return true;
        }
        // Ensure that there is an argument
        if (args.length != 1) {
            FusionUtilities.sendUserMessage(sender, HELP);
            FusionUtilities.sendConsoleInfo(sender.getName() + " could not use /kickout; Incorrect arguments.");
            return true;
        }
    
        Resident resident = this.dataManager.getTowny().getTownyUniverse().getResidentMap().get(sender.getName());
        if (resident == null) {
            FusionUtilities.sendUserMessage(sender, MSG_NOT_RESIDENT);
            FusionUtilities.sendConsoleInfo(sender.getName() + " could not use /kickout; They are not a Resident.");
            return true;
        }
        Town town;
        try {
            town = resident.getTown();
        } catch (NotRegisteredException e) {
            FusionUtilities.sendUserMessage(sender, MSG_NO_TOWN);
            FusionUtilities.sendConsoleInfo(sender.getName() + " could not use /kickout; They are not in a town.");
            return true;
        }
        
        
        
        return true;
    }
}
