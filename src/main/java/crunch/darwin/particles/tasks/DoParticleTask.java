package crunch.darwin.particles.tasks;

import java.util.Map.Entry;

import org.spongepowered.api.Sponge;

import crunch.darwin.particles.DarwinParticlesMain;
import crunch.darwin.particles.PlotParticles;

public class DoParticleTask implements Runnable {
	public void run() {
		for (Entry<String, PlotParticles> map : DarwinParticlesMain.allPlotsWithParticles.entrySet()) {
			String[] split = map.getKey().split(":");
			if (Sponge.getServer().getWorld(split[0]).isPresent() && Sponge.getServer().getWorld(split[0]).get().isLoaded()){
				//	for (int i = 0 ; i < map.getValue().size() ; i++) {
				PlotParticles pp = map.getValue();
				pp.spawnParticleForNearbyPlayer();
			}
			else {
				//allPlotsWithParticles.remove(map.getKey());
			}
		}
	}
}
