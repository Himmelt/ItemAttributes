package org.soraworld.attrib.data;

import org.bukkit.entity.HumanEntity;

import javax.annotation.Nonnull;

public class LoreInfo {
    public final int id;
    public final String owner;
    public final String inlay;
    public final ItemAttrib attrib;

    public LoreInfo(String owner, ItemAttrib attrib) {
        this.owner = owner;
        this.inlay = null;
        this.attrib = attrib;
        this.id = attrib == null ? -1 : attrib.id;
    }

    public LoreInfo(@Nonnull LoreInfo info, ItemAttrib attrib) {
        this.owner = info.owner;
        this.inlay = info.inlay;
        this.attrib = attrib;
        this.id = attrib == null ? -1 : attrib.id;
    }

    public boolean canUse(HumanEntity player) {
        if (attrib != null) {
            if (attrib.perm != null && !attrib.perm.isEmpty()) {
                if (!player.hasPermission(attrib.perm)) return false;
            }
            if (attrib.bindEnable && owner != null && !owner.isEmpty()) {
                return owner.equals(player.getName());
            }
        }
        return true;
    }

    public String line0() {
        if (owner == null || owner.isEmpty()) return "attrib id:" + id;
        return "attrib id:" + id + " bind:" + owner;
    }
}
