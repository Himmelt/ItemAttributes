package org.soraworld.attrib.listener;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.soraworld.attrib.data.ItemAttrib;
import org.soraworld.attrib.data.LoreInfo;
import org.soraworld.attrib.data.PlayerAttrib;
import org.soraworld.attrib.manager.AttribManager;

import java.util.List;
import java.util.Random;

import static org.soraworld.attrib.manager.AttribManager.getInfo;
import static org.soraworld.attrib.manager.AttribManager.getItemAttrib;
import static org.soraworld.attrib.task.PlayerTickTask.isHoldRight;

public class EventListener implements Listener {

    private static Random random = new Random(System.currentTimeMillis());

    private final AttribManager manager;

    public EventListener(AttribManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem()) {
            Player player = event.getPlayer();
            LoreInfo info = getInfo(event.getItem());
            if (info.attrib != null) {
                if (info.attrib.perm != null && !info.attrib.perm.isEmpty()) {
                    if (!player.hasPermission(info.attrib.perm)) {
                        event.setCancelled(true);
                        manager.sendKey(player, "noItemPerm", info.attrib.perm);
                    }
                }
                if (info.attrib.bindEnable) {
                    if (info.owner != null && !info.owner.isEmpty()) {
                        if (!info.owner.equals(player.getName())) {
                            event.setCancelled(true);
                            manager.sendKey(player, "notItemOwner");
                        }
                    } else {
                        ItemStack stack = event.getItem();
                        ItemMeta meta = stack.getItemMeta();
                        List<String> lore = meta.getLore();
                        lore.set(0, lore.get(0) + " bind:" + player.getName());
                        meta.setLore(lore);
                        stack.setItemMeta(meta);
                        manager.sendKey(player, "itemBind");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemAttrib attrib = getItemAttrib(event.getItem());
        if (attrib != null && attrib.immortalChance > 0 && random.nextFloat() < attrib.immortalChance) {
            event.setDamage(0);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Object e1 = event.getDamager();
        if (e1 instanceof Projectile) e1 = ((Projectile) e1).getShooter();
        Entity e2 = event.getEntity();
        if (e1 instanceof Damageable && e2 instanceof Damageable) {
            Damageable de1 = (Damageable) e1;
            Damageable de2 = (Damageable) e2;
            double origin = event.getDamage(), damage = origin;
            double attackDamage, suck = 0, thorn = 0;
            if (de1 instanceof Player) {
                Player attacker = (Player) de1;
                PlayerAttrib attrib = getPlayerAttrib(attacker);
                if (de2.getHealth() <= de2.getMaxHealth() * attrib.onekillRation && attrib.onekillChance > 0 && attrib.onekillChance > random.nextFloat()) {
                    event.setDamage(de2.getHealth() + 1.0D);
                    return;
                }
                if (attrib.critChance > 0 && attrib.critChance > random.nextFloat()) damage += origin * attrib.critRation;
                if (attacker.getHealth() <= attacker.getMaxHealth() * attrib.rageHealth) damage += origin * attrib.rageRation;
                if (attrib.suckChance > 0 && attrib.suckChance > random.nextFloat()) {
                    suck = damage * attrib.suckRation;
                }
            }
            attackDamage = damage;
            if (e2 instanceof Player) {
                Player victim = (Player) e2;
                PlayerAttrib attrib = getPlayerAttrib(victim);
                if (attrib.dodgeChance > 0 && attrib.dodgeChance > random.nextFloat()) {
                    event.setDamage(0);
                    return;
                }
                damage -= attackDamage * attrib.armor;
                if (attrib.blockChance > 0 && attrib.blockChance > random.nextFloat() && isHoldRight(victim)) {
                    damage -= attackDamage * attrib.blockRation;
                }
                if (attrib.thornChance > 0 && attrib.thornChance > random.nextFloat()) {
                    thorn = attackDamage * attrib.thornRatio;
                }
            }
            event.setDamage(damage < 0 ? 0 : damage);
            double health = de1.getHealth() - thorn + suck;
            de1.setHealth(health < 0 ? 0 : health < de1.getMaxHealth() ? health : de1.getMaxHealth());
        }
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

    private static PlayerAttrib getPlayerAttrib(Player player) {
        PlayerAttrib pa = new PlayerAttrib();
        // TODO 1.9+
        LoreInfo info = getInfo(player.getItemInHand());
        if (info.attrib != null && info.canUse(player)) {
            pa.blockChance = info.attrib.blockChance;
            pa.blockRation = info.attrib.blockRatio;
            pa.critChance = info.attrib.critChance;
            pa.critRation = info.attrib.critRatio;
            pa.onekillChance = info.attrib.onekillChance;
            pa.onekillRation = info.attrib.onekillRatio;
            pa.suckChance = info.attrib.suckChance;
            pa.suckRation = info.attrib.suckRatio;
            pa.rageHealth = info.attrib.rageHealth;
            pa.rageRation = info.attrib.rageRatio;
        }
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            info = getInfo(stack);
            if (info.attrib != null && info.canUse(player)) {
                pa.armor += info.attrib.armor;
                pa.dodgeChance += info.attrib.dodgeChance;
                pa.thornChance += info.attrib.thornChance;
                pa.thornRatio += info.attrib.thornRatio;
            }
        }
        return pa;
    }
}
