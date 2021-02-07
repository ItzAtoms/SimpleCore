package atoms.simple.utils.menu;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;


public class MenuListener implements Listener {

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {

		if (e.getCurrentItem() == null
				|| (e.getCurrentItem().getType() == Material.AIR && e.getSlotType() == InventoryType.SlotType.OUTSIDE))
			return;

		InventoryHolder holder = e.getClickedInventory().getHolder();
		if (holder instanceof Menu) {
			e.setCancelled(true);
			Menu menu = (Menu) holder;
			menu.handleMenu(e);
		}

	}
}
