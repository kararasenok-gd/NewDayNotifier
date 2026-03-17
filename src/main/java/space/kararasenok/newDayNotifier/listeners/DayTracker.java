package space.kararasenok.newDayNotifier.listeners;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DayTracker {
    private long lastDay = -1;

    public void start(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            String worldName = plugin.getConfig().getString("settings.world");
            if (worldName == null) { worldName = "world"; }
            World w = Bukkit.getWorld(worldName);
            if (w == null) {
                plugin.getLogger().warning("World " + worldName + " not found!");
                return;
            }

            long day = w.getFullTime() / 24000;

            if (day != lastDay) {
                lastDay = day;

                String method = plugin.getConfig().getString("settings.display");
                if (method == null) { method = "actionbar"; }

                if (method.equals("actionbar")) {
                    String text = plugin.getConfig().getString("messages.actionbar.text");
                    if (text == null) { text = "§eDay §6%day%"; }

                    text = text.replace("%day%", Long.toString(day));

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendActionBar(text);
                    }
                } else if (method.equals("chat")) {
                    String text = plugin.getConfig().getString("messages.chat.text");
                    if (text == null) { text = "§eDay §6%day%"; }
                    text = text.replace("%day%", Long.toString(day));

                    Component textComponent = Component.text(text);
                    Bukkit.broadcast(textComponent);
                } else if (method.equals("title")) {
                    String text = plugin.getConfig().getString("messages.title.text");
                    String subtext =  plugin.getConfig().getString("messages.title.subtext.text");
                    boolean subtextEnabled = plugin.getConfig().getBoolean("messages.title.subtext.enabled");

                    if (text == null) { text = "New day!"; }
                    if (subtext == null) { subtext = "Current day: %day%"; }

                    text = text.replace("%day%", Long.toString(day));
                    subtext = subtext.replace("%day%", Long.toString(day));

                    final Title titleComponent = Title.title(
                            Component.text(text),
                            Component.text(subtextEnabled ? subtext : "")
                    );

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.showTitle(titleComponent);
                    }
                } else {
                    plugin.getLogger().warning("I don't know what to do with " + method + " method.");
                }

                boolean sounds = plugin.getConfig().getBoolean("settings.sound");
                Sound newDaySound = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1.0f, 1.0f);
                if (sounds) {
                    Bukkit.getServer().playSound(newDaySound);
                }
            }
        }, 0L, 100L);
    }
}