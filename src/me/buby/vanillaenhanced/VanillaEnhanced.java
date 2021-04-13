package me.buby.vanillaenhanced;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.buby.devlib.events.DevLibCustomEvents;
import me.buby.vanillaenhanced.command.CommandManager;
import me.buby.vanillaenhanced.customitems.VanillaEnhancedItemManager;
import me.buby.vanillaenhanced.inventory.VanillaEnhancedInventoryManager;
import me.buby.vanillaenhanced.listeners.lumber.LumberListener;
import me.buby.vanillaenhanced.listeners.slotassist.SlotAssistListener;
import me.buby.vanillaenhanced.listeners.unstrip.StripListener;

public class VanillaEnhanced extends JavaPlugin{

	@Getter private VanillaEnhancedItemManager itemManager;
	@Getter private VanillaEnhancedInventoryManager invManager;
	@Getter private CommandManager cmdManager;
	
	DevLibCustomEvents customEventManager;
	
	@Override
	public void onEnable() {
		itemManager = new VanillaEnhancedItemManager(this);
		invManager = new VanillaEnhancedInventoryManager(this);
		cmdManager = new CommandManager(this);
		
		customEventManager = new DevLibCustomEvents(this);
		
		registerEvents(
			new LumberListener(this),
			new SlotAssistListener(this),
			new StripListener(this)
		);
	}
	
	private void registerEvents(Listener... listeners) {
		for(Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
}
