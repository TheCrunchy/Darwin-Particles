package crunch.darwin.particles.tasks;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map.Entry;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.intellectualcrafters.plot.object.Plot;

import crunch.darwin.particles.DarwinParticlesMain;
import crunch.darwin.particles.PlotParticles;

public class trollTask implements Runnable {
	public void run() {
		for (int i = 0 ; i < DarwinParticlesMain.playersToTroll.size() ; i++) {
			Player player = DarwinParticlesMain.playersToTroll.get(i);
			if (player.isOnline()) {
				Double up1, up2, left1, right1;
				up1 = (double) (player.getLocation().getBlockY() + 3);
				
				up2 = (double) (player.getLocation().getBlockY() + 4);

					left1 = (double) (player.getLocation().getBlockZ() - 1);
					right1 = (double) (player.getLocation().getBlockZ() + 1);
		
				ParticleEffect effect = ParticleEffect.builder()
						.type(ParticleTypes.CLOUD)
						.quantity(30)
						.build();
				Collection<Entity> entities = player.getNearbyEntities(25);
				for (Entity entity : entities) {				
					if (entity.getType() == EntityTypes.PLAYER) {
						Player spawnOnPlayer = (Player) entity;
						DarwinParticlesMain.spawnParticlesAtVector3(spawnOnPlayer, effect, new Vector3d(player.getLocation().getBlockX(), up1, player.getLocation().getBlockZ()));
						DarwinParticlesMain.spawnParticlesAtVector3(spawnOnPlayer, effect, new Vector3d(player.getLocation().getBlockX(), (up1 + 0.5), player.getLocation().getBlockZ()));
						DarwinParticlesMain.spawnParticlesAtVector3(spawnOnPlayer, effect, new Vector3d(player.getLocation().getBlockX(), up2, player.getLocation().getBlockZ()));
						DarwinParticlesMain.spawnParticlesAtVector3(spawnOnPlayer, effect, new Vector3d(player.getLocation().getBlockX(), (up2 + 0.5), player.getLocation().getBlockZ()));
						DarwinParticlesMain.spawnParticlesAtVector3(spawnOnPlayer, effect, new Vector3d(player.getLocation().getBlockX(), (up2 + 1), player.getLocation().getBlockZ()));
						DarwinParticlesMain.spawnParticlesAtVector3(spawnOnPlayer, effect, new Vector3d(player.getLocation().getBlockX(), up1, left1));
						DarwinParticlesMain.spawnParticlesAtVector3(spawnOnPlayer, effect, new Vector3d(player.getLocation().getBlockX(), up1, right1));
					}
				}
			}
		}
	}
}


