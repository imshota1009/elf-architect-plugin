package org.sylvan.elfarchitect.listeners;

import org.sylvan.elfarchitect.ElfArchitect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WandListener implements Listener {

    private final ElfArchitect plugin;
    private final Random random = new Random();

    public WandListener(ElfArchitect plugin) {
        this.plugin = plugin;
    }

    private enum Mode {
        SELECTION,
        LEAF_WEAVER,
        MOSSIFIER,
        FAIRY_LIGHT
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.GOLDEN_HOE || !item.hasItemMeta())
            return;
        if (!item.getItemMeta().getDisplayName().contains("Elder Wand"))
            return;

        event.setCancelled(true); // Prevent normal hoe use

        Mode currentMode = getModeFromLore(item);

        // Shift + Right Click = Change Mode
        if (player.isSneaking()
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            cycleMode(player, item, currentMode);
            return;
        }

        // Action Handling based on Mode
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            handleLeftClick(player, event.getClickedBlock(), currentMode);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            handleRightClick(player, event.getClickedBlock(), event.getBlockFace(), currentMode);
        }
    }

    private void handleLeftClick(Player player, Block block, Mode mode) {
        if (mode == Mode.SELECTION) {
            plugin.getSelectionManager().setPos1(player, block.getLocation());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Position 1 set at " + formatLoc(block.getLocation()));
        } else {
            // In other modes, left click could act as a 'remover' or secondary action
            // For now, let's keep it simple or allow selection in all modes?
            // Let's allow selection only in Selection mode to prevent accidents.
            player.sendMessage(ChatColor.GRAY + "Switch to Selection mode to select points.");
        }
    }

    private void handleRightClick(Player player, Block block, BlockFace face, Mode mode) {
        if (mode == Mode.SELECTION) {
            plugin.getSelectionManager().setPos2(player, block.getLocation());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Position 2 set at " + formatLoc(block.getLocation()));
        } else if (mode == Mode.LEAF_WEAVER) {
            spawnHangingLeaves(block.getRelative(face));
        } else if (mode == Mode.MOSSIFIER) {
            mossifyArea(block, 3);
        } else if (mode == Mode.FAIRY_LIGHT) {
            spawnFairyLight(block.getRelative(face));
        }
    }

    // --- Feature Implementations ---

    private void spawnHangingLeaves(Block center) {
        if (center.getType() != Material.AIR)
            return;

        Material[] leaves = { Material.OAK_LEAVES, Material.BIRCH_LEAVES, Material.AZALEA_LEAVES,
                Material.FLOWERING_AZALEA_LEAVES };

        // Simple organic hanging shape
        center.setType(leaves[random.nextInt(leaves.length)]);
        if (random.nextBoolean()) {
            center.getRelative(BlockFace.DOWN).setType(leaves[random.nextInt(leaves.length)]);
        }
        center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center.getLocation().add(0.5, 0.5, 0.5), 5);
        center.getWorld().playSound(center.getLocation(), Sound.BLOCK_GRASS_PLACE, 1f, 1f);
    }

    private void mossifyArea(Block center, int radius) {
        World world = center.getWorld();
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int y = cy - radius; y <= cy + radius; y++) {
                for (int z = cz - radius; z <= cz + radius; z++) {
                    if (random.nextDouble() > 0.4)
                        continue; // Random noise
                    Block b = world.getBlockAt(x, y, z);
                    if (b.getType() == Material.STONE_BRICKS) {
                        b.setType(random.nextBoolean() ? Material.MOSSY_STONE_BRICKS : Material.CRACKED_STONE_BRICKS);
                    } else if (b.getType() == Material.COBBLESTONE) {
                        b.setType(Material.MOSSY_COBBLESTONE);
                    }
                }
            }
        }
        world.playSound(center.getLocation(), Sound.BLOCK_STONE_BREAK, 1f, 0.5f);
    }

    private void spawnFairyLight(Block block) {
        if (block.getType() != Material.AIR)
            return;
        block.setType(Material.LIGHT); // Invisible light block
        block.getWorld().spawnParticle(Particle.END_ROD, block.getLocation().add(0.5, 0.5, 0.5), 10, 0.05, 0.05, 0.05,
                0.01);
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 2f);
    }

    // --- Utility ---

    private Mode getModeFromLore(ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();
        if (lore != null && !lore.isEmpty()) {
            String modeLine = lore.get(0);
            for (Mode m : Mode.values()) {
                if (modeLine.contains(m.name())) {
                    return m;
                }
            }
        }
        return Mode.SELECTION;
    }

    private void cycleMode(Player player, ItemStack item, Mode current) {
        int ord = current.ordinal();
        ord++;
        if (ord >= Mode.values().length)
            ord = 0;
        Mode newMode = Mode.values()[ord];

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(0, ChatColor.YELLOW + "Mode: " + ChatColor.WHITE + newMode.name());
        meta.setLore(lore);
        item.setItemMeta(meta);

        player.sendTitle("", ChatColor.GOLD + newMode.name(), 5, 20, 5);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
    }

    private String formatLoc(Location loc) {
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
}
