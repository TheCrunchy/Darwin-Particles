package deprecated;
//package Darwin.Particles.deprecated;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//import javax.sql.DataSource;
//
//import org.spongepowered.api.Sponge;
//import org.spongepowered.api.service.sql.SqlService;
//import org.spongepowered.api.text.Text;
//import org.spongepowered.api.text.format.TextColors;
//
//public class DatabaseCreation {
//	private Path root;
//	 
//    private SqlService sql;
//    DatabaseCreation(SqlService sqlP) throws SQLException{
//    	sql = sqlP;
//    	this.root =  rootSingleton.getInstance().getRoot();
//    	create();
//    }
//	public DataSource getDataSource(String jdbcUrl) throws SQLException {
//        if (sql == null) {
//            sql = Sponge.getServiceManager().provide(SqlService.class).get();
//        }
//        return sql.getDataSource(jdbcUrl);
//    }
//    
//    public void create() throws SQLException{
//    	
//        String uri = "jdbc:sqlite:" + root + "/ParticleStorage.db";
//        ArrayList <String> queries = new ArrayList<>();
//        //i know i should use a prepared statement but im lazy
//        queries.add("CREATE TABLE IF NOT EXISTS PrivateWorlds (`ID` INTEGER, `WorldName` TEXT, `PlotID` TEXT, `ChunkID` TEXT, `Location` TEXT , `ParticleEffect` TEXT, `Quantity` INTEGER,`Interval` NUMERIC,PRIMARY KEY(`ID`))");
//        queries.add("CREATE TABLE IF NOT EXISTS Plots (`ID` INTEGER, `WorldName` TEXT, `PlotID` TEXT, `ChunkID` TEXT, `Location` TEXT , `ParticleEffect` TEXT, `Quantity` INTEGER,`Interval` NUMERIC,PRIMARY KEY(`ID`))");
//        File file = new File(root + "/ParticleStorage.db");
//        if (!file.exists()) {
//        	try {
//				file.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            Connection conn = getDataSource(uri).getConnection();
//			Statement statement = conn.createStatement();
//
//			for (String query : queries) {
//				statement.addBatch(query);
//			}
//			statement.executeBatch();
//			statement.close();
//			conn.close();
//			}
//        
//    }
//}
