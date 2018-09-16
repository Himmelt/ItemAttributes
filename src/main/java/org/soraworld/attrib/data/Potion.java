package org.soraworld.attrib.data;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.soraworld.hocon.node.Node;
import org.soraworld.hocon.node.NodeBase;
import org.soraworld.hocon.node.Options;
import org.soraworld.hocon.serializer.TypeSerializer;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public class Potion implements TypeSerializer<Potion> {
    private String name;
    private int lvl;
    private int duration = 80;

    public Potion() {
        name = "";
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof Potion) return this.name.equals(((Potion) obj).name);
        return false;
    }

    public Potion(String name, int lvl, int duration) {
        this.name = name;
        this.lvl = lvl;
        this.duration = duration;
    }

    public PotionEffect getEffect() {
        PotionEffectType type = PotionEffectType.getByName(name);
        if (type != null) return new PotionEffect(type, duration, lvl, true);
        else return null;
    }

    public Potion deserialize(@Nonnull Type type, @Nonnull Node node) {
        if (node instanceof NodeBase) {
            String[] ss = ((NodeBase) node).getString().split("/");
            String id = "";
            int lvl = 0, duration = 80;
            if (ss.length >= 1) id = ss[0];
            if (ss.length >= 2) lvl = Integer.valueOf(ss[1]);
            if (ss.length >= 3) duration = Integer.valueOf(ss[2]);
            return new Potion(id, lvl, duration);
        }
        return null;
    }

    public String toString() {
        return name + "/" + lvl + "/" + duration;
    }

    public Node serialize(@Nonnull Type type, Potion value, @Nonnull Options options) {
        return new NodeBase(options, toString(), false);
    }

    @Nonnull
    public Type getRegType() {
        return Potion.class;
    }

    public String getName() {
        return "potionName." + name;
    }

    public String getLvl() {
        return "potionLvl." + lvl;
    }
}
