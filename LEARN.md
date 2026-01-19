# Learning ElfArchitect 🧝

Welcome to the **ElfArchitect** codebase!
This guide is designed to help developers understand how the plugin works and how to extend its magic.

## 🧭 Project Structure

The project follows a standard Maven structure. The source code is located in `src/main/java/org/sylvan/elfarchitect/`.

### Key Components

*   **`ElfArchitect.java`**:
    The main plugin class (entry point). It initializes the plugin, registers commands and event listeners, and manages the singleton instance.

*   **`listeners/WandListener.java`**:
    The heart of the magic! This class handles all interactions with the "Elder Wand".
    *   It defines the `Mode` enum (Selection, Leaf Weaver, Mossifier, Fairy Light).
    *   It detects player clicks and executes the logic for the active mode.
    *   It handles mode switching (Shift + Right Click).

*   **`utils/SelectionManager.java`**:
    Manages the region selections for each player.
    *   Stores `pos1` and `pos2` locations.
    *   Provides the `runTaskInRegion` helper method to easily iterate over all blocks in a selected cuboid.

*   **`commands/ElfCommand.java`**:
    Handles commands like `/elfwand`, `/set`, and `/replace`.
    *   `/set` and `/replace` utilize the `SelectionManager` to modify blocks within the selected area.

## 🪄 How to Add a New Wand Mode

Want to add a new magical feature? Follow these steps:

1.  **Update `WandListener.java`**:
    *   Add a new entry to the `Mode` enum (e.g., `FLOWER_SPREADER`).
    *   In `handleRightClick`, add a condition for your new mode.
    *   Create a private method (e.g., `spreadFlowers(Block center)`) to implement the logic.

2.  **Implement the Logic**:
    *   Use `block.setType(...)` or `world.spawnParticle(...)` to create your effect.
    *   Remember to check for `Material.AIR` if you only want to place things in empty space.

## 📚 Tips for Fantasy Building Logic

*   **Randomness is Key**: Nature isn't perfect. Use `random.nextBoolean()` or `random.nextInt()` to vary block placement (e.g., mixing Oak and Birch leaves) for a more organic look.
*   **Particles & Sound**: Visuals and audio are half the magic. Adding a small particle effect or sound when a wand action completes makes it feel satisfying (Game Juice!).

Happy coding, and may your code be as enduring as the ancient forests! 🌲
