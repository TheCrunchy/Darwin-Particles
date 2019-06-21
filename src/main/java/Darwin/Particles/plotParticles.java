package Darwin.Particles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;

public class plotParticles {
	private HashMap<Vector3i, HashMap<Location, ParticleEffect>> chunksWithParticles = new HashMap<>();
	private HashMap<Vector3d, Long> intervals = new HashMap<Vector3d, Long>();
	private HashMap<Vector3d, Long> cooldowns = new HashMap<Vector3d, Long>();
	private com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
	//setup the plotParticle by giving it arrays with locations and effects for each particle type
	public plotParticles(ArrayList<Location> locations, ArrayList<Vector3i> chunkLocations, ArrayList<ParticleEffect> effects, Long interval,  com.intellectualcrafters.plot.object.Location plotLoc) {
		//		for (int i = 0 ; i < chunkLocations.size() ; i++) {
		//			HashMap<Location, ParticleEffect> particlesAtLocation;
		//			if (chunksWithParticles.get(chunkLocations.get(i)) != null) {
		//				particlesAtLocation = chunksWithParticles.get(chunkLocations.get(i));
		//			}
		//			else {
		//				particlesAtLocation = new HashMap<>();
		//			}
		//			particlesAtLocation.put(locations.get(i), effects.get(i));
		//			chunksWithParticles.put(chunkLocations.get(i), particlesAtLocation);
		//		}
		this.plotLoc = plotLoc;
		addParticles(locations, chunkLocations, effects, interval);
	}
	//this is obvious
	public HashMap getParticles() {
		return chunksWithParticles;
	}
	public void removeFromMap(Vector3i chunk, Location loc) {
		chunksWithParticles.get(chunk).remove(loc);
		//also do database code
	}
	public void addParticles(ArrayList<Location> locations, ArrayList<Vector3i> chunkLocations, ArrayList<ParticleEffect> effects, Long interval){
		for (int i = 0 ; i < chunkLocations.size() ; i++) {
			HashMap<Location, ParticleEffect> particlesAtLocation;
			if (chunksWithParticles.get(chunkLocations.get(i)) != null) {
				particlesAtLocation = chunksWithParticles.get(chunkLocations.get(i));
			}
			else {
				particlesAtLocation = new HashMap<>();
			}
			intervals.put(new Vector3d(locations.get(i).getX() ,locations.get(i).getY(), locations.get(i).getZ()), interval);
			particlesAtLocation.put(locations.get(i), effects.get(i));
			chunksWithParticles.put(chunkLocations.get(i), particlesAtLocation);
		}
	}

	//spawn the particle effect for nearby player, loop through the particles stored for this plot to achieve this.
	@SuppressWarnings("unchecked")
	public void spawnParticleForNearbyPlayer() {
		for (Entry<Vector3i, HashMap<Location, ParticleEffect>> chunkLocs : chunksWithParticles.entrySet()) {		
			for (Entry<Location, ParticleEffect> loc : chunkLocs.getValue().entrySet()) {		
				Vector3d vectorLoc = new Vector3d(loc.getKey().getX() ,loc.getKey().getY(), loc.getKey().getZ());
				Plot plot = Plot.getPlot(plotLoc);
				Collection<PlotPlayer> players = plot.getPlayersInPlot();
				if (cooldowns.containsKey(vectorLoc)) {
					if (!((cooldowns.get(vectorLoc) + intervals.get(vectorLoc)) >= (System.currentTimeMillis() / 1000))) {
						cooldowns.put(vectorLoc, (System.currentTimeMillis() / 1000));
				for (PlotPlayer p : players) {
					if (Sponge.getServer().getPlayer(p.getName()).isPresent());
					Sponge.getServer().getPlayer(p.getName()).get().spawnParticles(loc.getValue(), vectorLoc);
				}
			  }
				}
				else {
					cooldowns.put(vectorLoc, (System.currentTimeMillis() / 1000));
					for (PlotPlayer p : players) {
						if (Sponge.getServer().getPlayer(p.getName()).isPresent());
						Sponge.getServer().getPlayer(p.getName()).get().spawnParticles(loc.getValue(), vectorLoc);
					}
				}
			}
		}
	}
}
