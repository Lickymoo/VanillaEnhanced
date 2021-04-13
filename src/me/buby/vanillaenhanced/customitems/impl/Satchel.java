package me.buby.vanillaenhanced.customitems.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.buby.devlib.customitems.ItemManager;
import me.buby.devlib.customitems.util.CustomItem;
import me.buby.devlib.customitems.util.interfaces.ShapedCraftableItem;
import me.buby.devlib.inventory.InventoryBase;
import me.buby.devlib.inventory.util.InventoryEvent;

public class Satchel extends CustomItem implements ShapedCraftableItem{

	public Satchel(ItemManager manager) {
		super(manager, "SATCHEL", Material.LEATHER);
		name("&fSatchel");
		persistentData("Content", "");
	}

	@Override
	public ShapedRecipe recipe() {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(manager.getPlugin(), getId()), this.getItemStack());
		recipe.shape("***", "*c*", "***");
		recipe.setIngredient('c', Material.CHEST);
		recipe.setIngredient('*', Material.LEATHER);
		return recipe;
	}
	
	@EventHandler
	public void onInteractEvent(PlayerInteractEvent event) {
		if(!isApplicable(event.getItem())) return;
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(getPersistentData(event.getItem(), "SatchelID").isEmpty())
			setPersistentData(event.getItem(), "SatchelID", UUID.randomUUID().toString());
		
		ItemStack[] contents = deserialize(this.getPersistentData(event.getItem(), "Content"));
		SatchelInventory satchelInv = new SatchelInventory(event.getItem(), contents);
		satchelInv.openInventory(event.getPlayer());
	}
	
	public class SatchelInventory extends InventoryBase {

		private ItemStack satchel;
		
		public SatchelInventory(ItemStack satchel, ItemStack[] contents) {
			super("&8Satchel", 27);
			this.satchel = satchel;
			this.setDefaultClickHandler(e -> {});
			
			int index = 0;
			for(ItemStack item : contents) {
				this.setItem(item, index);
				index++;
			}
		}
		
		@InventoryEvent
		public void inventoryClickEvent(InventoryClickEvent event) {
			ItemStack clickedItem = event.getCurrentItem();
			String id = getPersistentData(clickedItem, "SatchelID");
			String itemId = getPersistentData(clickedItem, CustomItem.CUSTOM_ITEM_ID_HEADER);
			
			if(itemId != null && !itemId.isEmpty())
			if(id.equals(getPersistentData(satchel, CustomItem.CUSTOM_ITEM_ID_HEADER))) {
				event.setCancelled(true);
			}
			
			if(id != null && !id.isEmpty())
			if(id.equals(getPersistentData(satchel, "SatchelID"))) {
				event.setCancelled(true);
			}
		}

		@InventoryEvent
		public void inventoryCloseEvent(InventoryCloseEvent event){
			ItemStack[] contents = event.getInventory().getContents();
			satchel = setPersistentData(satchel, "Content", serialize(contents));
		}
		
	}
	
	private String serialize(ItemStack[] obj) {    	
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        	BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        
        	dataOutput.writeInt(obj.length);
        
        	for (int i = 0; i < obj.length; i++) {
        		dataOutput.writeObject(obj[i]);
        	}
        
        	dataOutput.close();
        	return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	private ItemStack[] deserialize(String str) {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(str));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
    
            for (int i = 0; i < items.length; i++) {
            	items[i] = (ItemStack) dataInput.readObject();
            }
            
            dataInput.close();
            return items;
        } catch (Exception e) {
        	return new ItemStack[0];
        }
	}
}
