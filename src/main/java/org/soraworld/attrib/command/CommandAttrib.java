package org.soraworld.attrib.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.soraworld.attrib.data.Attributes;
import org.soraworld.attrib.manager.AttribManager;
import org.soraworld.violet.command.Paths;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.command.Sub;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.soraworld.attrib.manager.AttribManager.updateLore;

public final class CommandAttrib {

    /**
     * 生命(整数)
     * TODO 可以为负数
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, aliases = {"hp"}, usage = "/attrib health|hp [health]")
    public static void health(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Health",
                0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.health = value,
                attrib -> attrib.health
        );
    }

    /**
     * 生命(整数)
     * TODO 可以为负数
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib regain [value]")
    public static void regain(SpigotCommand self, CommandSender sender, Paths args) {
        getSetFloat(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Regain",
                0, Float.MAX_VALUE,
                (attrib, value) -> attrib.regain = value,
                attrib -> attrib.regain
        );
    }

    /**
     * 速度(比例%)
     * 建议使用位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib walkspeed [speed]")
    public static void walkspeed(SpigotCommand self, CommandSender sender, Paths args) {
        getSetFloat(
                (AttribManager) self.manager,
                (Player) sender,
                args, "WalkSpeed",
                0, 1,
                (attrib, value) -> attrib.walkspeed = value,
                attrib -> attrib.walkspeed
        );
    }

    /**
     * 速度(比例%)
     * 建议使用位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib flyspeed [speed]")
    public static void flyspeed(SpigotCommand self, CommandSender sender, Paths args) {
        getSetFloat(
                (AttribManager) self.manager,
                (Player) sender,
                args, "FlySpeed",
                0, 1,
                (attrib, value) -> attrib.flyspeed = value,
                attrib -> attrib.flyspeed
        );
    }

    /**
     * 攻击(整数)
     * 建议使用位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib attack [damage]")
    public static void attack(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Attack",
                0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.attack = value,
                attrib -> attrib.attack
        );
    }

    /**
     * 免疫击退(概率%)
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib knock [knock]")
    public static void knock(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Knock",
                0, 100,
                (attrib, value) -> attrib.knock = value / 100.0F,
                attrib -> (int) (attrib.knock * 100)
        );
    }

    /**
     * 护甲/防御(整数)
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib armor [armor]")
    public static void armor(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Armor",
                0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.armor = value,
                attrib -> attrib.armor
        );
    }

    /**
     * 格挡
     * 建议使用位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib block [chance%] [ratio%]")
    public static void block(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Block",
                0, 100, 0, 100,
                (attrib, value) -> attrib.block_chance = value / 100.0F,
                (attrib, value) -> attrib.block_ratio = value / 100.0F,
                attrib -> (int) (attrib.block_chance * 100),
                attrib -> (int) (attrib.block_ratio * 100)
        );
    }

    /**
     * 闪避(概率%)
     * 可配置是否叠加闪避几率，如果不叠加就使用最高几率。
     * 建议位置：全部
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib dodge [chance%]")
    public static void dodge(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Dodge",
                0, 100,
                (attrib, value) -> attrib.dodge_chance = value / 100.0F,
                attrib -> (int) (attrib.dodge_chance * 100)
        );
    }

    /**
     * 暴击(概率%)
     * 建议位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib crit [chance%] [ratio%]")
    public static void crit(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Crit",
                0, 100, 0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.crit_chance = value / 100.0F,
                (attrib, value) -> attrib.crit_ratio = value / 100.0F,
                attrib -> (int) (attrib.crit_chance * 100),
                attrib -> (int) (attrib.crit_ratio * 100)
        );
    }

    /**
     * 吸血 攻击值百分比(概率%)
     * 建议位置：武器
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib suck [chance%] [ratio%]")
    public static void suck(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Suck",
                0, 100, 0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.suck_chance = value / 100.0F,
                (attrib, value) -> attrib.suck_ratio = value / 100.0F,
                attrib -> (int) (attrib.suck_chance * 100),
                attrib -> (int) (attrib.suck_ratio * 100)
        );
    }

    /**
     * 一击击杀
     * 当攻击对象血量低于 ratio% 时，有 chance% 一击击杀.
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib onekill [chance%] [ratio%]")
    public static void onekill(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2(
                (AttribManager) self.manager,
                (Player) sender,
                args, "OneKill",
                0, 100, 0, 100,
                (attrib, value) -> attrib.onekill_chance = value / 100.0F,
                (attrib, value) -> attrib.onekill_ratio = value / 100.0F,
                attrib -> (int) (attrib.onekill_chance * 100),
                attrib -> (int) (attrib.onekill_ratio * 100)
        );
    }

    /**
     * 反伤 伤害值百分比(概率%)
     * 建议位置：护甲
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib thorn [chance%] [ratio%]")
    public static void thorn(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Thorn",
                0, 100, 0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.thorn_chance = value / 100.0F,
                (attrib, value) -> attrib.thorn_ratio = value / 100.0F,
                attrib -> (int) (attrib.thorn_chance * 100),
                attrib -> (int) (attrib.thorn_ratio * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib immortal [chance%]")
    public static void immortal(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Immortal",
                0, 100,
                (attrib, value) -> attrib.immortal_chance = value / 100.0F,
                attrib -> (int) (attrib.immortal_chance * 100)
        );
    }

    /**
     * 残血爆发
     * 当血量低于百分比时，攻击力提升百分比.
     */
    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib rage [health%] [ratio%]")
    public static void rage(SpigotCommand self, CommandSender sender, Paths args) {
        getSetInt2(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Rage",
                0, 100, 0, Integer.MAX_VALUE,
                (attrib, value) -> attrib.rage_health = value / 100.0F,
                (attrib, value) -> attrib.rage_ratio = value / 100.0F,
                attrib -> (int) (attrib.rage_health * 100),
                attrib -> (int) (attrib.rage_ratio * 100)
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib bind [enable]")
    public static void bind(SpigotCommand self, CommandSender sender, Paths args) {
        getSetBool(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Bind",
                (attrib, value) -> attrib.bind_enable = value,
                attrib -> attrib.bind_enable
        );
    }

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib perm [perm]")
    public static void perm(SpigotCommand self, CommandSender sender, Paths args) {
        getSetString(
                (AttribManager) self.manager,
                (Player) sender,
                args, "Bind",
                (attrib, value) -> attrib.perm = value,
                attrib -> attrib.perm
        );
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

    @Sub(perm = "admin", onlyPlayer = true, usage = "/attrib update")
    public static void update(SpigotCommand self, CommandSender sender, Paths args) {
        AttribManager manager = (AttribManager) self.manager;
        Player player = (Player) sender;
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            updateLore(stack);
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetInt(AttribManager manager, Player player, Paths args, String Name, int min, int max, BiConsumer<Attributes, Integer> consumer, Function<Attributes, Integer> function) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                try {
                    int value = Integer.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    Attributes attrib = manager.createAttrib(stack);
                    player.setItemInHand(stack);
                    consumer.accept(attrib, value);
                    manager.sendKey(player, "set" + Name, value);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                }
            } else {
                Attributes attrib = manager.getAttrib(stack);
                if (attrib != null) manager.sendKey(player, "get" + Name, function.apply(attrib));
                else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetInt2(AttribManager manager, Player player, Paths args, String Name, int min1, int max1, int min2, int max2, BiConsumer<Attributes, Integer> cs1, BiConsumer<Attributes, Integer> cs2, Function<Attributes, Integer> fun1, Function<Attributes, Integer> fun2) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                try {
                    int value1 = Integer.valueOf(args.get(0));
                    int value2 = Integer.valueOf(args.get(1));
                    value1 = value1 < min1 ? min1 : value1 > max1 ? max1 : value1;
                    value2 = value2 < min2 ? min2 : value2 > max2 ? max2 : value2;
                    Attributes attrib = manager.createAttrib(stack);
                    player.setItemInHand(stack);
                    cs1.accept(attrib, value1);
                    cs2.accept(attrib, value2);
                    manager.sendKey(player, "set" + Name, value1, value2);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidInt");
                }
            } else {
                Attributes attrib = manager.getAttrib(stack);
                if (attrib != null) manager.sendKey(player, "get" + Name, fun1.apply(attrib), fun2.apply(attrib));
                else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetFloat(AttribManager manager, Player player, Paths args, String Name, float min, float max, BiConsumer<Attributes, Float> consumer, Function<Attributes, Float> function) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                try {
                    float value = Float.valueOf(args.first());
                    value = value < min ? min : value > max ? max : value;
                    Attributes attrib = manager.createAttrib(stack);
                    player.setItemInHand(stack);
                    consumer.accept(attrib, value);
                    manager.sendKey(player, "set" + Name, value);
                } catch (NumberFormatException ignored) {
                    manager.sendKey(player, "invalidFloat");
                }
            } else {
                Attributes attrib = manager.getAttrib(stack);
                if (attrib != null) manager.sendKey(player, "get" + Name, function.apply(attrib));
                else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetBool(AttribManager manager, Player player, Paths args, String Name, BiConsumer<Attributes, Boolean> consumer, Function<Attributes, Boolean> function) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                boolean value = Boolean.valueOf(args.first());
                Attributes attrib = manager.createAttrib(stack);
                player.setItemInHand(stack);
                consumer.accept(attrib, value);
                manager.sendKey(player, "set" + Name, value);
            } else {
                Attributes attrib = manager.getAttrib(stack);
                if (attrib != null) manager.sendKey(player, "get" + Name, function.apply(attrib));
                else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

    private static void getSetString(AttribManager manager, Player player, Paths args, String Name, BiConsumer<Attributes, String> consumer, Function<Attributes, String> function) {
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR) {
            if (args.notEmpty()) {
                String value = args.first();
                Attributes attrib = manager.createAttrib(stack);
                player.setItemInHand(stack);
                consumer.accept(attrib, value);
                manager.sendKey(player, "set" + Name, value);
            } else {
                Attributes attrib = manager.getAttrib(stack);
                if (attrib != null) manager.sendKey(player, "get" + Name, function.apply(attrib));
                else manager.sendKey(player, "noAttrib");
            }
        } else manager.sendKey(player, "emptyHand");
    }

}
