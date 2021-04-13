package me.buby.vanillaenhanced.customitems.impl;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import me.buby.devlib.customitems.ItemManager;
import me.buby.devlib.customitems.util.CustomItem;
import me.buby.devlib.customitems.util.interfaces.ShapedCraftableItem;
import me.buby.devlib.util.Colour;

public class BigBucket extends CustomItem implements ShapedCraftableItem{

	public BigBucket(ItemManager manager) {
		super(manager, "BIG_BUCKET", Material.BUCKET);
		name("&fEmpty Big Bucket");
		lore("&7This bucket seems very deep");
		persistentData("placeMode", "false");
		persistentData("bucketType", "EMPTY");
		persistentData("level", "0");
	}

	@Override
	public ShapedRecipe recipe() {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(manager.getPlugin(), getId()), this.getItemStack());
		recipe.shape("***", "*c*", "***");
		recipe.setIngredient('c', Material.BUCKET);
		recipe.setIngredient('*', Material.IRON_BLOCK);
		return recipe;
	}
	
	private enum BucketType{
		WATER("&fBig Bucket of Water (%s/"+BucketInstance.maxLevel+")", Material.WATER_BUCKET), 
		LAVA("&fBig Bucket of Lava (%s/"+BucketInstance.maxLevel+")", Material.LAVA_BUCKET), 
		EMPTY("&fEmpty Big Bucket", Material.BUCKET);
		
		@Getter private final String name;
		@Getter private final Material displayMat;
		
		BucketType(String name, Material displayMat){
			this.name = name;
			this.displayMat = displayMat;
		}
		
		String displayName(Object... args) {
			if(this.getName().contains("%s")) {
				return String.format(this.getName(), args);
			}else {
				return this.getName();
			}
		}
	}
	
	private class BucketInstance{
		static final int maxLevel = 20;
		
		ItemStack item;
		
		boolean placeMode;
		BucketType type;
		int level;
		
		BucketInstance(ItemStack item){
			this.item = item;
			
			placeMode = Boolean.valueOf(getPersistentData(item, "placeMode"));
			type = BucketType.valueOf(getPersistentData(item, "bucketType"));
			level = Integer.parseInt(getPersistentData(item, "level"));		
		
		}
		
		void save(){
			if(level == 0)
				type = BucketType.EMPTY;
			if(level == maxLevel)
				placeMode = true;
			if(type == BucketType.EMPTY)
				placeMode = false;
			
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Colour.parse(type.displayName(level)));

			item.setType(placeMode ? type.displayMat : Material.BUCKET);
			item.setItemMeta(meta);
			
			setPersistentData(item, "placeMode", Boolean.toString(placeMode));
			setPersistentData(item, "bucketType", type.toString());
			setPersistentData(item, "level", level + "");
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void playerFillBucketEvent(PlayerBucketFillEvent event) {

		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(!isApplicable(item)) return;
		if(event.isCancelled()) return;
		BucketInstance bucket = new BucketInstance(item);
		event.setCancelled(true);
		
		Material mat = event.getItemStack().getType() == Material.WATER_BUCKET ? Material.WATER : Material.LAVA;
		switch(bucket.type) {
			case EMPTY:
				if(mat == Material.LAVA || mat == Material.WATER) {
					bucket.type = BucketType.valueOf(mat.toString());
					bucket.level++;
				}
				break;
			case LAVA:
				if(mat == Material.WATER) return;
				if(bucket.level == BucketInstance.maxLevel) return;
				bucket.level++;
				break;
			case WATER:
				if(mat == Material.LAVA) return;
				if(bucket.level == BucketInstance.maxLevel) return;
				bucket.level++;
				break;
		}
		event.getBlock().setType(Material.AIR);
		bucket.save();
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInteractEvent(PlayerInteractEvent event) {
		if(!isApplicable(event.getItem())) return;
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		BucketInstance bucket = new BucketInstance(item);
		
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(player.isSneaking()) {
				bucket.placeMode = !bucket.placeMode;
				
				bucket.save();
				event.getPlayer().updateInventory();
				
			}
		}else if(event.getAction() == Action.RIGHT_CLICK_BLOCK && bucket.placeMode) {
			if(event.isCancelled()) return;
			BlockFace face = event.getBlockFace();
			switch(bucket.type) {
				case EMPTY:
					return;
				case LAVA:
				case WATER:
					event.setCancelled(true);
					event.getClickedBlock().getRelative(face).setType(Material.valueOf(bucket.type.toString()));
					bucket.level--;
					break;
			}
			
			bucket.save();
			event.getPlayer().updateInventory();
		}
	}
}


































