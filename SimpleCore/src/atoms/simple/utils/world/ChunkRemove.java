package atoms.simple.utils.world;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.struct.Relation;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import atoms.simple.Core;

public class ChunkRemove implements Listener {

	private Core plugin;

	private final Set<Chunk> activeChunks = new HashSet<>();

	private final WorldGuardPlugin worldguardPlugin;

	public ChunkRemove(Core plugin) {
		this.plugin = plugin;
		Plugin wg = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		this.worldguardPlugin = (wg instanceof WorldGuardPlugin) ? (WorldGuardPlugin) wg : null;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlace(final BlockPlaceEvent event) {
		if (!event.getItemInHand().hasItemMeta())
			return;
		ItemMeta itemMeta = event.getItemInHand().getItemMeta();
		if (!itemMeta.hasLore())
			return;
		List<String> lore = itemMeta.getLore();
		if (!((String) lore.get(lore.size() - 1))
				.equalsIgnoreCase(this.plugin.getChunkRemoveUtils().getLoreIdentifier()))
			return;
		if (this.plugin.getChunkRemoveUtils().isLockdown()) {
			event.getPlayer().sendMessage(this.plugin.getChunkRemoveUtils().getMessageDenyLockdown());
			event.setCancelled(true);
			return;
		}
		if (this.plugin.getChunkRemoveUtils().getWorldBlackList().stream()
				.anyMatch(s -> s.equalsIgnoreCase(event.getBlock().getWorld().getName()))) {
			event.getPlayer().sendMessage(this.plugin.getChunkRemoveUtils().getMessageDenyWorldBlacklist());
			event.setCancelled(true);
			return;
		}
		final Chunk chunk = event.getBlock().getChunk();
		int chunkX = chunk.getX() * 16 + 8;
		int chunkZ = chunk.getZ() * 16 + 8;
		if (this.activeChunks.contains(chunk)) {
			event.getPlayer().sendMessage(this.plugin.getChunkRemoveUtils().getMessageAlready());
			event.setCancelled(true);
			return;
		}
		final Player player = event.getPlayer();
		FPlayer factionsPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
		Faction blockFaction = Board.getInstance().getFactionAt(new FLocation(event.getBlock().getLocation()));
		Relation relation = blockFaction.getRelationTo((RelationParticipator) factionsPlayer);
		if ((!this.plugin.getChunkRemoveUtils().isAllowAlly() && relation == Relation.ALLY)
				|| (!this.plugin.getChunkRemoveUtils().isAllowEnemy() && relation == Relation.ENEMY)
				|| (!this.plugin.getChunkRemoveUtils().isAllowNeutral() && blockFaction.isNormal()
						&& relation == Relation.NEUTRAL)
				|| (!this.plugin.getChunkRemoveUtils().isAllowOwn() && relation == Relation.MEMBER)
				|| (!this.plugin.getChunkRemoveUtils().isAllowTruce() && relation == Relation.TRUCE)
				|| (!this.plugin.getChunkRemoveUtils().isAllowSafezone()
						&& Factions.getInstance().getSafeZone().equals(blockFaction))
				|| (!this.plugin.getChunkRemoveUtils().isAllowWarzone()
						&& Factions.getInstance().getWarZone().equals(blockFaction))
				|| (!this.plugin.getChunkRemoveUtils().isAllowWilderness()
						&& Factions.getInstance().getWilderness().equals(blockFaction))) {
			event.getPlayer().sendMessage(this.plugin.getChunkRemoveUtils().getMessageDeny());
			event.setCancelled(true);
			return;
		}
				
		this.activeChunks.add(chunk);
		final Set<ArmorStand> stands = new HashSet<>();
		final int startingHeight = event.getBlockPlaced().getY() - 1;
		final Queue<Block> blocks = new LinkedList<>();
		processBlocks(player, chunk, startingHeight, blocks::add);
		for (int y = startingHeight - startingHeight % 10; y >= 0; y -= 10) {
			Location location = new Location(chunk.getWorld(), chunkX, y, chunkZ);
			ArmorStand armorStand = (ArmorStand) location.getWorld().spawn(location, ArmorStand.class);
			armorStand.setCanPickupItems(false);
			armorStand.setCustomNameVisible(true);
			armorStand.setGravity(false);
			armorStand.setVisible(false);
			armorStand.setCustomName(this.plugin.getChunkRemoveUtils().pGlass());
			stands.add(armorStand);
		}
		this.plugin.getServer().getOnlinePlayers().stream()
				.filter(p -> (p.getWorld().equals(chunk.getWorld())
						&& Math.abs(p.getLocation().getBlockX() - chunkX) < 50
						&& Math.abs(p.getLocation().getBlockZ() - chunkZ) < 50))
				.forEach(p -> p.sendMessage(this.plugin.getChunkRemoveUtils().getMessageUse()));
		BukkitRunnable killBlock = new BukkitRunnable() {
			public void run() {
				event.getBlock().setType(Material.AIR);
			}
		};
		killBlock.runTaskLater((Plugin) this.plugin, 1L);
		final BukkitRunnable cleanup = new BukkitRunnable() {
			public void run() {
				ChunkRemove.this.processBlocks(player, chunk, startingHeight, block -> block.setType(Material.AIR));
				ChunkRemove.this.activeChunks.remove(chunk);
				stands.forEach(Entity::remove);
			}
		};
		final BukkitRunnable countdown = new BukkitRunnable() {
			private int seconds = 10;

			public void run() {
				for (ArmorStand armorStand : stands)
					armorStand.setCustomName(ChunkRemove.this.plugin.getChunkRemoveUtils().pGlassRemove() + this.seconds + "s");
				if (--this.seconds < 0) {
					cancel();
					cleanup.runTask((Plugin) ChunkRemove.this.plugin);
				}
			}
		};
		BukkitRunnable buster = new BukkitRunnable() {
			public void run() {
				for (int x = 0; x < 300; x++) {
					Block block = blocks.poll();
					if (block == null) {
						cancel();
						countdown.runTaskTimer((Plugin) ChunkRemove.this.plugin, 1L, 20L);
						break;
					}
					block.setType((block.getY() % 10 == 0) ? Material.GLASS : Material.AIR);
				}
			}
		};
		buster.runTaskTimer((Plugin) this.plugin, 1L, 10L);
	}

	private void processBlocks(Player player, Chunk chunk, int startingHeight, Consumer<Block> consumer) {
		for (int y = startingHeight; y >= 0; y--) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Block block = chunk.getBlock(x, y, z);
					if (this.worldguardPlugin == null || this.worldguardPlugin.canBuild(player, block))
						if (block.getType() != Material.BEDROCK)
							consumer.accept(block);
				}
			}
		}
	}
}
