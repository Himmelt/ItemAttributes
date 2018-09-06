package org.soraworld.attrib.data;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.soraworld.hocon.node.Node;
import org.soraworld.hocon.node.NodeMap;
import org.soraworld.hocon.node.Options;
import org.soraworld.hocon.node.Setting;
import org.soraworld.hocon.serializer.TypeSerializer;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Attributes implements TypeSerializer<Attributes> {

    @Setting
    private int health = 0;
    @Setting
    private float regain = 0;
    @Setting
    private float walkspeed = 0;
    @Setting
    private float flyspeed = 0;
    @Setting
    private int attack = 0;
    @Setting
    private float knock = 0;
    @Setting
    private int armor = 0;
    @Setting
    private float block_chance = 0, block_ratio = 0;
    @Setting
    private float crit_chance = 0, crit_ratio = 0;
    @Setting
    private float suck_chance = 0, suck_ratio = 0;
    @Setting
    private float onekill_chance = 0, onekill_ratio = 0;
    @Setting
    private float thorn_chance = 0, thorn_ratio = 0;
    @Setting
    private float immortal_chance = 0;
    @Setting
    private float rage_health = 0, rage_ratio = 0;
    @Setting
    private float dodge_chance = 0;
    @Setting
    private boolean bind_enable = false;
    @Setting
    private String owner = null;
    @Setting
    private String perm = null;
    @Setting
    private List<Integer> potions = new ArrayList<>();
    @Setting
    private List<Integer> spells = new ArrayList<>();
    @Setting
    private List<String> skills = new ArrayList<>();

    public Attributes() {
    }

    public Attributes(NBTTagCompound compound) {
    }

    public Attributes deserialize(@Nonnull Type type, @Nonnull Node node) {
        Attributes attributes = new Attributes();
        if (node instanceof NodeMap) {
            ((NodeMap) node).modify(attributes);
        }
        return attributes;
    }

    public Node serialize(@Nonnull Type type, Attributes value, @Nonnull Options options) {
        NodeMap node = new NodeMap(options);
        if (value != null) node.extract(value);
        return node;
    }

    @Nonnull
    public Type getRegType() {
        return Attributes.class;
    }
}
