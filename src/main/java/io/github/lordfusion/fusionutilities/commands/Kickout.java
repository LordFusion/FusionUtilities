package io.github.lordfusion.fusionutilities.commands;

import com.earth2me.essentials.User;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import io.github.lordfusion.fusionutilities.DataManager;
import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kickout implements CommandExecutor
{
    private DataManager dataManager;
    
    private static TextComponent HELP;
    private static TextComponent MSG_CONSOLE_UNAVAILABLE, MSG_TOWNY_DISABLED, MSG_NOT_RESIDENT, MSG_NO_TOWN,
            MSG_PLAYER_NOT_FOUND, MSG_KICK_SUCCESS, MSG_KICK_FAIL, MSG_YOU_WERE_KICKED, MSG_NOT_IN_TOWN;
    
    public Kickout()
    {
        this.dataManager = FusionUtilities.getInstance().getDataManager();
    
        this.setupMsgConsoleUnavailable();
        this.setupMsgTownyDisabled();
        this.setupMsgNotResident();
        this.setupMsgNoTown();
        this.setupMsgPlayerNotFound();
        this.setupMsgKickSuccess();
        this.setupMsgKickFailure();
        this.setupMsgYouWereKicked();
        this.setupMsgNotInTown();
        this.setupHelp();
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
        // Ensure that the argument is an actual player
        Player target = findPlayer(args[0]);
        if (target == null) {
            TextComponent msg = (TextComponent)MSG_PLAYER_NOT_FOUND.duplicate();
            msg.addExtra(args[0]);
            FusionUtilities.sendUserMessage(sender, msg);
            FusionUtilities.sendConsoleInfo(sender.getName() + " could not use /kickout; Invalid player.");
            return true;
        }
    
        // Get command-sender resident data
        Resident resident = this.dataManager.getTowny().getTownyUniverse().getResidentMap()
                .get(sender.getName().toLowerCase());
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
        
        // Get target position data
        int chunkX = target.getLocation().getChunk().getX();
        int chunkZ = target.getLocation().getChunk().getZ();
        
        for (TownBlock townBlock : town.getTownBlocks()) {
            if (townBlock.getX() == chunkX && townBlock.getZ() == chunkZ) {
                if (kickOut(target)) {
                    FusionUtilities.sendUserMessage(sender, MSG_KICK_SUCCESS);
                    FusionUtilities.sendUserMessage(target, MSG_YOU_WERE_KICKED);
                    FusionUtilities.sendConsoleInfo(sender.getName() + " ejected " + target.getName() + " from town " +
                            town.getName());
                } else {
                    FusionUtilities.sendUserMessage(sender, MSG_KICK_FAIL);
                    FusionUtilities.sendConsoleWarn("Tried and failed to kick " + target.getName() + " from " + sender +
                            "'s town: " + town.getName());
                }
                return true;
            }
        }
        
        FusionUtilities.sendUserMessage(sender, MSG_NOT_IN_TOWN);
        FusionUtilities.sendConsoleInfo(sender.getName() + " tried to kick " + target.getName() + " from " +
                town.getName() + ", but target is not within town.");
        return true;
    }
    
    private static Player findPlayer(String name)
    {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name) || player.getDisplayName().equalsIgnoreCase(name) ||
                    player.getName().contains(name) || player.getDisplayName().contains(name))
                return player;
        }
        return null;
    }
    
    private boolean kickOut(Player player)
    {
        Location spawnLocation = player.getWorld().getSpawnLocation();
        
        // Override /back so that the person that got kicked out can't easily come back
        User essentialsUser = this.dataManager.getEssentials().getUser(player);
        
        boolean output = player.teleport(spawnLocation);
        essentialsUser.setLastLocation(spawnLocation);
        
        return output;
    }
    
    /* MESSAGES ******************************************************************************************** MESSAGES */
    private void setupMsgConsoleUnavailable()
    {
        MSG_CONSOLE_UNAVAILABLE = new TextComponent("This command is only available for players.");
        MSG_CONSOLE_UNAVAILABLE.setColor(ChatColor.RED);
    }
    private void setupMsgTownyDisabled()
    {
        MSG_TOWNY_DISABLED = new TextComponent("Towny is not available on this server.");
        MSG_TOWNY_DISABLED.setColor(ChatColor.DARK_RED);
    }
    private void setupMsgNotResident()
    {
        MSG_NOT_RESIDENT = new TextComponent("This command is only available for residents.");
        MSG_NOT_RESIDENT.setColor(ChatColor.RED);
    }
    private void setupMsgNoTown()
    {
        MSG_NO_TOWN = new TextComponent("You are not part of a town.");
        MSG_NO_TOWN.setColor(ChatColor.RED);
    }
    private void setupMsgPlayerNotFound()
    {
        MSG_PLAYER_NOT_FOUND = new TextComponent("Player not found: ");
        MSG_PLAYER_NOT_FOUND.setColor(ChatColor.YELLOW);
    }
    private void setupMsgKickSuccess()
    {
        MSG_KICK_SUCCESS = new TextComponent("Successfully kicked from your town: ");
        MSG_KICK_SUCCESS.setColor(ChatColor.GREEN);
    }
    private void setupMsgKickFailure()
    {
        MSG_KICK_FAIL = new TextComponent("Failed to kick player from your town. Contact an admin.");
        MSG_KICK_FAIL.setColor(ChatColor.DARK_RED);
    }
    private void setupMsgYouWereKicked()
    {
        MSG_YOU_WERE_KICKED = new TextComponent("You were driven out of a town.");
        MSG_YOU_WERE_KICKED.setColor(ChatColor.LIGHT_PURPLE);
    }
    private void setupMsgNotInTown()
    {
        MSG_NOT_IN_TOWN = new TextComponent("That player isn't inside your town.");
        MSG_NOT_IN_TOWN.setColor(ChatColor.YELLOW);
    }
    
    private void setupHelp()
    {
        HELP = new TextComponent("/ko <username> - Teleports the user out of your town.");
        HELP.setColor(ChatColor.BLUE);
    }
}
