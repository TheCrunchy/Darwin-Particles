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

public class plotParticles {
	public plotParticles(ArrayList<Location> locations, ArrayList<Vector3i> chunkLocations, ArrayList<ParticleEffect> effects) {
		for (int i = 0 ; i < chunkLocations.size() ; i++) {
			HashMap<Location, ParticleEffect> particlesAtLocation;
			if (chunksWithParticles.get(chunkLocations.get(i)) != null) {
				particlesAtLocation = chunksWithParticles.get(chunkLocations.get(i));
			}
			else {
				particlesAtLocation = new HashMap<>();
			}
			particlesAtLocation.put(locations.get(i), effects.get(i));
			chunksWithParticles.put(chunkLocations.get(i), particlesAtLocation);
		}
	}
	private HashMap<Vector3i, HashMap<Location, ParticleEffect>> chunksWithParticles = new HashMap<>();
	public HashMap getParticles() {
		return chunksWithParticles;
	}
	
	@SuppressWarnings("unchecked")
	public void spawnParticleForNearbyPlayer(double distance) {
		for (Entry<Vector3i, HashMap<Location, ParticleEffect>> chunkLocs : chunksWithParticles.entrySet()) {		
			for (Entry<Location, ParticleEffect> loc : chunkLocs.getValue().entrySet()) {		
				Vector3d vectorLoc = new Vector3d(loc.getKey().getX() ,loc.getKey().getY(), loc.getKey().getZ());
				Extent extent = Sponge.getServer().getWorld(loc.getKey().getExtent().getUniqueId()).get();
				if (extent.getNearbyEntities(vectorLoc, distance) != null) {			
					Collection<Entity> entities = extent.getNearbyEntities(vectorLoc, distance);
					for (Entity entity : entities) {				
						if (entity.getType() == EntityTypes.PLAYER) {
							Player player = (Player) entity;
							darwinParticlesMain.spawnParticlesAtVector3(player, loc.getValue(), vectorLoc);
						}
					}
				}
			}
		}
	}
}
