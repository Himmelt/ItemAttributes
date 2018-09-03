package org.soraworld.attrib.manager;

import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;
import org.soraworld.violet.util.ChatColor;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public class AttribManager extends SpigotManager {
    /**
     * 实例化管理器.
     *
     * @param plugin 插件实例
     * @param path   配置保存路径
     */
    public AttribManager(SpigotPlugin plugin, Path path) {
        super(plugin, path);
    }

    @Nonnull
    public ChatColor defChatColor() {
        return ChatColor.DARK_GREEN;
    }
}
