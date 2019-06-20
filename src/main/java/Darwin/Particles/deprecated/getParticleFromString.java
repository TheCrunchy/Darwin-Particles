package Darwin.Particles.deprecated;

import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;

public class getParticleFromString {
	public static ParticleEffect get(String type, Integer quantity) {
		ParticleEffect effect = null;
		switch (type) {
		  case "ambient_mob_spell":
			  effect = ParticleEffect.builder()
			    .type(ParticleTypes.AMBIENT_MOB_SPELL)
				.quantity(quantity)
				.build();
		    break;
		  case "angry_villager":
			    effect = ParticleEffect.builder()
			    .type(ParticleTypes.ANGRY_VILLAGER)
				.quantity(quantity)
				.build();
		    break;
		  case "barrier":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.BARRIER)
						.quantity(quantity)
						.build();
		    break;
		  case "block_crack":
			  effect = ParticleEffect.builder()
			   .type(ParticleTypes.BLOCK_CRACK)
						.quantity(quantity)
						.build();
		    break;
		  case "block_dust":
			  effect = ParticleEffect.builder()
			   .type(ParticleTypes.BLOCK_DUST)
						.quantity(quantity)
						.build();
		    break;
		  case "block_break":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.BREAK_BLOCK)
						.quantity(quantity)
						.build();
		    break;
		  case "cloud":
		  effect = ParticleEffect.builder()
		  .type(ParticleTypes.CLOUD)
					.quantity(quantity)
					.build();
		    break;
		  case "critical_hit":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.CRITICAL_HIT)
						.quantity(quantity)
						.build();
			    break;
		  case "damage_indicator":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.DAMAGE_INDICATOR)
						.quantity(quantity)
						.build();
			    break;
		  case "dragon_breath":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.DRAGON_BREATH)
						.quantity(quantity)
						.build();
			    break;
		  case "dragon_breath_attack":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.DRAGON_BREATH_ATTACK)
						.quantity(quantity)
						.build();
			    break;
		  case "drip_lava":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.DRIP_LAVA)
						.quantity(quantity)
						.build();
			    break;
		  case "drip_water":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.DRIP_WATER)
						.quantity(quantity)
						.build();
			    break;
		  case "enchanting_glyphs":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.ENCHANTING_GLYPHS)
						.quantity(quantity)
						.build();
			    break;
		  case "end_rod":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.END_ROD)
						.quantity(quantity)
						.build();
			    break;
		  case "ender_teleport":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.ENDER_TELEPORT)
						.quantity(quantity)
						.build();
			    break;
		  case "explosion":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.EXPLOSION)
						.quantity(quantity)
						.build();
			    break;
		  case "falling_dust":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.FALLING_DUST)
						.quantity(quantity)
						.build();
			    break;
		  case "fertilizer":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.FERTILIZER)
						.quantity(quantity)
						.build();
			    break;
		  case "fire_smoke":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.FIRE_SMOKE)
						.quantity(quantity)
						.build();
			    break;
		  case "fireworks":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.FIREWORKS)
						.quantity(quantity)
						.build();
			    break;
		  case "fireworks_spark":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.FIREWORKS_SPARK)
						.quantity(quantity)
						.build();
			    break;
		  case "flame":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.FLAME)
						.quantity(quantity)
						.build();
			    break;
		  case "footstep":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.FOOTSTEP)
						.quantity(quantity)
						.build();
			    break;
		  case "happy_villager":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.HAPPY_VILLAGER)
						.quantity(quantity)
						.build();
			    break;
		  case "heart":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.HEART)
						.quantity(quantity)
						.build();
			    break;
		  case "huge_explosion":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.HUGE_EXPLOSION)
						.quantity(quantity)
						.build();
			    break;
		  case "instant_spell":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.INSTANT_SPELL)
						.quantity(quantity)
						.build();
			    break;
		  case "item_crack":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.ITEM_CRACK)
						.quantity(quantity)
						.build();
			    break;
		  case "large_explosion":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.LARGE_EXPLOSION)
						.quantity(quantity)
						.build();
			    break;
		  case "large_smoke":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.LARGE_SMOKE)
						.quantity(quantity)
						.build();
			    break;
		  case "lava":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.LAVA)
						.quantity(quantity)
						.build();
			    break;
		  case "magic_critical_hit":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.MAGIC_CRITICAL_HIT)
						.quantity(quantity)
						.build();
			    break;
		  case "mob_spell":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.MOB_SPELL)
						.quantity(quantity)
						.build();
			    break;
		  case "mobspawner_flames":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.MOBSPAWNER_FLAMES)
						.quantity(quantity)
						.build();
			    break;
		  case "note":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.NOTE)
						.quantity(quantity)
						.build();
			    break;
		  case "portal":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.PORTAL)
						.quantity(quantity)
						.build();
			    break;
		  case "redstone_dust":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.REDSTONE_DUST)
						.quantity(quantity)
						.build();
			    break;
		  case "slime":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.SLIME)
						.quantity(quantity)
						.build();
			    break;
		  case "smoke":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.SMOKE)
						.quantity(quantity)
						.build();
			    break;
		  case "spell":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.SPELL)
						.quantity(quantity)
						.build();
			    break;
		  case "splash_potion":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.SPLASH_POTION)
						.quantity(quantity)
						.build();
			    break;
		  case "suspended":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.SUSPENDED)
						.quantity(quantity)
						.build();
			    break;
		  case "suspended_depth":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.SUSPENDED_DEPTH)
						.quantity(quantity)
						.build();
			    break;
		  case "sweep_attack":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.SWEEP_ATTACK)
						.quantity(quantity)
						.build();
			    break;
		  case "town_aura":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.TOWN_AURA)
						.quantity(quantity)
						.build();
			    break;
		  case "water_bubble":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.WATER_BUBBLE)
						.quantity(quantity)
						.build();
			    break;
		  case "water_drop":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.WATER_DROP)
						.quantity(quantity)
						.build();
			    break;
		  case "water_splash":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.WATER_SPLASH)
						.quantity(quantity)
						.build();
			    break;
		  case "water_wake":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.WATER_WAKE)
						.quantity(quantity)
						.build();
			    break;
		  case "witch_spell":
			  effect = ParticleEffect.builder()
			  .type(ParticleTypes.WITCH_SPELL)
						.quantity(quantity)
						.build();
			    break;
		}
		return effect;
	}
}
