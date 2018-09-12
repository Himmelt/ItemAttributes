package org.soraworld.attrib.data;

import org.soraworld.hocon.node.*;
import org.soraworld.hocon.serializer.TypeSerializer;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemAttrib implements TypeSerializer<ItemAttrib> {

    public String name;
    @Setting
    public int health = 0;
    @Setting
    public float regain = 0;
    @Setting
    public float walkspeed = 0;
    @Setting
    public float flyspeed = 0;
    @Setting
    public int attack = 0;
    @Setting
    public float knock = 0;
    @Setting
    public int armor = 0;
    @Setting
    public float blockChance = 0, blockRatio = 0;
    @Setting
    public float critChance = 0, critRatio = 0;
    @Setting
    public float suckChance = 0, suckRatio = 0;
    @Setting
    public float onekillChance = 0, onekillRatio = 0;
    @Setting
    public float thornChance = 0, thornRatio = 0;
    @Setting
    public float immortalChance = 0;
    @Setting
    public float rageHealth = 0, rageRatio = 0;
    @Setting
    public float dodgeChance = 0;
    @Setting
    public boolean bindEnable = false;
    // TODO owner is specific variable
    // public String owner = null;
    @Setting
    public String perm = null;
    @Setting
    public List<Integer> potions = new ArrayList<>();
    @Setting
    public List<Integer> spells = new ArrayList<>();
    @Setting
    public List<String> skills = new ArrayList<>();

    public ItemAttrib deserialize(@Nonnull Type type, @Nonnull Node node) {
        ItemAttrib attributes = new ItemAttrib();
        if (node instanceof NodeMap) {
            ((NodeMap) node).modify(attributes);
        }
        return attributes;
    }

    public Node serialize(@Nonnull Type type, ItemAttrib value, @Nonnull Options options) {
        NodeMap node = new NodeMap(options);
        if (value != null) {
            if (value.health != 0) node.set("health", value.health);
            if (value.regain != 0) node.set("regain", value.regain);
            if (value.walkspeed != 0) node.set("walkspeed", value.walkspeed);
            if (value.flyspeed != 0) node.set("flyspeed", value.flyspeed);
            if (value.attack != 0) node.set("attack", value.attack);
            if (value.knock != 0) node.set("knock", value.knock);
            if (value.armor != 0) node.set("armor", value.armor);
            if (value.blockChance != 0) node.set("blockChance", value.blockChance);
            if (value.blockRatio != 0) node.set("blockRatio", value.blockRatio);
            if (value.critChance != 0) node.set("critChance", value.critChance);
            if (value.critRatio != 0) node.set("critRatio", value.critRatio);
            if (value.suckChance != 0) node.set("suckChance", value.suckChance);
            if (value.suckRatio != 0) node.set("suckRatio", value.suckRatio);
            if (value.onekillChance != 0) node.set("onekillChance", value.onekillChance);
            if (value.onekillRatio != 0) node.set("onekillRatio", value.onekillRatio);
            if (value.thornChance != 0) node.set("thornChance", value.thornChance);
            if (value.thornRatio != 0) node.set("thornRatio", value.thornRatio);
            if (value.immortalChance != 0) node.set("immortalChance", value.immortalChance);
            if (value.rageHealth != 0) node.set("rageHealth", value.rageHealth);
            if (value.rageRatio != 0) node.set("rageRatio", value.rageRatio);
            if (value.dodgeChance != 0) node.set("dodgeChance", value.dodgeChance);
            node.set("bindEnable", value.bindEnable);
            if (value.perm != null && !value.perm.isEmpty()) node.set("perm", value.perm);
            if (value.potions != null && !value.potions.isEmpty()) {
                NodeList list = new NodeList(options);
                for (Integer i : value.potions) list.add(new NodeBase(options, i, false));
                node.set("potions", list);
            }
            if (value.spells != null && !value.spells.isEmpty()) {
                NodeList list = new NodeList(options);
                for (Integer i : value.spells) list.add(new NodeBase(options, i, false));
                node.set("spells", list);
            }
            if (value.skills != null && !value.skills.isEmpty()) {
                NodeList list = new NodeList(options);
                for (String s : value.skills) list.add(new NodeBase(options, s, false));
                node.set("skills", list);
            }
        }
        return node;
    }

    @Nonnull
    public Type getRegType() {
        return ItemAttrib.class;
    }
}
