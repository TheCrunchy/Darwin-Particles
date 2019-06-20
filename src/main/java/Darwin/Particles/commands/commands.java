package Darwin.Particles.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3i;
import com.intellectualcrafters.plot.object.Plot;

import Darwin.Particles.darwinParticlesMain;
import Darwin.Particles.getParticleFromString;
import Darwin.Particles.playerData;
import Darwin.Particles.plotParticles;
import Darwin.Particles.rootSingleton;

public class commands {
	public static class ChangeParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Player player = (Player) src;
			ParticleEffect type;
			if (args.getOne("particle effect").isPresent()) {
			type = getParticleFromString.get(args.getOne("particle effect").get().toString().toLowerCase(), Integer.valueOf(args.getOne("quantity").get().toString()));
			}
			else {
				if (darwinParticlesMain.playerData.containsKey(player.getUniqueId())) {
					type = getParticleFromString.get(darwinParticlesMain.playerData.get(player.getUniqueId()).getEffect().getType().getName().toLowerCase(), Integer.valueOf(args.getOne("quantity").get().toString()));
				}
				else {
					type = ParticleEffect.builder()
							.type(ParticleTypes.SMOKE)
							.quantity(Integer.valueOf(args.getOne("quantity").get().toString()))
							.build();
				}
			}
			playerData pd = new playerData();
			pd.setEffect(type);
			pd.setQuantity(Integer.valueOf(args.getOne("quantity").get().toString()));
			darwinParticlesMain.playerData.put(player.getUniqueId(), pd);
			player.sendMessage(Text.of(darwinParticlesMain.particlesDefault, "Particle type changed to ", type.getType().getName()));
			return CommandResult.success();
		}

	}
	public static class getStick implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Player player = (Player) src;
			player.getInventory().offer(darwinParticlesMain.makePPStick());
			return CommandResult.success();
		}

	}
	public static class deleteParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			darwinParticlesMain.allPlotsWithParticles.clear();
			return CommandResult.success();
		}

	}
	public static class MakeParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Player player = (Player) src;
			Location loc = new Location(Sponge.getServer().getWorld(player.getLocation().getExtent().getName()).get(), player.getLocation().getX(),player.getLocation().getY(), player.getLocation().getZ());
			darwinParticlesMain.addNewParticle(loc, player);
			return CommandResult.success();
		}
	}
}
