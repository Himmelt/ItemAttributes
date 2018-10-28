package org.soraworld.attrib.data;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.soraworld.hocon.node.*;

import java.util.HashSet;

public class ItemAttrib {

    public int globalId = 0;
    @Setting
    public String name = "";
    @Setting
    public int suit = -1;
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
    public float armor = 0;
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
    public float rageHealth = 0, rageRatio = 0;
    @Setting
    public float immortalChance = 0;
    @Setting
    public float dodgeChance = 0;
    @Setting
    public float fireChance = 0;// TODO fire
    @Setting
    public boolean bindEnable = false;
    @Setting
    public String perm = null;
    @Setting
    public HashSet<Potion> potions = new HashSet<>();
    @Setting
    public HashSet<Potion> spells = new HashSet<>();
    @Setting
    public HashSet<String> skills = new HashSet<>();

    public ItemAttrib() {
    }

    public ItemAttrib(int id) {
        this.globalId = id;
    }

    public ItemAttrib(int id, String name) {
        this.globalId = id;
        this.name = name;
    }

    public static ItemAttrib deserialize(Node node, int id) {
        ItemAttrib attrib = new ItemAttrib(id);
        if (node instanceof NodeMap) {
            ((NodeMap) node).modify(attrib);
        }
        return attrib;
    }

    public static NodeMap serialize(ItemAttrib attrib, Options options) {
        NodeMap node = new NodeMap(options);
        if (attrib != null) {
            if (attrib.name != null && !attrib.name.isEmpty()) node.set("name", attrib.name);
            if (attrib.suit >= 0) node.set("suit", attrib.suit);
            if (attrib.health != 0) node.set("health", attrib.health);
            if (attrib.regain != 0) node.set("regain", attrib.regain);
            if (attrib.walkspeed != 0) node.set("walkspeed", attrib.walkspeed);
            if (attrib.flyspeed != 0) node.set("flyspeed", attrib.flyspeed);
            if (attrib.attack != 0) node.set("attack", attrib.attack);
            if (attrib.knock != 0) node.set("knock", attrib.knock);
            if (attrib.armor != 0) node.set("armor", attrib.armor);
            if (attrib.blockChance != 0) node.set("blockChance", attrib.blockChance);
            if (attrib.blockRatio != 0) node.set("blockRatio", attrib.blockRatio);
            if (attrib.critChance != 0) node.set("critChance", attrib.critChance);
            if (attrib.critRatio != 0) node.set("critRatio", attrib.critRatio);
            if (attrib.suckChance != 0) node.set("suckChance", attrib.suckChance);
            if (attrib.suckRatio != 0) node.set("suckRatio", attrib.suckRatio);
            if (attrib.onekillChance != 0) node.set("onekillChance", attrib.onekillChance);
            if (attrib.onekillRatio != 0) node.set("onekillRatio", attrib.onekillRatio);
            if (attrib.thornChance != 0) node.set("thornChance", attrib.thornChance);
            if (attrib.thornRatio != 0) node.set("thornRatio", attrib.thornRatio);
            if (attrib.immortalChance != 0) node.set("immortalChance", attrib.immortalChance);
            if (attrib.rageHealth != 0) node.set("rageHealth", attrib.rageHealth);
            if (attrib.rageRatio != 0) node.set("rageRatio", attrib.rageRatio);
            if (attrib.dodgeChance != 0) node.set("dodgeChance", attrib.dodgeChance);
            if (attrib.bindEnable) node.set("bindEnable", true);
            if (attrib.perm != null && !attrib.perm.isEmpty()) node.set("perm", attrib.perm);
            if (attrib.potions != null && !attrib.potions.isEmpty()) {
                NodeList list = new NodeList(options);
                for (Potion potion : attrib.potions) list.add(new NodeBase(options, potion, false));
                node.set("potions", list);
            }
            if (attrib.spells != null && !attrib.spells.isEmpty()) {
                NodeList list = new NodeList(options);
                for (Potion spell : attrib.spells) list.add(new NodeBase(options, spell, false));
                node.set("spells", list);
            }
            if (attrib.skills != null && !attrib.skills.isEmpty()) {
                NodeList list = new NodeList(options);
                for (String skill : attrib.skills) list.add(new NodeBase(options, skill, false));
                node.set("skills", list);
            }
        }
        return node;
    }

    public void applyPotions(LivingEntity entity) {
        for (Potion potion : potions) {
            PotionEffect effect = potion.getEffect();
            if (effect != null) entity.addPotionEffect(effect, true);
        }
    }
}
