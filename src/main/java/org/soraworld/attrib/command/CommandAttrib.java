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

    /**
     * 生命(整数)
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, aliases = {"hp"}, usage = "/attrib health|hp [health]")
    public static void health(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Health", "health", 0, Integer.MAX_VALUE);
    }

    /*
     * 速度(比例%)
     * 建议使用位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib speed [speed]")
    public static void speed(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Speed", "speed", 0, Integer.MAX_VALUE);
    }

    /*
     * 攻击(整数)
     * 建议使用位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib attack [damage]")
    public static void attack(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Attack", "attack", 0, Integer.MAX_VALUE);
    }

    /*
     * 免疫击退(概率%)
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib knock [knock]")
    public static void knock(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Knock", "knock", 0, 100);
    }

    /*
     * 护甲/防御(整数)
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib armor [armor]")
    public static void armor(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Armor", "armor", 0, Integer.MAX_VALUE);
    }

    /*
     * 格挡
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib block [chance] [ratio]")
    public static void block(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "Block",
                "block-chance", "block-ratio", 0, 100, 0, 100);
    }

    /*
     * 闪避(概率%)
     * 可配置是否叠加闪避几率，如果不叠加就使用最高几率。
     * 建议位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib dodge [dodge]")
    public static void dodge(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Dodge", "dodge", 0, 100);
    }

    /*
     * 暴击(概率%)
     * 建议位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib crit [chance] [ratio]")
    public static void crit(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "Crit",
                "crit-chance", "crit-ratio", 0, 100, 100, Integer.MAX_VALUE);
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

    private static void getSetInt(AttribManager manager, Player player, Paths args, String Name, String name, int min, int max) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    int value = Integer.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setInt(name, value);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "set" + Name, value);
                    if (manager.isAutoUpdate()) updateLore(stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                int value = tag.getCompound(ATTRIBS).getInt(name);
                manager.sendKey(player, "get" + Name, value);
            } else manager.sendKey(player, "noTag");
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetInt2(AttribManager manager, Player player, Paths args, String Name, String name1, String name2, int min1, int max1, int min2, int max2) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    int value1 = Integer.valueOf(args.get(0));
                    int value2 = Integer.valueOf(args.get(1));
                    value1 = value1 < min1 ? min1 : value1 > max1 ? max1 : value1;
                    value2 = value2 < min2 ? min2 : value2 > max2 ? max2 : value2;
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setInt(name1, value1);
                    attribs.setInt(name2, value2);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "set" + Name, value1, value2);
                    if (manager.isAutoUpdate()) updateLore(stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                int value1 = tag.getCompound(ATTRIBS).getInt(name1);
                int value2 = tag.getCompound(ATTRIBS).getInt(name2);
                manager.sendKey(player, "get" + Name, value1, value2);
            } else manager.sendKey(player, "noTag");
        } else manager.sendKey(player, "emptyHand");
    }
}
