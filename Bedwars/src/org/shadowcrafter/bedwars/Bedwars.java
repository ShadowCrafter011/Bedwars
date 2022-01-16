package org.shadowcrafter.bedwars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shadowcrafter.bedwars.commands.BedwarsCommand;
import org.shadowcrafter.bedwars.commands.EndGameCommand;
import org.shadowcrafter.bedwars.commands.StartGameCommand;
import org.shadowcrafter.bedwars.commands.TpToWorld;
import org.shadowcrafter.bedwars.commands.UnloadWorld;
import org.shadowcrafter.bedwars.data.BwGame;
import org.shadowcrafter.bedwars.data.BwPlayer;
import org.shadowcrafter.bedwars.listeners.HandleEntityDamageEvent;
import org.shadowcrafter.bedwars.listeners.HandleItemDespawnEvent;
import org.shadowcrafter.bedwars.listeners.HandleItemMergeEvent;
import org.shadowcrafter.bedwars.listeners.PlayerJoinQuitListener;
import org.shadowcrafter.bedwars.lobby.FarmlandTrampleEvent;
import org.shadowcrafter.bedwars.lobby.HandlePlayerDamageEvent;
import org.shadowcrafter.bedwars.lobby.HandlePlayerFoodLevelChangeEvent;
import org.shadowcrafter.bedwars.lobby.HandleTeamSelectorEvents;
import org.shadowcrafter.bedwars.lobby.HandlePlayerMoveEvent;

import lombok.Getter;

public class Bedwars extends JavaPlugin {
	
	@Getter
	private static Bedwars plugin;
	
	@Getter
	private File tempMaps = new File("Bedwars/Temp");
	@Getter
	private File mapFolder = new File("Bedwars/Maps");
	@Getter
	private int mapNum;
	@Getter
	private List<String> mapPaths;
	@Getter
	private List<BwGame> games;
	@Getter
	private Map<Player, BwPlayer> players;
	@Getter
	private World lobby;
	
	public void onEnable() {
		plugin = this;
		
		if (!mapFolder.exists()) mapFolder.mkdir();
		if (!tempMaps.exists()) tempMaps.mkdir();
		
		PluginManager pl = Bukkit.getPluginManager();
		
		pl.registerEvents(new HandlePlayerMoveEvent(), plugin);
		pl.registerEvents(new PlayerJoinQuitListener(), plugin);
		pl.registerEvents(new HandlePlayerDamageEvent(), plugin);
		pl.registerEvents(new HandlePlayerFoodLevelChangeEvent(), plugin);
		pl.registerEvents(new HandleItemMergeEvent(), plugin);
		pl.registerEvents(new FarmlandTrampleEvent(), plugin);
		pl.registerEvents(new HandleItemDespawnEvent(), plugin);
		pl.registerEvents(new HandleEntityDamageEvent(), plugin);
		pl.registerEvents(new HandleTeamSelectorEvents(), plugin);
		
		getCommand("tptoworld").setExecutor(new TpToWorld());
		getCommand("bedwars").setExecutor(new BedwarsCommand());
		getCommand("endgame").setExecutor(new EndGameCommand());
		getCommand("unloadworld").setExecutor(new UnloadWorld());
		getCommand("start").setExecutor(new StartGameCommand());
		
		mapNum = mapFolder.listFiles().length;
		
		mapPaths = new ArrayList<>();
		for (File f : mapFolder.listFiles()) {
			mapPaths.add(f.getPath().replaceAll("\\\\", "/"));
		}
		
		games = new ArrayList<>();
		
		players = new HashMap<>();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			addPlayer(p, new BwPlayer(p));
		}
		
		lobby = Bukkit.createWorld(new WorldCreator("bedwars/lobby"));
	}
	
	public void removeGame(BwGame game) {
		games.remove(game);
	}
	
	public void addGame(BwGame game) {
		games.add(game);
	}
	
	public BwGame getGameWith(Player p) {
		for (BwGame g : games) {
			if (g.getPlayers().containsKey(p)) return g;
		}
		return null;
	}
	
	public BwGame getGameWith(HumanEntity p) {
		for (BwGame g : games) {
			if (g.getPlayers().containsKey((Player) p)) return g;
		}
		return null;
	}
	
	public void addPlayer(Player p, BwPlayer bp) {
		players.put(p, bp);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
	}
	
	public BwPlayer getPlayer(Player p) {
		return players.get(p);
	}

}
