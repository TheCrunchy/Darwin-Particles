package Darwin.Particles.events;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.intellectualcrafters.plot.flag.Flags;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.plotsquared.sponge.events.PlayerEnterPlotEvent;
import com.plotsquared.sponge.events.PlayerLeavePlotEvent;

import Darwin.Particles.darwinParticlesMain;

public class moveEvents {

	//when they enter a plot add to the arraylist for loading particles
	@Listener
	public void onEnterPlot(PlayerEnterPlotEvent event) { 
		darwinParticlesMain.plotsToLoadParticles.add(event.getPlot().getCenter());
	}
	
	//when they leave a plot add to the arraylist for loading particles
	@Listener
	public void onEnterPlot(PlayerLeavePlotEvent event) { 
		darwinParticlesMain.plotsTounLoadParticles.add(event.getPlot().getCenter());
	}
	
	//same thing but for login
	public void checkLogin(ClientConnectionEvent.Join event, Player player) {
		com.intellectualcrafters.plot.object.Location loc = new com.intellectualcrafters.plot.object.Location();
		loc.setX(event.getTargetEntity().getTransform().getLocation().getBlockX());
		loc.setY(event.getTargetEntity().getTransform().getLocation().getBlockY());
		loc.setZ(event.getTargetEntity().getTransform().getLocation().getBlockZ());
		loc.setWorld(event.getTargetEntity().getTransform().getLocation().getExtent().getName().toString());
		if (Plot.getPlot(loc) != null) {
			darwinParticlesMain.plotsToLoadParticles.add(loc);
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
			darwinParticlesMain.plotsTounLoadParticles.add(loc);
		}
	}
}

