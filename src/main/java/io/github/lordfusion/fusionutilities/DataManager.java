package io.github.lordfusion.fusionutilities;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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
    
    /**
     * The DataManager... Manages data. All of it. Any file-related data this plugin needs, here it is. Right here.
     * @param pluginDataFolder Folder where all data is/should be stored.
     */
    DataManager(String pluginDataFolder)
    {
        dataFolderPath = pluginDataFolder;
        this.loadConfigFile();
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
        boolean fileChanged = false;
        if (!config.getKeys(false).contains(MT_RELOAD)) {
            config.set(MT_RELOAD, false);
            fileChanged = true;
        }
        if (!config.getKeys(false).contains(TOWNY_CMD)) {
            config.set(TOWNY_CMD, false);
            fileChanged = true;
        }
        if (!config.getKeys(false).contains(VOTE_CMD)) {
            config.set(VOTE_CMD, false);
            fileChanged = true;
        }
        if (!config.getKeys(false).contains(DONATE_CMD)) {
            config.set(DONATE_CMD, false);
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
}
