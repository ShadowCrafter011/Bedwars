package org.shadowcrafter.bedwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.data.BwGame;


public class EndGameCommand extends CommandUtils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		if (args.length > 0) return endCommand(sender, "§cPlease use only §5/endgame");
		
		if (p.hasPermission("bedwars.endgame") == false) return endCommand(sender, "§cSorry, but you don't have enough permissions to do this");
		
		for (BwGame game : Bedwars.getPlugin().getGames()) {
			if (game.isPlayerHere(p)) {
				game.forceEnd();
				return endCommand(sender, "§aEnded the game you are currently in and unloaded world §3" + game.getMap().getName());
			}
			
		}
		return endCommand(sender, "§cYou are not currently in a game");
		
	}

}
