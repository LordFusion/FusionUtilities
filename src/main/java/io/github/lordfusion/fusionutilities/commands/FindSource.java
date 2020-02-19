package io.github.lordfusion.fusionutilities.commands;

import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class FindSource implements CommandExecutor
{
    private static TextComponent MSG_NO_CMD, MSG_SUCCESS;
    
    public FindSource()
    {
        setupMsgNoCmd();
        setupMsgSuccess();
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
        PluginCommand cmd = Bukkit.getPluginCommand(args[0]);
        if (cmd == null) {
            FusionUtilities.sendUserMessage(sender, MSG_NO_CMD);
            return true;
        }
        
        String source = cmd.getPlugin().getName();
        TextComponent output = (TextComponent)MSG_SUCCESS.duplicate();
        output.addExtra(source);
        
        FusionUtilities.sendUserMessage(sender, output);
        return true;
    }
    
    private void setupMsgNoCmd()
    {
        MSG_NO_CMD = new TextComponent("That command was not found.");
        MSG_NO_CMD.setColor(ChatColor.RED);
    }
    
    private void setupMsgSuccess()
    {
        MSG_SUCCESS = new TextComponent("Command source found: ");
        MSG_SUCCESS.setColor(ChatColor.DARK_AQUA);
    }
}
