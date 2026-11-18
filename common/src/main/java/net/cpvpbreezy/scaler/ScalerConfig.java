package net.cpvpbreezy.scaler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

/** Per-element GUI scale settings, stored in config/scaler.json. */
public final class ScalerConfig {
    public static final double MIN = 0.50, MAX = 8.00, DEFAULT = 2.00;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("scaler.json");

    public static final class Entry {
        public boolean enabled = false;
        public double scale = DEFAULT;
    }

    private static final Map<ScaledElement, Entry> ENTRIES = new EnumMap<>(ScaledElement.class);

    static {
        for (ScaledElement e : ScaledElement.values()) ENTRIES.put(e, new Entry());
        load();
    }

    private ScalerConfig() {}

    public static Entry get(ScaledElement e) {
        return ENTRIES.get(e);
    }

    /** Rounds to 0.01 and clamps to the allowed range. */
    public static double clean(double v) {
        if (Double.isNaN(v)) return DEFAULT;
        v = Math.round(v * 100.0) / 100.0;
        return Math.max(MIN, Math.min(MAX, v));
    }

    public static void load() {
        if (!Files.exists(FILE)) return;
        try {
            JsonObject root = GSON.fromJson(Files.readString(FILE), JsonObject.class);
            if (root == null) return;
            for (ScaledElement e : ScaledElement.values()) {
                if (!root.has(e.key) || !root.get(e.key).isJsonObject()) continue;
                JsonObject o = root.getAsJsonObject(e.key);
                Entry entry = ENTRIES.get(e);
                if (o.has("enabled")) entry.enabled = o.get("enabled").getAsBoolean();
                if (o.has("scale")) entry.scale = clean(o.get("scale").getAsDouble());
            }
        } catch (Exception ex) {
            ScalerMod.LOGGER.warn("Could not read {}, using defaults", FILE, ex);
        }
    }

    public static void save() {
        JsonObject root = new JsonObject();
        for (ScaledElement e : ScaledElement.values()) {
            JsonObject o = new JsonObject();
            o.addProperty("enabled", ENTRIES.get(e).enabled);
            o.addProperty("scale", ENTRIES.get(e).scale);
            root.add(e.key, o);
        }
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(root));
        } catch (IOException ex) {
            ScalerMod.LOGGER.warn("Could not save {}", FILE, ex);
        }
    }
}
