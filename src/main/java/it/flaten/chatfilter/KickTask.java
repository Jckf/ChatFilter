package it.flaten.chatfilter;

import org.bukkit.entity.Player;

public class KickTask implements Runnable {
    private final Player player;
    private final String message;

    public KickTask(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public void run() {
        this.player.kickPlayer(this.message);
    }
}
