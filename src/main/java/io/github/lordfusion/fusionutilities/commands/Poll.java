package io.github.lordfusion.fusionutilities.commands;

import io.github.lordfusion.fusionutilities.DataManager;
import io.github.lordfusion.fusionutilities.FusionUtilities;
import io.github.lordfusion.fusionutilities.utilities.PollInstance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

// todo: /poll close - Close voting early, but still tally and execute the votes
// todo: /poll stop - Force-stop the poll, do not pass go, do not collect votes.

public class Poll implements CommandExecutor
{
    private PollInstance instance;
    private DataManager dataManager;
    
    private static TextComponent MSG_NO_POLL, MSG_POLL_RUNNING, MSG_POLL_YES, MSG_POLL_NO, HELP_WEATHER, HELP_TIME,
            HELP_CUSTOM, MSG_TOO_LONG, MSG_CANNOT_AFFORD, MSG_CHRG_POLL, MSG_NO_CMD;
    private static TextComponent[] ALL_HELP;
    
    public Poll()
    {
        this.dataManager = FusionUtilities.getInstance().getDataManager();
        
        setupMsgNoPoll();
        setupMsgPollRunning();
        setupMsgPollYes();
        setupMsgPollNo();
        setupMsgTooLong();
        setupMsgCannotAfford();
        setupMsgChargedForPoll();
        setupMsgNoCmd();
        
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
        // Check for help
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            FusionUtilities.sendUserMessages(sender, ALL_HELP);
            return true;
        }
        
        if (instance == null || !instance.isRunning()) { // Poll is NOT running
            switch (args[0]) {
                case "yes":
                case "no": // Attempted vote
                    FusionUtilities.sendUserMessage(sender, MSG_NO_POLL);
                    break;
                case "weather":
                case "time":
                case "create": // Create a poll
                    if (dataManager.chargeForPoll(sender) &&
                            this.dataManager.getEconomy().getBalance((Player)sender) < dataManager.getPollCost()) {
                        FusionUtilities.sendUserMessage(sender, MSG_CANNOT_AFFORD);
                        return true;
                    }
                    switch (args[0]) {
                        case "weather":
                            switch (args[1].toLowerCase()) {
                                case "sunny":
                                case "sun":
                                case "clear":
                                    this.instance = new PollInstance(PollInstance.PollType.WEATHER, ((Player) sender).getPlayer(), "SUNNY");
                                    break;
                                case "rain":
                                    this.instance = new PollInstance(PollInstance.PollType.WEATHER, ((Player) sender).getPlayer(), "RAIN");
                                    break;
                                case "storm":
                                case "thunder":
                                    this.instance = new PollInstance(PollInstance.PollType.WEATHER, ((Player) sender).getPlayer(), "STORM");
                                    break;
                                default:
                                    FusionUtilities.sendUserMessage(sender, HELP_WEATHER);
                                    break;
                            }
                            break;
                        case "time":
                            switch (args[1].toLowerCase()) {
                                case "day":
                                    this.instance = new PollInstance(PollInstance.PollType.TIME, ((Player) sender).getPlayer(), "DAY");
                                    break;
                                case "night":
                                    this.instance = new PollInstance(PollInstance.PollType.TIME, ((Player) sender).getPlayer(), "NIGHT");
                                    break;
                                default:
                                    FusionUtilities.sendUserMessage(sender, HELP_TIME);
                                    break;
                            }
                            break;
                        case "create":
                            String q = IntStream.range(1, args.length).mapToObj(i -> args[i] + ' ').collect(Collectors.joining());
                            if (q.length() > 80) {
                                FusionUtilities.sendUserMessage(sender, MSG_TOO_LONG);
                                return true;
                            } else {
                                this.instance = new PollInstance(PollInstance.PollType.CUSTOM, ((Player) sender).getPlayer(), q);
                            }
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + args[0]);
                    }
                    break;
                default: // Unrecognized
                    FusionUtilities.sendUserMessage(sender, MSG_NO_CMD);
                    break;
            }
        } else { // Poll IS running
            switch (args[0]) {
                case "yes":
                    this.instance.voteYes(((Player) sender).getPlayer());
                    FusionUtilities.sendUserMessage(sender, MSG_POLL_YES);
                    break;
                case "no":
                    this.instance.voteNo(((Player) sender).getPlayer());
                    FusionUtilities.sendUserMessage(sender, MSG_POLL_NO);
                    break;
                case "weather":
                case "time":
                case "default":
                    FusionUtilities.sendUserMessage(sender, MSG_POLL_RUNNING);
                    break;
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
    private void setupMsgCannotAfford()
    {
        MSG_CANNOT_AFFORD = new TextComponent("You cannot afford to start a poll ($" + this.dataManager.getPollCost()
                + ")!");
        MSG_CANNOT_AFFORD.setColor(ChatColor.RED);
    }
    private void setupMsgChargedForPoll()
    {
        MSG_CHRG_POLL = new TextComponent("You were charged to start this poll: $");
        MSG_CHRG_POLL.setColor(ChatColor.YELLOW);
    }
    private void setupMsgNoCmd()
    {
        MSG_NO_CMD = new TextComponent("Unrecognized command. Try /poll help");
        MSG_NO_CMD.setColor(ChatColor.RED);
    }
    private void setupMsgHelpWeather()
    {
        HELP_WEATHER = new TextComponent("/poll weather <SUN|RAIN|THUNDER>");
        HELP_WEATHER.setColor(ChatColor.DARK_AQUA);
        ALL_HELP[0] = HELP_WEATHER;
    }
    private void setupMsgHelpTime()
    {
        HELP_TIME = new TextComponent("/poll time <DAY|NIGHT>");
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
