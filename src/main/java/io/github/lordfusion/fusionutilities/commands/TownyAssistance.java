package io.github.lordfusion.fusionutilities.commands;

import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TownyAssistance implements CommandExecutor
{
    TextComponent[] basicsMessage, nationsMessage, settingsMessage, outpostsMessage, helpMenu;
    
    public TownyAssistance()
    {
        setupBasicsMessage();
        setupNationsMessage();
        setupSettingsMessage();
        setupOutpostsMessage();
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
            case "basics":
                FusionUtilities.sendUserMessages(sender, this.basicsMessage);
                break;
            case "nation":
            case "nations":
                FusionUtilities.sendUserMessages(sender, this.nationsMessage);
                break;
            case "settings":
                FusionUtilities.sendUserMessages(sender, this.settingsMessage);
                break;
            case "outposts":
            case "outpost":
                FusionUtilities.sendUserMessages(sender, this.outpostsMessage);
                break;
            case "menu":
            case "":
            default:
                FusionUtilities.sendUserMessages(sender, this.helpMenu);
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
        
        this.helpMenu[2] = new TextComponent(" Nations and you: ");
        this.helpMenu[2].setColor(ChatColor.AQUA);
        TextComponent cmdMsg2 = new TextComponent("/townyhelp nations");
        cmdMsg2.setItalic(true);
        this.helpMenu[2].addExtra(cmdMsg2);
        this.helpMenu[2].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/townyhelp nations"));
        
        this.helpMenu[3] = new TextComponent(" Town and Nation settings: ");
        this.helpMenu[3].setColor(ChatColor.BLUE);
        TextComponent cmdMsg3 = new TextComponent("/townyhelp settings");
        cmdMsg3.setItalic(true);
        this.helpMenu[3].addExtra(cmdMsg3);
        this.helpMenu[3].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/townyhelp settings"));
        
        this.helpMenu[4] = new TextComponent(" Outpost claiming: ");
        this.helpMenu[4].setColor(ChatColor.AQUA);
        TextComponent cmdMsg4 = new TextComponent("/townyhelp outposts");
        cmdMsg4.setItalic(true);
        this.helpMenu[4].addExtra(cmdMsg4);
        this.helpMenu[4].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/townyhelp outposts"));
        
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
        this.basicsMessage[2] = new TextComponent(" 2. Create a town: \"/t new <name>\"");
        this.basicsMessage[2].setColor(ChatColor.AQUA);
        this.basicsMessage[3] = new TextComponent(" 3. Add money for taxes and and claiming: \"/t deposit <amount>\"");
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
    
    /**
     * Pre-generate the message shown to players when they run "/townyhelp nations"
     */
    private void setupNationsMessage()
    {
        this.nationsMessage = new TextComponent[8];
        this.nationsMessage[0] = new TextComponent("Helpful information for Towny's Nations:");
        this.nationsMessage[0].setColor(ChatColor.LIGHT_PURPLE);
        this.nationsMessage[1] = new TextComponent(" Nations are a grouping of towns. Benefits include:");
        this.nationsMessage[1].setColor(ChatColor.BLUE);
        this.nationsMessage[2] = new TextComponent(" - Bonus chunks (10 to every town, per 10 Nation residents)");
        this.nationsMessage[2].setColor(ChatColor.AQUA);
        this.nationsMessage[3] = new TextComponent(" - Ally permissions (give the nation access to your plots)");
        this.nationsMessage[3].setColor(ChatColor.BLUE);
        this.nationsMessage[4] = new TextComponent(" - Nation chat and taxes (similar to their town variants)");
        this.nationsMessage[4].setColor(ChatColor.AQUA);
        this.nationsMessage[5] = new TextComponent(" 1) Create a nation: \"/n create <name>\"");
        this.nationsMessage[5].setColor(ChatColor.BLUE);
        this.nationsMessage[6] = new TextComponent(" 2) Invite other towns: \"/n add <town name>\"");
        this.nationsMessage[6].setColor(ChatColor.AQUA);
        this.nationsMessage[7] = new TextComponent(" 3) See your nation's bank and other info: \"/nation\"");
        this.nationsMessage[7].setColor(ChatColor.BLUE);
    }
    
    /**
     * Pre-generate the message shown to players when they run "/townyhelp settings"
     */
    private void setupSettingsMessage()
    {
        this.settingsMessage = new TextComponent[6];
        this.settingsMessage[0] = new TextComponent("Some common Town and Nation settings:");
        this.settingsMessage[0].setColor(ChatColor.LIGHT_PURPLE);
        this.settingsMessage[1] = new TextComponent(" - Town spawn: \"/t set spawn\" and \"/t spawn\"");
        this.settingsMessage[1].setColor(ChatColor.BLUE);
        this.settingsMessage[2] = new TextComponent(" - Town toggles: \"/t toggle <fire/pvp/explosion/mobs>\"");
        this.settingsMessage[2].setColor(ChatColor.AQUA);
        this.settingsMessage[3] = new TextComponent(" - Taxes: \"/<t/n> set taxes <amount>\"");
        this.settingsMessage[3].setColor(ChatColor.BLUE);
        this.settingsMessage[4] = new TextComponent(" - Tax Percent: \"/t toggle taxpercent\"");
        this.settingsMessage[4].setColor(ChatColor.AQUA);
        this.settingsMessage[5] = new TextComponent(" - Tags (up to 4 characters): \"/<t/n> set tag <tag>\"");
        this.settingsMessage[5].setColor(ChatColor.BLUE);
    }
    
    /**
     * Pre-generates the message shown to players when they run "/townyhelp outposts"
     */
    private void setupOutpostsMessage()
    {
        this.outpostsMessage = new TextComponent[4];
        this.outpostsMessage[0] = new TextComponent("How to claim land away from your main town, with outposts:");
        this.outpostsMessage[0].setColor(ChatColor.LIGHT_PURPLE);
        this.outpostsMessage[1] = new TextComponent(" 1) Find and stand in the chunk you want to claim.");
        this.outpostsMessage[1].setColor(ChatColor.BLUE);
        this.outpostsMessage[2] = new TextComponent(" 2) Claim the outpost: \"/t claim outpost\"");
        this.outpostsMessage[2].setColor(ChatColor.AQUA);
        this.outpostsMessage[3] = new TextComponent(" 3) Claim nearby chunks as normal: \"/t claim\"");
        this.outpostsMessage[3].setColor(ChatColor.BLUE);
    }
}
