package space.kararasenok.newDayNotifier.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import space.kararasenok.newDayNotifier.NewDayNotifier;
import space.kararasenok.newDayNotifier.Updates;

public class PlayerJoin implements Listener {
    private final NewDayNotifier plugin;

    public PlayerJoin(NewDayNotifier plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Updates.checkUpdates(plugin).thenAccept(out -> {
            if (out && p.isOp()) {
                String btn = "<click:open_url:https://github.com/kararasenok-gd/NewDayNotifier/releases/latest><aqua>[Open Latest Release]</aqua></click>";
                Component comp = MiniMessage.miniMessage().deserialize(btn);

                p.sendMessage("§b[NewDayNotifier]§r You are using outdated version of NewDayNotifier");
                p.sendMessage(comp);
            }
        });
    }
}
