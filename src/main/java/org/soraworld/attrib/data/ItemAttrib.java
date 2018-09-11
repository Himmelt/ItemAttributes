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
    public float block_chance = 0, block_ratio = 0;
    @Setting
    public float critChance = 0, critRatio = 0;
    @Setting
    public float suck_chance = 0, suck_ratio = 0;
    @Setting
    public float onekill_chance = 0, onekill_ratio = 0;
    @Setting
    public float thornChance = 0, thornRatio = 0;
    @Setting
    public float immortalChance = 0;
    @Setting
    public float rage_health = 0, rage_ratio = 0;
    @Setting
    public float dodgeChance = 0;
    @Setting
    public boolean bind_enable = false;
    @Setting
    public String owner = null;
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
            if (value.block_chance != 0) node.set("block_chance", value.block_chance);
            if (value.block_ratio != 0) node.set("block_ratio", value.block_ratio);
            if (value.critChance != 0) node.set("crit_chance", value.critChance);
            if (value.critRatio != 0) node.set("crit_ratio", value.critRatio);
            if (value.suck_chance != 0) node.set("suck_chance", value.suck_chance);
            if (value.suck_ratio != 0) node.set("suck_ratio", value.suck_ratio);
            if (value.onekill_chance != 0) node.set("onekill_chance", value.onekill_chance);
            if (value.onekill_ratio != 0) node.set("onekill_ratio", value.onekill_ratio);
            if (value.thornChance != 0) node.set("thorn_chance", value.thornChance);
            if (value.thornRatio != 0) node.set("thorn_ratio", value.thornRatio);
            if (value.immortalChance != 0) node.set("immortalChance", value.immortalChance);
            if (value.rage_health != 0) node.set("rage_health", value.rage_health);
            if (value.rage_ratio != 0) node.set("rage_ratio", value.rage_ratio);
            if (value.dodgeChance != 0) node.set("dodge_chance", value.dodgeChance);
            node.set("bind_enable", value.bind_enable);
            if (value.owner != null && !value.owner.isEmpty()) node.set("owner", value.owner);
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
