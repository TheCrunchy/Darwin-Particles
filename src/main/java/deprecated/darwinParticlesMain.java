package deprecated;
//package Darwin.Particles.deprecated;
//
//import java.nio.file.Path;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map.Entry;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import javax.sql.DataSource;
//
//import org.spongepowered.api.Sponge;
//import org.spongepowered.api.command.CommandException;
//import org.spongepowered.api.command.CommandResult;
//import org.spongepowered.api.command.CommandSource;
//import org.spongepowered.api.command.args.CommandContext;
//import org.spongepowered.api.command.args.GenericArguments;
//import org.spongepowered.api.command.spec.CommandExecutor;
//import org.spongepowered.api.command.spec.CommandSpec;
//import org.spongepowered.api.config.ConfigDir;
//import org.spongepowered.api.config.DefaultConfig;
//import org.spongepowered.api.data.key.Keys;
//import org.spongepowered.api.data.type.HandTypes;
//import org.spongepowered.api.effect.particle.ParticleEffect;
//import org.spongepowered.api.effect.particle.ParticleTypes;
//import org.spongepowered.api.entity.living.player.Player;
//import org.spongepowered.api.event.Listener;
//import org.spongepowered.api.event.block.InteractBlockEvent;
//import org.spongepowered.api.event.filter.cause.First;
//import org.spongepowered.api.event.game.state.GameStartedServerEvent;
//import org.spongepowered.api.item.ItemTypes;
//import org.spongepowered.api.item.inventory.ItemStack;
//import org.spongepowered.api.plugin.Plugin;
//import org.spongepowered.api.scheduler.Task;
//import org.spongepowered.api.service.sql.SqlService;
//import org.spongepowered.api.text.Text;
//import org.spongepowered.api.text.format.TextColors;
//import org.spongepowered.api.world.Location;
//import org.spongepowered.api.world.World;
//
//import com.flowpowered.math.vector.Vector3d;
//import com.flowpowered.math.vector.Vector3i;
//import com.google.inject.Inject;
//import com.intellectualcrafters.plot.object.Plot;
//
//import Darwin.Particles.commands.commands;
//import ninja.leaping.configurate.commented.CommentedConfigurationNode;
//import ninja.leaping.configurate.loader.ConfigurationLoader;
//
//@Plugin(id = "darwinparticles", name = "Darwin Particles", version = "1.0", description = "Allow players to spawn particles")
//public class darwinParticlesMain {
//	@Inject
//	@ConfigDir(sharedRoot = false)
//	public Path root;
//
//	public static Path staticRoots;
//	@Inject
//	@DefaultConfig(sharedRoot = false)
//	private ConfigurationLoader<CommentedConfigurationNode> configManager;
//	public static SqlService sql;
//
//	public static  HashMap<String, HashMap<String, plotParticles>> allPlotsWithParticles = new HashMap<>();
//	public static  HashMap<UUID, playerData> playerData = new HashMap<>();
//	
//	public static Text particlesDefault = Text.of(TextColors.LIGHT_PURPLE, "Particles - ");
//	@Listener
//	public void onServerFinishLoad(GameStartedServerEvent event) throws SQLException {
//		//bad code, probably not necessary but i fucking hate making the root work for a database
//		staticRoots = root;
//		rootSingleton.getInstance().setRoot(staticRoots);
//		DatabaseCreation db = new DatabaseCreation(sql);
//		
//		//rest of this shit is fine
//		Sponge.getCommandManager().register(this, makeTest, "particleplacer", "pap");
//		Sponge.getEventManager().registerListeners(this, new doRightClick());
//		Collection<World> allWorlds = Sponge.getServer().getWorlds();
//		for (World world : allWorlds) {
//			if (world.isLoaded()) {
//				String tableName = null;
//				if (world.getName().toLowerCase().contains("plot") || world.getName().toLowerCase().contains("contest")) {
//					tableName = "Plots";
//				}
//				else {
//					tableName = "PrivateWorlds";
//				}
//				Integer iteration = 0;
//				String query = "SELECT * from " + tableName + " where worldName = '" + world.getName() + "'";
//				String uri = "jdbc:sqlite:" + root + "/ParticleStorage.db";
//				try (Connection conn2 = getDataSource(uri).getConnection()) {
//					PreparedStatement stmt = conn2.prepareStatement(query); {
//						ResultSet results = stmt.executeQuery(); {
//							while(results.next()) {
//								ArrayList<Location> locations = new ArrayList<>();
//								ArrayList<Vector3i> chunkLocations = new ArrayList<>();
//								ArrayList<ParticleEffect> effects = new ArrayList<>();
//								Long interval;
//								HashMap<String, plotParticles> globalParticles;
//					
//								String[] locSplit = results.getString("Location").split(",");
//
//								@SuppressWarnings("unchecked")
//								Location loc = new Location(world, Double.valueOf(locSplit[0]),Double.valueOf(locSplit[1]),Double.valueOf(locSplit[2]));
//								locations.add(loc);
//								com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
//								plotLoc.setX(loc.getBlockX());
//								plotLoc.setY(loc.getBlockY());
//								plotLoc.setZ(loc.getBlockZ());
//								plotLoc.setWorld(world.getName());
//								if (Plot.getPlot(plotLoc) != null) {
//									Plot plot = Plot.getPlot(plotLoc);
//								String[] chunkSplit = results.getString("ChunkID").replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace(" ", "").split(",");
//								chunkLocations.add(new Vector3i(Integer.valueOf(chunkSplit[0]),Integer.valueOf(chunkSplit[1]),Integer.valueOf(chunkSplit[2])));
//								effects.add(getParticleFromString.get(results.getString("ParticleEffect"), results.getInt("Quantity")));
//								interval = results.getLong("Interval");
//
//								//now for the massive fucking switch statement
//								plotParticles plotParticle = null; 
//								
//								if (allPlotsWithParticles.containsKey(world.getName())) {
//									globalParticles = allPlotsWithParticles.get(world.getName());
//								}
//								else {
//									globalParticles = new HashMap<String, plotParticles>();
//									plotParticle = new plotParticles(locations, chunkLocations, effects, interval);
//								}
//								if (globalParticles.containsKey(plot.getId().toString())) {
//								plotParticle = globalParticles.get(plot.getId().toString());
//								plotParticle.addParticles(locations, chunkLocations, effects, interval);
//								}
//								else {
//									plotParticle = new plotParticles(locations, chunkLocations, effects, interval);
//								}
//								iteration += 1;
//									globalParticles.put(plot.getId().toString(),plotParticle);
//									allPlotsWithParticles.put(world.getName(), globalParticles);	
//									
//								}						
//							}
//						}
//					}
//				}
//			}
//			System.out.println(allPlotsWithParticles.entrySet());
//			Task doParticleTask = Task.builder().execute(new doParticleTask())
//					.interval(1, TimeUnit.SECONDS)
//					.name("spawnParticles").submit(this);
//		}
//
//	}
//
//	CommandSpec changeParticle = CommandSpec.builder()
//			.description(Text.of("change particle type with command"))
//			.permission("pp.admin")
//			.arguments(GenericArguments.integer(Text.of("quantity")),GenericArguments.optional(GenericArguments.string(Text.of("particle effect"))))
//			.executor(new commands.ChangeParticle())
//			.build();
//
//	CommandSpec deleteParticles = CommandSpec.builder()
//			.description(Text.of("change particle type with command"))
//			.permission("pp.admin")
//			.executor(new commands.deleteParticle())
//			.build();
//	CommandSpec makeParticle = CommandSpec.builder()
//			.description(Text.of("Toggle main command"))
//			.permission("pp.admin")
//			.executor(new commands.MakeParticle())
//			.build();
//	CommandSpec getStick = CommandSpec.builder()
//			.description(Text.of("Toggle main command"))
//			.permission("pp.admin")
//			.executor(new commands.getStick())
//			.build();
//	CommandSpec makeTest = CommandSpec.builder()
//			.description(Text.of("Toggle main command"))
//			.child(makeParticle, "make")
//			.child(changeParticle, "change")
//			.child(deleteParticles, "delete")
//			.child(getStick, "getStick")
//			.build();
//
//	public static ItemStack makePPStick() {
//		ItemStack ppStick = ItemStack.builder()
//				.itemType(ItemTypes.BLAZE_ROD).build();
//		ppStick.offer(Keys.DISPLAY_NAME, Text.of(TextColors.YELLOW, "My PP Stick"));
//		List<Text> itemLore = new ArrayList<Text>();
//		itemLore.add(Text.of(TextColors.BLUE, "Your very own PP Stick made by Crunch"));
//		ppStick.offer(Keys.ITEM_LORE, itemLore);
//		return ppStick;
//	} 
//	public static DataSource getDataSource(String jdbcUrl) throws SQLException {
//		if (sql == null) {
//			sql = Sponge.getServiceManager().provide(SqlService.class).get();
//		}
//		return sql.getDataSource(jdbcUrl);
//	}
//
//	
//	//loop through the maps that store the particle and their locations, then run the method to spawn particles for any nearby player if that world is loaded
//	public class doParticleTask implements Runnable {
//		public void run() {
//			for (Entry<String, HashMap<String, plotParticles>> map : allPlotsWithParticles.entrySet()) {
//				if (Sponge.getServer().getWorld(map.getKey()).isPresent() && Sponge.getServer().getWorld(map.getKey()).get().isLoaded()){
//					for (Entry<String, plotParticles> map2 : map.getValue().entrySet()) {
//				//	for (int i = 0 ; i < map.getValue().size() ; i++) {
//						plotParticles pp = map2.getValue();
//						pp.spawnParticleForNearbyPlayer(25);
//					}
//				}
//				else {
//					//allPlotsWithParticles.remove(map.getKey());
//				}
//			}
//		}
//	}
//	public static void addNewParticle(Location loc, Player player) {
//		com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
//		plotLoc.setX(loc.getBlockX());
//		plotLoc.setY(loc.getBlockY());
//		plotLoc.setZ(loc.getBlockZ());
//		plotLoc.setWorld(player.getLocation().getExtent().getName());
//		if (Plot.getPlot(plotLoc) != null) {
//			Plot plot = Plot.getPlot(plotLoc);
//			ArrayList<Location> locations = new ArrayList<>();
//			ArrayList<Vector3i> chunkLocations = new ArrayList<>();
//			ArrayList<ParticleEffect> effects = new ArrayList<>();
//			Long interval;
//			int quantity = 10;
//			ParticleEffect effect;
//			if (playerData.containsKey(player.getUniqueId())) {
//				effect = playerData.get(player.getUniqueId()).getEffect();
//				quantity = playerData.get(player.getUniqueId()).getQuantity();
//				interval = playerData.get(player.getUniqueId()).getInterval();
//			}
//			else {
//				effect = ParticleEffect.builder()
//						.type(ParticleTypes.SMOKE)
//						.quantity(quantity)
//						.build();
//				interval = (long) 5;
//			}
//			locations.add(loc);
//			chunkLocations.add(loc.getChunkPosition());
//			effects.add(effect);
//
//			HashMap<String, plotParticles> globalParticles;
//			plotParticles plotParticle = null; 
//			
//			if (allPlotsWithParticles.containsKey(player.getLocation().getExtent().getName())) {
//				globalParticles = allPlotsWithParticles.get(player.getLocation().getExtent().getName());
//			}
//			else {
//				globalParticles = new HashMap<String, plotParticles>();
//				plotParticle = new plotParticles(locations, chunkLocations, effects, interval);
//			}
//			if (globalParticles.containsKey(plot.getId().toString())) {
//			plotParticle = globalParticles.get(plot.getId().toString());
//			plotParticle.addParticles(locations, chunkLocations, effects, interval);
//			}
//			else {
//				plotParticle = new plotParticles(locations, chunkLocations, effects, interval);
//			}
//			String uri = "jdbc:sqlite:" + rootSingleton.getInstance().getRoot() + "/ParticleStorage.db";
//			String tableName = null;
//			if (player.getLocation().getExtent().getName().toLowerCase().contains("plot") || player.getLocation().getExtent().getName().toLowerCase().contains("contest")) {
//				tableName = "Plots";
//			}
//			else {
//				tableName = "PrivateWorlds";
//			}
//			String insertQuery = "INSERT INTO " + tableName + "(WorldName, ChunkID, Location, ParticleEffect, Quantity, Interval) values (?, ?, ?, ?, ?, ?)";
//			try (Connection conn = getDataSource(uri).getConnection()) {
//				PreparedStatement stmt = conn.prepareStatement(insertQuery); {
//					stmt.setString(1, player.getLocation().getExtent().getName());
//					stmt.setString(2, chunkLocations.toString());
//					stmt.setString(3, loc.getX() + "," + loc.getY() + "," + loc.getZ());
//					stmt.setString(4, effect.getType().getName());
//					stmt.setInt(5, quantity);
//					stmt.setLong(6, interval);
//					stmt.execute();
//					conn.close();
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			globalParticles.put(plot.getId().toString(), plotParticle);
//			allPlotsWithParticles.put(player.getLocation().getExtent().getName(), globalParticles);
//			player.spawnParticles(effect, new Vector3d(loc.getX(), loc.getY(), loc.getZ()));
//			player.sendMessage(Text.of(darwinParticlesMain.particlesDefault, "Spawning particle ", effect.getType().getName(), " with quantity of ", quantity));
//		}
//	}
//
//	public class doRightClick {
//		@Listener
//		public void onBlockClick(InteractBlockEvent.Secondary.MainHand event, @First Player player) {
//			if (player.getItemInHand(HandTypes.MAIN_HAND) != null && player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(makePPStick())){		
//				@SuppressWarnings("unchecked")
//				Location loc;
//				if (event.getTargetBlock().getLocation().get().getExtent() != null && event.getInteractionPoint().isPresent()) {
//				loc = new Location(event.getTargetBlock().getLocation().get().getExtent(), event.getInteractionPoint().get());
//				}
//				else {
//					loc = player.getLocation();
//				}
//				addNewParticle(loc, player);
//				
//			}
//		}
//	}
//
//
//	public static void spawnParticlesAtVector3(Player player, ParticleEffect effect, Vector3d Vector3Location) {
//		player.spawnParticles(effect, Vector3Location);
//	}
//
//}
