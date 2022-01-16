package org.shadowcrafter.bedwars.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.shadowcrafter.bedwars.Bedwars;

import lombok.Data;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

@Data
public class Team {
	
	public Team() {
		players = new ArrayList<>();
		playerLimit = 4;
		hasBed = true;
		teamUpgrades = new ArrayList<>();
		upgrades = new HashMap<>();
		generatorSpeed = 20;
		goldLimit = 16;
		ironLimit = 48;
	}
	
	public boolean addPlayer(Player p) {
		if (players.size() < playerLimit) {
			players.add(p);
			return true;
		}
		return false;
	}
	
	public String[] getPlayerNames() {
		String[] output = new String[playerLimit + 1];
		output[0] = "  §aPlayers: " + players.size() + "/" + playerLimit;
		for (int i = 1; i < playerLimit + 1; i++) {
			try {
				output[i] = "  §a - " + players.get(i - 1).getName(); 
			}catch (IndexOutOfBoundsException e) {
				output[i] = "  §a - ";
			}
		}
		return output;
	}
	
	public void start() {
		players.forEach((p) -> {
			Bedwars.getPlugin().getPlayer(p).endTimer(false);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("").create());
			p.setGameMode(GameMode.SURVIVAL);
			p.teleport(spawn);
		});
	}
	
	private int playerLimit;
	private List<Player> players;
	private boolean hasBed;
	private Location spawn;
	private Location generator;
	private int generatorSpeed;
	private int goldLimit;
	private int ironLimit;
	private Location teamChest;
	private Entity upgradeShop;
	private Entity shop;
	private List<String> teamUpgrades;
	private HashMap<Player, List<String>> upgrades;

}
