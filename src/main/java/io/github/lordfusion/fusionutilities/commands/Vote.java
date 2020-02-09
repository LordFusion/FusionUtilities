package io.github.lordfusion.fusionutilities.commands;

import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor
{
    TextComponent[] msgs;
    
    public Vote() {}
    
    /**
     * Executes the given command, returning its success
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
        if (sender instanceof Player)
            FusionUtilities.sendUserMessages(sender, msgs);
        else
            sender.sendMessage("Command not supported.");
        return true;
    }
    
    /**
     * Pre-generate the help menu shown to players when they run "/vote"
     */
    private void setupMsgs()
    {
        this.msgs = new TextComponent[3];
        
        this.msgs[0] = new TextComponent("Thanks for voting for our server!");
        this.msgs[0].setItalic(true);
        this.msgs[0].setColor(ChatColor.GOLD);
        
        this.msgs[1] = new TextComponent("FTB Servers: ");
        this.msgs[1].setColor(ChatColor.DARK_AQUA);
        TextComponent link1 = new TextComponent("ftbservers.com/server/153BIFTF/vote");
        link1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://ftbservers.com/server/153BIFTF/vote"));
        link1.setColor(ChatColor.AQUA);
        this.msgs[1].addExtra(link1);
        
        this.msgs[2] = new TextComponent("MinecraftMP: ");
        this.msgs[2].setColor(ChatColor.DARK_AQUA);
        TextComponent link2 = new TextComponent("minecraft-mp.com/server/179220/vote");
        link2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft-mp.com/server/179220/vote"));
        link2.setColor(ChatColor.AQUA);
        this.msgs[2].addExtra(link2);
    }
    
    
}
