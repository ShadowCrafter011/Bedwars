package org.shadowcrafter.bedwars.lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HandlePlayerFoodLevelChangeEvent implements Listener {
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if (!e.getEntity().getWorld().getName().equals("bedwars/lobby")) return;
		e.getEntity().setFoodLevel(40);
		e.setCancelled(true);
	}

}
