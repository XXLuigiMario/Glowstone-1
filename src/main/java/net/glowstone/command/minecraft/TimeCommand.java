package net.glowstone.command.minecraft;

import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeCommand extends VanillaCommand {

    private static final List<String> SUBCOMMANDS = Arrays.asList("set", "add");
    private static final List<String> TIMES = Arrays.asList("day", "night");

    public TimeCommand() {
        super("time", "Changes the time of the world.", "/time <set|add> <value>", Collections.emptyList());
        setPermission("minecraft.command.time");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        GlowWorld world = CommandUtils.getWorld(sender);
        if (world == null) {
            return false;
        }
        String subcommand = args[0];
        String value = args[1];
        int mod;
        boolean add;
        if (subcommand.equals("set")) {
            add = false;
            if (value.equals("day")) {
                mod = 1000;
            } else if (value.equals("night")) {
                mod = 13000;
            } else {
                try {
                    mod = Integer.valueOf(value);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "'" + value + "' is not a valid number");
                    return false;
                }
            }
            sender.sendMessage("Set the time to " + mod);
        } else if (subcommand.equals("add")) {
            add = true;
            try {
                mod = Integer.valueOf(value);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "'" + value + "' is not a valid number");
                return false;
            }
            sender.sendMessage("Added " + mod + " to the time");
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        world.setTime(add ? world.getTime() + mod : mod);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return (List) StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, new ArrayList(SUBCOMMANDS.size()));
        }
        if (args.length == 2 && args[0].equals("set")) {
            return (List) StringUtil.copyPartialMatches(args[1], TIMES, new ArrayList(TIMES.size()));
        }
        return Collections.emptyList();
    }
}
