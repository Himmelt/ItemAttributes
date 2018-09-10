package org.soraworld.attrib.manager;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.soraworld.attrib.data.Attributes;
import org.soraworld.attrib.task.PlayerSecond;
import org.soraworld.hocon.node.FileNode;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;

public class AttribManager extends SpigotManager {

    public static final int TAG_END = 0;
    public static final int TAG_BYTE = 1;
    public static final int TAG_SHORT = 2;
    public static final int TAG_INT = 3;
    public static final int TAG_LONG = 4;
    public static final int TAG_FLOAT = 5;
    public static final int TAG_DOUBLE = 6;
    public static final int TAG_BYTE_A = 7;
    public static final int TAG_STRING = 8;
    public static final int TAG_LIST = 9;
    public static final int TAG_COMP = 10;
    public static final int TAG_INT_A = 11;
    public static final String ATTRIBS = "attribs";

    private static final Attributes serializer = new Attributes();

    // &f&r&f&r
    private static String SPLIT = ChatColor.COLOR_CHAR + "f" + ChatColor.COLOR_CHAR + "r" + ChatColor.COLOR_CHAR + "f" + ChatColor.COLOR_CHAR + "r";
    private static String COLOR_STRING = "" + ChatColor.COLOR_CHAR;

    private HashMap<Integer, Attributes> items = new HashMap<>();

    private final Path itemconfile;

    private HashMap<String, Integer> ids = new HashMap<>();

    private HashMap<UUID, PlayerSecond> tasks = new HashMap<>();

    private int NextID = 0;

    @Setting(comment = "comment.autoUpdate")
    private boolean autoUpdate = false;
    @Setting(comment = "comment.accumulateDodge")
    private boolean accumulateDodge = false;
    @Setting(comment = "comment.accumulateBlock")
    private boolean accumulateBlock = false;

    public AttribManager(SpigotPlugin plugin, Path path) {
        super(plugin, path);
        itemconfile = path.resolve("items.conf");
    }

    @Nonnull
    public ChatColor defChatColor() {
        return ChatColor.DARK_GREEN;
    }

    public Attributes getAttrib(ItemStack stack) {
        return items.get(getId(stack));
    }

    public void beforeLoad() {
        options.registerType(new Attributes());
    }

    public void loadItems() {
        FileNode node = new FileNode(itemconfile.toFile(), options);
        try {
            node.load(false);
            items.clear();
            for (String key : node.keys()) {
                try {
                    int id = Integer.valueOf(key);
                    Attributes attrib = serializer.deserialize(Attributes.class, node.get(key));
                    if (attrib != null) items.put(id, attrib);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveItems();
        }
    }

    public boolean load() {
        loadItems();
        return super.load();
    }

    public boolean save() {
        saveItems();
        return super.save();
    }

    public void saveItems() {
        FileNode node = new FileNode(itemconfile.toFile(), options);
        for (Map.Entry<Integer, Attributes> entry : items.entrySet()) {
            node.set(entry.getKey().toString(), serializer.serialize(Attributes.class, entry.getValue(), options));
        }
        try {
            node.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getId(ItemStack stack) {
        if (stack == null) return -1;
        if (stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasLore()) {
                String first = meta.getLore().get(0);
                int index = first.indexOf("attrib-id:");
                if (index >= 0) {
                    try {
                        return Integer.valueOf(first.substring(index + 10));
                    } catch (Throwable ignored) {
                        return -1;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 是否自动更新物品Lore.
     *
     * @return 是否自动更新
     */
    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    /**
     * 是否累积装备闪避几率.
     * 如果不累积，则使用最大的几率.
     *
     * @return 是否累积
     */
    public boolean isAccumulateDodge() {
        return accumulateDodge;
    }

    public static void updateLore(ItemStack stack) {
    }

    public boolean isHoldingRight(EntityPlayer player) {
        return player.by();
    }

    public Attributes createAttrib(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        if (lore.size() > 0) {
            String first = lore.get(0);
            int index = first.indexOf("attrib-id:");
            if (index >= 0) {
                try {
                    int id = Integer.valueOf(first.substring(index + 10));
                    lore.set(0, "" + ChatColor.RESET + ChatColor.AQUA + "attrib-id:" + id);
                    meta.setLore(lore);
                    stack.setItemMeta(meta);
                    Attributes attrib = items.get(id);
                    if (attrib != null) return attrib;
                    attrib = new Attributes();
                    items.put(id, attrib);
                    return attrib;
                } catch (Throwable e) {
                    e.printStackTrace();
                    Attributes attrib = new Attributes();
                    while (items.containsKey(NextID)) NextID++;
                    items.put(NextID, attrib);
                    lore.set(0, "" + ChatColor.RESET + ChatColor.AQUA + "attrib-id:" + NextID);
                    meta.setLore(lore);
                    stack.setItemMeta(meta);
                    return attrib;
                }
            }
        }
        Attributes attrib = new Attributes();
        while (items.containsKey(NextID)) NextID++;
        items.put(NextID, attrib);
        lore.add(0, "" + ChatColor.RESET + ChatColor.AQUA + "attrib-id:" + NextID);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return attrib;
    }

    public void startTask(Player player) {
        PlayerSecond task = tasks.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSecond(AttribManager.this, player));
        try {
            task.runTaskTimer(plugin, 1, 20);
        } catch (Throwable e) {
            if (debug) e.printStackTrace();
        }
    }

    public void stopTask(Player player) {
        PlayerSecond task = tasks.get(player.getUniqueId());
        if (task != null) task.cancel();
        tasks.remove(player.getUniqueId());
    }
}
