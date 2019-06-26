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
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

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
