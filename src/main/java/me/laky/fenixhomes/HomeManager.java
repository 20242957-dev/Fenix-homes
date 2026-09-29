package me.laky.fenixhomes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {

    private final JavaPlugin plugin;
    private final File homesFile;
    private final YamlConfiguration homesConfig;

    public HomeManager(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        homesFile = new File(plugin.getDataFolder(), "homes.yml");
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
    }

    public boolean setHome(UUID uuid, String name, Location location) {

        String path = "players." + uuid.toString() + ".homes." + name;

        homesConfig.set(path + ".world", location.getWorld().getName());
        homesConfig.set(path + ".x", location.getX());
        homesConfig.set(path + ".y", location.getY());
        homesConfig.set(path + ".z", location.getZ());
        homesConfig.set(path + ".yaw", location.getYaw());
        homesConfig.set(path + ".pitch", location.getPitch());

        save();

        return true;
    }

    public Location getHome(UUID uuid, String name) {

        String path = "players." + uuid.toString() + ".homes." + name;

        if (!homesConfig.contains(path)) {
            return null;
        }

        String worldName = homesConfig.getString(path + ".world");
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            return null;
        }

        double x = homesConfig.getDouble(path + ".x");
        double y = homesConfig.getDouble(path + ".y");
        double z = homesConfig.getDouble(path + ".z");

        float yaw = (float) homesConfig.getDouble(path + ".yaw");
        float pitch = (float) homesConfig.getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public boolean deleteHome(UUID uuid, String name) {

        String path = "players." + uuid.toString() + ".homes." + name;

        if (!homesConfig.contains(path)) {
            return false;
        }

        homesConfig.set(path, null);
        save();

        return true;
    }

    public Map<String, Location> getHomes(UUID uuid) {

        Map<String, Location> homes = new HashMap<String, Location>();

        String path = "players." + uuid.toString() + ".homes";

        ConfigurationSection section = homesConfig.getConfigurationSection(path);

        if (section == null) {
            return homes;
        }

        for (String name : section.getKeys(false)) {

            Location location = getHome(uuid, name);

            if (location != null) {
                homes.put(name, location);
            }
        }

        return homes;
    }

    public int getHomeLimit(org.bukkit.entity.Player player) {

        if (player.hasPermission("fenixhomes.homes.unlimited")) {
            return Integer.MAX_VALUE;
        }

        if (player.hasPermission("fenixhomes.homes.10")) {
            return 10;
        }

        return 5;
    }

    private void save() {

        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("No se pudo guardar homes.yml.");
            e.printStackTrace();
        }
    }
}
