package org.shadowcrafter.bedwars.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.shadowcrafter.bedwars.Bedwars;

import lombok.Data;
import lombok.Getter;

@Data
@SuppressWarnings("unused")
public class BwGame {
	
	@Getter
	private World map;
	private Map<Player, PlayerState> players;
	
	private List<Location> diamondGens;
	private List<Location> emeraldGens;
	
	private Map<Teams, Team> teams;
	
	public BwGame(World map, Player p) {
		this.players = new HashMap<>();
		this.teams = new HashMap<>();
		this.diamondGens = new ArrayList<>();
		this.emeraldGens = new ArrayList<>();
		
		this.map = map;
		this.players.put(p, PlayerState.PLAYING);
	}
	
	public void start() {
		players.keySet().forEach((Player player) -> {
			player.teleport(map.getSpawnLocation());
			Bedwars.getPlugin().getPlayer(player).endTimer();
		});
		
		Arrays.stream(Teams.values()).forEach((t) -> {
			teams.put(t, new Team());
		});
		
		CommandBlock cb = (CommandBlock) map.getBlockAt(new Location(map, 0, 0, 0)).getState();
		
		String data = cb.getCommand();
		String name = cb.getCommand().split("#")[0];
		String signLocs = cb.getCommand().split("#")[1];
		String[] signs = signLocs.split(":");
		
		cb.setType(Material.AIR);
		cb.update();
		
		Arrays.stream(signs).forEach((s) -> {
			int[] locs = new int[3];
			for (int i = 0; i < locs.length; i++) locs[i] = Integer.parseInt(s.split(" ")[i]);
			Location loc = new Location(map, locs[0], locs[1], locs[2]);
			Sign sign = (Sign) map.getBlockAt(loc).getState();
			
			String[] lines = sign.getLines();
				
			switch (lines[0]) {
			
			case "Bed":
				Block start = loc.getBlock();
				BlockFace face = BlockFace.valueOf(lines[2].toUpperCase());
				for (Bed.Part part : Bed.Part.values()) {
					start.setType(Material.valueOf(lines[1].toUpperCase() + "_BED"));
					Bed bedState = (Bed) start.getBlockData();
					bedState.setPart(part);
					bedState.setFacing(face);
					start.setBlockData(bedState);
					start = start.getRelative(face.getOppositeFace());
				}
				break;
				
			case "Spawn":
				Teams color = Teams.valueOf(lines[1].toUpperCase());
				BlockFace face1 = BlockFace.valueOf(lines[2].toUpperCase());
				
				teams.get(color).setFacing(face1);
				loc.getBlock().setType(Material.AIR);
				break;
			
			case "Isg":
				Teams color1 = Teams.valueOf(lines[1].toUpperCase());
				teams.get(color1).setGenerator(loc);
				loc.getBlock().setType(Material.AIR);
				break;
				
			case "Chest":
				teams.get(Teams.valueOf(lines[1].toUpperCase())).setTeamChest(loc);
				
				BlockFace face2 = BlockFace.valueOf(lines[2].toUpperCase());
				loc.getBlock().setType(Material.CHEST);
				Chest chestD = (Chest) loc.getBlock().getBlockData();
				chestD.setFacing(face2);
				loc.getBlock().setBlockData(chestD);
				break;
				
			case "Enderchest":
				BlockFace face3 = BlockFace.valueOf(lines[2].toUpperCase());
				loc.getBlock().setType(Material.ENDER_CHEST);
				EnderChest ecd = (EnderChest) loc.getBlock().getBlockData();
				ecd.setFacing(face3);
				loc.getBlock().setBlockData(ecd);
				break;
				
			case "Shop":
				
				double x = Double.parseDouble(lines[2].split(" ")[0]);
				double y = Double.parseDouble(lines[2].split(" ")[1]);
				double z = Double.parseDouble(lines[2].split(" ")[2]);
				
				float yaw = Float.parseFloat(lines[3].split(" ")[0]);
				float pitch = Float.parseFloat(lines[3].split(" ")[1]);
				
				Villager e = (Villager) map.spawnEntity(new Location(map, x, y, z, yaw, pitch), EntityType.VILLAGER);
				e.setAI(false);
				e.setCustomName("§aShop");
				e.setInvulnerable(true);
				loc.getBlock().setType(Material.AIR);
				break;
				
			case "Upgrades":
				
				double x1 = Double.parseDouble(lines[2].split(" ")[0]);
				double y1 = Double.parseDouble(lines[2].split(" ")[1]);
				double z1 = Double.parseDouble(lines[2].split(" ")[2]);
				
				float yaw1 = Float.parseFloat(lines[3].split(" ")[0]);
				float pitch1 = Float.parseFloat(lines[3].split(" ")[1]);
				
				Villager e1 = (Villager) map.spawnEntity(new Location(map, x1, y1, z1, yaw1, pitch1), EntityType.VILLAGER);
				e1.setAI(false);
				e1.setCustomName("§aUpgrades");
				e1.setInvulnerable(true);
				loc.getBlock().setType(Material.AIR);
				break;
				
			default: break;
			}
				
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
