package org.soraworld.attrib.nbt;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.soraworld.attrib.data.ItemAttrib;

import java.lang.reflect.Field;

public class NBTUtil {

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

    public static void setTag(org.bukkit.inventory.ItemStack stack, NBTTagCompound tag) {
        try {
            ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).setTag(tag);
        } catch (Throwable ignored) {
        }
    }

    public static NBTTagCompound getOrCreateTag(ItemStack stack, String path) {
        try {
            NBTTagCompound tag = ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).getTag();
            if (tag == null) {
                tag = new NBTTagCompound();
                setTag(stack, tag);
            }
            if (path != null && !path.isEmpty()) {
                NBTTagCompound child = tag.getCompound(path);
                tag.set(path, child);
                return child;
            }
            return tag;
        } catch (Throwable e) {
            e.printStackTrace();
            return new NBTTagCompound();
        }
    }

    public static NBTTagCompound getTag(ItemStack stack, String path) {
        if (!(stack instanceof CraftItemStack)) {
            stack = CraftItemStack.asCraftCopy(stack);
        }
        try {
            NBTTagCompound tag = ((net.minecraft.server.v1_12_R1.ItemStack) handle.get(stack)).getTag();
            if (tag == null) return null;
            if (path != null && !path.isEmpty()) {
                NBTTagCompound child = tag.getCompound(path);
                if (child == null) return null;
                return child;
            }
            return tag;
        } catch (Throwable e) {
            e.printStackTrace();
            return new NBTTagCompound();
        }
    }

    public static ItemAttrib getOrCreateAttrib(org.bukkit.inventory.ItemStack stack) {
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        ItemAttrib attrib = new ItemAttrib();
        attrib.globalId = tag.getInt("globalId");
        attrib.name = tag.getString("name");
        attrib.attack = tag.getInt("attack");
        attrib.critChance = tag.getFloat("critChance");
        attrib.walkspeed = tag.getFloat("walkspeed");
        attrib.blockChance = tag.getFloat("blockChance");
        attrib.dodgeChance = tag.getFloat("dodgeChance");
        attrib.suckRatio = tag.getFloat("suckRatio");
        attrib.fireChance = tag.getFloat("fireChance");
        attrib.blockChance = tag.getFloat("blockChance");
        return attrib;
    }

    public static ItemAttrib getAttrib(org.bukkit.inventory.ItemStack stack) {
        NBTTagCompound tag = getTag(stack, "attrib");
        if (tag == null || !tag.hasKey("active")) return null;
        ItemAttrib attrib = new ItemAttrib();
        attrib.globalId = tag.getInt("globalId");
        attrib.name = tag.getString("name");
        attrib.attack = tag.getInt("attack");
        attrib.critChance = tag.getFloat("critChance");
        attrib.walkspeed = tag.getFloat("walkspeed");
        attrib.blockChance = tag.getFloat("blockChance");
        attrib.dodgeChance = tag.getFloat("dodgeChance");
        attrib.suckRatio = tag.getFloat("suckRatio");
        attrib.fireChance = tag.getFloat("fireChance");
        attrib.blockChance = tag.getFloat("blockChance");
        return attrib;
    }

    public static void offerAttrib(org.bukkit.inventory.ItemStack stack, ItemAttrib attrib) {
        NBTTagCompound tag = getOrCreateTag(stack, "attrib");
        tag.setInt("globalId", attrib.globalId);
        tag.setString("name", attrib.name);
        tag.setInt("attack", attrib.attack);
        tag.setFloat("critChance", attrib.critChance);
        tag.setFloat("walkspeed", attrib.walkspeed);
        tag.setFloat("blockChance", attrib.blockChance);
        tag.setFloat("dodgeChance", attrib.dodgeChance);
        tag.setFloat("suckRatio", attrib.suckRatio);
        tag.setFloat("fireChance", attrib.fireChance);
        tag.setFloat("blockChance", attrib.blockChance);
    }
}
