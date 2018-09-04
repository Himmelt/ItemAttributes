package org.soraworld.attrib.command;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.soraworld.attrib.manager.AttribManager;
import org.soraworld.violet.command.Paths;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.command.Sub;

import static org.soraworld.attrib.manager.AttribManager.*;

public final class CommandAttrib {


    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib attack [damage]")
    public static void attack(SpigotCommand self, CommandSender sender, Paths args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    int damage = Integer.valueOf(args.first());
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setInt("attack", damage);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "setAttack", damage);
                    if (manager.autoUpdate) updateLore(stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                int damage = tag.getCompound(ATTRIBS).getInt("attack");
                manager.sendKey(player, "getAttack", damage);
            } else manager.sendKey(player, "noTag");
        } else manager.sendKey(player, "emptyHand");
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib armor [armor]")
    public static void armor(SpigotCommand self, CommandSender sender, Paths args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    int armor = Integer.valueOf(args.first());
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setInt("armor", armor);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "setArmor", armor);
                    if (manager.autoUpdate) updateLore(stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                int armor = tag.getCompound(ATTRIBS).getInt("armor");
                manager.sendKey(player, "getArmor", armor);
            } else manager.sendKey(player, "noTag");
        } else manager.sendKey(player, "emptyHand");
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib dodge [dodge]")
    public static void dodge(SpigotCommand self, CommandSender sender, Paths args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    int dodge = Integer.valueOf(args.first());
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setInt("dodge", dodge);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "setDodge", dodge);
                    if (manager.autoUpdate) updateLore(stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                int dodge = tag.getCompound(ATTRIBS).getInt("dodge");
                manager.sendKey(player, "getDodge", dodge);
            } else manager.sendKey(player, "noTag");
        } else manager.sendKey(player, "emptyHand");
    }


    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib crit [chance] [time]")
    public static void crit(SpigotCommand self, CommandSender sender, Paths args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    float chance = Float.valueOf(args.first());
                    chance = chance < 0 ? 0 : chance > 1 ? 1 : chance;
                    float time = 1;
                    try {
                        time = Float.valueOf(args.get(1));
                    } catch (Throwable ignored) {
                    }
                    time = time < 1 ? 1 : time;
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setFloat("crit-chance", chance);
                    attribs.setFloat("crit-time", time);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "setCrit", chance, time);
                    if (manager.autoUpdate) updateLore(stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                float chance = tag.getCompound(ATTRIBS).getFloat("crit-chance");
                float time = tag.getCompound(ATTRIBS).getFloat("crit-time");
                manager.sendKey(player, "getCrit", chance, time);
            } else manager.sendKey(player, "noTag");
        } else manager.sendKey(player, "emptyHand");
    }


    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib update")
    public static void update(SpigotCommand self, CommandSender sender, Paths args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            updateLore(stack);
        } else manager.sendKey(player, "emptyHand");
    }
}
