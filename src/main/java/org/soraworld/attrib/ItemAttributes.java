package org.soraworld.attrib;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.soraworld.attrib.command.CommandAttrib;
import org.soraworld.attrib.listener.EventListener;
import org.soraworld.attrib.manager.AttribManager;
import org.soraworld.violet.command.SpigotBaseSubs;
import org.soraworld.violet.command.SpigotCommand;
import org.soraworld.violet.manager.SpigotManager;
import org.soraworld.violet.plugin.SpigotPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ItemAttributes extends SpigotPlugin {
    @Nonnull
    public String getId() {
        return "attrib";
    }

    @Nonnull
    protected SpigotManager registerManager(Path path) {
        return new AttribManager(this, path);
    }

    @Nullable
    protected List<Listener> registerListeners() {
        return Collections.singletonList(new EventListener((AttribManager) manager));
    }

    protected void registerCommands() {
        SpigotCommand command = new SpigotCommand(getId(), manager.defAdminPerm(), false, manager);
        command.extractSub(SpigotBaseSubs.class, "lang");
        command.extractSub(SpigotBaseSubs.class, "debug");
        command.extractSub(SpigotBaseSubs.class, "save");
        command.extractSub(SpigotBaseSubs.class, "reload");
        command.extractSub(SpigotBaseSubs.class, "help");
        command.extractSub(CommandAttrib.class);
        register(this, command);
    }

    @Nonnull
    public String assetsId() {
        return "attrib";
    }

    public void afterEnable() {
        for (Player player : getServer().getOnlinePlayers()) {
            ((AttribManager) manager).startTask(player);
        }
    }
}
