package org.shadowcrafter.bedwars.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;

public class CoordUtils {
	
	public Location linesToLocation(World w, String[] lines) {
		double x = 0;
		double y = 0;
		double z = 0;
		
		float yaw = 0;
		float pitch = 0;
		
		if (lines[2] != null && lines[2].equals("") == false) {
			
			try {
				x = Double.parseDouble(lines[2].split(" ")[0]);
				y = Double.parseDouble(lines[2].split(" ")[1]);
				z = Double.parseDouble(lines[2].split(" ")[2]);
			}catch (NumberFormatException e) {
				return null;
			}
			
		}
		
		if (lines[3] != null && lines[3].equals("") == false) {
			
			try {
				yaw = Float.parseFloat(lines[3].split(" ")[0]);
				pitch = Float.parseFloat(lines[3].split(" ")[1]);
			}catch (NumberFormatException e) {
				return new Location(w, x, y, z);
			}
			
		}
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	int count = 0;
	public int countEntitiesAt(World w, Location loc, int radius, EntityType filter) {
		count = 0;
		
		w.getNearbyEntities(loc, radius, radius, radius).forEach((e) -> {
			if (e.getType() == filter) count++;
		});
		
		return count;
	}
	
	public int countItemsAt(World w, Location loc, int radius, Material filter) {
		count = 0;
		
		w.getNearbyEntities(loc, radius, radius, radius).forEach((e) -> {
			if (e.getType() == EntityType.DROPPED_ITEM && ((Item) e).getItemStack().getType() == filter) count += ((Item) e).getItemStack().getAmount();
		});
		
		return count;
	}

}
