package org.sylvan.elfarchitect;

import org.sylvan.elfarchitect.commands.ElfCommand;
import org.sylvan.elfarchitect.listeners.WandListener;
import org.sylvan.elfarchitect.utils.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ElfArchitect extends JavaPlugin {

    private static ElfArchitect instance;
    private SelectionManager selectionManager;
    private WandListener wandListener;

    @Override
    public void onEnable() {
        instance = this;
        selectionManager = new SelectionManager();
        
        // Commands
        getCommand("elfwand").setExecutor(new ElfCommand(this));
        getCommand("set").setExecutor(new ElfCommand(this));
        getCommand("replace").setExecutor(new ElfCommand(this));

        // Listeners
        wandListener = new WandListener(this);
        getServer().getPluginManager().registerEvents(wandListener, this);

        getLogger().info("ElfArchitect has been enabled! May the forest be with you.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ElfArchitect has been disabled.");
    }

    public static ElfArchitect getInstance() {
        return instance;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
}
