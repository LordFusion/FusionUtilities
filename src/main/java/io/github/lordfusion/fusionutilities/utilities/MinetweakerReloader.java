package io.github.lordfusion.fusionutilities.utilities;

import io.github.lordfusion.fusionutilities.FusionUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MinetweakerReloader implements Listener
{
    private static final int TIMER_DELAY = 20;
    private static Timer timer;
    private static MtTimer task;
    
    public MinetweakerReloader()
    {
        if (timer == null)
            timer = new Timer();
    }
    
    /**
     * When a player joins the server, wait a set amount of time, then trigger the Minetweaker Reload command.
     * If a timer is already pending, then the timer will be reset.
     * @param event PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public static void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        if (task == null)
            task = new MtTimer();
        else
            task.cancel();
        // Schedule the new task
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, TIMER_DELAY);
        timer.schedule(task, calendar.getTime());
    }
    
    /**
     * A timer task to run the /minetweaker reload command.
     */
    private static class MtTimer extends TimerTask
    {
        @Override
        public void run()
        {
            FusionUtilities.sendConsoleInfo("Running \"/minetweaker reload\" . . .");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "minetweaker reload");
            task = null;
        }
    }
    
    /**
     * Runs Timer.cancel();
     */
    public static void cancelTimers()
    {
        if (task != null)
            task.cancel();
    }
}
