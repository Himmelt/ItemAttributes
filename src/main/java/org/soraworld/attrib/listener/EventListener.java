package org.soraworld.attrib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.soraworld.attrib.data.Attributes;
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
        Attributes attrib = manager.getAttrib(event.getItem());
        if (attrib != null && attrib.immortal_chance > 0 && random.nextFloat() < attrib.immortal_chance) {
            event.setDamage(0);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerItemBreakEvent event) {
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        manager.startTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.stopTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
    }
}
