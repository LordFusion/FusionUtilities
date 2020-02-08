package io.github.lordfusion.fusionutilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageGenerator
{
    /**
     * Not intended to include command info or details.
     *  Ex: "/fr debug <true/false>"
     * @param helpMsg Command usage
     * @return Formatted command usage message
     */
    private static TextComponent generateBasicHelp(String helpMsg)
    {
        TextComponent output = new TextComponent(helpMsg);
        output.setColor(ChatColor.BLUE);
        
        return output;
    }
    
    /**
     * Gives all the information that generateFancyHelp does,
     * but displays it in multiple lines instead of using text-hovering
     *
     * @param helpMsgs Command usage; First parameter should be just the command.
     * @return Formatted multi-line command usage message
     */
    private static TextComponent generateExtendedHelp(String[] helpMsgs)
    {
        TextComponent output = new TextComponent(helpMsgs[0]);
        output.setColor(ChatColor.BLUE);
        
        for (int i=1; i<helpMsgs.length; i++) {
            TextComponent nextLine = new TextComponent("\n     " + helpMsgs[i]);
            nextLine.setColor(ChatColor.GRAY);
            
            output.addExtra(nextLine);
        }
        
        return output;
    }
    
    /**
     * Give full usage info in a clean way, by letting players hover over the things they need help with.
     * @param helpMsgs Command usage. Format: "/fr","debug","(debug description)","<true/false>","(t/f description)"
     * @return Formatted usage information with hoverable text
     */
    private static TextComponent generateFancyHelp(String[] helpMsgs)
    {
        // If I fuck up, catch it before it displays something wacky.
        if ((helpMsgs.length %2) != 1) {
            TextComponent helpUnavailMsg = new TextComponent("An error occurred. This message is unavailable.");
            helpUnavailMsg.setColor(ChatColor.RED);
            return helpUnavailMsg;
        }
        
        // Assuming I didn't fuck up in a really weird way...
        TextComponent output = new TextComponent(helpMsgs[0]);
        output.setColor(ChatColor.BLUE);
        
        for (int i=1; i<helpMsgs.length; i+= 2) {
            TextComponent nextLine = new TextComponent(helpMsgs[i]);
            nextLine.setColor(ChatColor.BLUE);
            
            ComponentBuilder hoverMsg = new ComponentBuilder(helpMsgs[i+1]);
            hoverMsg.color(ChatColor.AQUA);
            
            nextLine.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMsg.create()));
            output.addExtra(" ");
            output.addExtra(nextLine);
        }
        
        return output;
    }
}
