package org.shadowcrafter.bedwars.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.data.BwGame;
import org.shadowcrafter.bedwars.inventories.ItemBuilder;
import org.shadowcrafter.bedwars.util.Util;

public class BedwarsCommand extends CommandUtils implements CommandExecutor {

	Bedwars plugin = Bedwars.getPlugin();
	
	String name = "";
	
	World map = null;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player == false) return endCommand(sender, "§cSorry, but only players may use this command");
		Player p = (Player) sender;
		
		File temp = plugin.getTempMaps();
		int mapNum = plugin.getMapNum();
		List<String> mapPaths = plugin.getMapPaths();
		
		File templateMap = null;
		
		map = null;
		name = "";
		
		//Choosing map
		if (args.length == 0) {
			Random rand = new Random();
			int index = rand.nextInt(mapNum);
			
			templateMap = new File(mapPaths.get(index));
			name = mapPaths.get(index).replaceAll("Bedwars/Maps/", "");
		}else if (args.length == 1) {
			if (!mapPaths.contains("Bedwars/Maps/" + args[0])) return endCommand(sender, "§cNo map found");
			
			templateMap = new File("Bedwars/Maps/" + args[0]);
			name = args[0];
		}
		
		//Checking if game is already starting with this map
		plugin.getGames().forEach((g) -> {
			if (g.getMapName().equals(name) && g.isStarted() == false) {
				g.addPlayer(p, new Util().getRandomTeam());
				map = g.getMap();
			}
		});
		
		if (map == null) {
			//Loading map
			UUID mapUUID = UUID.randomUUID();
			File tempMap = new File(temp.getPath() + "/" + mapUUID);
			
			if (tempMap.exists()) return endCommand(sender, "§cA very rare error occured please try again");
			
			tempMap.mkdir();
			
			//Copying map
			try {
				FileUtils.copyDirectory(templateMap, tempMap);
				
				File uid = new File(tempMap.getPath() + "/uid.dat");
				boolean success = uid.delete();
				if (!success) return endCommand(sender, "§cSomething went wrong while copying the map");
			} catch (IOException e) {
				endCommand(sender, "§cSomething went wrong while copying the map");
			}
			
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(new Location(plugin.getLobby(), -0.5d, 52d, -50.5d));
			p.getInventory().setItem(0, new ItemBuilder(Material.NOTE_BLOCK).setName("§2Team selector").setLore(" ", "§3[Right click]").build());
			
			//Loading map
			BwGame game = new BwGame(map, name);
			game.addPlayer(p, new Util().getRandomTeam());
			plugin.addGame(game);
			map = Bukkit.createWorld(new WorldCreator(tempMap.getPath().replaceAll("\\\\", "/")));
			game.setMap(map);
			game.loadLocations();

			return true;
		}
		
		p.setGameMode(GameMode.ADVENTURE);
		p.teleport(new Location(plugin.getLobby(), -0.5d, 52d, -50.5d));
		p.getInventory().setItem(0, new ItemBuilder(Material.NOTE_BLOCK).setName("§2Team selector").setLore(" ", "§3[Right click]").build());
		
		return true;
	}

}
