package org.soraworld.attrib.listener;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem()) checkItemAccess(event.getPlayer(), event.getItem(), event);
    }

    /**
     * 当玩家操作护甲时更新属性.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        //checkItemAccess((Player) event.getWhoClicked(), event.getCurrentItem(), event);
    }

    @EventHandler
    public void on(InventoryCloseEvent event) {
    }

    /**
     * 物品消耗耐久时检查 不灭 属性.
     */
    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemAttrib attrib = getItemAttrib(event.getItem());
        if (attrib != null && attrib.immortalChance > 0 && random.nextFloat() < attrib.immortalChance) {
            event.setDamage(0);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerItemBreakEvent event) {
    }

    /**
     * TODO 攻击力相关的属性只能设置在武器上，防御相关的属性只能设置在护甲上
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity e1 = event.getDamager();
        Entity e2 = event.getEntity();
        if (e1 instanceof Damageable && e2 instanceof Damageable) {
            Damageable de1 = (Damageable) e1;
            Damageable de2 = (Damageable) e2;
            double origin = event.getDamage(), damage = origin;
            double attackDamage, suck = 0, thorn = 0;
            if (de1 instanceof Player) {
                Player attacker = (Player) de1;
                PlayerAttrib attrib = getPlayerAttrib(attacker);
                if (de2.getHealth() < de2.getMaxHealth() * attrib.onekillRation && attrib.onekillChance > 0 && attrib.onekillChance > random.nextFloat()) {
                    event.setDamage(de2.getHealth());
                    return;
                }
                if (attrib.critChance > 0 && attrib.critChance > random.nextFloat()) damage += origin * attrib.critRation;
                if (attacker.getHealth() < attacker.getMaxHealth() * attrib.rageHealth) damage += origin * attrib.rageRation;
                if (attrib.suckChance > 0 && attrib.suckChance > random.nextFloat()) {
                    suck = damage * attrib.suckRation;
                }
            }
            attackDamage = damage;
            if (e2 instanceof Player) {
                Player victim = (Player) e2;
                PlayerAttrib attrib = getPlayerAttrib(victim);
                if (attrib.dodgeChance > 0 && attrib.dodgeChance > random.nextFloat()) {
                    event.setDamage(0);// dodge will not cause thorn
                    return;
                }
                damage -= attrib.armor;
                if (attrib.blockChance > 0 && attrib.blockChance > random.nextFloat() && isHoldRight(victim)) {
                    damage -= attackDamage * attrib.blockRation;
                }
                if (attrib.thornChance > 0 && attrib.thornChance > random.nextFloat()) {
                    thorn = attackDamage * attrib.thornRatio;
                }
            }
            event.setDamage(damage);
            double health = de1.getHealth() - thorn + suck;
            de1.setHealth(health < de1.getMaxHealth() ? health : de1.getMaxHealth());
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

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
    }

    private PlayerAttrib getPlayerAttrib(Player player) {
        PlayerAttrib pa = new PlayerAttrib();
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

    private void checkItemAccess(Player player, ItemStack stack, Cancellable event) {
        ItemAttrib item = getItemAttrib(stack);
        if (item != null) {
            if (item.perm != null && !item.perm.isEmpty() && !player.hasPermission(item.perm)) {
                event.setCancelled(true);
                manager.sendKey(player, "noItemPerm");
                return;
            }
            if (item.bindEnable) {
                String owner = getOwner(stack);
                if (owner != null && !owner.isEmpty()) {
                    if (!owner.equals(player.getName())) {
                        event.setCancelled(true);
                        manager.sendKey(player, "notItemOwner");
                    }
                } else setOwner(stack, player.getName());
            }
        }
    }

    private static boolean isHoldRight(Player player) {
        return ((CraftPlayer) player).getHandle().by();
    }

    private static String getOwner(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        String line = lore.get(0);
        int index = line.indexOf("bind:");
        return index >= 0 ? line.substring(line.indexOf("bind:") + 5) : null;
    }

    private static ItemStack setOwner(ItemStack stack, String owner) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(0, lore.get(0) + "|bind:" + owner);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}
