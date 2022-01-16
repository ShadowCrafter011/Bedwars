package org.shadowcrafter.bedwars.inventories;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.shadowcrafter.bedwars.util.StringUtils;

public class InventoryUtils extends StringUtils {

	public static Inventory fillInventoryWith(Inventory inv, ItemStack item, boolean count) {
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, count ? new ItemBuilder(item).setAmount(i).build() : item);
		}
		return inv;
	}
	
	public static Inventory fillSpecificInventorySlotsWith(Inventory inv, ItemStack item, int[] slots) {
		for (int i : slots) {
			inv.setItem(i, item);
		}
		return inv;
	}
	
	public static Inventory fillBorders(Inventory inv, ItemStack item) {
		int size = inv.getSize();

		int rowCount = size / 9;

		for (int index = 0; index < size; index++) {
		    int row = index / 9;
		    int column = (index % 9) + 1;

		    if(row == 0 || row == rowCount-1 || column == 1 || column == 9)
		        inv.setItem(index, item);
		}
		return inv;
	}
	
}
