package io.github.lordfusion.fusionutilities.commands;

import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Donate implements CommandExecutor
{
    TextComponent[] msgs;
    
    public Donate()
    {
        this.setupMsgs();
    }
    
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
        FusionUtilities.sendUserMessages(sender, msgs);
        return true;
    }
    
    /**
     * Pre-generate the help menu shown to players when they run "/vote"
     */
    private void setupMsgs()
    {
        this.msgs = new TextComponent[2];
        
        this.msgs[0] = new TextComponent("Thanks for considering a donation to our server!");
        this.msgs[0].setItalic(true);
        this.msgs[0].setColor(ChatColor.GOLD);
        
        this.msgs[1] = new TextComponent("Donation link: ");
        this.msgs[1].setColor(ChatColor.DARK_AQUA);
        TextComponent link1 = new TextComponent("goreacraft.tebex.io");
        link1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://goreacraft.tebex.io/"));
        link1.setColor(ChatColor.AQUA);
        this.msgs[1].addExtra(link1);
    }
}
