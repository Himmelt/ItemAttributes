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
     * TODO 可以为负数
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, aliases = {"hp"}, usage = "/attrib health|hp [health]")
    public static void health(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Health", "health", 0, Integer.MAX_VALUE);
    }

    /**
     * 生命(整数)
     * TODO 可以为负数
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib regain [value]")
    public static void regain(SpigotCommand self, CommandSender sender, Paths args) {
        getSetFloat((AttribManager) self.manager, (Player) sender, args,
                "Heal", "heal", 0, Float.MAX_VALUE);
    }

    /**
     * 速度(比例%)
     * 建议使用位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib walkspeed [speed]")
    public static void walkspeed(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "WalkSpeed", "walkspeed", 0, Integer.MAX_VALUE);
    }

    /**
     * 速度(比例%)
     * 建议使用位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib flyspeed [speed]")
    public static void flyspeed(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "FlySpeed", "flyspeed", 0, Integer.MAX_VALUE);
    }

    /**
     * 攻击(整数)
     * 建议使用位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib attack [damage]")
    public static void attack(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Attack", "attack", 0, Integer.MAX_VALUE);
    }

    /**
     * 免疫击退(概率%)
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib knock [knock]")
    public static void knock(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Knock", "knock", 0, 100);
    }

    /**
     * 护甲/防御(整数)
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib armor [armor]")
    public static void armor(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Armor", "armor", 0, Integer.MAX_VALUE);
    }

    /**
     * 格挡
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib block [chance%] [ratio%]")
    public static void block(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "Block",
                "block-chance", "block-ratio", 0, 100, 0, 100);
    }

    /**
     * 闪避(概率%)
     * 可配置是否叠加闪避几率，如果不叠加就使用最高几率。
     * 建议位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib dodge [chance%]")
    public static void dodge(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args,
                "Dodge", "dodge-chance", 0, 100);
    }

    /**
     * 暴击(概率%)
     * 建议位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib crit [chance%] [ratio%]")
    public static void crit(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "Crit",
                "crit-chance", "crit-ratio", 0, 100, 0, Integer.MAX_VALUE);
    }

    /**
     * 吸血 攻击值百分比(概率%)
     * 建议位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib suck [chance%] [ratio%]")
    public static void suck(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "Crit",
                "suck-chance", "suck-ratio", 0, 100, 0, Integer.MAX_VALUE);
    }

    /**
     * 一击击杀
     * 当攻击对象血量低于 ratio% 时，有 chance% 一击击杀.
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib onekill [chance%] [ratio%]")
    public static void onekill(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "OneKill",
                "onekill-chance", "onekill-ratio", 0, 100, 0, 100);
    }

    /**
     * 反伤 伤害值百分比(概率%)
     * 建议位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib thorn [chance%] [ratio%]")
    public static void thorn(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "Crit",
                "thorn-chance", "thorn-ratio", 0, 100, 0, Integer.MAX_VALUE);
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

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib immortal [chance%]")
    public static void immortal(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt((AttribManager) self.manager, (Player) sender, args, "Immortal", "immortal-chance", 0, 100);
    }

    /**
     * 残血爆发
     * 当血量低于百分比时，攻击力提升百分比.
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib rage [health%] [ratio%]")
    public static void rage(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2((AttribManager) self.manager, (Player) sender, args, "Rage",
                "rage-hp", "rage-ratio", 0, 100, 0, Integer.MAX_VALUE);
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib bind [enable]")
    public static void bind(SpigotCommand self, CommandSender sender, Paths args) {
        getSetBool((AttribManager) self.manager, (Player) sender, args, "Bind", "bind-enable");
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib perm [perm]")
    public static void perm(SpigotCommand self, CommandSender sender, Paths args) {
        getSetString((AttribManager) self.manager, (Player) sender, args, "Perm", "perm");
    }

    /**
     * 穿戴时给予自身药水效果
     * 建议位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib potions [id]")
    public static void potions(SpigotCommand self, CommandSender sender, Paths args) {

    }

    /**
     * 左键魔咒(予以攻击对象)
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib spells")
    public static void spells(SpigotCommand self, CommandSender sender, Paths args) {

    }

    /**
     * 右键技能
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib skills")
    public static void skills(SpigotCommand self, CommandSender sender, Paths args) {
        // TODO 消耗生命值提升攻击力/防御
    }

    private static void getSetBool(AttribManager manager, Player player, Paths args, String Name, String name) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    boolean value = Boolean.valueOf(args.first());
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setBoolean(name, value);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "set" + Name, value);
                    if (manager.isAutoUpdate()) updateLore(stack);
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                boolean value = tag.getCompound(ATTRIBS).getBoolean(name);
                manager.sendKey(player, "get" + Name, value);
            } else manager.sendKey(player, "noTag");
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

    private static void getSetFloat(AttribManager manager, Player player, Paths args, String Name, String name, float min, float max) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    float value = Float.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setFloat(name, value);
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "set" + Name, value);
                    if (manager.isAutoUpdate()) updateLore(stack);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidFloat");
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                float value = tag.getCompound(ATTRIBS).getFloat(name);
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

    private static void getSetString(AttribManager manager, Player player, Paths args, String Name, String name) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            NBTTagCompound tag = getTag(stack);
            if (args.notEmpty()) {
                try {
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        setTag(stack, tag);
                    }
                    NBTTagCompound attribs = tag.getCompound(ATTRIBS);
                    attribs.setString(name, args.first());
                    tag.set(ATTRIBS, attribs);
                    manager.sendKey(player, "set" + Name, args.first());
                    if (manager.isAutoUpdate()) updateLore(stack);
                } catch (Throwable ignored) {
                    manager.sendKey(player, "nbtError");
                }
            } else if (tag != null && tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                String value = tag.getCompound(ATTRIBS).getString(name);
                manager.sendKey(player, "get" + Name, value);
            } else manager.sendKey(player, "noTag");
        } else manager.sendKey(player, "emptyHand");
    }
}
