package org.soraworld.attrib.task;

import net.minecraft.server.v1_9_R1.AttributeInstance;
import net.minecraft.server.v1_9_R1.AttributeModifier;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.GenericAttributes;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class v1_9_R1_TickTask extends PlayerTickTask {

    private AttributeInstance maxHealthInstance;
    private AttributeInstance moveSpeedInstance;
    private AttributeInstance attackDamageInstance;
    private AttributeInstance knockResistInstance;

    v1_9_R1_TickTask(Player player) {
        super(player);
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        maxHealthInstance = handle.getAttributeInstance(GenericAttributes.maxHealth);
        moveSpeedInstance = handle.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);// movementSpeed
        attackDamageInstance = handle.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);// attackDamage
        knockResistInstance = handle.getAttributeInstance(GenericAttributes.c);// knockbackResistance
    }

    public static boolean isHoldRight(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        return ep.cs() && ep.cv() != null;
    }

    public void updateModifier(double maxHealth, double moveSpeed, double attackDamage, double knockResist) {
        AttributeModifier modifier = new AttributeModifier(maxHealthUUID, "maxHealth", maxHealth, 0);
        maxHealthInstance.c(modifier);// remove
        if (maxHealth != 0) maxHealthInstance.b(modifier);// apply

        modifier = new AttributeModifier(moveSpeedUUID, "moveSpeed", moveSpeed, 0);
        moveSpeedInstance.c(modifier);// remove
        if (moveSpeed != 0) moveSpeedInstance.b(modifier);// apply

        modifier = new AttributeModifier(attackDamageUUID, "attackDamage", attackDamage, 0);
        attackDamageInstance.c(modifier);// remove
        if (attackDamage != 0) attackDamageInstance.b(modifier);// apply

        modifier = new AttributeModifier(knockResistUUID, "knockResist", knockResist, 0);
        knockResistInstance.c(modifier);// remove
        if (knockResist != 0) knockResistInstance.b(modifier);// apply
    }
}
