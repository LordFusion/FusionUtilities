package io.github.lordfusion.fusionutilities;

import com.earth2me.essentials.Essentials;
import com.palmergames.bukkit.towny.Towny;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
    private static final String KICKOUT_CMD = "Kickout Command";
    
    // Integrations
    private boolean economyEnabled;
    private Economy economy;
    private boolean townyEnabled;
    private Plugin towny;
    private boolean essentialsEnabled;
    private Plugin essentials;
    
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
        this.townyEnabled = checkTownyIntegration();
        this.essentialsEnabled = checkEssentialsIntegration();
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
        if (!configKeys.contains(KICKOUT_CMD)) {
            config.set(KICKOUT_CMD, false);
            fileChanged = true;
        }
        if (!configKeys.contains(POLL_COST)) {
            config.set(POLL_COST, 0.0);
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
        this.config.set(KICKOUT_CMD, false);
        this.config.set(POLL_COST, 0.0);
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
    public boolean doKickoutCommand()
    {
        if (this.config != null && this.config.contains(KICKOUT_CMD))
            return this.config.getBoolean(KICKOUT_CMD, false);
        return false;
    }
    public void setKickoutCmd(boolean b)
    {
        this.config.set(KICKOUT_CMD, b);
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
    
    /**
     * Checks if Towny is present on the server.
     * @return True if Towny is found, false otherwise.
     */
    private boolean checkTownyIntegration()
    {
        FusionUtilities.sendConsoleInfo("Checking for Towny integration...");
        if (Bukkit.getPluginManager().isPluginEnabled("Towny")) {
            FusionUtilities.sendConsoleInfo("Towny integration found!");
            this.towny = Bukkit.getPluginManager().getPlugin("Towny");
            return true;
        } else {
            FusionUtilities.sendConsoleWarn("No Towny integrations found.");
            this.towny = null;
            return false;
        }
    }
    public boolean isTownyEnabled()
    {
        return this.townyEnabled;
    }
    public Towny getTowny()
    {
        return (Towny)this.towny;
    }
    
    private boolean checkEssentialsIntegration()
    {
        FusionUtilities.sendConsoleInfo("Checking for Essentials integration...");
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            FusionUtilities.sendConsoleInfo("Essentials integration found!");
            this.essentials = Bukkit.getPluginManager().getPlugin("Essentials");
            return true;
        } else {
            FusionUtilities.sendConsoleWarn("No Essentials integrations found.");
            this.essentials = null;
            return false;
        }
    }
    public Essentials getEssentials()
    {
        return (Essentials)this.essentials;
    }
}
