package org.soraworld.attrib.task;

import net.minecraft.server.v1_7_R4.AttributeInstance;
import net.minecraft.server.v1_7_R4.AttributeModifier;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

class v1_10_R1_TickTask extends PlayerTickTask {

    private AttributeInstance maxHealthInstance;
    private AttributeInstance moveSpeedInstance;
    private AttributeInstance attackDamageInstance;
    private AttributeInstance knockResistInstance;

    v1_10_R1_TickTask(Player player) {
        super(player);
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        maxHealthInstance = handle.getAttributeInstance(GenericAttributes.maxHealth);
        moveSpeedInstance = handle.getAttributeInstance(GenericAttributes.d);// movementSpeed
        attackDamageInstance = handle.getAttributeInstance(GenericAttributes.e);// attackDamage
        knockResistInstance = handle.getAttributeInstance(GenericAttributes.c);// knockbackResistance
    }

    public static boolean isHoldRight(Player player) {
        return ((CraftPlayer) player).getHandle().by();
    }

    public void updateModifier(double maxHealth, double moveSpeed, double attackDamage, double knockResist) {
        AttributeModifier modifier = new AttributeModifier(maxHealthUUID, "maxHealth", maxHealth, 0);
        maxHealthInstance.b(modifier);// remove TODO
        if (maxHealth != 0) maxHealthInstance.a(modifier);// apply

        modifier = new AttributeModifier(moveSpeedUUID, "moveSpeed", moveSpeed, 0);
        moveSpeedInstance.b(modifier);// remove
        if (moveSpeed != 0) moveSpeedInstance.a(modifier);// apply

        modifier = new AttributeModifier(attackDamageUUID, "attackDamage", attackDamage, 0);
        attackDamageInstance.b(modifier);// remove
        if (attackDamage != 0) attackDamageInstance.a(modifier);// apply

        modifier = new AttributeModifier(knockResistUUID, "knockResist", knockResist, 0);
        knockResistInstance.b(modifier);// remove
        if (knockResist != 0) knockResistInstance.a(modifier);// apply
    }
}
