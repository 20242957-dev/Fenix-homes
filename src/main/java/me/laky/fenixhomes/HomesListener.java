package me.laky.fenixhomes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HomesListener implements Listener {

    private final HomeManager homeManager;

    public HomesListener(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals(ChatColor.DARK_AQUA + "Tus Homes")) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        ItemStack item = event.getCurrentItem();

        if (item == null || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String homeName = ChatColor.stripColor(meta.getDisplayName());

        if (homeName.equals("No tienes homes")) {
            return;
        }

        // Click izquierdo = teletransportarse
        if (event.isLeftClick()) {

            if (homeManager.getHome(player.getUniqueId(), homeName) == null) {
                player.sendMessage(ChatColor.RED
                        + "Ese home ya no existe.");
                player.closeInventory();
                return;
            }

            player.closeInventory();

            player.teleport(
                    homeManager.getHome(
                            player.getUniqueId(),
                            homeName
                    )
            );

            player.sendMessage(ChatColor.GREEN
                    + "Teletransportado a '" + homeName + "'.");
        }

        // Click derecho = eliminar
        if (event.isRightClick()) {

            if (homeManager.deleteHome(
                    player.getUniqueId(),
                    homeName
            )) {

                player.sendMessage(ChatColor.GREEN
                        + "Home '" + homeName + "' eliminado.");

                HomesMenu.open(player, homeManager);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {

        if (event.getView().getTitle().equals(ChatColor.DARK_AQUA + "Tus Homes")) {
            event.setCancelled(true);
        }
    }
}
