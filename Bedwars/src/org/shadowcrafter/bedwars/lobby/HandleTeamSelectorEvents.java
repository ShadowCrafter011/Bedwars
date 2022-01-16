package org.shadowcrafter.bedwars.lobby;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.data.Teams;
import org.shadowcrafter.bedwars.inventories.TeamSelectorInventory;

public class HandleTeamSelectorEvents implements Listener {
	
	HashMap<Player, Integer> tasks = new HashMap<>();
	
	Bedwars plugin = Bedwars.getPlugin();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!e.getPlayer().getWorld().getName().equals("bedwars/lobby")) return;
		if (e.getItem() == null) return;
		if (e.getItem().getType() != Material.NOTE_BLOCK) return;
		e.setCancelled(true);
		if (plugin.getGameWith(e.getPlayer()).getMap() == null) {
			e.getPlayer().sendMessage("§cThe map needs to load before you do that");
			return;
		}
		
		if (tasks.containsKey(e.getPlayer())) return;
		
		e.getPlayer().openInventory(new TeamSelectorInventory(plugin.getGameWith(e.getPlayer()), Teams.values()).build());
		tasks.put(e.getPlayer(), Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Inventory updated = new TeamSelectorInventory(plugin.getGameWith(e.getPlayer()), Teams.values()).build();
				e.getPlayer().getOpenInventory().getTopInventory().setContents(updated.getContents());
			}
		}, 0, 20));
		
	}
	
	@EventHandler
	public void onThrow(PlayerDropItemEvent e) {
		if (!e.getPlayer().getWorld().getName().equals("bedwars/lobby")) return;
		if (e.getItemDrop() == null) return;
		if (e.getItemDrop().getItemStack().getType() != Material.NOTE_BLOCK) return;
		e.setCancelled(true);
		if (plugin.getGameWith(e.getPlayer()).getMap() == null) {
			e.getPlayer().sendMessage("§cThe map needs to load before you do that");
			return;
		}
		
		if (tasks.containsKey(e.getPlayer())) return;
		
		e.getPlayer().openInventory(new TeamSelectorInventory(plugin.getGameWith(e.getPlayer()), Teams.values()).build());
		tasks.put(e.getPlayer(), Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Inventory updated = new TeamSelectorInventory(plugin.getGameWith(e.getPlayer()), Teams.values()).build();
				e.getPlayer().getOpenInventory().getTopInventory().setContents(updated.getContents());
			}
		}, 0, 20));
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.getWhoClicked().getWorld().getName().equals("bedwars/lobby")) return;
		if (e.getCurrentItem() == null) return;
		if (e.getCurrentItem().getType() != Material.NOTE_BLOCK) return;
		if (plugin.getGameWith(e.getWhoClicked()).getMap() == null) {
			e.getWhoClicked().sendMessage("§cThe map needs to load before you do that");
			return;
		}
		e.setCancelled(true);
		
		if (tasks.containsKey(e.getWhoClicked())) return;
		
		e.getWhoClicked().openInventory(new TeamSelectorInventory(plugin.getGameWith(e.getWhoClicked()), Teams.values()).build());
		tasks.put((Player) e.getWhoClicked(), Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Inventory updated = new TeamSelectorInventory(plugin.getGameWith(e.getWhoClicked()), Teams.values()).build();
				e.getWhoClicked().getOpenInventory().getTopInventory().setContents(updated.getContents());
			}
		}, 0, 20));
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (tasks.containsKey(e.getPlayer())) {
			Bukkit.getScheduler().cancelTask(tasks.get(e.getPlayer()));
			tasks.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onInventoryClick2(InventoryClickEvent e) {
		if (!e.getWhoClicked().getWorld().getName().equals("bedwars/lobby")) return;
		if (e.getCurrentItem() == null) return;
		if (!e.getView().getTitle().equals("§2Team selector")) return;
		if (e.getView().getBottomInventory().equals(e.getClickedInventory())) return;
		
		e.setCancelled(true);
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getCurrentItem().getType() == Material.COMPASS) {
			plugin.getGameWith(p).setSpectating(p);
			p.sendMessage("§aYou are not spectating");
			
			Inventory updated = new TeamSelectorInventory(plugin.getGameWith(p), Teams.values()).build();
			p.getOpenInventory().getTopInventory().setContents(updated.getContents());
			return;
		}
		
		String teamToSwitchTo = e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[3];
		Teams t = Teams.valueOf(teamToSwitchTo.toUpperCase());
		
		int code = plugin.getGameWith(p).changeTeam(p, t);
		
		if (code == 0) {
			p.sendMessage("§aYou are now in team " + t.toString().toLowerCase());
			
			Inventory updated = new TeamSelectorInventory(plugin.getGameWith(p), Teams.values()).build();
			p.getOpenInventory().getTopInventory().setContents(updated.getContents());
		}else if (code == 1) {
			p.sendMessage("§cThis team is full");
		}else
			p.sendMessage("§cYou are already in this team");
	}

}
