package org.shadowcrafter.bedwars.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.util.CoordUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
@SuppressWarnings("unused")
public class BwGame extends CoordUtils {
	
	@Getter
	private World map;
	private String mapName;
	private Map<Player, PlayerState> players;
	
	private List<Location> breakableBlocks;
	
	private List<Location> diamondGens;
	private List<Location> emeraldGens;
	
	private int diamondLimit = 4;
	private int emeraldLimit = 2;
	
	private int emeraldSpeed = 1200;
	private int diamondSpeed = 600;
	
	private Map<Teams, Team> teams;
	
	private List<Integer> tasks;
	
	private boolean started;
	
	public BwGame(World map, String name) {
		this.players = new HashMap<>();
		this.teams = new HashMap<>();
		this.diamondGens = new ArrayList<>();
		this.emeraldGens = new ArrayList<>();
		this.tasks = new ArrayList<>();
		this.breakableBlocks = new ArrayList<>();
		
		this.mapName = name;
		this.map = map;
		
		Arrays.stream(Teams.values()).forEach((t) -> {
			teams.put(t, new Team(t));
		});
	}
	
	public Team getTeam(Teams color) {
		return teams.get(color);
	}
	
	public Team getTeam(Player p) {
		for (Team t : teams.values()) {
			if (t.getPlayers().contains(p)) return t;
		}
		return null;
	}
	
	public int changeTeam(Player p, Teams team) {
		
		if (teams.get(team).getPlayers().contains(p)) return 2;
		
		boolean success = teams.get(team).addPlayer(p);
		
		if (!success) return 1;
		
		teams.values().forEach((t) -> {
			if (t.getPlayers().contains(p) && t.equals(teams.get(team)) == false) t.getPlayers().remove(p);
		});
		
		players.put(p, PlayerState.PLAYING);
		
		return 0;
	}
	
	public void setSpectating(Player p) {
		teams.values().forEach((t) -> {
			if (t.getPlayers().contains(p)) t.getPlayers().remove(p);
		});
		
		players.put(p, PlayerState.SPECTATING);
	}
	
	public void loadLocations() {
		CommandBlock cb = (CommandBlock) map.getBlockAt(new Location(map, 0, 0, 0)).getState();
		
		String data = cb.getCommand();
		String n = cb.getCommand().split("#")[0];
		String signLocs = cb.getCommand().split("#")[1];
		String[] signs = signLocs.split(":");
		
		new Location(map, 0, 0, 0).getBlock().setType(Material.AIR);
		
		Arrays.stream(signs).forEach((s) -> {
			//System.out.println(s);
			double[] locs = new double[3];
			for (int i = 0; i < locs.length; i++) locs[i] = Double.parseDouble(s.split(" ")[i]);
			Location loc = new Location(map, locs[0], locs[1], locs[2]);
			Sign sign = (Sign) map.getBlockAt(loc).getState();
			
			String[] lines = sign.getLines();
				
			lines[0] = lines[0].toLowerCase();
			switch (lines[0]) {
			
			case "bed":
				Block start = loc.getBlock();
				BlockFace face = BlockFace.valueOf(lines[2].toUpperCase());
				Teams c = Teams.valueOf(lines[1].toUpperCase());
				
				int count = 0;
				for (Bed.Part part : Bed.Part.values()) {
					if (lines[1].equalsIgnoreCase("aqua")) {
						start.setType(Material.LIGHT_BLUE_BED);
					}else {
						start.setType(Material.valueOf(lines[1].toUpperCase() + "_BED"));
					}
					
					teams.get(c).getBedLocation()[count] = start.getLocation();
					count++;
					
					Bed bedState = (Bed) start.getBlockData();
					bedState.setPart(part);
					bedState.setFacing(face);
					start.setBlockData(bedState);
					start = start.getRelative(face.getOppositeFace());
				}
				break;
				
			case "spawn":
				Teams color = Teams.valueOf(lines[1].toUpperCase());
				
				teams.get(color).setSpawn(linesToLocation(map, lines));
				loc.getBlock().setType(Material.AIR);
				break;
			
			case "isg":
				Teams color1 = Teams.valueOf(lines[1].toUpperCase());
				teams.get(color1).setGenerator(linesToLocation(map, lines));
				loc.getBlock().setType(Material.AIR);
				break;
				
			case "chest":
				teams.get(Teams.valueOf(lines[1].toUpperCase())).setTeamChest(loc);
				
				BlockFace face2 = BlockFace.valueOf(lines[2].toUpperCase());
				loc.getBlock().setType(Material.CHEST);
				Chest chestD = (Chest) loc.getBlock().getBlockData();
				chestD.setFacing(face2);
				loc.getBlock().setBlockData(chestD);
				break;
				
			case "enderchest":
				BlockFace face3 = BlockFace.valueOf(lines[2].toUpperCase());
				loc.getBlock().setType(Material.ENDER_CHEST);
				EnderChest ecd = (EnderChest) loc.getBlock().getBlockData();
				ecd.setFacing(face3);
				loc.getBlock().setBlockData(ecd);
				break;
				
			case "shop":				
				Villager e = (Villager) map.spawnEntity(linesToLocation(map, lines), EntityType.VILLAGER);
				e.setCustomNameVisible(true);
				Teams teams = Teams.valueOf(lines[1].toUpperCase());
				e.setAI(false);
				e.setCustomName("§aShop");
				e.setInvulnerable(true);
				e.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 4, false, false, false));
				this.teams.get(teams).setShop(e);
				loc.getBlock().setType(Material.AIR);
				break;
				
			case "upgrades":				
				Villager e1 = (Villager) map.spawnEntity(linesToLocation(map, lines), EntityType.VILLAGER);
				e1.setCustomNameVisible(true);
				e1.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 4, false, false, false));
				Teams teams1 = Teams.valueOf(lines[1].toUpperCase());
				e1.setAI(false);
				e1.setCustomName("§aUpgrades");
				e1.setInvulnerable(true);
				this.teams.get(teams1).setUpgradeShop(e1);
				loc.getBlock().setType(Material.AIR);
				break;
				
			case "diamondg":
				diamondGens.add(linesToLocation(map, lines));
				loc.getBlock().setType(Material.AIR);
				break;
				
			case "emeraldg":
				emeraldGens.add(linesToLocation(map, lines));
				loc.getBlock().setType(Material.AIR);
				break;
				
			default: break;
			}
				
		});
	}
	
	public void addPlayer(Player p, Teams team) {
		players.put(p, PlayerState.PLAYING);
		teams.get(team).addPlayer(p);
	}
	
	public void start() {
		teams.values().forEach((t) -> {t.start();});
		
		players.entrySet().forEach((e) -> {
			if (e.getValue() == PlayerState.SPECTATING) {
				e.getKey().teleport(new Location(map, 0, 120, 0));
				
				e.getKey().setAllowFlight(true);
				e.getKey().setFlying(true);
				e.getKey().setGameMode(GameMode.ADVENTURE);
				e.getKey().setInvulnerable(true);
				
				map.getPlayers().forEach((p) -> {p.hidePlayer(Bedwars.getPlugin(), e.getKey());});
			}
		});
		
		this.started = true;
		
		tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getPlugin(), new Runnable() {
			
			int count = 1;
			
			@Override
			public void run() {
				
				//Drop diamonds
				if (count % diamondSpeed == 0) {
					for (Location l : diamondGens) {
						if (l != null && countItemsAt(map, l, 6, Material.DIAMOND) < diamondLimit) {
							Item i = map.dropItem(l, new ItemStack(Material.DIAMOND));
							i.setVelocity(i.getVelocity().zero());
						}
					}
				}
				
				//Drop emeralds
				if (count % emeraldSpeed == 0) {
					for (Location l : emeraldGens) {
						if (l != null && countItemsAt(map, l, 6, Material.EMERALD) < emeraldLimit) {
							Item i = map.dropItem(l, new ItemStack(Material.EMERALD));
							i.setVelocity(i.getVelocity().zero());
						}
					}
				}
				
				for (Team t : teams.values()) {
					if (t.getGenerator() != null) {
						
						//Drop iron
						if (count % t.getGeneratorSpeed() == 0 && countItemsAt(map, t.getGenerator(), 3, Material.IRON_INGOT) < t.getIronLimit()) {
							Item i = map.dropItem(t.getGenerator(), new ItemStack(Material.IRON_INGOT));
							i.setVelocity(i.getVelocity().zero());
						}
						
						//Drop gold
						if (count % (t.getGeneratorSpeed() * 5) == 0 && countItemsAt(map, t.getGenerator(), 3, Material.GOLD_INGOT) < t.getGoldLimit()) {
							Item i = map.dropItem(t.getGenerator(), new ItemStack(Material.GOLD_INGOT));
							i.setVelocity(i.getVelocity().zero());
						}
						
					}
				}	
				count++;
			}
		}, 0, 1));
		
	}
	
	public void forceEnd() {
		for (Player p : map.getPlayers()) {
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			p.sendMessage("§aYou have been evacuated from the world §3" + map.getName() + " §abecause it is about to get unloaded");
		}
		
		tasks.forEach((t) -> {
			Bukkit.getScheduler().cancelTask(t);
		});
		
		Bukkit.unloadWorld(map.getName(), true);
		Bedwars.getPlugin().removeGame(this);
	}
	
	public boolean isPlayerHere(Player p) {
		return players.containsKey(p);
	}

}
