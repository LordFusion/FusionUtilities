package io.github.lordfusion.fusionutilities.utilities;

import io.github.lordfusion.fusionutilities.FusionUtilities;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PollInstance implements Runnable
{
    public enum PollType { TIME, WEATHER, CUSTOM }
    
    private int yesVotes, noVotes;
    private PollType type;
    private boolean isRunning;
    private String question;
    private Player creator;
    private World world;
    
    public PollInstance(PollType type, Player creator, String question)
    {
        if (!creator.hasPermission(FusionUtilities.PERMISSION_FREEPOLL)) {
            // Todo: Charge poll creation cost
        }
        
        this.type = type;
        this.creator = creator;
        if (this.type == PollType.CUSTOM)
            this.question = question;
        else
            world = creator.getWorld();
        
        this.broadcastPoll();
        Bukkit.getScheduler().runTaskLater(FusionUtilities.getInstance(), this, 2400);
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
        
        if (yesVotes > noVotes) {
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
            if (type != PollType.CUSTOM && !this.creator.hasPermission(FusionUtilities.PERMISSION_FREEPOLL)) {
                // Todo: Refund poll cost
            }
        }
    }
    
    private void broadcastResults()
    {
        TextComponent broadcast = new TextComponent("Poll ");
        if (yesVotes > noVotes) {
            broadcast.setColor(ChatColor.GREEN);
            broadcast.addExtra("passes! ");
        } else {
            broadcast.setColor(ChatColor.RED);
            broadcast.addExtra("fails! ");
        }
        
        broadcast.addExtra("(" + yesVotes + " - " + noVotes + ")");
        
        if (yesVotes > noVotes) {
            if (type == PollType.WEATHER) {
                TextComponent weather = new TextComponent("Weather in " + this.world + " changed to " + this.question + ".");
                weather.setColor(ChatColor.GREEN);
                weather.setItalic(true);
                broadcast.addExtra(weather);
            } else if (type == PollType.TIME) {
                TextComponent time = new TextComponent("Time in " + this.world + " changed to " + this.question + ".");
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
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    public int getYesVotes() {
        return yesVotes;
    }
    public int getNoVotes() {
        return noVotes;
    }
    public void voteYes()
    {
        this.yesVotes++;
    }
    public void voteNo()
    {
        this.noVotes++;
    }
}
