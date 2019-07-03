package crunch.darwin.particles;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.intellectualcrafters.plot.object.Plot;

public class DatabaseStuff {
	private Path root;

	private SqlService sql;
	DatabaseStuff(SqlService sqlP) throws SQLException{
		sql = sqlP;
		this.root =  RootSingleton.getInstance().getRoot();
		create();
	}
	public DataSource getDataSource(String jdbcUrl) throws SQLException {
		if (sql == null) {
			sql = Sponge.getServiceManager().provide(SqlService.class).get();
		}
		return sql.getDataSource(jdbcUrl);
	}
	public void loadParticleFromDB(String worldName, String plotID) throws SQLException {
		String tableName;
		if (worldName.toLowerCase().contains("plot") || worldName.toLowerCase().contains("contest")) {
			tableName = "Plots";
		}
		else {
			tableName = "PrivateWorlds";
		}
		String query = "SELECT * from " + tableName + " where (worldName = '" + worldName+ "' AND PlotID = '" + plotID + "')";
		String uri = "jdbc:sqlite:" + root + "/ParticleStorage.db";
		try (Connection conn2 = getDataSource(uri).getConnection()) {
			PreparedStatement stmt = conn2.prepareStatement(query); {
				ResultSet results = stmt.executeQuery(); {
					String plotid = "";
					while(results.next()) {
						ArrayList<Location> locations = new ArrayList<>();
						ArrayList<Vector3i> chunkLocations = new ArrayList<>();
						ArrayList<ParticleEffect> effects = new ArrayList<>();
						Long interval;

						//HashMap<String, plotParticles> globalParticles;

						String[] locSplit = results.getString("Location").split(",");

						@SuppressWarnings("unchecked")
						Location loc = new Location(Sponge.getServer().getWorld(worldName).get(), Double.valueOf(locSplit[0]),Double.valueOf(locSplit[1]),Double.valueOf(locSplit[2]));
						locations.add(loc);
						com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
						plotLoc.setX(loc.getBlockX());
						plotLoc.setY(loc.getBlockY());
						plotLoc.setZ(loc.getBlockZ());
						plotLoc.setWorld(worldName);
						if (Plot.getPlot(plotLoc) != null) {
							Plot plot = Plot.getPlot(plotLoc);
							if (plotid != null && !plotid.equals(plot.getId().toString())) {
							Sponge.getServer().getConsole().sendMessage(Text.of(DarwinParticlesMain.particlesDefault, " Loading ", plot.getWorldName() + ":" + plot.getId().toString()));
						
							}
							plotid = plot.getId().toString();
							String[] chunkSplit = results.getString("ChunkID").replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace(" ", "").split(",");
							chunkLocations.add(new Vector3i(Integer.valueOf(chunkSplit[0]),Integer.valueOf(chunkSplit[1]),Integer.valueOf(chunkSplit[2])));
							effects.add(GetParticleFromString.get(results.getString("ParticleEffect"), results.getInt("Quantity")));
							interval = results.getLong("Interval");

							//now for the massive fucking switch statement
							PlotParticles plotParticle = null; 

							if (DarwinParticlesMain.allPlotsWithParticles.containsKey(worldName + ":" + plot.getId().toString())) {
								plotParticle = DarwinParticlesMain.allPlotsWithParticles.get(worldName + ":" + plot.getId().toString());
								plotParticle.addParticles(locations, chunkLocations, effects, interval);
							}
							else {
								plotParticle = new PlotParticles(locations, chunkLocations, effects, interval, plotLoc);
							}
							DarwinParticlesMain.allPlotsWithParticles.put(worldName + ":" + plot.getId().toString(), plotParticle);	

						}						
					}
				}
			}
		}
	}

	public void removeFromDB(String tableName, String worldName, String plotID, String location) throws SQLException {
		String uri = "jdbc:sqlite:" + root + "/ParticleStorage.db";
		String query = "DELETE from " + tableName + " where (WorldName = '" + worldName + "' AND PlotID = '" + plotID + "' AND Location = '" + location + "')";
		try (Connection conn2 = getDataSource(uri).getConnection()) {
			PreparedStatement stmt = conn2.prepareStatement(query); {
				stmt.executeUpdate(); {
				}
			}	
		}
	}
	public void addNewParticle(Location loc, Player player) {
		com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
		plotLoc.setX(loc.getBlockX());
		plotLoc.setY(loc.getBlockY());
		plotLoc.setZ(loc.getBlockZ());
		plotLoc.setWorld(player.getLocation().getExtent().getName());
		if (Plot.getPlot(plotLoc) != null) {

			Plot plot = Plot.getPlot(plotLoc);
			if (plot.isAdded(player.getUniqueId()) || player.hasPermission("plots.admin.build.other")) {


				ArrayList<Location> locations = new ArrayList<>();
				ArrayList<Vector3i> chunkLocations = new ArrayList<>();
				ArrayList<ParticleEffect> effects = new ArrayList<>();
				Long interval;
				int quantity = 10;
				ParticleEffect effect;
				if (DarwinParticlesMain.playerData.containsKey(player.getUniqueId())) {
					effect = DarwinParticlesMain.playerData.get(player.getUniqueId()).getEffect();
					quantity = DarwinParticlesMain.playerData.get(player.getUniqueId()).getQuantity();
					interval = DarwinParticlesMain.playerData.get(player.getUniqueId()).getInterval();
				}
				else {
					effect = ParticleEffect.builder()
							.type(ParticleTypes.SMOKE)
							.quantity(quantity)
							.build();
					interval = (long) 15;
				}
				locations.add(loc);
				chunkLocations.add(loc.getChunkPosition());
				effects.add(effect);
				PlotParticles plotParticle = null; 

				if (DarwinParticlesMain.allPlotsWithParticles.containsKey(player.getLocation().getExtent().getName() + ":" + plot.getId().toString())) {
					plotParticle = DarwinParticlesMain.allPlotsWithParticles.get(player.getLocation().getExtent().getName()  + ":" + plot.getId().toString());
					plotParticle.addParticles(locations, chunkLocations, effects, interval);
				}
				else {
					plotParticle = new PlotParticles(locations, chunkLocations, effects, interval, plotLoc);
				}

				ArrayList<Text> contents = plotParticle.showParticlesInChunk(loc.getChunkPosition(), player);
				int max = PlotParticles.getPlayerChunkLimit(player);
				if (contents.size() >= max) {
					player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, " You have reached the limit of ", max ," particles for this chunk"));
					return;
				}


				DarwinParticlesMain.allPlotsWithParticles.put(player.getLocation().getExtent().getName() + ":" + plot.getId().toString(), plotParticle);	

				//make a method in the database for this shit so its out of this class
				String uri = "jdbc:sqlite:" + RootSingleton.getInstance().getRoot() + "/ParticleStorage.db";
				String tableName = null;
				if (player.getLocation().getExtent().getName().toLowerCase().contains("plot") || player.getLocation().getExtent().getName().toLowerCase().contains("contest")) {
					tableName = "Plots";
				}
				else {
					tableName = "PrivateWorlds";
				}
				String insertQuery = "INSERT INTO " + tableName + "(WorldName, PlotID, ChunkID, Location, ParticleEffect, Quantity, Interval) values (?, ?, ?, ?, ?, ?, ?)";
				try (Connection conn = getDataSource(uri).getConnection()) {
					PreparedStatement stmt = conn.prepareStatement(insertQuery); {
						stmt.setString(1, player.getLocation().getExtent().getName());
						stmt.setString(2, plot.getId().toString());
						stmt.setString(3, chunkLocations.toString());
						stmt.setString(4, loc.getX() + "," + loc.getY() + "," + loc.getZ());
						stmt.setString(5, effect.getType().getName());
						stmt.setInt(6, quantity);
						stmt.setLong(7, interval);
						stmt.execute();
						conn.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DarwinParticlesMain.allPlotsWithParticles.put(player.getLocation().getExtent().getName() + ":" + plot.getId().toString(), plotParticle);
				player.spawnParticles(effect, new Vector3d(loc.getX(), loc.getY(), loc.getZ()));
				player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "Spawning particle ", effect.getType().getName(), " with quantity of ", quantity));
			}
		}
	}
	public void clearPlotFromDB(String tableName, String worldName, String plotID) throws SQLException {
		String uri = "jdbc:sqlite:" + root + "/ParticleStorage.db";
		String query = "DELETE from " + tableName + " where (WorldName = '" + worldName + "' AND PlotID = '" + plotID + "')";
		Sponge.getServer().getConsole().sendMessage(Text.of(DarwinParticlesMain.particlesDefault, " Deleting from DB ", worldName , " " , plotID));
		if (DarwinParticlesMain.allPlotsWithParticles.containsKey(worldName + ":" + plotID)) {
			DarwinParticlesMain.allPlotsWithParticles.remove(worldName + ":" + plotID);
		}
		try (Connection conn2 = getDataSource(uri).getConnection()) {
			PreparedStatement stmt = conn2.prepareStatement(query); {
				stmt.executeUpdate(); {
				}
			}	
		}
	}
	public void create() throws SQLException{

		String uri = "jdbc:sqlite:" + root + "/ParticleStorage.db";
		ArrayList <String> queries = new ArrayList<>();
		//i know i should use a prepared statement but im lazy
		queries.add("CREATE TABLE IF NOT EXISTS PrivateWorlds (`ID` INTEGER, `WorldName` TEXT, `PlotID` TEXT, `ChunkID` TEXT, `Location` TEXT , `ParticleEffect` TEXT, `Quantity` INTEGER,`Interval` NUMERIC,PRIMARY KEY(`ID`))");
		queries.add("CREATE TABLE IF NOT EXISTS Plots (`ID` INTEGER, `WorldName` TEXT, `PlotID` TEXT, `ChunkID` TEXT, `Location` TEXT , `ParticleEffect` TEXT, `Quantity` INTEGER,`Interval` NUMERIC,PRIMARY KEY(`ID`))");
		File file = new File(root + "/ParticleStorage.db");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Connection conn = getDataSource(uri).getConnection();
			Statement statement = conn.createStatement();

			for (String query : queries) {
				statement.addBatch(query);
			}
			statement.executeBatch();
			statement.close();
			conn.close();
		}

	}
}
