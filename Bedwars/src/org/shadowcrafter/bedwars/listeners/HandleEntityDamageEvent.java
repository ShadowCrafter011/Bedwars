package org.shadowcrafter.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.shadowcrafter.bedwars.Bedwars;

public class HandleEntityDamageEvent implements Listener {
	
	boolean cancel = true;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity().getWorld().getName().startsWith("Bedwars/Temp")) {
			
			cancel = true;
			
			Bedwars.getPlugin().getGames().forEach((g) -> {
				if (e.getEntity() instanceof Player && g.getPlayers().containsKey((Player) e.getEntity())) {
					cancel = false;
				}
			});
			
			e.setCancelled(cancel);
		}
	}

}
