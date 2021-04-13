package me.buby.vanillaenhanced.customitems;

import java.util.List;

import org.bukkit.plugin.Plugin;

import me.buby.devlib.customitems.ItemManager;
import me.buby.devlib.customitems.util.CustomItem;
import me.buby.vanillaenhanced.VanillaEnhanced;
import me.buby.vanillaenhanced.customitems.impl.BigBucket;
import me.buby.vanillaenhanced.customitems.impl.Satchel;
import me.buby.vanillaenhanced.customitems.impl.VeryUsefulStick;

public class VanillaEnhancedItemManager extends ItemManager{

	public VanillaEnhancedItemManager(Plugin plugin) {
		super(plugin);
		registerItemVE(
				new VeryUsefulStick(this),
				new Satchel(this),
				new BigBucket(this)
				);
	}
	
	private void registerItemVE(CustomItem... items) {
		@SuppressWarnings("unchecked")
		List<String> disabledItems = (List<String>) ((VanillaEnhanced)plugin).getPluginConfig().getList("disabled-items");
		
		for(CustomItem item : items) {
			if(disabledItems.contains(item.getId())) continue;
			registerItem(item);	
			
		}

	}
}
