package org.shadowcrafter.bedwars.lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.shadowcrafter.bedwars.Bedwars;

public class HandlePlayerDamageEvent implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			if (!p.getWorld().getName().equals("bedwars/lobby")) return;
			
			if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
			
			if (p.getLocation().getY() >= 230) return;
			
			if (e.getDamage() >= p.getHealth()) {
				e.setCancelled(true);
				p.setHealth(20);
				p.teleport(Bedwars.getPlugin().getPlayer(p).getCheckpoint());
				Bedwars.getPlugin().getPlayer(p).addCheckPointTimer();
				p.setFireTicks(0);
			}
		}
	}

}
