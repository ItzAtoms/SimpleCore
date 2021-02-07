package atoms.simple;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import atoms.simple.utils.Utils;
import atoms.simple.utils.config.DataManager;
import atoms.simple.utils.menu.MenuListener;
import atoms.simple.utils.menu.PlayerMenuUtility;
import atoms.simple.utils.menu.menus.master.MenuMasterCommand;
import atoms.simple.utils.world.ChunkRemove;
import atoms.simple.utils.world.ChunkRemoveCommand;
import atoms.simple.utils.world.ChunkRemoveUtils;
import net.milkbowl.vault.economy.Economy;

public class Core extends JavaPlugin {
	private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

	private static Utils u;
		
	public static DataManager config;
	
	public static ChunkRemoveUtils cru;

	public static Economy eco = null;
	@Override
	public void onEnable() {
		config = new DataManager(this);
		cru = new ChunkRemoveUtils(this);
		cru.loadBusterSettings();
		setupEconomy();
		commands();
		listeners();
	}

	@Override
	public void onDisable() {

	}

	void commands() {
		
		getCommand("chunk").setExecutor(new ChunkRemoveCommand(this));
		getCommand("master").setExecutor(new MenuMasterCommand());
	}

	void listeners() {
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvents(new  ChunkRemove(this), (Plugin)this);
		
		pm.registerEvents(new MenuListener(), this);
		
		
		
	}
	public static PlayerMenuUtility getPlayerUtility(Player p) {
		if (playerMenuUtilityMap.containsKey(p))
			return playerMenuUtilityMap.get(p);
		PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(p);
		playerMenuUtilityMap.put(p, playerMenuUtility);
		return playerMenuUtility;
	}

	public Utils getUtils() {
		return u;
	}

	public ChunkRemoveUtils getChunkRemoveUtils() {
		return cru;
	}
	
	/*
	 * 
	 * 
	 * 
	 *      /////////////////////                 //////////////////             //////////////////
	 *      /									//                             //                  //
	 *      /									//                            ||                   ||
	 *      /									//							  ||				   ||
	 *      /                                   //                            ||				   ||
	 *      /                                   //                            ||				   ||
	 *      /                                   //                            ||                   ||
	 *      //////////////////////              //                            ||                   ||
	 *      /                                   //                            ||                   ||
	 *      /                                   //                            ||                   ||
	 *      /                                   //                            ||                   ||
	 *      /                                   //                            ||                   ||
	 *      /                                   //                            ||                   ||
	 *      /                                   //							  ||                   ||
	 *      /                                   //                            ||                   ||
	 *      //////////////////////               ////////////////////           ///////////////////
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static Economy getEco() {
		return eco;
	}
	
	private boolean setupEconomy() {
	    if (getServer().getPluginManager().getPlugin("Vault") == null)
	        return false; 
	      RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	      if (rsp == null)
	        return false; 
	      eco = (Economy)rsp.getProvider();
	      return (eco != null);
	}
}
