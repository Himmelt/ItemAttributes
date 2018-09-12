package org.soraworld.attrib.manager;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.soraworld.attrib.data.ItemAttrib;
import org.soraworld.attrib.task.PlayerTickTask;
import org.soraworld.hocon.node.FileNode;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;

public class AttribManager extends SpigotManager {

    private int NextID = 0;
    private final Path itemsFile;
    private HashMap<String, Integer> ids = new HashMap<>();
    private HashMap<Integer, ItemAttrib> items = new HashMap<>();
    private HashMap<UUID, PlayerTickTask> tasks = new HashMap<>();
    private static final ItemAttrib serializer = new ItemAttrib();

    @Setting
    private byte updateTicks = 10;
    @Setting(comment = "comment.autoUpdate")
    private boolean autoUpdate = false;
    @Setting(comment = "comment.accumulateDodge")
    private boolean accumulateDodge = false;
    @Setting(comment = "comment.accumulateBlock")
    private boolean accumulateBlock = false;

    public AttribManager(SpigotPlugin plugin, Path path) {
        super(plugin, path);
        itemsFile = path.resolve("items.conf");
    }

    @Nonnull
    public ChatColor defChatColor() {
        return ChatColor.DARK_GREEN;
    }

    public ItemAttrib getItemAttrib(ItemStack stack) {
        return items.get(getId(stack));
    }

    public void beforeLoad() {
        options.registerType(new ItemAttrib());
    }

    public void loadItems() {
        FileNode node = new FileNode(itemsFile.toFile(), options);
        try {
            node.load(false);
            items.clear();
            for (String key : node.keys()) {
                try {
                    int id = Integer.valueOf(key);
                    ItemAttrib attrib = serializer.deserialize(ItemAttrib.class, node.get(key));
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
        FileNode node = new FileNode(itemsFile.toFile(), options);
        for (Map.Entry<Integer, ItemAttrib> entry : items.entrySet()) {
            node.set(entry.getKey().toString(), serializer.serialize(ItemAttrib.class, entry.getValue(), options));
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

    public ItemAttrib createAttrib(ItemStack stack) {
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
                    ItemAttrib attrib = items.get(id);
                    if (attrib != null) return attrib;
                    attrib = new ItemAttrib();
                    items.put(id, attrib);
                    return attrib;
                } catch (Throwable e) {
                    e.printStackTrace();
                    ItemAttrib attrib = new ItemAttrib();
                    while (items.containsKey(NextID)) NextID++;
                    items.put(NextID, attrib);
                    lore.set(0, "" + ChatColor.RESET + ChatColor.AQUA + "attrib-id:" + NextID);
                    meta.setLore(lore);
                    stack.setItemMeta(meta);
                    return attrib;
                }
            }
        }
        ItemAttrib attrib = new ItemAttrib();
        while (items.containsKey(NextID)) NextID++;
        items.put(NextID, attrib);
        lore.add(0, "" + ChatColor.RESET + ChatColor.AQUA + "attrib-id:" + NextID);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return attrib;
    }

    public void startTask(Player player) {
        PlayerTickTask task = tasks.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerTickTask(AttribManager.this, player));
        try {
            task.runTaskTimer(plugin, 1, updateTicks);
        } catch (Throwable e) {
            if (debug) e.printStackTrace();
        }
    }

    public void stopTask(Player player) {
        PlayerTickTask task = tasks.get(player.getUniqueId());
        if (task != null) task.cancel();
        tasks.remove(player.getUniqueId());
    }

    public ItemAttrib getPlayerAttrib(Player player) {
        PlayerTickTask task = tasks.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerTickTask(AttribManager.this, player));
        return task.getAttrib();
    }
}
