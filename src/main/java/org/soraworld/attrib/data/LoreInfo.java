package org.soraworld.attrib.data;

import org.bukkit.entity.Player;

public class LoreInfo {
    public final int id;
    public final String owner;
    public final String inlay;
    public final ItemAttrib attrib;

    public LoreInfo(int id, String owner, ItemAttrib attrib) {
        this.id = id;
        this.owner = owner;
        this.inlay = null;
        this.attrib = attrib;
    }

    public boolean canUse(Player player) {
        if (attrib != null) {
            if (attrib.perm != null && !attrib.perm.isEmpty()) {
                if (!player.hasPermission(attrib.perm)) return false;
            }
            if (attrib.bindEnable && owner != null && !owner.isEmpty()) {
                return owner.equals(player.getName());
            }
            return true;
        }
        return false;
    }
}
