package org.shadowcrafter.bedwars.lobby;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.shadowcrafter.bedwars.Bedwars;
import org.shadowcrafter.bedwars.data.BwPlayer;

public class HandlePlayerMoveEvent implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerMoveEvent e) {
		World w = e.getPlayer().getWorld();
		Player p = e.getPlayer();
		BwPlayer bp = Bedwars.getPlugin().getPlayer(p);
		
		if (!w.getName().equals("bedwars/lobby")) return;
		
		if (p.getLocation().getY() < 52) {
			try {
				p.teleport(bp.getCheckpoint());
				bp.addCheckPointTimer();
			}catch (IllegalArgumentException x) {
				p.teleport(new Location(Bedwars.getPlugin().getLobby(), -0.5d, 52d, -50.5d));
				bp.addCheckPointTimer();
			}
		}
		
		if (w.getBlockAt(p.getLocation()).getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE) return;
		
		if (!bp.isCanCheckpoint()) return;
		
		if (w.getBlockAt(new Location(w, p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ())).getType() == Material.GOLD_BLOCK) {
			p.sendTitle("§aStarted the parkour", null, 10, 20, 10);
			bp.startNewTimer();
			bp.addCheckPointTimer();
		}else if (w.getBlockAt(new Location(w, p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ())).getType() == Material.DIAMOND_BLOCK) {
			bp.endTimer();
			bp.addCheckPointTimer();
		}else {
			p.sendTitle("§6Checkpoint!", null, 10, 20, 10);
			bp.setCheckpoint(p.getLocation());
			bp.addCheckPointTimer();
		}
	}

}
