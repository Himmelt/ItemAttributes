package org.soraworld.attrib.listener;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.soraworld.attrib.manager.AttribManager.ATTRIBS;
import static org.soraworld.attrib.manager.AttribManager.TAG_COMP;

public class EventListener implements Listener {

    private static final UUID maxHealthUUID = UUID.fromString("e0b4701b-9843-4aa0-9aa5-4c7c97be3d27");
    private static final UUID moveSpeedUUID = UUID.fromString("23d25cb0-1b72-44c8-b125-3aa3c8b5fe0f");
    private static final UUID attackDamageUUID = UUID.fromString("b2123860-0b68-49b2-a631-d13b6a27f686");
    private static final UUID knockResistUUID = UUID.fromString("96d8c6f6-41b6-42ea-8a1b-cfa3131b7d72");

    private static long last = System.currentTimeMillis();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        updatePlayer(event.getPlayer());
    }

    private static void updatePlayer(Player player) {
        if (System.currentTimeMillis() - last < 1000) return;
        last = System.currentTimeMillis();
        double maxHealth = 0.0D, moveSpeed = 0.0D, attackDamage = 0.0D, knockResist = 0.0D;
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        List<ItemStack> stacks = Arrays.asList(
                handle.inventory.getItemInHand(),
                handle.inventory.armor[0],
                handle.inventory.armor[1],
                handle.inventory.armor[2],
                handle.inventory.armor[3]);
        for (ItemStack stack : stacks) {
            if (stack != null && stack.tag != null && stack.tag.hasKeyOfType(ATTRIBS, TAG_COMP)) {
                NBTTagCompound attrib = stack.tag.getCompound(ATTRIBS);
                maxHealth += attrib.getInt("health");
                moveSpeed += attrib.getInt("speed") / 100.D;
                attackDamage += attrib.getInt("attack");
                knockResist += attrib.getInt("knock") / 100.0D;
            }
        }

        AttributeModifier maxHealthModifier = new AttributeModifier(maxHealthUUID, "maxHealth", maxHealth, 0);
        AttributeInstance attrib = handle.getAttributeInstance(GenericAttributes.maxHealth);
        attrib.b(maxHealthModifier);// remove
        if (maxHealth > 0) attrib.a(maxHealthModifier);// apply

        AttributeModifier moveSpeedModifier = new AttributeModifier(moveSpeedUUID, "moveSpeed", moveSpeed, 0);
        attrib = handle.getAttributeInstance(GenericAttributes.d);// movementSpeed
        attrib.b(moveSpeedModifier);// remove
        if (moveSpeed > 0) attrib.a(moveSpeedModifier);// apply

        AttributeModifier attackDamageModifier = new AttributeModifier(attackDamageUUID, "attackDamage", attackDamage, 0);
        attrib = handle.getAttributeInstance(GenericAttributes.e);// attackDamage
        attrib.b(attackDamageModifier);// remove
        if (attackDamage > 0) attrib.a(attackDamageModifier);// apply

        AttributeModifier knockResistModifier = new AttributeModifier(knockResistUUID, "knockResist", knockResist, 0);
        attrib = handle.getAttributeInstance(GenericAttributes.c);// knockbackResistance
        attrib.b(knockResistModifier);// remove
        if (knockResist > 0) attrib.a(knockResistModifier);// apply
    }
}
