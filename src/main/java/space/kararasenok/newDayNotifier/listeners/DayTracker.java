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

    private static void display(JavaPlugin plugin, Long day, String method, String configRoot) {
        if (method.equals("actionbar")) {
            String text = plugin.getConfig().getString(configRoot + ".actionbar.text", "§eDay §6%day%");

            text = text.replace("%day%", Long.toString(day));

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendActionBar(text);
            }
        } else if (method.equals("chat")) {
            String text = plugin.getConfig().getString(configRoot + ".chat.text", "§eDay §6%day%");
            text = text.replace("%day%", Long.toString(day));

            Component textComponent = Component.text(text);
            Bukkit.broadcast(textComponent);
        } else if (method.equals("title")) {
            String text = plugin.getConfig().getString(configRoot + ".title.text", "New day!");
            String subtext =  plugin.getConfig().getString(configRoot + ".title.subtext.text", "Current day: %day%");
            boolean subtextEnabled = plugin.getConfig().getBoolean(configRoot + ".title.subtext.enabled", true);

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
            plugin.getComponentLogger().warn("I don't know what to do with {} method.", method);
        }
    }

    public void start(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            String worldName = plugin.getConfig().getString("settings.world");
            if (worldName == null) { worldName = "world"; }
            World w = Bukkit.getWorld(worldName);
            if (w == null) {
                plugin.getComponentLogger().warn("World {} not found!", worldName);
                return;
            }

            long day = w.getFullTime() / 24000;

            if (day != lastDay) {
                lastDay = day;

                boolean specialEvents = plugin.getConfig().getBoolean("settings.special.enabled");
                long specialDays = plugin.getConfig().getLong("settings.special.days");

                if (specialEvents && day % specialDays == 0) {
                    boolean displayEnabled = plugin.getConfig().getBoolean("settings.special.displayText.enabled", true);
                    // boolean customEnabled = plugin.getConfig().getBoolean("settings.special.custom.enabled", false);

                    if (displayEnabled) {
                        display(plugin, day, plugin.getConfig().getString("settings.special.displayText.type", "title"), "settings.special.displayText.messages");
                    }

                    // TODO: This

                    // Config (in settings.special):
                    //    custom:
                    //      # Here you can set up your own events using in-game commands.
                    //      enabled: false
                    //      commands:
                    //        # All commands will run as player (as in /execute as @a at @a run <your command here>)
                    //        - "summon firework_rocket ~ ~10 ~ {LifeTime:0,FireworksItem:{id:firework_rocket,components:{fireworks:{flight_duration:1,explosions:[{shape:star,has_twinkle:1b,has_trail:1b,colors:[I;16701501],fade_colors:[I;16351261]}]}}}}"
                    //        - "effect give @s minecraft:regeneration 90 0 true"

                    // if (customEnabled) {
                        // List<String> commands = plugin.getConfig().getStringList("settings.special.custom.commands");

                        // for (String cmd : commands) {

                        // }
                    // }
                }

                String method = plugin.getConfig().getString("settings.display");
                if (method == null) { method = "actionbar"; }

                display(plugin, day, method, "messages");

                boolean sounds = plugin.getConfig().getBoolean("settings.sound.enabled", true);
                String soundID = plugin.getConfig().getString("settings.sound.id", "entity.player.levelup");
                Sound newDaySound = Sound.sound(Key.key(soundID), Sound.Source.MASTER, 1.0f, 1.0f);
                if (sounds) {
                    Bukkit.getServer().playSound(newDaySound);
                }
            }
        }, 0L, 100L);
    }
}
