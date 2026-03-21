package space.kararasenok.newDayNotifier;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import space.kararasenok.newDayNotifier.commands.ReloadConfigCommand;
import space.kararasenok.newDayNotifier.listeners.DayTracker;
import space.kararasenok.newDayNotifier.listeners.PlayerJoin;

public final class NewDayNotifier extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("NewDayNotifier loaded successfully!");

        Updates.checkUpdates(this).thenAccept(out -> {
            if (out) {
                getLogger().info("You are using outdated version of NewDayNotifier!");
                getLogger().info("Download update here: https://github.com/kararasenok-gd/NewDayNotifier/releases/latest");
                getComponentLogger().info("You are using outdated version of NewDayNotifier!");
                getComponentLogger().info("Download update here: https://github.com/kararasenok-gd/NewDayNotifier/releases/latest");
            } else {
                getLogger().info("NewDayNotifier is up to date!");
                getComponentLogger().info("NewDayNotifier is up to date!");
            };
        });

        new DayTracker().start(this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register(
                    "day",
                    "NewDayNotifier command",
                    new ReloadConfigCommand(this)
            );
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("NewDayNotifier deactivated successfully!");
        getComponentLogger().info("NewDayNotifier deactivated successfully!");
    }
}
