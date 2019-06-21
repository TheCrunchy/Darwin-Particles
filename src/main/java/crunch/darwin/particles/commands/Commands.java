package crunch.darwin.particles.commands;


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


import crunch.darwin.particles.DarwinParticlesMain;
import crunch.darwin.particles.GetParticleFromString;
import crunch.darwin.particles.PlayerData;

public class Commands {
	public static class ChangeParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Player player = (Player) src;
			ParticleEffect type = null;
			Long interval;
			if (args.getOne("particle effect").isPresent()) {
				if (GetParticleFromString.get(args.getOne("particle effect").get().toString().toLowerCase(), Integer.valueOf(args.getOne("quantity").get().toString())) != null) {
			type = GetParticleFromString.get(args.getOne("particle effect").get().toString().toLowerCase(), Integer.valueOf(args.getOne("quantity").get().toString()));
				}
				else {
					src.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, " That particle does not exist or is not enabled"));
					return CommandResult.success();
				}
			}
			else {
				if (DarwinParticlesMain.playerData.containsKey(player.getUniqueId())) {
					type = GetParticleFromString.get(DarwinParticlesMain.playerData.get(player.getUniqueId()).getEffect().getType().getName().toLowerCase(), Integer.valueOf(args.getOne("quantity").get().toString()));
				}
				else {
					type = ParticleEffect.builder()
							.type(ParticleTypes.SMOKE)
							.quantity(Integer.valueOf(args.getOne("quantity").get().toString()))
							.build();
				}
			}
			if (args.getOne("interval").isPresent()) {
				interval = (Long) args.getOne("interval").get();
				}
			else {
				interval = (long) 5;
			}
			PlayerData pd = new PlayerData();
			pd.setEffect(type);
			pd.setQuantity(Integer.valueOf(args.getOne("quantity").get().toString()));
			pd.setInterval(interval);
			DarwinParticlesMain.playerData.put(player.getUniqueId(), pd);
			player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "Particle type changed to ", type.getType().getName()));
			return CommandResult.success();
		}

	}
	public static class getStick implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Player player = (Player) src;
			player.getInventory().offer(DarwinParticlesMain.makePPStick());
			return CommandResult.success();
		}

	}
	public static class deleteParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			DarwinParticlesMain.allPlotsWithParticles.clear();
			return CommandResult.success();
		}

	}
	public static class MakeParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Player player = (Player) src;
			Location loc = new Location(Sponge.getServer().getWorld(player.getLocation().getExtent().getName()).get(), player.getLocation().getX(),player.getLocation().getY(), player.getLocation().getZ());
			DarwinParticlesMain.addNewParticle(loc, player);
			return CommandResult.success();
		}
	}
}