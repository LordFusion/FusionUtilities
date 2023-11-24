package io.github.lordfusion.fusionutilities.listeners;

import com.earth2me.essentials.Essentials;
import io.github.lordfusion.fusionutilities.FusionUtilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;

public class FlightMaintainer implements Listener
{
    private HashMap<Player, Status> flyStatus;
    private Essentials essentials;
    
    public FlightMaintainer(Essentials ess)
    {
        this.flyStatus = new HashMap<>();
        this.essentials = ess;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(final PlayerTeleportEvent pte)
    {
        if (pte.isCancelled())
            return;
        
        Player player = pte.getPlayer();
        if (!flyStatus.containsKey(player) || (System.currentTimeMillis() > (flyStatus.get(player).epoch + 500))) {
            FusionUtilities.sendConsoleInfo("Logging flight status for " + player.getName() + ": " + player.getAllowFlight());
            flyStatus.put(player, new Status(System.currentTimeMillis(), player.getAllowFlight()));
        } else {
            FusionUtilities.sendConsoleInfo("Skipping flight status log for " + player.getName() + ", too recent (" + player.getAllowFlight() + ")");

        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent pcwe)
    {
        Player player = pcwe.getPlayer();
        if (!flyStatus.containsKey(player) || (System.currentTimeMillis() < (flyStatus.get(player).epoch + 10000))) {
            boolean allowFlight = flyStatus.get(player).flyEnabled;
            
            Player bPlayer = Bukkit.getPlayer(player.getUniqueId());
            if (bPlayer != null) {
                FusionUtilities.sendConsoleInfo("Setting flight status for " + player.getName() + ": " + allowFlight);
                bPlayer.setAllowFlight(allowFlight);
            } else {
                FusionUtilities.sendConsoleInfo("Invalid Bukkit Player!");
            }
        } else {
            FusionUtilities.sendConsoleInfo("Not setting flight status for " + player.getName() + ", too much time passed.");
        }
    }
    
    private class Status
    {
        public long epoch;
        public boolean flyEnabled;
        
        public Status(long e, boolean s)
        {
            epoch = e;
            flyEnabled = s;
        }
    }
}
