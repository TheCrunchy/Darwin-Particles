package crunch.darwin.particles.tasks;

import java.sql.SQLException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import com.intellectualcrafters.plot.object.Plot;

import crunch.darwin.particles.DarwinParticlesMain;

public class LoadParticleTask implements Runnable {
	public void run() {
				for (int i = 0 ; i < DarwinParticlesMain.plotsToLoadParticles.size() ; i++) {
					com.intellectualcrafters.plot.object.Location plotLoc = DarwinParticlesMain.plotsToLoadParticles.get(i);
					if (Plot.getPlot(plotLoc) != null && Plot.getPlot(plotLoc).getPlayersInPlot().size() > 0) {
						Plot plot = Plot.getPlot(plotLoc);
						DarwinParticlesMain.plotsToLoadParticles.remove(i);
						Sponge.getServer().getConsole().sendMessage(Text.of(DarwinParticlesMain.particlesDefault, " Loading ", plot.getWorldName() + ":" + plot.getId().toString()));
						try {
							DarwinParticlesMain.db.loadParticleFromDB(plot.getWorldName(), plot.getId().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						DarwinParticlesMain.plotsToLoadParticles.remove(i);
					}
			}
				for (int i = 0 ; i < DarwinParticlesMain.plotsTounLoadParticles.size() ; i++) {
					com.intellectualcrafters.plot.object.Location plotLoc = DarwinParticlesMain.plotsTounLoadParticles.get(i);
					if (Plot.getPlot(plotLoc) != null && Plot.getPlot(plotLoc).getPlayersInPlot().size() == 0) {
						Plot plot = Plot.getPlot(plotLoc);
						DarwinParticlesMain.plotsTounLoadParticles.remove(i);
						DarwinParticlesMain.allPlotsWithParticles.remove(plot.getWorldName() + ":" + plot.getId().toString());
						Sponge.getServer().getConsole().sendMessage(Text.of(DarwinParticlesMain.particlesDefault, " Unloading ", plot.getWorldName() + ":" + plot.getId().toString()));
					}
					else {
						DarwinParticlesMain.plotsTounLoadParticles.remove(i);
					}
			}
		}
	}