package space.kararasenok.newDayNotifier;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class ReloadConfigCommand implements BasicCommand {
    private final NewDayNotifier plugin;

    public ReloadConfigCommand(NewDayNotifier plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("newdaynotifier.reload")) {
                sender.sendMessage("§4You don't have permission to do that!");
                return;
            }

            sender.sendMessage("§eReloading config file...");
            plugin.reloadConfig();
            sender.sendMessage("§aConfig reloaded successfully!");
        } else {
            String worldName = plugin.getConfig().getString("world", "world");
            World w = Bukkit.getWorld(worldName);

            if (w == null) {
                sender.sendMessage("World " + worldName + " not found!");
                return;
            }

            long day = w.getFullTime() / 24000;
            sender.sendMessage("§bCurrent day: §r§6" + day);
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload");
        }
        return List.of();
    }
}
