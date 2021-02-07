package atoms.simple.utils.world;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import atoms.simple.Core;
import atoms.simple.utils.Utils;

public class ChunkRemoveCommand implements CommandExecutor{
	
	private Core plugin;
	
	public ChunkRemoveCommand(Core plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length > 0 && "give".equalsIgnoreCase(args[0]) && sender.isOp()) {
			Player p;
			Player target;
			int amount = 1;
			if(sender instanceof Player) {
				
				p = (Player)sender;
				
				if(args.length == 3) {
				
					try {
						
						target = plugin.getServer().getPlayerExact(args[1]);
						try {
							
							amount = Integer.parseInt(args[2]);
							
						}catch(NumberFormatException nfe) {
							this.plugin.getUtils();
							p.sendMessage(Utils.color("&c&lHey! &cThat's not a number silly!"));
							return true;
						}
						
						ItemStack buster = new ItemStack(Material.ENDER_PORTAL_FRAME);
						ItemMeta meta = buster.getItemMeta();
						meta.setDisplayName(this.plugin.getChunkRemoveUtils().getItemName());
						p.sendMessage(this.plugin.getChunkRemoveUtils().getItemName());
						meta.setLore(this.plugin.getChunkRemoveUtils().getLore());
						buster.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
						meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						buster.setItemMeta(meta);
						buster.setAmount(amount);
						
						target.getInventory().addItem(buster);
						
						
					}catch(Exception e) {
						p.sendMessage(this.plugin.getChunkRemoveUtils().getMessageGiveTargetNeeded());
						return true;
					}
				}else {
					return true;
				}
				
			}else {sender.sendMessage("Can't do this from console sorry!");return true;}

		}else if(args.length > 0 && "lockdown".equalsIgnoreCase(args[0]) && sender.isOp()) {
			this.plugin.getChunkRemoveUtils().flipLockdown();
		}else {
			return true;
		}
		
		return true;
	}

}
