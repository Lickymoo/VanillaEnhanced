package me.buby.vanillaenhanced.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.buby.devlib.customitems.ItemManager;
import me.buby.devlib.customitems.util.CustomItem;

@CommandAlias("giveitem")
public class GiveItemCommand extends BaseCommand{
	
	private final ItemManager itemManager;
	
	public GiveItemCommand(ItemManager itemManager) {
		this.itemManager = itemManager;
	}
	
	@Default
	public void execute(final CommandSender sender, final String id, final int amount) {
		if(!(sender instanceof Player)) return;
		Player player = (Player)sender;
		
		CustomItem handler = itemManager.getHandle(id);
		if(handler == null) {
			player.sendMessage("Could not find item with id: " + id);
			return;
		}else {
			player.getInventory().addItem(handler.getItemStack(amount));
			return;
		}
		
	}
	
	@Default
	public void execute(final CommandSender sender, final String id) {
		execute(sender, id, 1);
		
	}
}





