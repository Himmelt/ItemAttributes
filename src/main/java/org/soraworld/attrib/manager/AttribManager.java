package org.soraworld.attrib.manager;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;

public class AttribManager extends SpigotManager {

    public static final int TAG_END = 0;
    public static final int TAG_BYTE = 1;
    public static final int TAG_SHORT = 2;
    public static final int TAG_INT = 3;
    public static final int TAG_LONG = 4;
    public static final int TAG_FLOAT = 5;
    public static final int TAG_DOUBLE = 6;
    public static final int TAG_BYTE_A = 7;
    public static final int TAG_STRING = 8;
    public static final int TAG_LIST = 9;
    public static final int TAG_COMP = 10;
    public static final int TAG_INT_A = 11;
    public static final String ATTRIBS = "attribs";

    private static final Field handle;
    private static final boolean support;

    static {
        Field _handle = null;
        boolean _support = false;
        try {
            _handle = CraftItemStack.class.getDeclaredField("handle");
            _handle.setAccessible(true);
            _support = true;
        } catch (Throwable ignored) {
        }
        handle = _handle;
        support = _support;
    }

    @Setting(comment = "comment.autoUpdate")
    private boolean autoUpdate = false;
    @Setting(comment = "comment.accumulateDodge")
    private boolean accumulateDodge = false;
    @Setting(comment = "comment.accumulateBlock")
    private boolean accumulateBlock = false;

    public AttribManager(SpigotPlugin plugin, Path path) {
        super(plugin, path);
    }

    @Nonnull
    public ChatColor defChatColor() {
        return ChatColor.DARK_GREEN;
    }

    public static net.minecraft.server.v1_7_R4.ItemStack getNMStack(org.bukkit.inventory.ItemStack stack) {
        try {
            return (net.minecraft.server.v1_7_R4.ItemStack) handle.get(stack);
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static NBTTagCompound getTag(org.bukkit.inventory.ItemStack stack) {
        try {
            return ((net.minecraft.server.v1_7_R4.ItemStack) handle.get(stack)).tag;
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static void setTag(org.bukkit.inventory.ItemStack stack, NBTTagCompound tag) {
        try {
            ((net.minecraft.server.v1_7_R4.ItemStack) handle.get(stack)).tag = tag;
        } catch (Throwable ignored) {
        }
    }

    public static void updateLore(ItemStack stack) {
        // TODO will
        NBTTagCompound tag = getTag(stack);
        if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
            NBTTagCompound attribs = tag.getCompound(ATTRIBS);
            ArrayList<String> lore = new ArrayList<>();
            // TODO add attribs
            stack.getItemMeta().setLore(lore);
        }
    }

    /**
     * 是否自动更新物品Lore.
     *
     * @return 是否自动更新
     */
    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    /**
     * 是否累积装备闪避几率.
     * 如果不累积，则使用最大的几率.
     *
     * @return 是否累积
     */
    public boolean isAccumulateDodge() {
        return accumulateDodge;
    }
}
