package io.github.lordfusion.fusionutilities;

import io.github.lordfusion.fusionutilities.commands.*;
import io.github.lordfusion.fusionutilities.utilities.MinetweakerReloader;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;

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
        else
            unRegisterBukkitCommand(this, getCommand("townyhelp"));
        
        // Vote Command
        if (this.dataManager.doVoteCommand()) {
            if (Bukkit.getPluginManager().getPlugin("GAListener") != null) {
                // todo: Check GAListener's config to see if their command should be disabled
                // IF YOU'RE GOING TO HAVE A CONFIG OPTION TO REMOVE A PLUGIN, THEN UNREGISTER IT, YOU USELESS FUCKS
                unRegisterBukkitCommand(Bukkit.getPluginManager().getPlugin("GAListener"), Bukkit.getPluginCommand("vote"));
                registerBukkitCommand(getCommand("fusion-vote"));
            }
            getCommand("fusion-vote").setExecutor(new Vote());
        } else
            unRegisterBukkitCommand(this, getCommand("fusion-vote"));
        
        // Donate Command
        if (this.dataManager.doDonateCommand())
            getCommand("fusion-donate").setExecutor(new Donate());
        else
            unRegisterBukkitCommand(this, getCommand("fusion-donate"));
        
        // Poll Command
        if (this.dataManager.doPollCommand())
            getCommand("fusion-poll").setExecutor(new Poll());
        else
            unRegisterBukkitCommand(this, getCommand("fusion-poll"));
        
        // Find Source Command
        if (this.dataManager.doFindSourceCommand())
            getCommand("find-source").setExecutor(new FindSource());
        else
            unRegisterBukkitCommand(this, getCommand("find-source"));
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
        else {
            if (msg.getExtra() != null) {
                StringBuilder line = new StringBuilder(msg.getText());
                for (BaseComponent extra : msg.getExtra())
                    line.append(((TextComponent)extra).getText());
                sender.sendMessage(line.toString());
            } else {
                sender.sendMessage(msg.getText());
            }
        }
    }
    
    public static void sendUserMessages(CommandSender sender, TextComponent[] msgs)
    {
        for (TextComponent msg : msgs)
            sendUserMessage(sender, msg);
    }
    
    public static void broadcast(TextComponent msg)
    {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.isOnline())
                player.spigot().sendMessage(msg);
    }
    
    public static void worldBroadcast(World world, TextComponent msg)
    {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.isOnline() && player.getWorld().equals(world))
                player.spigot().sendMessage(msg);
    }
    
    /* UNREGISTER COMMAND ************************************************************************ UNREGISTER COMMAND */
    private static Object getPrivateField(Object object, String field)throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }
    
    public static void unRegisterBukkitCommand(Plugin plugin, PluginCommand cmd) {
        try {
            Object result = getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(cmd.getName());
            sendConsoleInfo("COMMAND NAME: " + cmd.getName());
            for (String alias : cmd.getAliases()){
                sendConsoleInfo("COMMAND NAME: " + cmd.getName());
                if(knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(plugin.getName())){
                    knownCommands.remove(alias);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void registerBukkitCommand(PluginCommand cmd) {
        try {
            Object result = getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.put("vote", cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
