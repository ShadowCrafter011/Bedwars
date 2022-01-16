package org.shadowcrafter.bedwars.lobby;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
		
		Location wheat = new Location(w, -18, 70, 39);
		if (w.getNearbyEntities(wheat, 2.5, 2.5, 2.5).contains(p)) {
			p.sendBlockChange(wheat, new Location(w, -17, 70, 45).getBlock().getBlockData());
		}
		
		BlockData diamond = w.getBlockAt(0, 231, -2).getBlockData();
		if (p.getLocation().getY() >= 230) {
			for (int x = (int) p.getLocation().getX() - 3; x < (int) p.getLocation().getX() + 3; x++) {
				for (int z = (int) p.getLocation().getZ() - 3; z < (int) p.getLocation().getZ() + 3; z++) {
					if (w.getBlockAt(x, 229, z).getType() == Material.WATER) {
						p.sendBlockChange(new Location(w, x, 229, z), diamond);
					}
				}
			}
		}
		
		if (w.getBlockAt(p.getLocation()).getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
			if (w.getBlockAt(new Location(w, p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ())).getType() == Material.DIAMOND_BLOCK) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 300, 0));
			}
			return;
		}
		
		if (!bp.isCanCheckpoint()) return;
		
		if (w.getBlockAt(new Location(w, p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ())).getType() == Material.GOLD_BLOCK) {
			p.sendTitle("§aStarted the parkour", null, 10, 20, 10);
			bp.startNewTimer();
			bp.addCheckPointTimer();
		}else if (w.getBlockAt(new Location(w, p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ())).getType() == Material.DIAMOND_BLOCK) {
			bp.endTimer(true);
			bp.addCheckPointTimer();
		}else {
			p.sendTitle("§6Checkpoint!", null, 10, 20, 10);
			bp.setCheckpoint(p.getLocation());
			bp.addCheckPointTimer();
		}
	}

}
