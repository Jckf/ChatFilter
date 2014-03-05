package it.flaten.chatfilter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EventListener implements Listener {
    private final JavaPlugin plugin;

    private final Map<UUID, Boolean> hasMoved = new ConcurrentHashMap<>();

    public EventListener(JavaPlugin plugin) {
        this.plugin = plugin;

        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.hasMoved.put(player.getUniqueId(), false);
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        this.hasMoved.put(event.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.hasMoved.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (this.hasMoved.get(player.getUniqueId()))
            return;

        if (event.getFrom().getBlock() == event.getTo().getBlock())
            return;

        this.plugin.getLogger().info("Player \"" + player.getName() + "\" has now moved from " + event.getFrom().getBlock() + " to " + event.getTo().getBlock());

        this.hasMoved.put(player.getUniqueId(), true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (this.hasMoved.get(event.getPlayer().getUniqueId()))
            return;

        switch (this.plugin.getConfig().getInt("mode")) {
            case 0:
                break;

            case 1:
                event.setCancelled(true);
                event.getPlayer().sendMessage(
                    String.format(
                        event.getFormat(),
                        event.getPlayer().getDisplayName(),
                        event.getMessage()
                    )
                );
                break;

            case 2:
                event.setCancelled(true);
                break;

            case 3:
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.plugin.getConfig().getString("block-message"));
                break;

            case 4:
                event.setCancelled(true);
                this.plugin.getServer().getScheduler().runTask(this.plugin, new KickTask(
                    event.getPlayer(),
                    this.plugin.getConfig().getString("kick-message")
                ));
                break;
        }
    }
}
