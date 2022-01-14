package org.shadowcrafter.bedwars.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpToWorld extends CommandUtils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		if (args.length != 1) return endCommand(sender, "§cPlease use §5/tptoworld <path>");
		
		if (p.hasPermission("bedwars.tptoworld") == false) return endCommand(sender, "§cSorry, but you don't have enough permissions to do that");
		
		if (Bukkit.getWorld(args[0]) == null && new File(args[0]).exists()) {
			p.sendMessage("§aWorld has been found but isn't loaded yet. Please stand by");
			Bukkit.createWorld(new WorldCreator(args[0]));
		}
		
		if (Bukkit.getWorld(args[0]) != null) {
			p.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
			return endCommand(sender, "§aTeleported you to the world §3" + args[0]);
		}
		return endCommand(sender, "§cWorld " + args[0] + " doesn't exist");
		
	}

}
