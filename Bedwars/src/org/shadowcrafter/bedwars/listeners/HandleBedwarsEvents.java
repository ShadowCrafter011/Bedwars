package org.shadowcrafter.bedwars.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.data.BwGame;
import org.shadowcrafter.bedwars.data.PlayerState;
import org.shadowcrafter.bedwars.data.Teams;

public class HandleBedwarsEvents extends StringUtils implements Listener {
	
	Bedwars plugin = Bedwars.getPlugin();
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player == false) return;
		
		Player p = (Player) e.getEntity();
		
		if (plugin.getGameWith(p) == null) return;
		
		BwGame game = plugin.getGameWith(p);
		
		boolean died = false;
		
		if (e.getCause() == DamageCause.VOID) died = true;
		
		if (e.getDamage() >= p.getHealth()) died = true;
		
		if (died) {
			e.setCancelled(true);
			
			p.teleport(new Location(game.getMap(), 0, 120, 0));
			
			p.setGameMode(GameMode.ADVENTURE);
			p.setAllowFlight(true);
			p.setFlying(true);
			p.setInvulnerable(true);
			p.getInventory().clear();
			
			p.setHealth(20);
			
			if (game.getTeam(p).isHasBed()) {
				p.sendMessage("§aYou are getting respawned in 5 seconds");
				
				game.getTeam(p).respawnPlayerIn(p, 5);
			}else {
				p.sendMessage("§cYOU DIED");
				game.getPlayers().put(p, PlayerState.SPECTATING);
				
				boolean teamStillAlive = false;
				for (Player current : game.getTeam(p).getPlayers()) {
					if (game.getPlayers().get(current) == PlayerState.PLAYING) teamStillAlive = true;
				}
				if (!teamStillAlive) {
					for (Player current : game.getMap().getPlayers()) {
						current.sendMessage("§6Team " + capitalize(game.getTeam(p).getColor().toString().toLowerCase()) + " has been eliminated");
					}
				}
			}
			
			game.getMap().getPlayers().forEach((pl) -> pl.hidePlayer(Bedwars.getPlugin(), p));
		}
		
	}
	
	@EventHandler
	public void onEntityAttackEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player p = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			
			if (plugin.getGameWith(p) == null) return;
			
			if (damager.isInvulnerable() == false) return;
			
			if (damager.getGameMode() == GameMode.CREATIVE) return;
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if (plugin.getGameWith(e.getEntity()) == null) return;
		e.setCancelled(true);
		e.getEntity().setFoodLevel(40);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (!e.getBlock().getWorld().getName().startsWith("Bedwars/Temp")) return;
		
		plugin.getGameWith(e.getPlayer()).getBreakableBlocks().add(e.getBlock().getLocation());
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (!e.getBlock().getWorld().getName().startsWith("Bedwars/Temp")) return;
		
		if (!plugin.getGameWith(e.getPlayer()).getBreakableBlocks().contains(e.getBlock().getLocation())) {
			String[] name = e.getBlock().getType().toString().split("_");
			if (name[name.length - 1].equals("BED")) {
				e.setDropItems(false);
				
				BwGame game = plugin.getGameWith(e.getPlayer());
				
				if (name.length == 3) {
					
					if (game.getTeam(Teams.AQUA).getPlayers().contains(e.getPlayer())) {
						e.getPlayer().sendMessage("§cYou sadly aren't allowed to break your own bed ._.");
						e.setCancelled(true);
						return;
					}
					
					game.getTeam(Teams.AQUA).setHasBed(false);
					game.getPlayers().keySet().forEach((p) -> {
						p.sendMessage(ChatColor.AQUA + "Aqua Bed has beed destroyed!");
						game.getMap().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
					});
					return;
				}
				
				Teams color = Teams.valueOf(name[0]);
				
				if (game.getTeam(color).getPlayers().contains(e.getPlayer())) {
					e.getPlayer().sendMessage("§cYou sadly aren't allowed to break your own bed ._.");
					e.setCancelled(true);
					return;
				}
				
				game.getTeam(color).setHasBed(false);
				
				String message;
				
				if (color == Teams.PINK) {
					message = ChatColor.LIGHT_PURPLE + "Pink Bed has been destroyed!";
				}else if (color == Teams.LIME) {
					message = ChatColor.GREEN + "Lime Bed has beed destroyed!";
				}else {
					message = ChatColor.valueOf(name[0]) + capitalize(name[0].toLowerCase()) + " Bed has beed destroyed!";
				}
				
				for (Player p : game.getPlayers().keySet()) {
					p.sendMessage(message);
					game.getMap().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
				}
				
				return;
			}
			
			if (e.getBlock().getType() == Material.GRASS) return;
			
			if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
			e.getPlayer().sendMessage("§cYou may only break beds or blocks placed by players");
			e.setCancelled(true);
		}
	}

}
