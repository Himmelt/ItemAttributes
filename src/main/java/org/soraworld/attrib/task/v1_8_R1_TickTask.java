package org.soraworld.attrib.task;

import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.AttributeModifier;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class v1_8_R1_TickTask extends PlayerTickTask {

    private AttributeInstance maxHealthInstance;
    private AttributeInstance moveSpeedInstance;
    private AttributeInstance attackDamageInstance;
    private AttributeInstance knockResistInstance;

    public v1_8_R1_TickTask(Player player) {
        super(player);
        // TODO 检查 respawn 和 切换世界 重新加入 EntityPlayer 是否改变
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        maxHealthInstance = handle.getAttributeInstance(GenericAttributes.maxHealth);
        moveSpeedInstance = handle.getAttributeInstance(GenericAttributes.d);// movementSpeed
        attackDamageInstance = handle.getAttributeInstance(GenericAttributes.e);// attackDamage
        knockResistInstance = handle.getAttributeInstance(GenericAttributes.c);// knockbackResistance
    }

    public static boolean isHoldRight(Player player) {
        return ((CraftPlayer) player).getHandle().bR();
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
