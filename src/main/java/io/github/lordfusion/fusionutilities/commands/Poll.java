package io.github.lordfusion.fusionutilities.commands;

import io.github.lordfusion.fusionutilities.FusionUtilities;
import io.github.lordfusion.fusionutilities.utilities.PollInstance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Poll implements CommandExecutor
{
    private PollInstance instance;
    
    private static TextComponent MSG_NO_POLL, MSG_POLL_RUNNING, MSG_POLL_YES, MSG_POLL_NO, HELP_WEATHER, HELP_TIME,
            HELP_CUSTOM, MSG_TOO_LONG;
    private static TextComponent[] ALL_HELP;
    
    public Poll()
    {
        setupMsgNoPoll();
        setupMsgPollRunning();
        setupMsgPollYes();
        setupMsgPollNo();
        setupMsgTooLong();
        
        ALL_HELP = new TextComponent[3];
        setupMsgHelpWeather();
        setupMsgHelpTime();
        setupMsgHelpCustom();
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot participate in polls.");
            return true;
        }
        
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            // Todo: Help menu
            return true;
        }
        
        if (instance == null || !instance.isRunning()) { // No poll running
            if (args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("no") || args.length < 2) {
                FusionUtilities.sendUserMessage(sender, MSG_NO_POLL);
                return true;
            }
            if (args[0].equalsIgnoreCase("weather")) {
                // Start a weather poll?
                switch (args[1].toLowerCase()) {
                    case "sunny":
                    case "sun":
                    case "clear":
                        this.instance = new PollInstance(PollInstance.PollType.WEATHER, ((Player)sender).getPlayer(), "SUNNY");
                        break;
                    case "rain":
                        this.instance = new PollInstance(PollInstance.PollType.WEATHER, ((Player)sender).getPlayer(), "RAIN");
                        break;
                    case "storm":
                    case "thunder":
                        this.instance = new PollInstance(PollInstance.PollType.WEATHER, ((Player)sender).getPlayer(), "STORM");
                        break;
                    default:
                        FusionUtilities.sendUserMessage(sender, HELP_WEATHER);
                        break;
                }
            } else if (args[0].equalsIgnoreCase("time")) {
                // Start a time poll?
                switch (args[1].toLowerCase()) {
                    case "day":
                        this.instance = new PollInstance(PollInstance.PollType.TIME, ((Player)sender).getPlayer(), "DAY");
                        break;
                    case "night":
                        this.instance = new PollInstance(PollInstance.PollType.TIME, ((Player)sender).getPlayer(), "NIGHT");
                        break;
                    default:
                        FusionUtilities.sendUserMessage(sender, HELP_TIME);
                        break;
                }
            } else {
                // Custom poll?
                StringBuilder q = new StringBuilder();
                for (int i=1; i<args.length; i++)
                    q.append(args[i]);
                if (q.length() > 80) {
                    FusionUtilities.sendUserMessage(sender, MSG_TOO_LONG);
                } else {
                    this.instance = new PollInstance(PollInstance.PollType.CUSTOM, ((Player)sender).getPlayer(), q.toString());
                }
            }
        } else if (instance.isRunning()) {
            FusionUtilities.sendUserMessage(sender, MSG_POLL_RUNNING);
        } else {
            if (args[0].equalsIgnoreCase("yes")) {
                this.instance.voteYes(((Player) sender).getPlayer());
                FusionUtilities.sendUserMessage(sender, MSG_POLL_YES);
            } else if (args[0].equalsIgnoreCase("no")) {
                this.instance.voteNo(((Player) sender).getPlayer());
                FusionUtilities.sendUserMessage(sender, MSG_POLL_NO);
            }
        }
        
        return true;
    }
    
    private void setupMsgNoPoll()
    {
        MSG_NO_POLL = new TextComponent("There is no poll currently running.");
        MSG_NO_POLL.setColor(ChatColor.RED);
    }
    private void setupMsgPollRunning()
    {
        MSG_POLL_RUNNING = new TextComponent("A poll is already running!");
        MSG_POLL_RUNNING.setColor(ChatColor.RED);
    }
    private void setupMsgPollYes()
    {
        MSG_POLL_YES = new TextComponent("You voted YES.");
        MSG_POLL_YES.setColor(ChatColor.GREEN);
        MSG_POLL_YES.setItalic(true);
    }
    private void setupMsgPollNo()
    {
        MSG_POLL_NO = new TextComponent("You voted NO");
        MSG_POLL_NO.setColor(ChatColor.YELLOW);
        MSG_POLL_NO.setItalic(true);
    }
    private void setupMsgTooLong()
    {
        MSG_TOO_LONG = new TextComponent("Question too long! (max 80 characters)");
        MSG_TOO_LONG.setColor(ChatColor.RED);
    }
    private void setupMsgHelpWeather()
    {
        HELP_WEATHER = new TextComponent("/poll create weather <SUN|RAIN|THUNDER>");
        HELP_WEATHER.setColor(ChatColor.DARK_AQUA);
        ALL_HELP[0] = HELP_WEATHER;
    }
    private void setupMsgHelpTime()
    {
        HELP_TIME = new TextComponent("/poll create time <DAY|NIGHT>");
        HELP_TIME.setColor(ChatColor.DARK_AQUA);
        ALL_HELP[1] = HELP_TIME;
    }
    private void setupMsgHelpCustom()
    {
        HELP_CUSTOM = new TextComponent("/poll create <QUESTION>");
        HELP_CUSTOM.setColor(ChatColor.DARK_AQUA);
        ALL_HELP[2] = HELP_CUSTOM;
    }
}
