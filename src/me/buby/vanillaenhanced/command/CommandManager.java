package me.buby.vanillaenhanced.command;

import org.bukkit.plugin.Plugin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import lombok.Getter;
import me.buby.vanillaenhanced.VanillaEnhanced;
import me.buby.vanillaenhanced.command.impl.GiveItemCommand;
import me.buby.vanillaenhanced.command.impl.VanillaEnhancedCommand;

public class CommandManager {

	@Getter public static BukkitCommandManager instance;
	
	
	public CommandManager(Plugin plugin) {
		instance = new BukkitCommandManager(plugin);
		
		registerCommand(
			new GiveItemCommand(((VanillaEnhanced)plugin).getItemManager()),
			new VanillaEnhancedCommand(plugin)
		);
	}
	
	private void registerCommand(BaseCommand... cmds) {
		for(BaseCommand cmd : cmds){
			instance.registerCommand(cmd);
		}
	}
	
}




















