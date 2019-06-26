package crunch.darwin.particles.commands;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;

import com.flowpowered.math.vector.Vector3i;
import com.intellectualcrafters.plot.object.Plot;

import crunch.darwin.particles.DarwinParticlesMain;
import crunch.darwin.particles.GetParticleFromString;
import crunch.darwin.particles.PlayerData;
import crunch.darwin.particles.PlotParticles;

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

	public static class getParticlesInChunk implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Player player = (Player) src;
			com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
			Location loc = player.getLocation();
			plotLoc.setX(loc.getBlockX());
			plotLoc.setY(loc.getBlockY());
			plotLoc.setZ(loc.getBlockZ());
			plotLoc.setWorld(player.getLocation().getExtent().getName());
			if (Plot.getPlot(plotLoc) != null) {
				Plot plot = Plot.getPlot(plotLoc);
				if (DarwinParticlesMain.allPlotsWithParticles.containsKey(player.getLocation().getExtent().getName() + ":" + plot.getId().toString())) {

					PlotParticles pp =  DarwinParticlesMain.allPlotsWithParticles.get(player.getLocation().getExtent().getName() + ":" + plot.getId().toString());
					ArrayList<Text> contents = pp.showParticlesInChunk(loc.getChunkPosition(), player);
					ArrayList<Text> formattedContents = new ArrayList<>();
					
					if (!contents.isEmpty() && contents != null) {
						for (Text toFormat : contents) {
							Text.Builder sendToPlayer = Text.builder();
							Text.Builder sendParticle = Text.builder();
							sendParticle.append(Text.of(TextColors.LIGHT_PURPLE, toFormat)).build();

							sendToPlayer.append(Text.of(sendParticle));
							sendParticle.removeAll();
							sendParticle.append(Text.of(TextColors.AQUA, " [TP]")).onClick(TextActions.runCommand("/pap teleport " + toFormat.getChildren().get(2).toPlainSingle() + " " +  toFormat.getChildren().get(4).toPlainSingle() + " " + toFormat.getChildren().get(6).toPlainSingle())).build();
							sendToPlayer.append(Text.of(sendParticle));
							sendParticle.removeAll();
							sendParticle.append(Text.of(TextColors.DARK_RED, " [Remove]")).onClick(TextActions.runCommand("/pap delete " + toFormat.getChildren().get(2).toPlainSingle() + " " +  toFormat.getChildren().get(4).toPlainSingle() + " " + toFormat.getChildren().get(6).toPlainSingle())).build();
							sendToPlayer.append(Text.of(sendParticle));
							sendParticle.removeAll();

							formattedContents.add(Text.of(sendToPlayer));
						}
						PaginationList.builder()
						.contents(formattedContents)
						.title(Text.of("Particles in chunk - ", loc.getChunkPosition()))
						// .header(Text.of("Particles in chunk - ", loc.getChunkPosition()))
						.padding(Text.of("="))
						.sendTo(player);
					}
					else {
						player.sendMessage(Text.of("The chunk you are in does not currently have any particles loaded, wait 5 seconds then try again."));
					}
				}
				else {
					player.sendMessage(Text.of("The chunk you are in does not currently have any particles loaded, wait 5 seconds then try again."));
				}
			}
			return CommandResult.success();
		}
	}
	public static class deleteParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Double x,y,z;
			Player player = (Player) src;
			x = Double.valueOf(args.getOne(Text.of("x")).get().toString());
			y = Double.valueOf(args.getOne(Text.of("y")).get().toString());
			z = Double.valueOf(args.getOne(Text.of("z")).get().toString());
			Location loc = new Location(player.getLocation().getExtent(), x, y, z);
			Location playerLoc = player.getLocation();
			com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
			plotLoc.setX(playerLoc.getBlockX());
			plotLoc.setY(playerLoc.getBlockY());
			plotLoc.setZ(playerLoc.getBlockZ());
			plotLoc.setWorld(player.getLocation().getExtent().getName());
			if (Plot.getPlot(plotLoc) != null) {
				Plot plot = Plot.getPlot(plotLoc);
				if (plot.isAdded(player.getUniqueId()) || player.hasPermission("plots.admin.build.other")) {
					PlotParticles pp = DarwinParticlesMain.allPlotsWithParticles.get(player.getLocation().getExtent().getName() + ":" + plot.getId().toString());

					HashMap<Vector3i, HashMap<Location, ParticleEffect>> allParticles = pp.getParticles();
					if (allParticles.containsKey(loc.getChunkPosition()) && allParticles.get(loc.getChunkPosition()).containsKey(loc)) { 
						pp.removeFromMap(loc.getChunkPosition(), loc);
						player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "Removing particle."));
						DarwinParticlesMain.allPlotsWithParticles.put(player.getLocation().getExtent().getName() + ":" + plot.getId().toString(), pp);
						String tableName;
						if (player.getLocation().getExtent().getName().toLowerCase().contains("plot") || player.getLocation().getExtent().getName().toLowerCase().contains("contest")) {
							tableName = "Plots";
						}
						else {
							tableName = "PrivateWorlds";
						}
						try {
							System.out.println(tableName  + " " + player.getLocation().getExtent().getName() + " " + plot.getId().toString()  + " " +(loc.getBlockX() + "," + loc.getBlockY()  + "," + loc.getBlockZ()).toString());
							DarwinParticlesMain.db.removeFromDB(tableName, player.getLocation().getExtent().getName(), plot.getId().toString(), (loc.getX() + "," + loc.getY()  + "," + loc.getZ()).toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//also do database stuff
					}
					else {
						player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "This particle does not exist in this plot."));
						return CommandResult.success();
					}

				}
				else {
					player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "You must be trusted or added to remove particles."));
					return CommandResult.success();
				}
			}
			else {
				player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "You must be in the plot to delete particles."));
				return CommandResult.success();
			}
			return CommandResult.success();
		}

	}

	public static class teleportToParticle implements CommandExecutor {
		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			Double x,y,z;
			Player player = (Player) src;
			x = Double.valueOf(args.getOne(Text.of("x")).get().toString());
			y = Double.valueOf(args.getOne(Text.of("y")).get().toString());
			z = Double.valueOf(args.getOne(Text.of("z")).get().toString());
			Location loc = new Location(player.getLocation().getExtent(), x, y, z);
			com.intellectualcrafters.plot.object.Location plotLoc = new com.intellectualcrafters.plot.object.Location();
			plotLoc.setX(loc.getBlockX());
			plotLoc.setY(loc.getBlockY());
			plotLoc.setZ(loc.getBlockZ());
			plotLoc.setWorld(player.getLocation().getExtent().getName());
			if (Plot.getPlot(plotLoc) != null) {
				Plot plot = Plot.getPlot(plotLoc);
				if (plot.isAdded(player.getUniqueId()) || player.hasPermission("plots.admin.build.other")) {
					player.setLocation(loc);
				}
			}
			else {
				player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "You must be in the plot to teleport to particles."));
				return CommandResult.success();
			}
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
