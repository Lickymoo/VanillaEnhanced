package me.buby.vanillaenhanced.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.buby.devlib.util.Colour;

@CommandAlias("ve|vanillaenhanced")
public class VanillaEnhancedCommand extends BaseCommand{
	
	private final Plugin plugin;
	
	public VanillaEnhancedCommand(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Default
	public void execute(final CommandSender sender) {
		sendMessage(sender,
				"&7----------------------------------",
				"&d&lVanilla Enhanced",
				"&7Developed by: &dBuby",
				"&7Version: &d" + plugin.getDescription().getVersion(),
				"&7https://github.com/Lickymoo",
				"&7----------------------------------"
				);
	}
	
	private void sendMessage(final CommandSender sender, final String... str) {
		for(String msg : str) {
			sender.sendMessage(Colour.parse(msg));
		}
	}
	
}



