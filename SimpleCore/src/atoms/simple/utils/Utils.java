package atoms.simple.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import atoms.simple.Core;

public class Utils {
	
	Core plugin;
	
	public Utils(Core plugin) {
		this.plugin = plugin;
	}
	
	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static ItemStack itemBuilder(Material mat, String name, List<String>lore) {
		ItemStack s = new ItemStack(mat);
		ItemMeta meta = s.getItemMeta();
		meta.setDisplayName(color(name));
		List<String> l = new ArrayList<>();
		for(String a : lore) {
			l.add(color(a));
		}
		meta.setLore(l);
		s.setItemMeta(meta);
		return s;
		
	}
}
