package io.github.lordfusion.fusionutilities.utilities;

import io.github.lordfusion.fusionutilities.DataManager;
import io.github.lordfusion.fusionutilities.FusionUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

public class InventoryOverflowDetector implements Listener
{
    private DataManager dataManager = FusionUtilities.getInstance().getDataManager();
    
    public InventoryOverflowDetector()
    {
    
    }
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerQuitEvent(PlayerQuitEvent event)
    {
        // Verify that this event isn't garbage and actually has a player inside
        if (event.getPlayer() == null)
            return;
        // Find the player's userdata file
        String uuid = event.getPlayer().getUniqueId().toString();
        File playerFile = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/playerdata" + uuid + ".yml");
        // Verify that the userdata file exists.
        if (!playerFile.exists())
            return;
        
        // Check if the userdata file is dummy thicc
        if (playerFile.length() < this.dataManager.getMaxInventorySize())
            return;
        
        FusionUtilities.sendConsoleWarn("INVENTORY OVERFLOW | " + event.getPlayer().getName() + "'s file size: ");
    }
}
