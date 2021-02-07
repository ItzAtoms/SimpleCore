package atoms.simple.utils.menu.menus.master;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import atoms.simple.Core;

public class MenuMasterCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player))
			return true;
		Player p = (Player)sender;
		if(!(p.isOp()))
			return true;
		
		new MenuMaster(Core.getPlayerUtility(p)).open();
		
		return true;
	}
}
