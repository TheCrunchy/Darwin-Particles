package Darwin.Particles.tasks;

import java.sql.SQLException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import com.intellectualcrafters.plot.object.Plot;

import Darwin.Particles.darwinParticlesMain;

public class loadParticleTask implements Runnable {
	public void run() {
				for (int i = 0 ; i < darwinParticlesMain.plotsToLoadParticles.size() ; i++) {
					com.intellectualcrafters.plot.object.Location plotLoc = darwinParticlesMain.plotsToLoadParticles.get(i);
					if (Plot.getPlot(plotLoc) != null && Plot.getPlot(plotLoc).getPlayersInPlot().size() > 0) {
						Plot plot = Plot.getPlot(plotLoc);
						darwinParticlesMain.plotsToLoadParticles.remove(i);
						Sponge.getServer().getConsole().sendMessage(Text.of(darwinParticlesMain.particlesDefault, " Loading ", plot.getWorldName() + ":" + plot.getId().toString()));
						try {
							darwinParticlesMain.db.loadParticleFromDB(plot.getWorldName(), plot.getId().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						darwinParticlesMain.plotsToLoadParticles.remove(i);
					}
			}
				for (int i = 0 ; i < darwinParticlesMain.plotsTounLoadParticles.size() ; i++) {
					com.intellectualcrafters.plot.object.Location plotLoc = darwinParticlesMain.plotsTounLoadParticles.get(i);
					if (Plot.getPlot(plotLoc) != null && Plot.getPlot(plotLoc).getPlayersInPlot().size() == 0) {
						Plot plot = Plot.getPlot(plotLoc);
						darwinParticlesMain.plotsTounLoadParticles.remove(i);
						darwinParticlesMain.allPlotsWithParticles.remove(plot.getWorldName() + ":" + plot.getId().toString());
						Sponge.getServer().getConsole().sendMessage(Text.of(darwinParticlesMain.particlesDefault, " Unloading ", plot.getWorldName() + ":" + plot.getId().toString()));
					}
					else {
						darwinParticlesMain.plotsTounLoadParticles.remove(i);
					}
			}
		}
	}