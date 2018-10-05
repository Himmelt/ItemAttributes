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

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ItemAttributes extends SpigotPlugin {
    public String getId() {
        return "attrib";
    }

    protected SpigotManager registerManager(Path path) {
        return new AttribManager(this, path);
    }

    protected List<Listener> registerListeners() {
        return Collections.singletonList(new EventListener((AttribManager) manager));
    }

    protected void registerCommands() {
        SpigotCommand command = new SpigotCommand(getId(), manager.defAdminPerm(), false, manager);
        command.extractSub(SpigotBaseSubs.class);
        command.extractSub(CommandAttrib.class);
        command.setUsage("/attrib ....");
        register(this, command);
    }

    public String assetsId() {
        return "attrib";
    }

    public void afterEnable() {
        for (Player player : getServer().getOnlinePlayers()) {
            ((AttribManager) manager).startTask(player);
        }
    }
}
