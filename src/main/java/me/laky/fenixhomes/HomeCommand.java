package me.laky.fenixhomes;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public HomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        String commandName = command.getName().toLowerCase();

        // /sethome <nombre>
        if (commandName.equals("sethome")) {

            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Uso: /sethome <nombre>");
                return true;
            }

            String homeName = args[0].toLowerCase();

            if (!homeName.matches("[a-zA-Z0-9_-]+")) {
                player.sendMessage(ChatColor.RED +
                        "El nombre del home solo puede contener letras, numeros, _ o -.");
                return true;
            }

            Map<String, Location> homes = homeManager.getHomes(uuid);

            if (!homes.containsKey(homeName)
                    && homes.size() >= homeManager.getHomeLimit(player)) {

                player.sendMessage(ChatColor.RED +
                        "Has alcanzado tu limite de "
                        + homeManager.getHomeLimit(player)
                        + " homes.");

                return true;
            }

            homeManager.setHome(uuid, homeName, player.getLocation());

            player.sendMessage(ChatColor.GREEN +
                    "Home '" + homeName + "' establecido correctamente.");

            return true;
        }

        // /home <nombre>
        if (commandName.equals("home")) {

            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Uso: /home <nombre>");
                return true;
            }

            String homeName = args[0].toLowerCase();

            Location location = homeManager.getHome(uuid, homeName);

            if (location == null) {
                player.sendMessage(ChatColor.RED +
                        "No existe un home llamado '" + homeName + "'.");
                return true;
            }

            player.teleport(location);

            player.sendMessage(ChatColor.GREEN +
                    "Teletransportado a '" + homeName + "'.");

            return true;
        }

        // /delhome <nombre>
        if (commandName.equals("delhome")) {

            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Uso: /delhome <nombre>");
                return true;
            }

            String homeName = args[0].toLowerCase();

            if (!homeManager.deleteHome(uuid, homeName)) {
                player.sendMessage(ChatColor.RED +
                        "No existe un home llamado '" + homeName + "'.");
                return true;
            }

            player.sendMessage(ChatColor.GREEN +
                    "Home '" + homeName + "' eliminado.");

            return true;
        }

        // /homes
        if (commandName.equals("homes")) {

            Map<String, Location> homes = homeManager.getHomes(uuid);

            player.sendMessage(
                    ChatColor.GOLD + "===== Tus Homes ====="
            );

            if (homes.isEmpty()) {

                player.sendMessage(
                        ChatColor.GRAY + "No tienes ningún home."
                );

            } else {

                for (String homeName : homes.keySet()) {

                    player.sendMessage(
                            ChatColor.YELLOW + "- "
                                    + ChatColor.WHITE + homeName
                    );
                }
            }

            player.sendMessage(
                    ChatColor.GRAY
                            + "Homes: "
                            + homes.size()
                            + "/"
                            + homeManager.getHomeLimit(player)
            );

            return true;
        }

        return false;
    }
}
