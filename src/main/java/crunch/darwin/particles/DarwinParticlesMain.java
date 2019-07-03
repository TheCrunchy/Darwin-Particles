package crunch.darwin.particles;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.inject.Inject;
import com.intellectualcrafters.plot.object.Plot;

import crunch.darwin.particles.commands.Commands;
import crunch.darwin.particles.events.MoveEvents;
import crunch.darwin.particles.events.PlotClearListener;
import crunch.darwin.particles.tasks.DoParticleTask;
import crunch.darwin.particles.tasks.LoadParticleTask;
import crunch.darwin.particles.tasks.trollTask;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "darwinparticles", name = "Darwin Particles", version = "1.0", description = "Allow players to spawn particles")
public class DarwinParticlesMain {
	@Inject
	@ConfigDir(sharedRoot = false)
	public Path root;
	
	public static Path staticRoots;
	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	public static SqlService sql;
	
	public static  HashMap<String, PlotParticles> allPlotsWithParticles = new HashMap<>();
	public static  HashMap<UUID, PlayerData> playerData = new HashMap<>();
	
	public static Text particlesDefault = Text.of(TextColors.LIGHT_PURPLE, "Particles - ");
	
	public static ArrayList<com.intellectualcrafters.plot.object.Location> plotsToLoadParticles = new ArrayList<>();
	public static ArrayList<com.intellectualcrafters.plot.object.Location> plotsTounLoadParticles = new ArrayList<>();
	
	public static ArrayList<Player> playersToTroll = new ArrayList<>();
	
	public static DatabaseStuff db;
	
	private HashMap<UUID, Long> playerCooldowns = new HashMap<UUID, Long>();
	
	@Listener
	public void onServerFinishLoad(GameStartedServerEvent event) throws SQLException {
		//bad code, probably not necessary but i fucking hate making the root work for a database
		staticRoots = root;
		RootSingleton.getInstance().setRoot(staticRoots);
		db = new DatabaseStuff(sql);
		Sponge.getCommandManager().register(this, makeTest, "particleplacer", "pap");
		Sponge.getEventManager().registerListeners(this, new doRightClick());
		Sponge.getEventManager().registerListeners(this, new MoveEvents());
		Sponge.getEventManager().registerListeners(this, new PlotClearListener());
		Task.builder().execute(new DoParticleTask())
				.interval(1, TimeUnit.SECONDS)
				.name("spawnParticles").submit(this);
		Task.builder().execute(new trollTask())
				.interval(3, TimeUnit.SECONDS)
				.name("spawnParticles").submit(this);
		Task.builder().execute(new LoadParticleTask())
				.interval(5, TimeUnit.SECONDS)
				.async()
				.name("loadParticles").submit(this);
	}
	private CommandSpec changeParticle = CommandSpec.builder()
			.description(Text.of("change particle type with command"))
			.permission("pp.user")
			.arguments(GenericArguments.integer(Text.of("quantity")),
					GenericArguments.optional(GenericArguments.string(Text.of("particle effect"))),
					GenericArguments.optional(GenericArguments.longNum(Text.of("interval"))))
			.executor(new Commands.ChangeParticle())
			.build();
	private CommandSpec makeParticle = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.user")
			.executor(new Commands.MakeParticle())
			.build();
	private CommandSpec getStick = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.user")
			.executor(new Commands.getStick())
			.build();
	private CommandSpec showParticlesInChunk = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.user")
			.executor(new Commands.getParticlesInChunk())
			.build();
	private CommandSpec teleportToParticle = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.user")
			.arguments(GenericArguments.doubleNum(Text.of("x")), GenericArguments.doubleNum(Text.of("y")), GenericArguments.doubleNum(Text.of("z")) )
			.executor(new Commands.teleportToParticle())
			.build();
	private CommandSpec deleteParticles = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.user")
			.arguments(GenericArguments.doubleNum(Text.of("x")), GenericArguments.doubleNum(Text.of("y")), GenericArguments.doubleNum(Text.of("z")) )
			.executor(new Commands.deleteParticle())
			.build();
	private CommandSpec addToTroll = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.admin")
			.arguments(GenericArguments.player(Text.of("target")))
			.executor(new Commands.addToTroll())
			.build();
	private CommandSpec removeFromTroll = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.admin")
			.arguments(GenericArguments.player(Text.of("target")))
			.executor(new Commands.removeFromTroll())
			.build();
	private CommandSpec openGUI = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.permission("pp.user")
			.executor(new Commands.openGUI())
			.build();
	private CommandSpec makeTest = CommandSpec.builder()
			.description(Text.of("Toggle main command"))
			.child(makeParticle, "make")
			.child(changeParticle, "change")
			.child(deleteParticles, "delete")
			.child(getStick, "get")
			.child(teleportToParticle, "teleport")
			.child(showParticlesInChunk, "show")
			.child(addToTroll, "addTarget")
			.child(removeFromTroll, "removeTarget")
			.child(openGUI, "gui")
			.build();

	public static ItemStack makePPStick() {
		ItemStack ppStick = ItemStack.builder()
				.itemType(ItemTypes.BLAZE_ROD).build();
		ppStick.offer(Keys.DISPLAY_NAME, Text.of(TextColors.YELLOW, "My PP Stick"));
		List<Text> itemLore = new ArrayList<Text>();
		itemLore.add(Text.of(TextColors.BLUE, "Your very own PP Stick made by Crunch"));
		ppStick.offer(Keys.ITEM_LORE, itemLore);
		return ppStick;
	}

	//might move all database stuff in this class to the database class
	public static DataSource getDataSource(String jdbcUrl) throws SQLException {
		if (sql == null) {
			sql = Sponge.getServiceManager().provide(SqlService.class).get();
		}
		return sql.getDataSource(jdbcUrl);
	}

	//should probably get rid of this method eventually
	public static void addNewParticle(Location loc, Player player) {
		db.addNewParticle(loc, player);
	}
	
	public class doRightClick {
		@Listener
		public void onBlockClick(InteractBlockEvent.Secondary.MainHand event, @First Player player) {
			if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(makePPStick())){		
				@SuppressWarnings("unchecked")
				Location loc;
				if (playerCooldowns.containsKey(player.getUniqueId())) {
					if (!((playerCooldowns.get(player.getUniqueId()) + 1 ) >= (System.currentTimeMillis() / 1000))) {
				if (player.getLocation().getExtent() != null && event.getInteractionPoint().isPresent()) {
					loc = new Location(player.getLocation().getExtent(), event.getInteractionPoint().get());
				}
				else {
					loc = player.getLocation();
				}
				addNewParticle(loc, player);
				playerCooldowns.put(player.getUniqueId(), (System.currentTimeMillis() / 1000));
				}

			   }
				else {
					if (player.getLocation().getExtent() != null && event.getInteractionPoint().isPresent()) {
						loc = new Location(player.getLocation().getExtent(), event.getInteractionPoint().get());
					}
					else {
						loc = player.getLocation();
					}
					addNewParticle(loc, player);
					playerCooldowns.put(player.getUniqueId(), (System.currentTimeMillis() / 1000));
				}
			}
		}
	}
	public static void spawnParticlesAtVector3(Player player, ParticleEffect effect, Vector3d Vector3Location) {
		player.spawnParticles(effect, Vector3Location);
	}

}
