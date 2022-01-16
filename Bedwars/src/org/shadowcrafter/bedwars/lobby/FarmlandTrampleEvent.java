package org.shadowcrafter.bedwars.lobby;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class FarmlandTrampleEvent implements Listener {
	
	@EventHandler
	public void onTrample(PlayerInteractEvent e) {
		if (e.getAction() != Action.PHYSICAL) return;
		if (e.getClickedBlock().getType() != Material.FARMLAND) return;
		if (!e.getPlayer().getWorld().getName().equals("bedwars/lobby")) return;
		e.setCancelled(true);
	}

}
