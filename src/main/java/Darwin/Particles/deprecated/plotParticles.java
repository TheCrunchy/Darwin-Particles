//package Darwin.Particles.deprecated;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.UUID;
//import java.util.Map.Entry;
//
//import org.spongepowered.api.Sponge;
//import org.spongepowered.api.effect.particle.ParticleEffect;
//import org.spongepowered.api.entity.Entity;
//import org.spongepowered.api.entity.EntityTypes;
//import org.spongepowered.api.entity.living.player.Player;
//import org.spongepowered.api.text.Text;
//import org.spongepowered.api.world.Location;
//import org.spongepowered.api.world.World;
//import org.spongepowered.api.world.extent.Extent;
//
//import com.flowpowered.math.vector.Vector3d;
//import com.flowpowered.math.vector.Vector3i;
//
//public class plotParticles {
//	private HashMap<Vector3i, HashMap<Location, ParticleEffect>> chunksWithParticles = new HashMap<>();
//
//	private HashMap<Location, Long> intervals = new HashMap<Location, Long>();
//	private HashMap<Location, Long> cooldowns = new HashMap<Location, Long>();
//	//setup the plotParticle by giving it arrays with locations and effects for each particle type
//	public plotParticles(ArrayList<Location> locations, ArrayList<Vector3i> chunkLocations, ArrayList<ParticleEffect> effects, Long interval) {
//		//		for (int i = 0 ; i < chunkLocations.size() ; i++) {
//		//			HashMap<Location, ParticleEffect> particlesAtLocation;
//		//			if (chunksWithParticles.get(chunkLocations.get(i)) != null) {
//		//				particlesAtLocation = chunksWithParticles.get(chunkLocations.get(i));
//		//			}
//		//			else {
//		//				particlesAtLocation = new HashMap<>();
//		//			}
//		//			particlesAtLocation.put(locations.get(i), effects.get(i));
//		//			chunksWithParticles.put(chunkLocations.get(i), particlesAtLocation);
//		//		}
//		addParticles(locations, chunkLocations, effects, interval);
//	}
//	//this is obvious
//	public HashMap getParticles() {
//		return chunksWithParticles;
//	}
//
//	public void removeFromMap(Vector3i chunk, Location loc) {
//		chunksWithParticles.get(chunk).remove(loc);
//		//also do database code
//	}
//	public void addParticles(ArrayList<Location> locations, ArrayList<Vector3i> chunkLocations, ArrayList<ParticleEffect> effects, Long interval){
//		for (int i = 0 ; i < chunkLocations.size() ; i++) {
//			HashMap<Location, ParticleEffect> particlesAtLocation;
//			if (chunksWithParticles.get(chunkLocations.get(i)) != null) {
//				particlesAtLocation = chunksWithParticles.get(chunkLocations.get(i));
//			}
//			else {
//				particlesAtLocation = new HashMap<>();
//			}
//			intervals.put(locations.get(i), interval);
//			particlesAtLocation.put(locations.get(i), effects.get(i));
//			chunksWithParticles.put(chunkLocations.get(i), particlesAtLocation);
//		}
//	}
//
//	//spawn the particle effect for nearby player, loop through the particles stored for this plot to achieve this.
//	@SuppressWarnings("unchecked")
//	public void spawnParticleForNearbyPlayer(double distance) {
//		for (Entry<Vector3i, HashMap<Location, ParticleEffect>> chunkLocs : chunksWithParticles.entrySet()) {		
//			for (Entry<Location, ParticleEffect> loc : chunkLocs.getValue().entrySet()) {		
//				Vector3d vectorLoc = new Vector3d(loc.getKey().getX() ,loc.getKey().getY(), loc.getKey().getZ());
//				Extent extent = Sponge.getServer().getWorld(loc.getKey().getExtent().getUniqueId()).get();
//				if (extent.getNearbyEntities(vectorLoc, distance) != null) {			
//					Collection<Entity> entities = extent.getNearbyEntities(vectorLoc, distance);
//					if (intervals.containsKey(loc.getKey()) && cooldowns.containsKey(loc.getKey())){
//						if (intervals.get(cooldowns.get(loc.getKey())) + intervals.get(loc.getKey()) >= System.currentTimeMillis() / 1000){
//							cooldowns.put(loc.getKey(), System.currentTimeMillis() / 1000);
//							for (Entity entity : entities) {				
//								if (entity.getType() == EntityTypes.PLAYER) {
//									Player player = (Player) entity;
//									darwinParticlesMain.spawnParticlesAtVector3(player, loc.getValue(), vectorLoc);
//								}
//							}
//						}
//					}
//					else {
//						cooldowns.put(loc.getKey(), System.currentTimeMillis() / 1000);
//					}
//				}
//			}
//		}
//	}
//}
