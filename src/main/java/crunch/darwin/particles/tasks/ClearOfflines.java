package crunch.darwin.particles.tasks;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Map.Entry;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import crunch.darwin.particles.DarwinParticlesMain;
import crunch.darwin.particles.PlayerData;


public class ClearOfflines implements Runnable {
	public void run() {
		ArrayList <UUID> offlinePlayers = new ArrayList<UUID>();
		Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.DARK_PURPLE, "Clearing offline players from PlotID maps"));
		for (Entry<UUID, PlayerData> playerData : DarwinParticlesMain.playerData.entrySet()) {
			if (!Sponge.getServer().getPlayer(playerData.getKey()).isPresent()) {
				offlinePlayers.add(playerData.getKey());
			}
		}
		for (UUID player : offlinePlayers) {
			DarwinParticlesMain.playerData.remove(player);
		}
		offlinePlayers.clear();
	}
}
