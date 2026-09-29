package me.laky.fenixhomes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class HomesMenu {

    public static void open(Player player, HomeManager homeManager) {

        Inventory inventory = Bukkit.createInventory(
                null,
                27,
                ChatColor.DARK_AQUA + "Tus Homes"
        );

        Map<String, org.bukkit.Location> homes =
                homeManager.getHomes(player.getUniqueId());

        int slot = 10;

        for (String homeName : homes.keySet()) {

            if (slot >= 17) {
                break;
            }

            ItemStack item = new ItemStack(Material.BED);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(
                    ChatColor.GREEN + homeName
            );

            org.bukkit.Location location = homes.get(homeName);

            meta.setLore(java.util.Arrays.asList(
                    ChatColor.GRAY + "Mundo: " + location.getWorld().getName(),
                    ChatColor.GRAY + "X: " + location.getBlockX(),
                    ChatColor.GRAY + "Y: " + location.getBlockY(),
                    ChatColor.GRAY + "Z: " + location.getBlockZ(),
                    "",
                    ChatColor.GREEN + "Click izquierdo: Teletransportarte",
                    ChatColor.RED + "Click derecho: Eliminar"
            ));

            item.setItemMeta(meta);
            inventory.setItem(slot, item);

            slot++;

            if (slot == 13) {
                slot = 14;
            }
        }

        // Rellenar espacios vacios
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta fillerMeta = filler.getItemMeta();

        fillerMeta.setDisplayName(ChatColor.GRAY + " ");
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < 27; i++) {

            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }

        // Si no tiene homes
        if (homes.isEmpty()) {

            ItemStack noHomes = new ItemStack(Material.BARRIER);
            ItemMeta meta = noHomes.getItemMeta();

            meta.setDisplayName(
                    ChatColor.RED + "No tienes homes"
            );

            meta.setLore(java.util.Arrays.asList(
                    ChatColor.GRAY + "Usa /sethome <nombre>",
                    ChatColor.GRAY + "para crear uno."
            ));

            noHomes.setItemMeta(meta);
            inventory.setItem(13, noHomes);
        }

        player.openInventory(inventory);
    }
}
