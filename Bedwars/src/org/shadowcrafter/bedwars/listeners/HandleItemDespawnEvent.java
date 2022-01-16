package org.shadowcrafter.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

public class HandleItemDespawnEvent implements Listener {
	
	@EventHandler
	public void onDespawn(ItemDespawnEvent e) {
		if (e.getEntity().getWorld().getName().startsWith("Bedwars/Temp")) {
			e.setCancelled(true);
		}
	}

}
