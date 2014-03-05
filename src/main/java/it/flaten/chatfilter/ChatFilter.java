package it.flaten.chatfilter;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatFilter extends JavaPlugin {
    private Listener eventListener;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.eventListener = new EventListener(this);

        this.getServer().getPluginManager().registerEvents(eventListener, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(eventListener);

        this.eventListener = null;
    }
}
