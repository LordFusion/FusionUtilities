package io.github.lordfusion.fusionutilities.utilities;

import io.github.lordfusion.fusionutilities.DataManager;
import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

// todo: make polls only show up and be voteable for people in the same world.

public class PollInstance implements Runnable
{
    public enum PollType { TIME, WEATHER, CUSTOM }
    
    private ArrayList<OfflinePlayer> yesVotes, noVotes;
    private PollType type;
    private boolean isRunning;
    private String question;
    private Player creator;
    private World world;
    
    private BukkitTask scheduledTask;
    private DataManager dataManager;
    
    /**
     * Creates a poll instance that will run a poll to the server, accepting votes until a scheduled close point.
     * Creator must have enough money to afford the poll cost.
     * @param type     Poll type (CUSTOM / TIME / WEATHER)
     * @param creator  Player that created the poll
     * @param question Question or poll details associated with the poll
     */
    public PollInstance(PollType type, Player creator, String question)
    {
        this.dataManager = FusionUtilities.getInstance().getDataManager();
        
        if (this.dataManager.isEconomyEnabled() && !creator.hasPermission(FusionUtilities.PERMISSION_FREEPOLL) &&
                (this.dataManager.getPollCost() > 0)) {
            // Charge poll cost
            EconomyResponse response = this.dataManager.getEconomy().withdrawPlayer(creator,
                    this.dataManager.getPollCost());
            if (response.type != EconomyResponse.ResponseType.SUCCESS) { // Couldn't charge, cancel the poll
                FusionUtilities.sendConsoleWarn("Couldn't charge $" + this.dataManager.getPollCost() + " from " +
                        creator.getName() + " to create poll.");
                TextComponent error = new TextComponent("An internal error occurred. Poll canceled.");
                error.setColor(ChatColor.RED);
                FusionUtilities.sendUserMessage(creator, error);
                return;
            }
        }
        
        this.type = type;
        this.creator = creator;
        this.question = question;
        if (this.type != PollType.CUSTOM)
            world = creator.getWorld();
        
        this.yesVotes = new ArrayList<>();
        this.yesVotes.add(creator);
        this.noVotes = new ArrayList<>();
        
        this.broadcastPoll();
        this.scheduledTask = Bukkit.getScheduler().runTaskLater(FusionUtilities.getInstance(), this, 2400);
        isRunning = true;
    }
    
    private void broadcastPoll()
    {
        TextComponent[] broadcast = new TextComponent[2];
        
        broadcast[0] = new TextComponent("Poll Created: ");
        broadcast[0].setColor(ChatColor.GOLD);
        if (type == PollType.WEATHER || type == PollType.TIME) {
            if (type == PollType.WEATHER) {
                broadcast[0].addExtra("Change weather in ");
            } else {
                broadcast[0].addExtra("Change time in ");
            }
            TextComponent worldNameText = new TextComponent(this.world.getName());
            worldNameText.setColor(ChatColor.YELLOW);
            broadcast[0].addExtra(worldNameText);
            broadcast[0].addExtra(" to ");
            TextComponent questionText = new TextComponent(this.question);
            questionText.setColor(ChatColor.YELLOW);
            broadcast[0].addExtra(questionText);
            broadcast[0].addExtra("?");
        } else {
            TextComponent questionText = new TextComponent(this.question);
            questionText.setColor(ChatColor.YELLOW);
            questionText.setItalic(true);
            broadcast[0].addExtra(questionText);
        }
        
        broadcast[1] = new TextComponent("YES: ");
        TextComponent voteYesText = new TextComponent("/poll yes ");
        voteYesText.setColor(ChatColor.GREEN);
        voteYesText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/poll yes"));
        broadcast[1].addExtra(voteYesText);
        broadcast[1].addExtra("| NO: ");
        TextComponent voteNoText = new TextComponent("/poll no ");
        voteNoText.setColor(ChatColor.RED);
        voteNoText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/poll no"));
        broadcast[1].addExtra(voteNoText);

        FusionUtilities.broadcast(broadcast[0]);
        FusionUtilities.broadcast(broadcast[1]);
    }
    
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        this.isRunning = false;
        broadcastResults();
        
        if (yesVotes.size() > noVotes.size()) {
            if (type == PollType.WEATHER) {
                if (question.equalsIgnoreCase("STORM")) {
                    world.setStorm(true);
                    world.setThundering(true);
                }
                else if (question.equalsIgnoreCase("RAIN")) {
                    world.setStorm(true);
                } else {
                    world.setStorm(false);
                    world.setThundering(false);
                }
                world.setWeatherDuration(6000);
            } else if (type == PollType.TIME) {
                if (question.equalsIgnoreCase("DAY"))
                    world.setTime(0);
                else
                    world.setTime(14000);
            }
        } else {
            if (type != PollType.CUSTOM && this.dataManager.isEconomyEnabled() &&
                    !creator.hasPermission(FusionUtilities.PERMISSION_FREEPOLL) &&
                    (this.dataManager.getPollCost() > 0)) {
                // Refund poll cost
                EconomyResponse response = this.dataManager.getEconomy().depositPlayer(creator,
                        this.dataManager.getPollCost());
                if (response.type != EconomyResponse.ResponseType.SUCCESS) { // Couldn't charge, cancel the poll
                    FusionUtilities.sendConsoleWarn("Couldn't refund $" + this.dataManager.getPollCost() + " to " +
                            creator.getName() + " after failed poll.");
                    TextComponent error = new TextComponent("An internal error occurred. Poll cost NOT refunded.");
                    error.setColor(ChatColor.RED);
                    FusionUtilities.sendUserMessage(creator, error);
                } else {
                    FusionUtilities.sendConsoleInfo("Refunded $" + this.dataManager.getPollCost() + " to " +
                            creator.getName() + " after failed poll.");
                    TextComponent succ = new TextComponent("Poll cost refunded.");
                    succ.setColor(ChatColor.YELLOW);
                    FusionUtilities.sendUserMessage(creator, succ);
                }
            }
        }
    }
    
    /**
     * Sends a broadcast to all players to display the results of the finished poll.
     */
    private void broadcastResults()
    {
        TextComponent broadcast = new TextComponent("Poll ");
        if (yesVotes.size() > noVotes.size()) {
            broadcast.setColor(ChatColor.GREEN);
            broadcast.addExtra("passes! ");
        } else {
            broadcast.setColor(ChatColor.RED);
            broadcast.addExtra("fails! ");
        }
        
        broadcast.addExtra("(" + yesVotes.size() + " - " + noVotes.size() + ") ");
        
        if (yesVotes.size() > noVotes.size()) {
            if (type == PollType.WEATHER) {
                TextComponent weather = new TextComponent("Weather in " + this.world.getName() + " changed to " + this.question + ".");
                weather.setColor(ChatColor.GREEN);
                weather.setItalic(true);
                broadcast.addExtra(weather);
            } else if (type == PollType.TIME) {
                TextComponent time = new TextComponent("Time in " + this.world.getName() + " changed to " + this.question + ".");
                time.setColor(ChatColor.GREEN);
                time.setItalic(true);
                broadcast.addExtra(time);
            } else {
                TextComponent questionMsg = new TextComponent(this.question);
                questionMsg.setColor(ChatColor.YELLOW);
                questionMsg.setItalic(true);
                broadcast.addExtra(questionMsg);
            }
        }
        
        FusionUtilities.broadcast(broadcast);
    }
    
    /**
     * Check if there is a poll currently running.
     * @return True if poll is running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Trims the ArrayList into an Array, to return the names of all players who voted yes on the most recent poll.
     * @return Player names of players who voted yes
     */
    public OfflinePlayer[] getYesVotes() {
        return yesVotes.toArray(new OfflinePlayer[0]);
    }
    /**
     * Trims the ArrayList into an Array, to return the names of all players who voted no on the most recent poll.
     * @return Player names of players who voted no
     */
    public OfflinePlayer[] getNoVotes() {
        return noVotes.toArray(new OfflinePlayer[0]);
    }
    
    /**
     * Votes yes on the current poll. May throw an error if there is no poll running.
     * Removes the vote from "noVotes" if they already voted. Does not duplicate-vote.
     * @param voter Voting player
     */
    public void voteYes(OfflinePlayer voter)
    {
        this.noVotes.remove(voter);
        if (!this.yesVotes.contains(voter)) // Avoid duplicates
            this.yesVotes.add(voter);
    }
    
    /**
     * Votes no on the current poll. May throw an error if there is no poll running.
     * Removes the vote from "yesVotes" if they already voted. Does not duplicate-vote.
     * @param voter Voting player
     */
    public void voteNo(OfflinePlayer voter)
    {
        this.yesVotes.remove(voter);
        if (!this.noVotes.contains(voter)) // Avoid duplicates
            this.noVotes.add(voter);
    }
    
    public void close()
    {
        this.scheduledTask.cancel();
        this.run();
    }
    
    public void kill()
    {
        this.scheduledTask.cancel();
    }
}
