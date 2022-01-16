package org.shadowcrafter.bedwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.data.BwGame;

public class StartGameCommand extends CommandUtils implements CommandExecutor {

	BwGame game = null;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player == false) return endCommand(sender, 0);
		Player p = (Player) sender;
		
		if (p.hasPermission("bedwars.start") == false) return endCommand(sender, 1);
		
		if (args.length != 0) return endCommand(sender, "§cPlease use §5/start");
		
		Bedwars.getPlugin().getGames().forEach((BwGame g) -> {
			if (g.isPlayerHere(p)) game = g;
		});
		
		if (game == null) return endCommand(sender, "§cYou are not currently in a pre-game lobby");
		
		if (game.getMap() == null) return endCommand(sender, "§cThe map is not fully loaded yet");
		
		game.start();
		return endCommand(sender, "§aGame successfully started");
	}
    
}
