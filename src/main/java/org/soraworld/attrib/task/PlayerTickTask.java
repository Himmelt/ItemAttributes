package org.soraworld.attrib.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.soraworld.attrib.data.ItemAttrib;
import org.soraworld.attrib.data.LoreInfo;
import org.soraworld.attrib.manager.AttribManager;
import org.soraworld.attrib.nbt.NBTUtil;

import java.util.UUID;

import static org.soraworld.attrib.manager.AttribManager.getInfo;
import static org.soraworld.violet.nms.Version.*;

public class PlayerTickTask extends BukkitRunnable {

    private final Player player;
    private static AttribManager manager;

    static final UUID maxHealthUUID = UUID.fromString("e0b4701b-9843-4aa0-9aa5-4c7c97be3d27");
    static final UUID moveSpeedUUID = UUID.fromString("23d25cb0-1b72-44c8-b125-3aa3c8b5fe0f");
    static final UUID attackDamageUUID = UUID.fromString("b2123860-0b68-49b2-a631-d13b6a27f686");
    static final UUID knockResistUUID = UUID.fromString("96d8c6f6-41b6-42ea-8a1b-cfa3131b7d72");

    public PlayerTickTask(Player player) {
        this.player = player;
    }

    public static boolean isHoldRight(Player player) {
        if (v1_7_R4) return v1_7_R4_TickTask.isHoldRight(player);
        if (v1_8_R1) return v1_8_R1_TickTask.isHoldRight(player);
        if (v1_8_R3) return v1_8_R3_TickTask.isHoldRight(player);
        if (v1_9_R1) return v1_9_R1_TickTask.isHoldRight(player);
        if (v1_9_R2) return v1_9_R2_TickTask.isHoldRight(player);
        if (v1_10_R1) return v1_10_R1_TickTask.isHoldRight(player);
        if (v1_11_R1) return v1_11_R1_TickTask.isHoldRight(player);
        if (v1_12_R1) return v1_12_R1_TickTask.isHoldRight(player);
        if (v1_13_R1) return v1_13_R1_TickTask.isHoldRight(player);
        if (v1_13_R2) return v1_13_R2_TickTask.isHoldRight(player);
        return false;
    }

    public static PlayerTickTask createTask(Player player, AttribManager manager) {
        PlayerTickTask.manager = manager;
        if (v1_7_R4) return new v1_7_R4_TickTask(player);
        if (v1_8_R1) return new v1_8_R1_TickTask(player);
        if (v1_8_R3) return new v1_8_R3_TickTask(player);
        if (v1_9_R1) return new v1_9_R1_TickTask(player);
        if (v1_9_R2) return new v1_9_R2_TickTask(player);
        if (v1_10_R1) return new v1_10_R1_TickTask(player);
        if (v1_11_R1) return new v1_11_R1_TickTask(player);
        if (v1_12_R1) return new v1_12_R1_TickTask(player);
        if (v1_13_R1) return new v1_13_R1_TickTask(player);
        if (v1_13_R2) return new v1_13_R2_TickTask(player);
        return new PlayerTickTask(player);
    }

    public void run() {
        if (player.isValid()) {
            float regain = 0.0F, flyspeed = 0.1F;
            double maxHealth = 0.0D, moveSpeed = 0.0D, attackDamage = 0.0D, knockResist = 0.0D;

            // Check ItemInHand
            LoreInfo info = getInfo(player.getItemInHand());
            if (info.attrib != null && info.canUse(player)) attackDamage += info.attrib.attack;

            PlayerInventory inventory = player.getInventory();
            // Check Armors
            for (ItemStack stack : inventory.getArmorContents()) {
                info = getInfo(stack);
                if (info.attrib != null && info.canUse(player)) {
                    maxHealth += info.attrib.health;
                    //moveSpeed += info.attrib.walkspeed;
                    knockResist += info.attrib.knock;
                    regain += info.attrib.regain;
                    flyspeed += info.attrib.flyspeed;
                    info.attrib.applyPotions(player);
                }
            }

            // Check Baubles
            for (int slot : manager.baubleSlots) {
                if (slot != inventory.getHeldItemSlot()) {
                    info = getInfo(inventory.getItem(slot));
                    if (info.attrib != null && info.canUse(player)) {
                        maxHealth += info.attrib.health;
                        //moveSpeed += info.attrib.walkspeed;
                        knockResist += info.attrib.knock;
                        regain += info.attrib.regain;
                        flyspeed += info.attrib.flyspeed;
                        info.attrib.applyPotions(player);
                    }
                }
            }

            ItemAttrib held = NBTUtil.getAttrib(player.getItemInHand());

            updateModifier(maxHealth, moveSpeed, attackDamage, knockResist);

            player.setFlySpeed(flyspeed > 1.0F ? 1.0F : flyspeed);
            maxHealth = player.getMaxHealth();
            double health = player.getHealth() + regain;
            player.setHealth(health < 0 ? 0 : health > maxHealth ? maxHealth : health);
        } else cancel();
    }

    public void updateModifier(double maxHealth, double moveSpeed, double attackDamage, double knockResist) {
    }
}
