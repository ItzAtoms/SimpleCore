package atoms.simple.utils.world;

import java.util.List;

import org.bukkit.ChatColor;

import com.google.common.collect.ImmutableList;

import atoms.simple.Core;

public class ChunkRemoveUtils {
	
	private Core plugin;
	
	
	public ChunkRemoveUtils(Core plugin) {
		this.plugin = plugin;

	}
	
	private boolean lockdown;

	private String itemName;

	private List<String> lore;
	
	private List<String> broadcast;

	private String loreIdentifier;

	private boolean allowAlly;

	private boolean allowEnemy;

	private boolean allowNeutral;

	private boolean allowOwn;

	private boolean allowSafezone;

	private boolean allowTruce;

	private boolean allowWarzone;

	private boolean allowWilderness;

	private String messageUse;

	private String messageDeny;

	private String messageDenyLockdown;

	private String messageDenyWorldBlackList;

	private String messageGiveTargetNeeded;

	private String messageAlready;
	
	private String messageDenyWorldBlacklist;
	
	private String messageLockdownOn;

	private String messageLockdownOff;

	private List<String> worldBlackList;

	public void loadBusterSettings() {

		this.itemName = loadString("config.chunkbuster.item.name", "&c&lDefault");
		this.loreIdentifier = loadString("config.chunkbuster.item.lore-identifier", "&c&lBe extremely careful where you put this.");
		this.lore = loadLore("config.chunkbuster.item.lore", new String[] {"&3An acient weapon","&3Destroys everything inside designated chunk(s)" });
		this.allowAlly = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.ally", false);
		this.allowEnemy = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.enemy", false);
		this.allowNeutral = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.neutral", false);
		this.allowOwn = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.own", true);
		this.allowSafezone = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.safezone", false);
		this.allowTruce = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.truce", false);
		this.allowWarzone = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.warzone", false);
		this.allowWilderness = Core.config.getConfig().getBoolean("config.chunkbuster.allowed.wilderness", true);
		this.messageAlready = loadString("config.chunkbuster.messages.already", "&cOnly one Bust a Chunk can be active in a chunk at once!");
	    this.messageDeny = loadString("config.chunkbuster.message.deny", "&cYou cannot place Bust a Chunk in this chunk!");
	    this.messageDenyLockdown = loadString("config.chunkbuster.message.deny-lockdown", "&cBust a Chunk is temporarily disabled!");
	    this.messageDenyWorldBlacklist = loadString("config.chunkbuster.message.deny-world-blacklist", "&cBust a Chunk disabled on this world!");
	    this.messageGiveTargetNeeded = loadString("config.chunkbuster.message.give-target-needed", "&cNeed a valid target to give to!");
	    this.messageLockdownOff = loadString("config.chunkbuster.messages.lockdown-off", "&bBust a Chunk now enabled!");
	    this.messageLockdownOn = loadString("config.chunkbuster.messages.lockdown-on", "&cBust a Chunk temporarily disabled!");
	    this.broadcast = loadLore("config.chunkbuster.messages.broadcast", new String[] {"&dDefault"});
	    this.messageUse = loadString("config.chunkbuster.messages.use", "&cTime to bust a chunk! Be careful! The glass layers may last only ten seconds!");
	    this.worldBlackList = Core.config.getConfig().getStringList("config.chunkbuster.world-blacklist");
	}

	private List<String> loadLore(String name, String[] defaultText) {
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		if (Core.config.getConfig().contains(name)) {
			for (String s : Core.config.getConfig().getStringList(name))
				builder.add(ChatColor.translateAlternateColorCodes('&', s));
		} else {
			for (String s : defaultText)
				builder.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		builder.add(this.loreIdentifier);
		return (List<String>) builder.build();
	}

	private String loadString(String name, String defaultText) {
		return ChatColor.translateAlternateColorCodes('&', Core.config.getConfig().getString(name, defaultText));
	}

	public boolean isLockdown() {
		return lockdown;
	}

	public void flipLockdown() {
		this.lockdown = !this.lockdown;
		this.plugin.getServer().broadcast(this.lockdown ? getMessageLockdownOn() : getMessageLockdownOff(), "Lockdown initiated");
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public String getLoreIdentifier() {
		return loreIdentifier;
	}

	public void setLoreIdentifier(String loreIdentifier) {
		this.loreIdentifier = loreIdentifier;
	}

	public boolean isAllowAlly() {
		return allowAlly;
	}

	public void setAllowAlly(boolean allowAlly) {
		this.allowAlly = allowAlly;
	}

	public boolean isAllowEnemy() {
		return allowEnemy;
	}

	public void setAllowEnemy(boolean allowEnemy) {
		this.allowEnemy = allowEnemy;
	}

	public boolean isAllowNeutral() {
		return allowNeutral;
	}

	public void setAllowNeutral(boolean allowNeutral) {
		this.allowNeutral = allowNeutral;
	}

	public boolean isAllowOwn() {
		return allowOwn;
	}

	public void setAllowOwn(boolean allowOwn) {
		this.allowOwn = allowOwn;
	}

	public boolean isAllowSafezone() {
		return allowSafezone;
	}

	public void setAllowSafezone(boolean allowSafezone) {
		this.allowSafezone = allowSafezone;
	}

	public boolean isAllowTruce() {
		return allowTruce;
	}

	public void setAllowTruce(boolean allowTruce) {
		this.allowTruce = allowTruce;
	}

	public boolean isAllowWarzone() {
		return allowWarzone;
	}

	public void setAllowWarzone(boolean allowWarzone) {
		this.allowWarzone = allowWarzone;
	}

	public boolean isAllowWilderness() {
		return allowWilderness;
	}

	public void setAllowWilderness(boolean allowWilderness) {
		this.allowWilderness = allowWilderness;
	}

	public String getMessageUse() {
		return messageUse;
	}

	public void setMessageUse(String messageUse) {
		this.messageUse = messageUse;
	}

	public String getMessageDeny() {
		return messageDeny;
	}

	public void setMessageDeny(String messageDeny) {
		this.messageDeny = messageDeny;
	}

	public String getMessageDenyLockdown() {
		return messageDenyLockdown;
	}

	public void setMessageDenyLockdown(String messageDenyLockdown) {
		this.messageDenyLockdown = messageDenyLockdown;
	}

	public String getMessageDenyWorldBlackList() {
		return messageDenyWorldBlackList;
	}

	public void setMessageDenyWorldBlackList(String messageDenyWorldBlackList) {
		this.messageDenyWorldBlackList = messageDenyWorldBlackList;
	}

	public String getMessageGiveTargetNeeded() {
		return messageGiveTargetNeeded;
	}

	public void setMessageGiveTargetNeeded(String messageGiveTargetNeeded) {
		this.messageGiveTargetNeeded = messageGiveTargetNeeded;
	}

	public String getMessageAlready() {
		return messageAlready;
	}

	public void setMessageAlready(String messageAlready) {
		this.messageAlready = messageAlready;
	}

	public String getMessageLockdownOn() {
		return messageLockdownOn;
	}

	public void setMessageLockdownOn(String messageLockdownOn) {
		this.messageLockdownOn = messageLockdownOn;
	}

	public String getMessageLockdownOff() {
		return messageLockdownOff;
	}

	public void setMessageLockdownOff(String messageLockdownOff) {
		this.messageLockdownOff = messageLockdownOff;
	}

	public List<String> getWorldBlackList() {
		return worldBlackList;
	}
	

	public void setWorldBlackList(List<String> worldBlackList) {
		this.worldBlackList = worldBlackList;
	}

	/**
	 * @return the messageDenyWorldBlacklist
	 */
	public String getMessageDenyWorldBlacklist() {
		return messageDenyWorldBlacklist;
	}

	/**
	 * @param messageDenyWorldBlacklist the messageDenyWorldBlacklist to set
	 */
	public void setMessageDenyWorldBlacklist(String messageDenyWorldBlacklist) {
		this.messageDenyWorldBlacklist = messageDenyWorldBlacklist;
	}


	public String pGlass() {
		return ChatColor.translateAlternateColorCodes('&', "&a&lProtective Glass Layer!");
	}
	
	public String pGlassRemove() {
		return ChatColor.translateAlternateColorCodes('&', "&c&lRemoving glass layers in &a");
	}

	/**
	 * @return the broadcast
	 */
	public List<String> getBroadcast() {
		return broadcast;
	}

	/**
	 * @param broadcast the broadcast to set
	 */
	public void setBroadcast(List<String> broadcast) {
		this.broadcast = broadcast;
	}
	
}
