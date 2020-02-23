package io.github.lordfusion.fusionutilities.utilities;

import io.github.lordfusion.fusionutilities.DataManager;
import io.github.lordfusion.fusionutilities.FusionUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class InventoryOverflowDetector implements Listener
{
    private String BACKUP_FOLDER_NAME = "/Overflow Backups/";
    
    private DataManager dataManager = FusionUtilities.getInstance().getDataManager();
    private ArrayList<UUID> damagedPlayers;
    
    public InventoryOverflowDetector()
    {
        File overflowBackupFolder = new File(FusionUtilities.getInstance().getDataFolder().getAbsolutePath() +
                BACKUP_FOLDER_NAME);
        if (overflowBackupFolder.exists()) {
            String[] fileList = overflowBackupFolder.list();
            if (fileList != null) {
                this.damagedPlayers = new ArrayList<>();
                for (String filename : fileList) {
                    UUID uuid = UUID.fromString(filename.substring(0, 36));
                    damagedPlayers.add(uuid);
                }
            }
        }
    }
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        if (!this.damagedPlayers.contains(event.getPlayer().getUniqueId()))
            return;
        
        event.getPlayer().kickPlayer("Inventory suffered Stack Overflow! Could not auto-repair; contact an admin.");
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
        this.damagedPlayers.add(event.getPlayer().getUniqueId());
        
        // Move the player's data into a folder to be kept as a backup
        try {
            Files.move(Paths.get(playerFile.getAbsolutePath()), Paths.get(FusionUtilities.getInstance().getDataFolder().
                    getAbsolutePath() + "/Overflow Backups/" + playerFile.getName()));
        } catch (IOException e) {
            e.printStackTrace();
            FusionUtilities.sendConsoleWarn("Unable to back up overflowed player file for " + event.getPlayer()
                    .getName() + ". Automatic correction aborted.");
            return;
        }
        
        //
    }
    
    private boolean isInventoryOversized(PlayerEvent event)
    {
        // Verify that this event isn't garbage and actually has a player inside
        if (event.getPlayer() == null)
            return false;
        // Find the player's userdata file
        String uuid = event.getPlayer().getUniqueId().toString();
        File playerFile = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/playerdata" + uuid + ".yml");
        // Verify that the userdata file exists.
        if (!playerFile.exists())
            return false;
    
        // Check if the userdata file is dummy thicc
        if (playerFile.length() < this.dataManager.getMaxInventorySize())
            return false;
    
        FusionUtilities.sendConsoleWarn("INVENTORY OVERFLOW | " + event.getPlayer().getName() + "'s file size: ");
        return true;
    }
}
