package atoms.simple.utils.menu.menus.master;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import atoms.simple.utils.Utils;
import atoms.simple.utils.menu.Menu;
import atoms.simple.utils.menu.PlayerMenuUtility;

public class MenuMaster extends Menu{

	static Utils utils;
	public MenuMaster(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
	}

	@Override
	public String getMenuName() {
		// TODO Auto-generated method stub
		return "Admin Menu";
	}

	@Override
	public int getSlots() {
		// TODO Auto-generated method stub
		return 9;
	}

	@Override
	public void handleMenu(InventoryClickEvent paramInventoryCliCkEvent) {
		// TODO Auto-generated method stub
		switch(paramInventoryCliCkEvent.getCurrentItem().getType()) {
		case ENDER_CHEST:
			break;
		default:
			break;
		}
	}

	@Override
	public void setMenuItems() {
		inventory.addItem(Utils.itemBuilder(Material.ENDER_CHEST, "&c&lMonthly Crates", Arrays.asList("Enter the monthly crates menu", "")));
	}

}
