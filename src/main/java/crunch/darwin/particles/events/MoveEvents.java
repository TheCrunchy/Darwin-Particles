package crunch.darwin.particles.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.sponge.events.PlayerEnterPlotEvent;
import com.plotsquared.sponge.events.PlayerLeavePlotEvent;

import crunch.darwin.particles.DarwinParticlesMain;

public class MoveEvents {

	//when they enter a plot add to the arraylist for loading particles
	@Listener
	public void onEnterPlot(PlayerEnterPlotEvent event) { 
		DarwinParticlesMain.plotsToLoadParticles.add(event.getPlot().getCenter());
	}
	
	//when they leave a plot add to the arraylist for loading particles
	@Listener
	public void onEnterPlot(PlayerLeavePlotEvent event) { 
		DarwinParticlesMain.plotsTounLoadParticles.add(event.getPlot().getCenter());
	}
	
	//same thing but for login
	public void checkLogin(ClientConnectionEvent.Join event, Player player) {
		com.intellectualcrafters.plot.object.Location loc = new com.intellectualcrafters.plot.object.Location();
		loc.setX(event.getTargetEntity().getTransform().getLocation().getBlockX());
		loc.setY(event.getTargetEntity().getTransform().getLocation().getBlockY());
		loc.setZ(event.getTargetEntity().getTransform().getLocation().getBlockZ());
		loc.setWorld(event.getTargetEntity().getTransform().getLocation().getExtent().getName().toString());
		if (Plot.getPlot(loc) != null) {
			DarwinParticlesMain.plotsToLoadParticles.add(loc);
		}
	}
	//same thing but for disconnect
	public void checkDisconnect(ClientConnectionEvent.Disconnect event, Player player) {
		com.intellectualcrafters.plot.object.Location loc = new com.intellectualcrafters.plot.object.Location();
		loc.setX(event.getTargetEntity().getTransform().getLocation().getBlockX());
		loc.setY(event.getTargetEntity().getTransform().getLocation().getBlockY());
		loc.setZ(event.getTargetEntity().getTransform().getLocation().getBlockZ());
		loc.setWorld(event.getTargetEntity().getTransform().getLocation().getExtent().getName().toString());
		if (Plot.getPlot(loc) != null) {
			DarwinParticlesMain.plotsTounLoadParticles.add(loc);
		}
	}
}

