package me.buby.vanillaenhanced.command;

import org.bukkit.plugin.Plugin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.buby.vanillaenhanced.VanillaEnhanced;
import me.buby.vanillaenhanced.command.impl.GiveItemCommand;

public class CommandManager {

	@Getter public static PaperCommandManager instance;
	
	
	public CommandManager(Plugin plugin) {
		instance = new PaperCommandManager(plugin);
		
		registerCommand(
			new GiveItemCommand(((VanillaEnhanced)plugin).getItemManager())
		);
	}
	
	private void registerCommand(BaseCommand... cmds) {
		for(BaseCommand cmd : cmds){
			instance.registerCommand(cmd);
		}
	}
	
}




















