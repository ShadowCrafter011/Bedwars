package org.shadowcrafter.bedwars.inventories;

import java.util.Arrays;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.shadowcrafter.bedwars.data.BwGame;
import org.shadowcrafter.bedwars.data.PlayerState;
import org.shadowcrafter.bedwars.data.Teams;

public class TeamSelectorInventory extends InventoryUtils {
	
	Inventory inv;
	
	public TeamSelectorInventory(BwGame g, Teams... teams) {
		inv = Bukkit.createInventory(null, 9, "§2Team selector");
		
		Arrays.stream(teams).forEach((t) -> {
			Material mat = t == Teams.AQUA ? Material.LIGHT_BLUE_WOOL : Material.valueOf(t.toString() + "_WOOL");			
			ChatColor c = t == Teams.LIME || t == Teams.PINK ? (t == Teams.LIME ? ChatColor.GREEN : ChatColor.LIGHT_PURPLE) : ChatColor.valueOf(t.toString());
			
			ItemBuilder item = new ItemBuilder(mat).setName(c + "§lChange to the " + t.toString().toLowerCase() + " team");
			
			for (String str :  g.getTeams().get(t).getPlayerNames()) {
				item.addLoreLine(str);
			}
			
			inv.addItem(item.build());
		});
		
		ItemBuilder specs = new ItemBuilder(Material.COMPASS).setName("§a§lBecome a spectator").setLore("§a  Spectators: ");
		
		for (Entry<Player, PlayerState> p : g.getPlayers().entrySet()) {
			if (p.getValue() == PlayerState.SPECTATING) {
				specs.addLoreLine("§a   - " + p.getKey().getName());
			}
		}
		
		inv.setItem(8, specs.build());
	}
	
	public Inventory build() {
		return inv;
	}

}
