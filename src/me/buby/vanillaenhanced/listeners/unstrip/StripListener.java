package me.buby.vanillaenhanced.listeners.unstrip;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableSet;

import me.buby.devlib.events.blockinteractevent.BlockInteractEvent;
import me.buby.devlib.util.SwitchMap;

public class StripListener implements Listener{

	Plugin plugin;
	public final ImmutableSet<Material> axeTypes = ImmutableSet.<Material>builder()
		.add(Material.STONE_AXE)
		.add(Material.IRON_AXE)
		.add(Material.DIAMOND_AXE)
		.add(Material.NETHERITE_AXE)
		.add(Material.GOLDEN_AXE)
		.build();
	
	
	private Material getUnstripped(Material mat) {
		SwitchMap<Material, Material> map = new SwitchMap<>(null);
		map.add(Material.STRIPPED_OAK_LOG, Material.OAK_LOG)
		.add(Material.STRIPPED_SPRUCE_LOG, Material.SPRUCE_LOG)
		.add(Material.STRIPPED_BIRCH_LOG, Material.BIRCH_LOG)
		.add(Material.STRIPPED_JUNGLE_LOG, Material.JUNGLE_LOG)
		.add(Material.STRIPPED_ACACIA_LOG, Material.ACACIA_LOG)
		.add(Material.STRIPPED_DARK_OAK_LOG, Material.DARK_OAK_LOG)
		.add(Material.STRIPPED_WARPED_STEM, Material.WARPED_STEM)
		.add(Material.STRIPPED_CRIMSON_STEM, Material.CRIMSON_STEM);
		
		return map.match(mat);
	}
	
	public StripListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockInteract(BlockInteractEvent event) {
		PlayerInteractEvent e = event.getEventRaw();
		if(e.isCancelled()) return;
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem() == null) return;
			Material type = e.getItem().getType();
			if(!axeTypes.contains(type)) return;
			Material unstripped = getUnstripped(e.getClickedBlock().getType());
			if(unstripped != null) {
				e.setCancelled(true);
				e.getClickedBlock().setType(unstripped);
			}
		}
				
	}
	
}
