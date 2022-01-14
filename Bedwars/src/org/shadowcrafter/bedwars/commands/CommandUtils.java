package org.shadowcrafter.bedwars.commands;

import org.bukkit.command.CommandSender;

public class CommandUtils {
	
	public boolean endCommand(CommandSender sender, String message) {
		sender.sendMessage(message);
		return true;
	}
	
	String[] commons = {"§cSorry, but only players may use this command",
						"§cSorry, but you don't have enough permissions to do that"};
	
	public boolean endCommand(CommandSender sender, int common) {
		if (commons.length > common && common >= 0) {
			sender.sendMessage(commons[common]);
		}
		return true;
	}

}
