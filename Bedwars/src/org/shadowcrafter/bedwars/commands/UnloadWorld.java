package org.shadowcrafter.bedwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnloadWorld extends CommandUtils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		if (args.length != 0) return endCommand(sender, "§cPlease use §5/unloadworld");
		
		if (p.hasPermission("bedwars.unloadworld") == false) return endCommand(sender, "§cSorry, but you don't have enough permissions to do that");
		
		World w = p.getWorld();
		
		for (Player player : w.getPlayers()) {
			player.teleport(Bukkit.getWorld("world").getSpawnLocation());
			player.sendMessage("§aYou have been evacuated from §3" + w.getName() + "§a because it is about to get unloaded");
		}
		
		boolean success = Bukkit.unloadWorld(w.getName(), true);
		
		if (success) {
			return endCommand(sender, "§aWorld §3" + w.getName() + " §ahas been successfully unloaded");
		}
		return endCommand(sender, "§cSomething went wrong while unloading world " + w.getName());

	}

}
