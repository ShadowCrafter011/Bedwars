package org.shadowcrafter.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.data.BwPlayer;

public class PlayerJoinQuitListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Bedwars.getPlugin().addPlayer(e.getPlayer(), new BwPlayer(e.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Bedwars.getPlugin().removePlayer(e.getPlayer());
	}

}
