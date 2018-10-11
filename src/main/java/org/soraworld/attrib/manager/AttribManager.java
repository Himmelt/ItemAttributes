package org.soraworld.attrib.manager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.soraworld.attrib.data.ItemAttrib;
import org.soraworld.attrib.data.LoreInfo;
import org.soraworld.attrib.data.Potion;
import org.soraworld.attrib.task.PlayerTickTask;
import org.soraworld.hocon.node.FileNode;
import org.soraworld.hocon.node.Setting;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern NUMBER = Pattern.compile("\\d+(\\.\\d+)?");
    private static final Pattern PERM = Pattern.compile("<[A-Za-z0-9._]+>");

    @Setting(comment = "comment.updateTicks")
    private byte updateTicks = 10;
    @Setting(comment = "comment.baubleSlots")
    public ArrayList<Integer> baubleSlots = new ArrayList<>();
    @Setting(comment = "comment.namePotion")
    private HashMap<String, String> namePotion = new HashMap<>();
    @Setting(comment = "comment.defaultLore")
    private HashMap<String, String> defaultLore = new LinkedHashMap<>();
    @Setting(comment = "comment.loreKey")
    private LoreKeys loreKeys = new LoreKeys();

    public AttribManager(SpigotPlugin plugin, Path path) {
        super(plugin, path);
        options.registerType(new Potion());
        itemsFile = path.resolve("items.conf");
    }

    public ChatColor defChatColor() {
        return ChatColor.YELLOW;
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
        setDefaultLore();
        setDefaultNamePotion();
        baubleSlots.removeIf(slot -> slot < 0 || slot > 35);
    }

    private void setDefaultLore() {
        if (defaultLore.isEmpty()) {
            for (Map.Entry<String, String> entry : langMap.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (key.startsWith("defLore.")) {
                    defaultLore.put(key.replaceFirst("defLore\\.", ""), val);
                }
            }
        }
    }

    private void setDefaultNamePotion() {
        if (namePotion.isEmpty()) {
            for (Map.Entry<String, String> entry : langMap.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (key.startsWith("potionName.")) {
                    namePotion.put(val, key.replaceFirst("potionName\\.", ""));
                }
            }
        }
    }

    public boolean save() {
        saveItems();
        setDefaultLore();
        setDefaultNamePotion();
        baubleSlots.removeIf(slot -> slot < 0 || slot > 35);
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
            //saveItems();
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

    public void addLine(ItemStack stack, LoreInfo info, String content) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        if (lore.size() > 0) {
            String first = lore.get(0);
            int index = first.indexOf("attrib id:");
            if (index >= 0) {
                lore.set(0, AT_PREFIX + info.line0());
                lore.add(content);
                fetchAttrib(info.attrib, content);
                meta.setLore(lore);
                stack.setItemMeta(meta);
                return;
            }
        }
        lore.add(0, AT_PREFIX + info.line0());
        lore.add(content);
        fetchAttrib(info.attrib, content);
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    public void setLine(ItemStack stack, LoreInfo info, String content, int line) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        if (lore.size() > line) {
            String first = lore.get(0);
            int index = first.indexOf("attrib id:");
            if (index >= 0) {
                String oldLine = lore.get(line);
                lore.set(0, AT_PREFIX + info.line0());
                lore.set(line, content);
                removeAttrib(info.attrib, oldLine);
                fetchAttrib(info.attrib, content);
                meta.setLore(lore);
                stack.setItemMeta(meta);
                return;
            }
        }
        lore.add(0, AT_PREFIX + info.line0());
        while (lore.size() < line) lore.add("");
        lore.add(content);
        fetchAttrib(info.attrib, content);
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    public void removeLine(ItemStack stack, LoreInfo info, int line) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        if (lore.size() > line) {
            String oldLine = lore.remove(line);
            removeAttrib(info.attrib, oldLine);
            String first = lore.get(0);
            int index = first.indexOf("attrib id:");
            if (index >= 0) lore.set(0, AT_PREFIX + info.line0());
            meta.setLore(lore);
            stack.setItemMeta(meta);
        }
    }

    private void fetchAttrib(ItemAttrib attrib, String line) {
        Matcher matcher = NUMBER.matcher(line);
        double val1 = 0, val2 = 0;
        if (matcher.find()) val1 = Double.valueOf(matcher.group());
        if (matcher.find()) val2 = Double.valueOf(matcher.group());
        if (line.contains(loreKeys.keyHealth)) attrib.health = (int) val1;
        else if (line.contains(loreKeys.keyAttack)) attrib.attack = (int) val1;
        else if (line.contains(loreKeys.keyRegain)) attrib.regain = (float) val1;
        else if (line.contains(loreKeys.keyWalkSpeed)) attrib.walkspeed = (float) val1;
        else if (line.contains(loreKeys.keyFlySpeed)) attrib.flyspeed = (float) val1;
        else if (line.contains(loreKeys.keyKnock)) attrib.knock = (float) (val1 / 100.0D);
        else if (line.contains(loreKeys.keyArmor)) attrib.armor = (float) (val1 / 100.0D);
        else if (line.contains(loreKeys.keyDodge)) attrib.dodgeChance = (float) (val1 / 100.0D);
        else if (line.contains(loreKeys.keyBind)) attrib.bindEnable = true;
        else if (line.contains(loreKeys.keyImmortal)) attrib.immortalChance = (float) (val1 / 100.0D);
        else if (line.contains(loreKeys.keyPerm)) {
            Matcher mat = PERM.matcher(line);
            if (mat.find()) {
                String text = mat.group();
                attrib.perm = text.substring(1, text.length() - 1);
            }
        } else if (line.contains(loreKeys.keyBlock)) {
            attrib.blockChance = (float) (val1 / 100.0D);
            attrib.blockRatio = (float) (val2 / 100.0D);
        } else if (line.contains(loreKeys.keyCrit)) {
            attrib.critChance = (float) (val1 / 100.0D);
            attrib.critRatio = (float) (val2 / 100.0D);
        } else if (line.contains(loreKeys.keySuck)) {
            attrib.suckChance = (float) (val1 / 100.0D);
            attrib.suckRatio = (float) (val2 / 100.0D);
        } else if (line.contains(loreKeys.keyOneKill)) {
            attrib.onekillChance = (float) (val1 / 100.0D);
            attrib.onekillRatio = (float) (val2 / 100.0D);
        } else if (line.contains(loreKeys.keyThorn)) {
            attrib.thornChance = (float) (val1 / 100.0D);
            attrib.thornRatio = (float) (val2 / 100.0D);
        } else if (line.contains(loreKeys.keyRage)) {
            attrib.rageHealth = (float) (val1 / 100.0D);
            attrib.rageRatio = (float) (val2 / 100.0D);
        } else if (line.contains(loreKeys.keyPotion)) {
            attrib.potions.add(new Potion(getPotionName(line), (int) val1));
        } else if (line.contains(loreKeys.keySpell)) {
            attrib.spells.add(new Potion(getPotionName(line), (int) val1, (int) val2));
        } else if (line.contains(loreKeys.keySkill)) {
            // TODO attrib.skills.add("skill");
        }
    }

    private void removeAttrib(ItemAttrib attrib, String line) {
        if (line.contains(loreKeys.keyHealth)) attrib.health = 0;
        else if (line.contains(loreKeys.keyAttack)) attrib.attack = 0;
        else if (line.contains(loreKeys.keyRegain)) attrib.regain = 0;
        else if (line.contains(loreKeys.keyWalkSpeed)) attrib.walkspeed = 0;
        else if (line.contains(loreKeys.keyFlySpeed)) attrib.flyspeed = 0;
        else if (line.contains(loreKeys.keyKnock)) attrib.knock = 0;
        else if (line.contains(loreKeys.keyArmor)) attrib.armor = 0;
        else if (line.contains(loreKeys.keyDodge)) attrib.dodgeChance = 0;
        else if (line.contains(loreKeys.keyBind)) attrib.bindEnable = false;
        else if (line.contains(loreKeys.keyPerm)) attrib.perm = null;
        else if (line.contains(loreKeys.keyImmortal)) attrib.immortalChance = 0;
        else if (line.contains(loreKeys.keyBlock)) attrib.blockChance = attrib.blockRatio = 0;
        else if (line.contains(loreKeys.keyCrit)) attrib.critChance = attrib.critRatio = 0;
        else if (line.contains(loreKeys.keySuck)) attrib.suckChance = attrib.suckRatio = 0;
        else if (line.contains(loreKeys.keyOneKill)) attrib.onekillChance = attrib.onekillRatio = 0;
        else if (line.contains(loreKeys.keyThorn)) attrib.thornChance = attrib.thornRatio = 0;
        else if (line.contains(loreKeys.keyRage)) attrib.rageHealth = attrib.rageRatio = 0;
        else if (line.contains(loreKeys.keyPotion)) {
            attrib.potions.remove(new Potion(getPotionName(line)));
        } else if (line.contains(loreKeys.keySpell)) {
            attrib.spells.remove(new Potion(getPotionName(line)));
        } else if (line.contains(loreKeys.keySkill)) {
            // TODO attrib.skills.add("skill");
        }
    }

    private String getPotionName(String line) {
        for (Map.Entry<String, String> entry : namePotion.entrySet()) {
            if (line.contains(entry.getKey())) return entry.getValue();
        }
        return "";
    }

    public static void setAttribId(ItemStack stack, ItemAttrib attrib) {
        LoreInfo old = getInfo(stack);
        LoreInfo info = new LoreInfo(old, attrib);
        updateInfo(stack, info);
    }

    public static ItemAttrib createAttrib(ItemStack stack) {
        LoreInfo info = getInfo(stack);
        if (info.attrib != null) return info.attrib;
        while (items.containsKey(NextID)) NextID++;
        ItemAttrib attrib = new ItemAttrib(NextID);
        items.put(NextID, attrib);
        setAttribId(stack, attrib);
        return attrib;
    }

    public static ItemAttrib createAttrib(String id) {
        ItemAttrib attrib = getItemAttrib(id);
        if (attrib != null) return attrib;
        while (items.containsKey(NextID)) NextID++;
        attrib = new ItemAttrib(NextID, id);
        items.put(NextID, attrib);
        if (attrib.name != null && !attrib.name.isEmpty()) names.put(attrib.name, attrib.id);
        return attrib;
    }

    public static void setAttribName(ItemAttrib attrib, String name) {
        names.remove(attrib.name);
        if (name != null && !name.isEmpty()) names.put(name, attrib.id);
        attrib.name = name;
    }

    public void startTask(Player player) {
        PlayerTickTask task = tasks.computeIfAbsent(player.getUniqueId(), uuid -> PlayerTickTask.createTask(player, this));
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
                        if (at.regain > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf(updateTicks / 20.0F))
                                .replace(VAR_1, String.valueOf(at.regain)));
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
                        if (at.knock > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.knock * 100))));
                        break;
                    case "armor":
                        if (at.armor > 0) lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.armor * 100))));
                        break;
                    case "block":
                        if (at.blockChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.blockChance * 100)))
                                    .replace(VAR_1, String.valueOf((int) (at.blockRatio * 100))));
                        break;
                    case "crit":
                        if (at.critChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.critChance * 100)))
                                    .replace(VAR_1, String.valueOf((int) (at.critRatio * 100))));
                        break;
                    case "suck":
                        if (at.suckChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.suckChance * 100)))
                                    .replace(VAR_1, String.valueOf((int) (at.suckRatio * 100))));
                        break;
                    case "onekill":
                        if (at.onekillChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.onekillChance * 100)))
                                    .replace(VAR_1, String.valueOf((int) (at.onekillRatio * 100))));
                        break;
                    case "thorn":
                        if (at.thornChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.thornChance * 100)))
                                    .replace(VAR_1, String.valueOf((int) (at.thornRatio * 100))));
                        break;
                    case "rage":
                        if (at.rageHealth > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.rageHealth * 100)))
                                    .replace(VAR_1, String.valueOf((int) (at.rageRatio * 100))));
                        break;
                    case "dodge":
                        if (at.dodgeChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.dodgeChance * 100))));
                        break;
                    case "immortal":
                        if (at.immortalChance > 0)
                            lore.add(PREFIX + val.replace(VAR_0, String.valueOf((int) (at.immortalChance * 100))));
                        break;
                    case "perm":
                        if (at.perm != null && !at.perm.isEmpty()) lore.add(PREFIX + val.replace(VAR_0, at.perm));
                        break;
                    case "potion":
                        if (at.potions != null && at.potions.size() > 0) {
                            for (Potion potion : at.potions) {
                                lore.add(PREFIX + val.replace(VAR_0, trans(potion.getName()))
                                        .replace(VAR_1, trans(potion.getLvl())));
                            }
                        }
                        break;
                    case "spell":
                        if (at.spells != null && at.spells.size() > 0) {
                            for (Potion spell : at.spells) {
                                lore.add(PREFIX + val.replace(VAR_0, trans(spell.getName()))
                                        .replace(VAR_1, trans(spell.getLvl()))
                                        .replace(VAR_2, spell.getDuration()));
                            }
                        }
                        break;
                    case "skill":
                        if (at.skills != null && at.skills.size() > 0) {
                            for (String skill : at.skills) {
                                // TODO
                            }
                        }
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
