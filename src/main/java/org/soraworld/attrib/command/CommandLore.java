package org.soraworld.attrib.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.soraworld.attrib.data.ItemAttrib;
import org.soraworld.attrib.data.LoreInfo;
import org.soraworld.attrib.manager.AttribManager;
import org.soraworld.violet.command.Args;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.command.Sub;
import org.soraworld.violet.util.ChatColor;

import static org.soraworld.attrib.manager.AttribManager.createAttrib;
import static org.soraworld.attrib.manager.AttribManager.getInfo;

public final class CommandLore {
    /**
     * 多个空格请使用{@code <sp>}代替.
     *
     * @param self   命令封装自身
     * @param sender 命令发送者
     * @param args   参数
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/atl add <lore>")
    public static void add(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        if (args.notEmpty()) {
            ItemStack stack = player.getItemInHand();
            if (stack != null && stack.getType() != Material.AIR) {
                LoreInfo info = getInfo(stack);
                ItemAttrib attrib = info.attrib != null ? info.attrib : createAttrib("");
                String content = ChatColor.colorize(args.getContent().replaceAll("<sp>", " "));
                manager.addLine(stack, new LoreInfo(info, attrib), ChatColor.RESET + content);
                player.setItemInHand(stack);
            } else manager.sendKey(player, "emptyHand");
        } else manager.sendKey(player, "emptyArgs");
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/atl set <line> <lore>")
    public static void set(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        if (args.notEmpty()) {
            if (args.size() >= 2) {
                try {
                    int line = Integer.valueOf(args.first());
                    if (line > 50) {
                        manager.sendKey(player, "lineNumTooBig");
                    } else if (line > 0) {
                        ItemStack stack = player.getItemInHand();
                        if (stack != null && stack.getType() != Material.AIR) {
                            LoreInfo info = getInfo(stack);
                            ItemAttrib attrib = info.attrib != null ? info.attrib : createAttrib("");
                            String content = ChatColor.colorize(args.next().getContent().replaceAll("<sp>", " "));
                            manager.setLine(stack, new LoreInfo(info, attrib), ChatColor.RESET + content, line);
                            player.setItemInHand(stack);
                        } else manager.sendKey(player, "emptyHand");
                    } else manager.sendKey(player, "cantEditFirstLine");
                } catch (NumberFormatException e) {
                    manager.sendKey(player, "invalidInt");
                }
            } else manager.sendKey(player, "invalidArgs");
        } else manager.sendKey(player, "emptyArgs");
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/atl remove <line>")
    public static void remove(SpigotCommand self, CommandSender sender, Args args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        if (args.notEmpty()) {
            ItemStack stack = player.getItemInHand();
            if (stack != null && stack.getType() != Material.AIR) {
                try {
                    int line = Integer.valueOf(args.first());
                    LoreInfo info = getInfo(stack);
                    manager.removeLine(stack, new LoreInfo(info, info.attrib), line);
                    player.setItemInHand(stack);
                } catch (NumberFormatException e) {
                    manager.sendKey(player, "invalidInt");
                }
            } else manager.sendKey(player, "emptyHand");
        } else manager.sendKey(player, "emptyArgs");
    }
}
