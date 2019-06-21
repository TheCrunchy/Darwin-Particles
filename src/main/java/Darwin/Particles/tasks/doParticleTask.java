package Darwin.Particles.tasks;

import java.util.Map.Entry;

import org.spongepowered.api.Sponge;

import Darwin.Particles.darwinParticlesMain;
import Darwin.Particles.plotParticles;

public class doParticleTask implements Runnable {
	public void run() {
		for (Entry<String, plotParticles> map : darwinParticlesMain.allPlotsWithParticles.entrySet()) {
			String[] split = map.getKey().split(":");
			if (Sponge.getServer().getWorld(split[0]).isPresent() && Sponge.getServer().getWorld(split[0]).get().isLoaded()){
				//	for (int i = 0 ; i < map.getValue().size() ; i++) {
				plotParticles pp = map.getValue();
				pp.spawnParticleForNearbyPlayer();
			}
			else {
				//allPlotsWithParticles.remove(map.getKey());
			}
		}
	}
}
