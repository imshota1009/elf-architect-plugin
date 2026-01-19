# ElfArchitect Plugin

Elf Architect is a Spigot/Paper plugin designed to effortlessly create fantasy or elven-style structures in Minecraft.
Using a magical tool called the "Elder Wand," you can perform intuitive building operations that harmonize with nature.

## Features

### 🪄 Elder Wand
Use the dedicated wand (Golden Hoe) to cast various building spells.
**Shift + Right Click** to cycle through modes.

#### Modes:
1.  **SELECTION**
    *   **Left Click**: Set Position 1
    *   **Right Click**: Set Position 2
    *   The basic range selection mode. Used in combination with `/set` or `/replace` commands.

2.  **LEAF_WEAVER**
    *   **Right Click**: Generates naturally hanging leaves (Oak, Birch, Azalea, etc.) at the clicked location.
    *   Perfect for creating elven treehouses or natural decorations.

3.  **MOSSIFIER**
    *   **Right Click**: Randomly transforms Stone Bricks or Cobblestone within a 3-block radius into mossy or cracked variants.
    *   Instantly ages ruins or old buildings.

4.  **FAIRY_LIGHT**
    *   **Right Click**: Places an invisible light source (Light Block) and spawns magical particles.
    *   Creates a mystical atmosphere with hidden lighting.

### 🛠 Commands
*   `/elfwand`: Gives you the Elder Wand.
*   `/set <material>`: Fills the selected region with the specified block.
*   `/replace <from_material> <to_material>`: Replaces specific blocks within the selected region.

## How to Install

1.  Place `ElfArchitect-1.0.jar` into your server's `plugins` folder.
2.  Restart or reload the server.
3.  Use the `/elfwand` command to get your wand and start building!

## Permissions

*   `elfarchitect.use`: Permission to use all plugin features (default: OP).

## Development Environment
*   Java: 17+
*   Minecraft Version: 1.20+
*   API: Spigot/Paper API
