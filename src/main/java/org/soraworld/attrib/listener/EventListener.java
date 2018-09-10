package org.soraworld.attrib.listener;

import net.minecraft.server.v1_7_R4.AttributeInstance;
import net.minecraft.server.v1_7_R4.AttributeModifier;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.soraworld.attrib.data.Attributes;
import org.soraworld.attrib.manager.AttribManager;
import org.soraworld.violet.plugin.SpigotPlugin;

import java.util.Random;
import java.util.UUID;

public class EventListener implements Listener {

    private static final UUID maxHealthUUID = UUID.fromString("e0b4701b-9843-4aa0-9aa5-4c7c97be3d27");
    private static final UUID moveSpeedUUID = UUID.fromString("23d25cb0-1b72-44c8-b125-3aa3c8b5fe0f");
    private static final UUID attackDamageUUID = UUID.fromString("b2123860-0b68-49b2-a631-d13b6a27f686");
    private static final UUID knockResistUUID = UUID.fromString("96d8c6f6-41b6-42ea-8a1b-cfa3131b7d72");

    private static long last = System.currentTimeMillis();
    private static Random random = new Random(System.currentTimeMillis());

    private final SpigotPlugin plugin;
    private final AttribManager manager;
    private final BukkitScheduler scheduler;

    public EventListener(AttribManager manager) {
        this.manager = manager;
        this.scheduler = Bukkit.getScheduler();
        this.plugin = manager.getPlugin();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        //updateArmor(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        scheduler.runTask(plugin, () -> updateWeapon(event.getPlayer()));
    }

    /**
     * 当玩家右键装备护甲时更新属性.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            scheduler.runTask(plugin, () -> updateArmor(event.getPlayer()));
        }
    }

    /**
     * 当玩家操作护甲时更新属性.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) {
            scheduler.runTask(plugin, () -> updateArmor((Player) event.getWhoClicked()));
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent event) {
        System.out.println("InventoryCloseEvent");
        if (event.getInventory() instanceof PlayerInventory) {
            //updateArmor((Player) event.getPlayer());
        }
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
        if (manager.getAttrib(event.getBrokenItem()) != null) {
            scheduler.runTask(plugin, () -> updateArmor(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        System.out.println("PlayerJoinEvent" + event.getPlayer());
        manager.startTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        // TODO ??? check sequence with join event
        System.out.println("PlayerLoginEvent" + event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.stopTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
    }

    private void updateArmor(Player player) {
        if (System.currentTimeMillis() - last < 300) return;
        last = System.currentTimeMillis();
        double maxHealth = 0.0D, moveSpeed = 0.0D, /*attackDamage = 0.0D,*/ knockResist = 0.0D;
        ItemStack[] stacks = player.getInventory().getArmorContents();
        for (ItemStack stack : stacks) {
            if (stack != null) {
                Attributes attrib = manager.getAttrib(stack);
                if (attrib != null) {
                    maxHealth += attrib.health;
                    moveSpeed += attrib.walkspeed;
                    //attackDamage += attrib.attack;
                    knockResist += attrib.knock;
                }
            }
        }

        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        AttributeModifier maxHealthModifier = new AttributeModifier(maxHealthUUID, "maxHealth", maxHealth, 0);
        AttributeInstance attrib = handle.getAttributeInstance(GenericAttributes.maxHealth);
        attrib.b(maxHealthModifier);// remove
        if (maxHealth != 0) attrib.a(maxHealthModifier);// apply

        AttributeModifier moveSpeedModifier = new AttributeModifier(moveSpeedUUID, "moveSpeed", moveSpeed, 0);
        attrib = handle.getAttributeInstance(GenericAttributes.d);// movementSpeed
        attrib.b(moveSpeedModifier);// remove
        if (moveSpeed != 0) attrib.a(moveSpeedModifier);// apply

        //AttributeModifier attackDamageModifier = new AttributeModifier(attackDamageUUID, "attackDamage", attackDamage, 0);
        //attrib = handle.getAttributeInstance(GenericAttributes.e);// attackDamage
        //attrib.b(attackDamageModifier);// remove
        //if (attackDamage != 0) attrib.a(attackDamageModifier);// apply

        AttributeModifier knockResistModifier = new AttributeModifier(knockResistUUID, "knockResist", knockResist, 0);
        attrib = handle.getAttributeInstance(GenericAttributes.c);// knockbackResistance
        attrib.b(knockResistModifier);// remove
        if (knockResist != 0) attrib.a(knockResistModifier);// apply
    }

    private void updateWeapon(Player player) {
        if (System.currentTimeMillis() - last < 300) return;
        last = System.currentTimeMillis();
        Attributes attrib = manager.getAttrib(player.getItemInHand());
        if (attrib != null && attrib.attack > 0) {
            EntityPlayer handle = ((CraftPlayer) player).getHandle();
            AttributeModifier modifier = new AttributeModifier(attackDamageUUID, "attackDamage", attrib.attack, 0);
            AttributeInstance attack = handle.getAttributeInstance(GenericAttributes.e);// attackDamage
            attack.b(modifier);// remove
            attack.a(modifier);// apply
        }
    }
}
