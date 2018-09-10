package org.soraworld.attrib.task;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.soraworld.attrib.data.Attributes;
import org.soraworld.attrib.manager.AttribManager;

public class PlayerSecond extends BukkitRunnable {

    private final Player player;
    private final AttribManager manager;

    public PlayerSecond(AttribManager manager, Player player) {
        // TODO check respawn player reference changes ?
        this.player = player;
        this.manager = manager;
    }

    public void run() {
        ItemStack[] stacks = player.getInventory().getArmorContents();
        float regain = 0.0F, flyspeed = 0.0F;
        for (ItemStack stack : stacks) {
            Attributes attrib = manager.getAttrib(stack);
            if (attrib != null) {
                regain += attrib.regain;
                flyspeed += attrib.flyspeed;
                // TODO potions
            }
        }
        player.setHealth(player.getHealth() + regain);
        player.setFlySpeed(flyspeed);
    }
}
