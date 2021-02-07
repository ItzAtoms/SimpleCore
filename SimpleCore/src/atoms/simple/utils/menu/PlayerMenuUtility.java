package atoms.simple.utils.menu;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {
	private Player owner;

	public PlayerMenuUtility(Player owner) {
		setOwner(owner);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
