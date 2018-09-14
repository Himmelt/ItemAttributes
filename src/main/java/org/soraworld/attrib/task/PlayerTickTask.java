package org.soraworld.attrib.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.soraworld.attrib.data.ItemAttrib;

import java.util.UUID;

import static org.soraworld.attrib.manager.AttribManager.getItemAttrib;
import static org.soraworld.violet.nms.Version.*;

public class PlayerTickTask extends BukkitRunnable {

    private final Player player;

    static final UUID maxHealthUUID = UUID.fromString("e0b4701b-9843-4aa0-9aa5-4c7c97be3d27");
    static final UUID moveSpeedUUID = UUID.fromString("23d25cb0-1b72-44c8-b125-3aa3c8b5fe0f");
    static final UUID attackDamageUUID = UUID.fromString("b2123860-0b68-49b2-a631-d13b6a27f686");
    static final UUID knockResistUUID = UUID.fromString("96d8c6f6-41b6-42ea-8a1b-cfa3131b7d72");

    public PlayerTickTask(Player player) {
        this.player = player;
        // TODO 检查 respawn 和 切换世界 重新加入 EntityPlayer 是否改变
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

    public static PlayerTickTask createTask(Player player) {
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
        ItemAttrib attrib;
        float regain = 0.0F, flyspeed = 0.1F;
        double maxHealth = 0.0D, moveSpeed = 0.0D, attackDamage = 0.0D, knockResist = 0.0D;

        // Check ItemInHand
        attrib = getItemAttrib(player.getItemInHand());
        if (attrib != null) attackDamage += attrib.attack;

        // Check Armors
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            attrib = getItemAttrib(stack);
            if (attrib != null) {
                maxHealth += attrib.health;
                moveSpeed += attrib.walkspeed;
                knockResist += attrib.knock;
                regain += attrib.regain;
                flyspeed += attrib.flyspeed;
                // TODO potions
            }
        }

        updateModifier(maxHealth, moveSpeed, attackDamage, knockResist);

        player.setFlySpeed(flyspeed > 1.0F ? 1.0F : flyspeed);
        maxHealth = player.getMaxHealth();
        player.setHealth(player.getHealth() + regain > maxHealth ? maxHealth : player.getHealth() + regain);
    }

    public void updateModifier(double maxHealth, double moveSpeed, double attackDamage, double knockResist) {
    }
}
