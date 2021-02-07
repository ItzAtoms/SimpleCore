package atoms.simple.utils.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import atoms.simple.Core;


public class DataManager {

	private Core plugin;
	public FileConfiguration dataConfig = null;
	private File configFile = null;

	public DataManager(Core plugin) {
		this.plugin = plugin;
		saveDefaultConfig();
	}

	public void reloadConfig() {
		if (this.configFile == null)
			this.configFile = new File(this.plugin.getDataFolder(), "config.yml");

		this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

		InputStream defaultStream = this.plugin.getResource("config.yml");
		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			this.dataConfig.setDefaults(defaultConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (this.dataConfig == null)
			reloadConfig();
		return this.dataConfig;
	}

	public void saveConfig() {
		if (this.dataConfig == null || this.configFile == null)
			return;
		try {
			getConfig().save(this.configFile);
		} catch (IOException e) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
		}
	}

	public void saveDefaultConfig() {
		if (this.configFile == null)
			this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
		if (!this.configFile.exists())
			this.plugin.saveResource("config.yml", false);
	}
}
