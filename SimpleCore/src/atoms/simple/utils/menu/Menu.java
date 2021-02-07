package atoms.simple.utils.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


public abstract class Menu implements InventoryHolder {

	protected Inventory inventory;

	protected PlayerMenuUtility playerMenuUtiity;

	public Menu(PlayerMenuUtility playerMenuUtility) {
		this.playerMenuUtiity = playerMenuUtility;
	}

	public abstract String getMenuName();

	public abstract int getSlots();

	public abstract void handleMenu(InventoryClickEvent paramInventoryCliCkEvent);

	public abstract void setMenuItems();

	public void open() {
		this.inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
		setMenuItems();
		this.playerMenuUtiity.getOwner().openInventory(this.inventory);
	}

	public Inventory getInventory() {
		return this.inventory;
	}
}
