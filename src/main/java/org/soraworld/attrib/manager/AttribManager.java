package org.soraworld.attrib.manager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.soraworld.attrib.data.ItemAttrib;
import org.soraworld.attrib.data.LoreInfo;
import org.soraworld.attrib.task.PlayerTickTask;
import org.soraworld.hocon.node.FileNode;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;

import static org.soraworld.attrib.data.ItemAttrib.deserialize;
import static org.soraworld.attrib.data.ItemAttrib.serialize;

public class AttribManager extends SpigotManager {

    private final Path itemsFile;
    private static int NextID = 0;
    private HashMap<UUID, PlayerTickTask> tasks = new HashMap<>();
    private static HashMap<String, Integer> names = new HashMap<>();
    private static HashMap<Integer, ItemAttrib> items = new HashMap<>();
    private static final LoreInfo BAD_INFO = new LoreInfo((String) null, null);
    private static final String AT_PREFIX = "" + ChatColor.RESET + ChatColor.AQUA;
    private static final String PREFIX = "" + ChatColor.RESET;
    private static final String VAR_0 = "{0}", VAR_1 = "{1}", VAR_2 = "{2}";

    @Setting
    private byte updateTicks = 10;
    @Setting(comment = "comment.autoUpdate")
    private boolean autoUpdate = false;
    @Setting(comment = "comment.accumulateDodge")
    private boolean accumulateDodge = false;
    @Setting(comment = "comment.accumulateBlock")
    private boolean accumulateBlock = false;
    @Setting(comment = "comment.defaultLore")
    private HashMap<String, String> defaultLore = new LinkedHashMap<>();

    public AttribManager(SpigotPlugin plugin, Path path) {
        super(plugin, path);
        itemsFile = path.resolve("items.conf");
    }

    @Nonnull
    public ChatColor defChatColor() {
        return ChatColor.DARK_GREEN;
    }

    public static ItemAttrib getItemAttrib(ItemStack stack) {
        return getInfo(stack).attrib;
    }

    public static ItemAttrib getItemAttrib(int id) {
        return items.get(id);
    }

    public static ItemAttrib getItemAttrib(String id) {
        if (id.matches("\\d+")) {
            ItemAttrib attrib = items.get(Integer.valueOf(id));
            if (attrib != null) return attrib;
        }
        return items.get(names.getOrDefault(id, -1));
    }

    public boolean load() {
        loadItems();
        return super.load();
    }

    public void afterLoad() {
        if (defaultLore.isEmpty()) {
            defaultLore.put("health", "Boost {0} maxHealth");
            defaultLore.put("regain", "Regain {0} health every cycle");
            defaultLore.put("walkspeed", "Boost walkspeed {0}");
            defaultLore.put("block", "{0}% chance block {1}% damage");
        }
    }

    public boolean save() {
        saveItems();
        if (defaultLore.isEmpty()) {
            defaultLore.put("health", "Boost {0} maxHealth");
            defaultLore.put("regain", "Regain {0} health every cycle");
            defaultLore.put("walkspeed", "Boost walkspeed {0}");
            defaultLore.put("block", "{0}% chance block {1}% damage");
        }
        return super.save();
    }

    public void loadItems() {
        FileNode node = new FileNode(itemsFile.toFile(), options);
        try {
            node.load(false);
            items.clear();
            for (String key : node.keys()) {
                try {
                    int id = Integer.valueOf(key);
                    ItemAttrib attrib = deserialize(node.get(key), id);
                    if (attrib != null) {
                        items.put(attrib.id, attrib);
                        if (attrib.name != null && !attrib.name.isEmpty()) {
                            names.put(attrib.name, attrib.id);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveItems();
        }
    }

    public void saveItems() {
        FileNode node = new FileNode(itemsFile.toFile(), options);
        for (Map.Entry<Integer, ItemAttrib> entry : items.entrySet()) {
            node.set(entry.getKey().toString(), serialize(entry.getValue(), options));
        }
        try {
            node.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LoreInfo getInfo(ItemStack stack) {
        if (stack != null && stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasLore()) {
                int id;
                String owner = null;
                String first = meta.getLore().get(0);
                int index1 = first.indexOf("attrib id:");
                int index2 = first.indexOf("bind:");
                if (index1 >= 0) {
                    if (index2 > index1) {
                        id = Integer.valueOf(first.substring(index1 + 10, index2).trim());
                        owner = first.substring(index2 + 5);
                    } else {
                        id = Integer.valueOf(first.substring(index1 + 10).trim());
                    }
                    return new LoreInfo(owner, getItemAttrib(id));
                }
            }
        }
        return BAD_INFO;
    }

    public static void updateInfo(ItemStack stack, LoreInfo info) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        if (lore.size() > 0) {
            String first = lore.get(0);
            int index = first.indexOf("attrib id:");
            if (index >= 0) {
                lore.set(0, AT_PREFIX + info.line0());
                meta.setLore(lore);
                stack.setItemMeta(meta);
                return;
            }
        }
        lore.add(0, AT_PREFIX + info.line0());
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    public static void setAttribId(ItemStack stack, ItemAttrib attrib) {
        LoreInfo old = getInfo(stack);
        LoreInfo info = new LoreInfo(old, attrib);
        updateInfo(stack, info);
    }

    public static ItemAttrib createAttrib(@Nonnull ItemStack stack) {
        LoreInfo info = getInfo(stack);
        if (info.attrib != null) return info.attrib;
        while (items.containsKey(NextID)) NextID++;
        ItemAttrib attrib = new ItemAttrib(NextID);
        items.put(NextID, attrib);
        setAttribId(stack, attrib);
        return attrib;
    }

    @Nonnull
    public static ItemAttrib createAttrib(String id) {
        ItemAttrib attrib = getItemAttrib(id);
        if (attrib != null) return attrib;
        while (items.containsKey(NextID)) NextID++;
        attrib = new ItemAttrib(NextID, id);
        items.put(NextID, attrib);
        names.put(attrib.name, attrib.id);
        return attrib;
    }

    public static void setAttribName(ItemAttrib attrib, String name) {
        names.remove(attrib.name);
        names.put(name, attrib.id);
        attrib.name = name;
    }

    public void startTask(Player player) {
        PlayerTickTask task = tasks.computeIfAbsent(player.getUniqueId(), uuid -> PlayerTickTask.createTask(player));
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

    public List<String> getLore(LoreInfo info) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(AT_PREFIX + info.line0());
        ItemAttrib at = info.attrib;
        if (at != null) {
            for (Map.Entry<String, String> entry : defaultLore.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                switch (key) {
                    case "health":
                        if (at.health > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.health)));
                        break;
                    case "regain":
                        if (at.regain > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.regain)));
                        break;
                    case "walkspeed":
                        if (at.walkspeed > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.walkspeed)));
                        break;
                    case "flyspeed":
                        if (at.flyspeed > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.flyspeed)));
                        break;
                    case "attack":
                        if (at.attack > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.attack)));
                        break;
                    case "knock":
                        if (at.knock > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.knock)));
                        break;
                    case "armor":
                        if (at.armor > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.armor)));
                        break;
                    case "block":
                        if (at.blockChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.blockChance))
                                    .replace(VAR_1, String.valueOf(at.blockRatio)));
                        break;
                    case "crit":
                        if (at.critChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf(at.critChance))
                                    .replace(VAR_1, String.valueOf(at.critRatio)));
                        break;
                    default:
                        lore.add(PREFIX + val);
                        break;
                }
            }
        }
        return lore;
    }
}
