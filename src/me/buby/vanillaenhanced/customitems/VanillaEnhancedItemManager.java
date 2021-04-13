package me.buby.vanillaenhanced.customitems;

import org.bukkit.plugin.Plugin;

import me.buby.devlib.customitems.ItemManager;
import me.buby.vanillaenhanced.customitems.impl.BigBucket;
import me.buby.vanillaenhanced.customitems.impl.Satchel;
import me.buby.vanillaenhanced.customitems.impl.VeryUsefulStick;

public class VanillaEnhancedItemManager extends ItemManager{

	public VanillaEnhancedItemManager(Plugin plugin) {
		super(plugin);
		registerItem(
				new VeryUsefulStick(this),
				new Satchel(this),
				new BigBucket(this)
				);
	}

}
