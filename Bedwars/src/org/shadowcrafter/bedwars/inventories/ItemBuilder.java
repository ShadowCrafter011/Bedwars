package org.shadowcrafter.bedwars.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	private ItemStack item;
	private ItemMeta meta;
	
	public ItemBuilder(ItemStack item) {
		this.item = item;
		meta = item.getItemMeta();
	}
	
	public ItemBuilder(Material mat) {
		item = new ItemStack(mat);
		meta = item.getItemMeta();
	}
	
	public ItemBuilder(Material mat, int amount) {
		item = new ItemStack(mat, amount);
		meta = item.getItemMeta();
	}
	
	public ItemBuilder setName(String name) {
		meta.setDisplayName(name);
		return this;
	}
	
	public ItemBuilder addEnchant(Enchantment enchantment, int level) {
		meta.addEnchant(enchantment, level, false);
		return this;
	}
	
	public ItemBuilder hideEnchants() {
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}
	
	public ItemBuilder setLore(String... lore) {
		meta.setLore(Arrays.asList(lore));
		return this;
	}
	
	public ItemBuilder setLore(List<String> lore) {
		meta.setLore(lore);
		return this;
	}
	
	public ItemBuilder removeLoreLine(String line){
		   List<String> lore = new ArrayList<>(meta.getLore());
		   if(!lore.contains(line))return this;
		   lore.remove(line);
		   meta.setLore(lore);
		   return this;
	   }

	public ItemBuilder removeLoreLine(int index){
		   List<String> lore = new ArrayList<>(meta.getLore());
		   if(index<0||index>lore.size())return this;
		   lore.remove(index);
		   meta.setLore(lore);
		   return this;
	   }

	   public ItemBuilder addLoreLine(String line){
		   List<String> lore = new ArrayList<>();
		   if(meta.hasLore())lore = new ArrayList<>(meta.getLore());
		   lore.add(line);
		   meta.setLore(lore);
		   return this;
	   }

	   public ItemBuilder setLoreLine(String line, int position){
		   List<String> lore = new ArrayList<>(meta.getLore());
		   lore.set(position, line);
		   meta.setLore(lore);
		   return this;
	   }
	   
	   public ItemBuilder setAmount(int amount) {
		   item.setAmount(amount);
		   return this;
	   }
	
	   public ItemStack build() {
		   item.setItemMeta(meta);
		   return item;
	   }
	
}
