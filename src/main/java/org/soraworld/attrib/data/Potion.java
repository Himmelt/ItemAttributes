package org.soraworld.attrib.data;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.soraworld.hocon.node.Node;
import org.soraworld.hocon.node.NodeBase;
import org.soraworld.hocon.node.Options;
import org.soraworld.hocon.serializer.TypeSerializer;

import java.lang.reflect.Type;

public class Potion implements TypeSerializer<Potion> {
    private final String name;
    private final int lvl;
    private final int duration;

    public Potion() {
        name = "";
        lvl = 0;
        duration = 80;
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
        this.lvl = lvl < 0 ? 0 : lvl;
        this.duration = duration;
    }

    public PotionEffect getEffect() {
        PotionEffectType type = PotionEffectType.getByName(name);
        if (type != null) return new PotionEffect(type, duration, lvl, true);
        else return null;
    }

    public Potion deserialize(Type type, Node node) {
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

    public Node serialize(Type type, Potion value, Options options) {
        return new NodeBase(options, toString(), false);
    }


    public Type getRegType() {
        return Potion.class;
    }

    public String getName() {
        return "potionName." + name;
    }

    public String getLvl() {
        return "potionLvl." + lvl;
    }

    public String getDuration() {
        return String.valueOf(duration / 20.0);
    }
}
