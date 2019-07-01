package crunch.darwin.particles.events;

import java.sql.SQLException;

import org.spongepowered.api.event.Listener;

import com.plotsquared.sponge.events.PlotClearEvent;

import crunch.darwin.particles.DarwinParticlesMain;

public class PlotClearListener {
	@Listener
	public void onPlotClear(PlotClearEvent event) throws SQLException { 
		
		String worldName = event.getWorld();
		String plotID = event.getPlotId().toString();
		String tableName;
		if (worldName.toLowerCase().contains("plot") || worldName.toLowerCase().contains("contest")) {
			tableName = "Plots";
		}
		else {
			tableName = "PrivateWorlds";
		}
		System.out.println(tableName);
		DarwinParticlesMain.db.clearPlotFromDB(tableName, worldName, plotID);
	}
}
