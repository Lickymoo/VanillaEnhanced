package me.buby.vanillaenhanced.customitems.impl;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.buby.devlib.customitems.ItemManager;
import me.buby.devlib.customitems.util.CustomItem;

public class VeryUsefulStick extends CustomItem{

	public VeryUsefulStick(ItemManager manager) {
		super(manager, "A_VERY_USEFUL_STICK", Material.STICK);
		name("&dA Very Useful Stick");
		lore("&7This seems to be a very useful stick.");
	}

	@EventHandler
	public void onInteractEvent(PlayerInteractEvent event) {
		if(!isApplicable(event.getItem())) return;
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	
		System.out.println("clicked");
	}
}
