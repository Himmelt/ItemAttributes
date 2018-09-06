package org.soraworld.attrib;

import org.bukkit.event.Listener;
import org.soraworld.attrib.listener.EventListener;
import org.soraworld.attrib.manager.AttribManager;
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
        return Collections.singletonList(new EventListener());
    }

    @Nonnull
    public String assetsId() {
        return "attrib";
    }
}
