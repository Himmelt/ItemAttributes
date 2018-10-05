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
                manager.addLine(stack, new LoreInfo(info, attrib), args.getContent().replaceAll("<sp>", " "));
                player.setItemInHand(stack);
            } else manager.sendKey(player, "emptyHand");
        } else manager.sendKey(player, "emptyArgs");
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/atl set <line> <lore>")
    public static void set(SpigotCommand self, CommandSender sender, Args args) {

    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/atl remove <line>")
    public static void remove(SpigotCommand self, CommandSender sender, Args args) {

    }
}
