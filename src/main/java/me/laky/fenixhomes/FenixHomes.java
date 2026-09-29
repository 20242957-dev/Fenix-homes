package me.laky.fenixhomes;

import org.bukkit.plugin.java.JavaPlugin;

public class FenixHomes extends JavaPlugin {

    private HomeManager homeManager;

    @Override
    public void onEnable() {

        getLogger().info("FenixHomes se esta iniciando...");

        // Cargar el sistema de homes
        homeManager = new HomeManager(this);

        // Registrar comandos
        getCommand("sethome").setExecutor(new HomeCommand(homeManager));
        getCommand("home").setExecutor(new HomeCommand(homeManager));
        getCommand("delhome").setExecutor(new HomeCommand(homeManager));
        getCommand("homes").setExecutor(new HomeCommand(homeManager));

        // Registrar eventos del menu
        getServer().getPluginManager().registerEvents(
                new HomesListener(homeManager),
                this
        );

        getLogger().info("FenixHomes ha sido activado correctamente.");
        getLogger().info("Limite de jugador normal: 5 homes.");
    }

    @Override
    public void onDisable() {
        getLogger().info("FenixHomes ha sido desactivado.");
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }
}
