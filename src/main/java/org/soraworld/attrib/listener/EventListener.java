package org.soraworld.attrib.listener;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.soraworld.attrib.data.ItemAttrib;
import org.soraworld.attrib.data.PlayerAttrib;
import org.soraworld.attrib.manager.AttribManager;

import java.util.Random;

public class EventListener implements Listener {

    private static Random random = new Random(System.currentTimeMillis());

    private final AttribManager manager;

    public EventListener(AttribManager manager) {
        this.manager = manager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        //updateArmor(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
    }

    /**
     * 当玩家右键装备护甲时更新属性.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
    }

    /**
     * 当玩家操作护甲时更新属性.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInventoryClick(InventoryClickEvent event) {
    }

    @EventHandler
    public void on(InventoryCloseEvent event) {
    }

    /**
     * 物品消耗耐久时检查 不灭 属性.
     */
    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemAttrib attrib = manager.getAttrib(event.getItem());
        if (attrib != null && attrib.immortalChance > 0 && random.nextFloat() < attrib.immortalChance) {
            event.setDamage(0);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerItemBreakEvent event) {
    }

    // TODO 攻击力相关的属性只能设置在武器上，防御相关的属性只能设置在护甲上
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity _damager = event.getDamager();
        Entity _damagee = event.getEntity();
        if (_damager instanceof Player && _damagee instanceof Damageable) {
            Player damager = (Player) _damager;
            PlayerAttrib attack = getPlayerAttrib((Player) _damager);
            // 0. 计算一击击杀概率
            // 1. 计算攻击者攻击力
            // 2. 计算受害者防御力
            // 3. 计算受害者损伤值
            // 4. 计算受害者反伤值

            // 一击击杀
            if (((Damageable) _damagee).getHealth() < ((Damageable) _damagee).getMaxHealth() * attack.onekillRation && random.nextFloat() < attack.onekillChance) {
                // 一击击杀不再吸血反伤等等且无法闪避无法格挡
                event.setDamage(((Damageable) _damagee).getHealth());
                return;
            }
            // 基础
            double base = event.getDamage(), damage = base, suck = 0, thorn = 0;// 基础攻击力
            // 暴击
            if (random.nextFloat() < attack.critChance) damage += base * attack.critRation;
            // 残血爆发
            if (damager.getHealth() < damager.getMaxHealth() * attack.rageHealth) damage += base * attack.rageRation;
            double attackDamage = damage;
            // 受害者防御
            if (_damagee instanceof Player) {
                Player damagee = (Player) _damagee;
                PlayerAttrib victim = getPlayerAttrib(damagee);
                // 闪避
                if (random.nextFloat() < victim.dodgeChance) {
                    event.setDamage(0);
                    return;
                }
                // 护甲
                damage -= victim.armor;
                // 右键格挡
                if (random.nextFloat() < victim.blockChance && isHoldRight(damagee)) {
                    damage -= attackDamage * victim.blockRation;
                }
                // 反伤
                if (random.nextFloat() < victim.thornChance) {
                    thorn = attackDamage * victim.thornRatio;
                }
            }

            // 吸血 护甲无法防御吸血
            if (random.nextFloat() < attack.suckChance) {
                suck = attackDamage * attack.suckRation;
                damage += suck;
            }
            event.setDamage(damage);
            double health = damager.getHealth() - thorn + suck;
            damager.setHealth(health < damager.getMaxHealth() ? health : damager.getMaxHealth());

        } else if (_damager instanceof Player) {
            System.out.println();
        } else if (_damagee instanceof Player) {
            System.out.println();
        }
    }

    private boolean isHoldRight(Player player) {
        return ((CraftPlayer) player).getHandle().by();
    }

    private PlayerAttrib getPlayerAttrib(Player player) {
        PlayerAttrib pAttrib = new PlayerAttrib();
        ItemAttrib attrib = manager.getAttrib(player.getItemInHand());
        if (attrib != null) {
            pAttrib.blockChance = attrib.block_chance;
            pAttrib.blockRation = attrib.block_ratio;
            pAttrib.critChance = attrib.critChance;
            pAttrib.critRation = attrib.critRatio;
            pAttrib.onekillChance = attrib.onekill_chance;
            pAttrib.onekillRation = attrib.onekill_ratio;
            pAttrib.suckChance = attrib.suck_chance;
            pAttrib.suckRation = attrib.suck_ratio;
            pAttrib.rageHealth = attrib.rage_health;
            pAttrib.rageRation = attrib.rage_ratio;
        }
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            ItemAttrib itemAttrib = manager.getAttrib(stack);
            if (itemAttrib != null) {
                pAttrib.armor += itemAttrib.armor;
                pAttrib.dodgeChance += itemAttrib.dodgeChance;
                pAttrib.thornChance += itemAttrib.thornChance;
                pAttrib.thornRatio += itemAttrib.thornRatio;
            }
        }

        return pAttrib;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        manager.startTask(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        manager.stopTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.stopTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
    }
}
