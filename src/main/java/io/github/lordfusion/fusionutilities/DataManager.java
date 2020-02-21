package io.github.lordfusion.fusionutilities;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class DataManager
{
    private static final String FILE_CONFIG = "/config.yml";
    private static String dataFolderPath;
    private YamlConfiguration config;
    
    // Config option names
    private static final String MT_RELOAD = "MineTweaker AutoReload";
    private static final String TOWNY_CMD = "Towny Assistance Command";
    private static final String VOTE_CMD = "Vote Command";
    private static final String DONATE_CMD = "Donate Command";
    private static final String POLL_CMD = "Poll Command";
    private static final String POLL_COST = "Poll Cost";
    private static final String FNDSRC_CMD = "Find Source Command";
    private static final String MAX_INV_CHECK = "Inventory Overflow Protection";
    private static final String MAX_INV_SIZE = "Max Inventory Size";
    
    // Integrations
    private boolean economyEnabled;
    private Economy economy;
    
    /**
     * The DataManager... Manages data. All of it. Any file-related data this plugin needs, here it is. Right here.
     * @param pluginDataFolder Folder where all data is/should be stored.
     */
    DataManager(String pluginDataFolder)
    {
        // Configurations
        dataFolderPath = pluginDataFolder;
        this.loadConfigFile();
        
        // Integrations
        this.economyEnabled = checkEconomyIntegration();
    }
    
    /**
     * Loads config.yml from file.
     * If it does not exist, a new one will be generated according to DataManager.resetConfigFile();
     */
    private void loadConfigFile()
    {
        File configFile = new File(dataFolderPath + FILE_CONFIG);
        if (configFile.exists()) {
            this.config = YamlConfiguration.loadConfiguration(configFile);
        } else {
            resetConfigFile();
        }
    
        // Make sure the file is complete
        Set<String> configKeys = config.getKeys(false);
        boolean fileChanged = false;
        if (!configKeys.contains(MT_RELOAD)) {
            config.set(MT_RELOAD, false);
            fileChanged = true;
        }
        if (!configKeys.contains(TOWNY_CMD)) {
            config.set(TOWNY_CMD, false);
            fileChanged = true;
        }
        if (!configKeys.contains(VOTE_CMD)) {
            config.set(VOTE_CMD, false);
            fileChanged = true;
        }
        if (!configKeys.contains(DONATE_CMD)) {
            config.set(DONATE_CMD, false);
            fileChanged = true;
        }
        if (!configKeys.contains(POLL_CMD)) {
            config.set(POLL_CMD, false);
            fileChanged = true;
        }
        if (!configKeys.contains(FNDSRC_CMD)) {
            config.set(FNDSRC_CMD, false);
            fileChanged = true;
        }
        if (!configKeys.contains(POLL_COST)) {
            config.set(POLL_COST, 0.0);
            fileChanged = true;
        }
        if (!configKeys.contains(MAX_INV_CHECK)) {
            config.set(MAX_INV_CHECK, false);
            fileChanged = true;
        }
        if (!configKeys.contains(MAX_INV_SIZE)) {
            config.set(MAX_INV_SIZE, 2147483647);
            fileChanged = true;
        }
        
        // Save the config if we added a missing default value
        if (fileChanged)
            saveConfigFile();
    }
    
    /**
     * Resets the plugin's config file, and fills it with the header and the default config values.
     */
    private void resetConfigFile()
    {
        // Completely reset the config.
        this.config = new YamlConfiguration();
        // Header
        this.config.options().header("Fusion Utilities Configuration File");
        this.config.options().indent(2);
        // Default Values
        this.config.set(MT_RELOAD, false);
        this.config.set(TOWNY_CMD, false);
        this.config.set(VOTE_CMD, false);
        this.config.set(DONATE_CMD, false);
        this.config.set(POLL_CMD, false);
        this.config.set(FNDSRC_CMD, false);
        this.config.set(POLL_COST, 0.0);
        this.config.set(MAX_INV_CHECK, false);
        this.config.set(MAX_INV_SIZE, 2147483647);
        FusionUtilities.sendConsoleInfo("Default config restored.");
        // Save
        this.saveConfigFile();
    }
    
    private void saveConfigFile()
    {
        try {
            this.config.save(dataFolderPath + FILE_CONFIG);
        } catch (IOException e) {
            FusionUtilities.sendConsoleWarn("FAILED to save config file:");
            e.printStackTrace();
            return;
        }
        FusionUtilities.sendConsoleInfo("Config saved to file.");
    }
    
    // Config Editing ******************************************************************************** Config Editing //
    public boolean doMinetweakerReload()
    {
        if (this.config != null && this.config.contains(MT_RELOAD))
            return this.config.getBoolean(MT_RELOAD, false);
        return false;
    }
    public void setMinetweakerReload(boolean b)
    {
        this.config.set(MT_RELOAD, b);
    }
    public boolean doTownyAssistance()
    {
        if (this.config != null && this.config.contains(TOWNY_CMD))
            return this.config.getBoolean(TOWNY_CMD, false);
        return false;
    }
    public void setTownyAssistance(boolean b)
    {
        this.config.set(TOWNY_CMD, b);
    }
    public boolean doVoteCommand()
    {
        if (this.config != null && this.config.contains(VOTE_CMD))
            return this.config.getBoolean(VOTE_CMD, false);
        return false;
    }
    public void setVoteCommand(boolean b)
    {
        this.config.set(VOTE_CMD, b);
    }
    public boolean doDonateCommand()
    {
        if (this.config != null && this.config.contains(DONATE_CMD))
            return this.config.getBoolean(DONATE_CMD, false);
        return false;
    }
    public void setDonateCommand(boolean b)
    {
        this.config.set(DONATE_CMD, b);
    }
    public boolean doPollCommand()
    {
        if (this.config != null && this.config.contains(POLL_CMD))
            return this.config.getBoolean(POLL_CMD, false);
        return false;
    }
    public void setPollCommand(boolean b)
    {
        this.config.set(POLL_CMD, b);
    }
    public boolean doFindSourceCommand()
    {
        if (this.config != null && this.config.contains(FNDSRC_CMD))
            return this.config.getBoolean(FNDSRC_CMD, false);
        return false;
    }
    public void setFindSourceCommand(boolean b)
    {
        this.config.set(FNDSRC_CMD, b);
    }
    public boolean doInventoryOverflowCheck()
    {
        if (this.config != null && this.config.contains(MAX_INV_CHECK))
            return this.config.getBoolean(MAX_INV_CHECK, false);
        return false;
    }
    public void setInventoryOverflowCheck(boolean b)
    {
        this.config.set(MAX_INV_CHECK, b);
    }
    public int getMaxInventorySize()
    {
        if (this.config != null && this.config.contains(MAX_INV_SIZE))
            return this.config.getInt(MAX_INV_SIZE, 2147483647);
        return 2147483647;
    }
    public void setMaxInventorySize(int i)
    {
        this.config.set(MAX_INV_SIZE, i);
    }
    
    public void setPollCost(double d)
    {
        this.config.set(POLL_COST, d);
    }
    public double getPollCost()
    {
        if (this.config != null && this.config.contains(POLL_COST))
            return this.config.getDouble(POLL_COST, 0.0);
        return 0.0;
    }
    
    
    // Integrations ************************************************************************************ Integrations //
    
    /**
     * Checks if any economy plugins are present on the server.
     * Currently checks for: Vault
     * @return True if an economy plugin is found, false otherwise.
     */
    private boolean checkEconomyIntegration()
    {
        FusionUtilities.sendConsoleInfo("Checking for economy integration...");
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            FusionUtilities.sendConsoleInfo("Vault integration found!");
            this.economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
            return true;
        } else {
            FusionUtilities.sendConsoleWarn("No economy integrations found.");
            this.economy = null;
            return false;
        }
    }
    public boolean isEconomyEnabled()
    {
        return this.economyEnabled;
    }
    public Economy getEconomy()
    {
        if (this.isEconomyEnabled())
            return this.economy;
        return null;
    }
    public boolean doPollCost()
    {
        return this.isEconomyEnabled() && this.getPollCost() > 0;
    }
    public boolean chargeForPoll(CommandSender sender)
    {
        return sender instanceof Player && doPollCost() && !sender.hasPermission(FusionUtilities.PERMISSION_FREEPOLL);
    }
    
}
