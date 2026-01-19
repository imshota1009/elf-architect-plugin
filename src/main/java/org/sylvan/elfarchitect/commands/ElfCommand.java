package org.sylvan.elfarchitect.commands;

import org.sylvan.elfarchitect.ElfArchitect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ElfCommand implements CommandExecutor {

    private final ElfArchitect plugin;

    public ElfCommand(ElfArchitect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Players only!");
            return true;
        }
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("elfwand")) {
            giveWand(player);
            return true;
        }

        if (label.equalsIgnoreCase("set")) {
            if (args.length < 1)
                return false;
            Material mat = Material.matchMaterial(args[0]);
            if (mat == null) {
                player.sendMessage(ChatColor.RED + "Invalid material: " + args[0]);
                return true;
            }
            if (!plugin.getSelectionManager().hasSelection(player)) {
                player.sendMessage(ChatColor.RED + "Make a selection first!");
                return true;
            }

            plugin.getSelectionManager().runTaskInRegion(player, block -> {
                block.setType(mat);
            });
            player.sendMessage(ChatColor.GREEN + "Region set to " + mat.name());
            return true;
        }

        if (label.equalsIgnoreCase("replace")) {
            if (args.length < 2)
                return false;
            Material from = Material.matchMaterial(args[0]);
            Material to = Material.matchMaterial(args[1]);

            if (from == null || to == null) {
                player.sendMessage(ChatColor.RED + "Invalid material.");
                return true;
            }
            if (!plugin.getSelectionManager().hasSelection(player)) {
                player.sendMessage(ChatColor.RED + "Make a selection first!");
                return true;
            }

            plugin.getSelectionManager().runTaskInRegion(player, block -> {
                if (block.getType() == from) {
                    block.setType(to);
                }
            });
            player.sendMessage(ChatColor.GREEN + "Replaced " + from.name() + " with " + to.name());
            return true;
        }

        return false;
    }

    public static void giveWand(Player player) {
        ItemStack wand = new ItemStack(Material.GOLDEN_HOE);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "The Elder Wand");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Mode: " + ChatColor.WHITE + "Selection");
        lore.add(ChatColor.GRAY + "L-Click: Pos1 / Action");
        lore.add(ChatColor.GRAY + "R-Click: Pos2 / Action");
        lore.add(ChatColor.GRAY + "Shift+R-Click: Change Mode");
        meta.setLore(lore);
        wand.setItemMeta(meta);
        player.getInventory().addItem(wand);
        player.sendMessage(ChatColor.GREEN + "You have received the Elder Wand!");
    }
}
