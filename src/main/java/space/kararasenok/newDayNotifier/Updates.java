package space.kararasenok.newDayNotifier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Updates {
    private static final String repository = "kararasenok-gd/NewDayNotifier";

    private static boolean isOutdated(String current, String latest) {
        String[] c = current.split("\\.");
        String[] l = latest.split("\\.");

        int length = Math.max(c.length, l.length);

        for  (int i = 0; i < length; i++) {
            int cv = i < c.length ? Integer.parseInt(c[i]) : 0;
            int lv =  i < l.length ? Integer.parseInt(l[i]) : 0;

            if (cv < lv) return true;
            if (cv > lv) return false;
        }

        return false;
    };

    public static CompletableFuture<Boolean> checkUpdates(JavaPlugin plugin) {
        String version = plugin.getDescription().getVersion();

        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.github.com/repos/" + repository + "/releases/latest"))
                        .header("Accept", "application/vnd.github.v3+json")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JsonObject json = (JsonObject) new JsonParser().parse(response.body()).getAsJsonObject();
                    String latestVersion = json.get("tag_name").getAsString().replace("v", "");

                    return isOutdated(version, latestVersion);
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to check updates.");
                plugin.getLogger().severe(e.getMessage());
                plugin.getComponentLogger().error("Failed to check updates.");
                plugin.getComponentLogger().error(e.getMessage());
            }

            return false;
        });
    };
}
