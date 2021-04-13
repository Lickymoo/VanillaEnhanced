package me.buby.vanillaenhanced.listeners.lumber;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableSet;

import me.buby.devlib.events.blockstartbreakevent.BlockStartBreakEvent;
import me.buby.devlib.events.blockstartbreakevent.BlockStartBreakEventListener;
import me.buby.devlib.util.SwitchMap;

public class LumberListener implements Listener{
	private static final int maxLumberSize = 50;
	Plugin plugin;
	
	public LumberListener(Plugin plugin){
		this.plugin = plugin;
	}
	
	public final ImmutableSet<Material> logTypes = ImmutableSet.<Material>builder()
		.add(Material.OAK_LOG)
		.add(Material.SPRUCE_LOG)
		.add(Material.BIRCH_LOG)
		.add(Material.JUNGLE_LOG)
		.add(Material.ACACIA_LOG)
		.add(Material.DARK_OAK_LOG)
		.add(Material.CRIMSON_STEM)
		.add(Material.WARPED_STEM)
		.build();
	
	//Get break speed for logs based on item used
	private int getBreakSpeed(Material mat) {
		SwitchMap<Material, Integer> map = new SwitchMap<>(60);
		map.add(Material.WOODEN_AXE, 30)
		.add(Material.STONE_AXE, 15)
		.add(Material.IRON_AXE, 10)
		.add(Material.DIAMOND_AXE, 8)
		.add(Material.NETHERITE_AXE, 7)
		.add(Material.GOLDEN_AXE, 1);

		return map.match(mat);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void breakStartListener(BlockStartBreakEvent event) {
		Location loc = BlockStartBreakEventListener.getLocation(event.getContext().getBlockPos(), event.getPlayer());
		if(!logTypes.contains(loc.getBlock().getType())) return;
		if(!event.getPlayer().isSneaking()) return;
		Set<Block> blocks = new HashSet<>();
		getConnectedBlocks(blocks, loc.getBlock().getType(), loc.getBlock());

		event.getContext().setBreakSpeed(getBreakSpeed(event.getPlayer().getInventory().getItemInMainHand().getType()) * blocks.size());
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void breakListener(BlockBreakEvent event) {
		//Super hacky way of avoiding using worldguard API
		if(event.getExpToDrop() == Integer.MAX_VALUE - 333){
			event.setExpToDrop(0);
			return;
		}
		
		Set<Block> blocks = new HashSet<>();		
		if(event.isCancelled()) return;
		if(!logTypes.contains(event.getBlock().getType())) return;
		if(!event.getPlayer().isSneaking()) return;
		getConnectedBlocks(blocks, event.getBlock().getType(), event.getBlock());
		int durability = 0;
		
		for(Block b : blocks) {
			BlockBreakEvent e = new BlockBreakEvent(b, event.getPlayer());
			e.setExpToDrop(Integer.MAX_VALUE - 333);
			Bukkit.getPluginManager().callEvent(e);
			if(!e.isCancelled()){
				b.breakNaturally();
				durability++;
			}
		}
		
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(item == null) return;
		Damageable dmg = (Damageable)item.getItemMeta();
		dmg.setDamage(dmg.getDamage()+durability);
		if(dmg.getDamage() >= item.getType().getMaxDurability()) {
			item.setAmount(0);
	        event.getPlayer().playSound(
	        		event.getPlayer().getLocation(),
	                Sound.ENTITY_ITEM_BREAK, 1F, 1F);
		}
		item.setItemMeta((ItemMeta)dmg);
		
	}
	
	private void getConnectedBlocks(Set<Block> blocks, Material mat, Block origin){
		BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
		blocks.add(origin);
		for(BlockFace face : faces) {
			Block block = origin.getRelative(face);
			if(!blocks.contains(block) && block.getType() == mat && blocks.size() < maxLumberSize) {
				getConnectedBlocks(blocks, mat, block);
			}
		}
	}
}
























