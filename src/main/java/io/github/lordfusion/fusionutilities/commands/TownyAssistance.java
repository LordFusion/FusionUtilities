package io.github.lordfusion.fusionutilities.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownyAssistance implements CommandExecutor
{
    TextComponent[] basicsMessage, helpMenu;
    
    public TownyAssistance()
    {
        setupBasicsMessage();
        setupMenuMessage();
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
        String option = "";
        if (args.length > 0)
            option = args[0];
        
        switch (option) {
            case "menu":
            case "":
            default:
                for (TextComponent tc : this.helpMenu) {
                    ((Player)sender).spigot().sendMessage(tc);
                }
                break;
            case "basics":
                for (TextComponent tc : this.basicsMessage) {
                    ((Player)sender).spigot().sendMessage(tc);
                }
                break;
        }
        
        return true;
    }
    
    /**
     * Pre-generate the help menu shown to players when they run "/townyhelp"
     */
    private void setupMenuMessage()
    {
        this.helpMenu = new TextComponent[8]; // Todo: Shrink when finished
        
        this.helpMenu[0] = new TextComponent("TownyHelp Menu - Click a command in chat to run it!");
        this.helpMenu[0].setColor(ChatColor.LIGHT_PURPLE);
        TextComponent hoverMsg0 = new TextComponent("Executed by FusionUtilities");
        hoverMsg0.setColor(ChatColor.GRAY);
        this.helpMenu[0].setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{hoverMsg0}));
        
        this.helpMenu[1] = new TextComponent(" Getting started: ");
        this.helpMenu[1].setColor(ChatColor.BLUE);
        TextComponent cmdMsg1 = new TextComponent("/townyhelp basics");
        cmdMsg1.setItalic(true);
        this.helpMenu[1].addExtra(cmdMsg1);
        this.helpMenu[1].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/townyhelp basics"));
        
        this.helpMenu[2] = new TextComponent(" Nations and you: Coming soon!");
        this.helpMenu[2].setColor(ChatColor.AQUA);
        
        this.helpMenu[3] = new TextComponent(" Toggleable town settings: Coming soon!");
        this.helpMenu[3].setColor(ChatColor.BLUE);
        
        this.helpMenu[4] = new TextComponent(" Outpost claiming: Coming soon!");
        this.helpMenu[4].setColor(ChatColor.AQUA);
        
        this.helpMenu[5] = new TextComponent(" Town permissions: Coming soon!");
        this.helpMenu[5].setColor(ChatColor.BLUE);
        
        this.helpMenu[6] = new TextComponent(" Town ranks: Coming soon!");
        this.helpMenu[6].setColor(ChatColor.AQUA);
        
        this.helpMenu[7] = new TextComponent(" All commands: http://bit.ly/townyCommands");
        this.helpMenu[7].setColor(ChatColor.BLUE);
    }
    
    /**
     * Pre-generate the message shown to players when they run "/townyhelp basics"
     */
    private void setupBasicsMessage()
    {
        this.basicsMessage = new TextComponent[8];
        this.basicsMessage[0] = new TextComponent("How to Get Started with Towny:");
        this.basicsMessage[0].setColor(ChatColor.LIGHT_PURPLE);
        this.basicsMessage[1] = new TextComponent(" 1. Stand in the area you want to claim. (At your base)");
        this.basicsMessage[1].setColor(ChatColor.BLUE);
        this.basicsMessage[2] = new TextComponent(" 2. Create a town: \"/t new [name]\"");
        this.basicsMessage[2].setColor(ChatColor.AQUA);
        this.basicsMessage[3] = new TextComponent(" 3. Add money for taxes and and claiming: \"/t deposit [amount]\"");
        this.basicsMessage[3].setColor(ChatColor.BLUE);
        this.basicsMessage[4] = new TextComponent(" 4. Press F9 twice to see chunk boundaries.");
        this.basicsMessage[4].setColor(ChatColor.AQUA);
        this.basicsMessage[5] = new TextComponent(" 5. Claim additional chunks for $50 each: \"/t claim\"");
        this.basicsMessage[5].setColor(ChatColor.BLUE);
        this.basicsMessage[6] = new TextComponent(" 6. Invite your friends: \"/t add <username>\"");
        this.basicsMessage[6].setColor(ChatColor.AQUA);
        this.basicsMessage[7] = new TextComponent(" 7. See your town's bank and other info: \"/town\"");
        this.basicsMessage[7].setColor(ChatColor.BLUE);
    }
    
    
}
