package space.kararasenok.newDayNotifier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class NewDayNotifier extends JavaPlugin {

    private void checkUpdates() {
        String version = this.getDescription().getVersion();
        getLogger().info("Current version: " +  version);

        String repository = "kararasenok-gd/NewDayNotifier";

        CompletableFuture.runAsync(() -> {
           try {
               HttpClient client = HttpClient.newHttpClient();
               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create("https://api.github.com/" + repository + "/releases/latest"))
                       .header("Accept", "application/vnd.github.v3+json")
                       .build();

               HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
               if (response.statusCode() == 200) {
                   JsonObject json = (JsonObject) new JsonParser().parse(response.body()).getAsJsonObject();
                   String latestVersion = json.get("tag_name").getAsString().replace("v", "");
                   getLogger().info("Latest version: " + latestVersion);
                   if (!version.equalsIgnoreCase(latestVersion)) {
                       getLogger().info("NewDayNotifier has new update!");
                       getLogger().info("Download it here: https://github.com/" + repository + "/releases/latest");
                   }
               }
           } catch (Exception e) {
               getLogger().severe("Failed to check updates.");
               getLogger().severe(e.getMessage());
           }
        });
    };

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("NewDayNotifier loaded successfully!");

        checkUpdates();

        new DayTracker().start(this);
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
    }
}
