package org.shadowcrafter.bedwars.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.shadowcrafter.bedwars.Bedwars;

import lombok.Data;
import lombok.Getter;

@Data
public class BwGame {
	
	@Getter
	private World map;
	private Map<Player, PlayerState> players;
	
	public BwGame(World map, Player p) {
		this.players = new HashMap<>();
		
		this.map = map;
		this.players.put(p, PlayerState.PLAYING);
	}
	
	public void start() {
		players.keySet().forEach((Player player) -> {
			player.teleport(map.getSpawnLocation());
			Bedwars.getPlugin().getPlayer(player).endTimer();
		});
	}
	
	public void end() {
		for (Player p : map.getPlayers()) {
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			p.sendMessage("§aYou have been evacuated from the world §3" + map.getName() + " §abecause it is about to get unloaded");
		}
		Bukkit.unloadWorld(map.getName(), true);
		Bedwars.getPlugin().removeGame(this);
	}
	
	public boolean isPlayerHere(Player p) {
		return players.containsKey(p);
	}

}
