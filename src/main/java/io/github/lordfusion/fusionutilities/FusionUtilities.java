package io.github.lordfusion.fusionutilities;

import io.github.lordfusion.fusionutilities.commands.Donate;
import io.github.lordfusion.fusionutilities.commands.Poll;
import io.github.lordfusion.fusionutilities.commands.TownyAssistance;
import io.github.lordfusion.fusionutilities.commands.Vote;
import io.github.lordfusion.fusionutilities.utilities.MinetweakerReloader;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class FusionUtilities extends JavaPlugin
{
    static final String CONSOLE_PREFIX = "[Fusion Utilities] ";
    
    private DataManager dataManager;
    private static FusionUtilities INSTANCE;
    
    public static String PERMISSION_FREEPOLL = "fusion.utilities.poll.free";
    
    @Override
    public void onEnable()
    {
        sendConsoleInfo("Time to do nothing useful!");
        this.dataManager = new DataManager(this.getDataFolder().getAbsolutePath());
        INSTANCE = this;
        
        // Minetweaker Reloader
        if (this.dataManager.doMinetweakerReload())
            Bukkit.getPluginManager().registerEvents(new MinetweakerReloader(), this);
        
        // Towny Assistance
        if (this.dataManager.doTownyAssistance())
            getCommand("townyhelp").setExecutor(new TownyAssistance());
        
        // Vote Command
        if (this.dataManager.doVoteCommand())
            getCommand("fusion-vote").setExecutor(new Vote());
        
        // Donate Command
        if (this.dataManager.doDonateCommand())
            getCommand("fusion-donate").setExecutor(new Donate());
        
        // Poll Command
        if (this.dataManager.doPollCommand())
            getCommand("fusion-poll").setExecutor(new Poll());
    }
    
    @Override
    public void onDisable()
    {
    
    }
    
    public static FusionUtilities getInstance()
    {
        return INSTANCE;
    }
    public DataManager getDataManager()
    {
        return this.dataManager;
    }
    
    /**
     * Sends a message to the server console, with the Info priority level.
     * @param message Message for console
     */
    public static void sendConsoleInfo(String message)
    {
        Bukkit.getServer().getLogger().info(CONSOLE_PREFIX + message);
    }
    
    /**
     * Sends a message to the server console, with the Warning priority level.
     * @param message Message for console
     */
    public static void sendConsoleWarn(String message)
    {
        Bukkit.getServer().getLogger().warning(CONSOLE_PREFIX + message);
    }
    
    public static void sendUserMessage(CommandSender sender, TextComponent msg)
    {
        if (sender instanceof Player)
            ((Player)sender).spigot().sendMessage(msg);
        else
            sender.sendMessage(msg.getText());
    }
    
    public static void sendUserMessages(CommandSender sender, TextComponent[] msgs)
    {
        for (TextComponent msg : msgs)
            sendUserMessage(sender, msg);
    }
}
