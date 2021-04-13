package me.buby.vanillaenhanced.listeners.slotassist;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SlotAssistListener implements Listener{
	
	private Plugin plugin;
	
	public SlotAssistListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void placeBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemInHand().clone();
		Inventory inv = player.getInventory();
		int slot = player.getInventory().getHeldItemSlot();
		
		if(item.getAmount() == 1 && hasSameItem(item, player.getInventory())) {
			new BukkitRunnable() {
				@Override
				public void run() {

					int fromSlot = getSlotOfSame(item, player.getInventory(), slot);
					if(fromSlot == -Integer.MAX_VALUE) return;
					
					inv.setItem(slot, player.getInventory().getItem(fromSlot).clone());
					
					player.getInventory().setItem(fromSlot, null);
				}
			}.runTaskLater(plugin, 1);
		}
	}
	
	private int getSlotOfSame(ItemStack item, Inventory inv, int curSlot) {
		for(int i = 0; i < inv.getSize()-1; i++) {
			if(i == curSlot) continue;
			if(inv.getItem(i) != null && inv.getItem(i).isSimilar(item))
				return i;
		}
		return -Integer.MAX_VALUE;
	}
	
	private boolean hasSameItem(ItemStack item, Inventory inv) {
		for(ItemStack i : inv.getContents()) {
			if(i == null) continue;
			if(i.isSimilar(item))
				return true;
		}
		return false;
	}
	
}
