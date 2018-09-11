package org.soraworld.attrib.task;

import net.minecraft.server.v1_7_R4.AttributeInstance;
import net.minecraft.server.v1_7_R4.AttributeModifier;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.soraworld.attrib.data.Attributes;
import org.soraworld.attrib.manager.AttribManager;

import java.util.UUID;

public class PlayerTickTask extends BukkitRunnable {

    private final Player player;
    private final AttribManager manager;

    private AttributeInstance maxHealthInstance;
    private AttributeInstance moveSpeedInstance;
    private AttributeInstance knockResistInstance;
    private AttributeInstance attackDamageInstance;

    private static final UUID maxHealthUUID = UUID.fromString("e0b4701b-9843-4aa0-9aa5-4c7c97be3d27");
    private static final UUID moveSpeedUUID = UUID.fromString("23d25cb0-1b72-44c8-b125-3aa3c8b5fe0f");
    private static final UUID attackDamageUUID = UUID.fromString("b2123860-0b68-49b2-a631-d13b6a27f686");
    private static final UUID knockResistUUID = UUID.fromString("96d8c6f6-41b6-42ea-8a1b-cfa3131b7d72");

    public PlayerTickTask(AttribManager manager, Player player) {
        this.player = player;
        this.manager = manager;
        // TODO 检查 respawn 和 切换世界 重新加入 EntityPlayer 是否改变
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        maxHealthInstance = handle.getAttributeInstance(GenericAttributes.maxHealth);
        moveSpeedInstance = handle.getAttributeInstance(GenericAttributes.d);// movementSpeed
        knockResistInstance = handle.getAttributeInstance(GenericAttributes.c);// knockbackResistance
        attackDamageInstance = handle.getAttributeInstance(GenericAttributes.e);// attackDamage
    }

    public void run() {
        Attributes attrib;
        float regain = 0.0F, flyspeed = 0.1F;
        double maxHealth = 0.0D, moveSpeed = 0.0D, attackDamage = 0.0D, knockResist = 0.0D;

        // Check ItemInHand
        attrib = manager.getAttrib(player.getItemInHand());
        if (attrib != null) attackDamage += attrib.attack;

        // Check Armors
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            attrib = manager.getAttrib(stack);
            if (attrib != null) {
                maxHealth += attrib.health;
                moveSpeed += attrib.walkspeed;
                knockResist += attrib.knock;
                regain += attrib.regain;
                flyspeed += attrib.flyspeed;
                // TODO potions
            }
        }

        AttributeModifier modifier = new AttributeModifier(attackDamageUUID, "attackDamage", attackDamage, 0);
        attackDamageInstance.b(modifier);// remove
        if (attackDamage != 0) attackDamageInstance.a(modifier);// apply

        modifier = new AttributeModifier(maxHealthUUID, "maxHealth", maxHealth, 0);
        maxHealthInstance.b(modifier);// remove
        if (maxHealth != 0) maxHealthInstance.a(modifier);// apply

        modifier = new AttributeModifier(moveSpeedUUID, "moveSpeed", moveSpeed, 0);
        moveSpeedInstance.b(modifier);// remove
        if (moveSpeed != 0) moveSpeedInstance.a(modifier);// apply

        modifier = new AttributeModifier(knockResistUUID, "knockResist", knockResist, 0);
        knockResistInstance.b(modifier);// remove
        if (knockResist != 0) knockResistInstance.a(modifier);// apply

        player.setFlySpeed(flyspeed > 1.0F ? 1.0F : flyspeed);
        maxHealth = player.getMaxHealth();
        player.setHealth(player.getHealth() + regain > maxHealth ? maxHealth : player.getHealth() + regain);
    }
}
