package org.shadowcrafter.bedwars.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.Data;

@Data
public class Team {
	
	public Team() {
		players = new ArrayList<>();
		hasBed = true;
		teamUpgrades = new ArrayList<>();
		upgrades = new HashMap<>();		
	}
	
	private List<Player> players;
	private boolean hasBed;
	private Location spawn;
	private BlockFace facing;
	private Location generator;
	private Location teamChest;
	private Entity upgradeShop;
	private Entity shop;
	private List<String> teamUpgrades;
	private HashMap<Player, List<String>> upgrades;

}
