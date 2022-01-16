package org.shadowcrafter.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;

public class HandleItemMergeEvent implements Listener {
	
	@EventHandler
	public void onMerge(ItemMergeEvent e) {
		e.setCancelled(true);
	}

}
