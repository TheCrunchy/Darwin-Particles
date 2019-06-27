package crunch.darwin.particles;

import java.util.UUID;

import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;

public class PlayerData {
	
	public PlayerData getPlayer(Player player) {
		if (DarwinParticlesMain.playerData.containsKey(player.getUniqueId())) {
			return DarwinParticlesMain.playerData.get(player.getUniqueId());
		}
		this.effect = ParticleEffect.builder()
				.type(ParticleTypes.SMOKE)
				.quantity(1)
				.build();
		interval = (long) 5;
		this.interval = (long) 5;
		this.quantity = 1;
		this.playerUUID = player.getUniqueId();
		return this;
	}
	
	private UUID playerUUID;
	private ParticleEffect effect;
	
	public void setEffect(ParticleEffect type) {
		this.effect = type;
		DarwinParticlesMain.playerData.put(playerUUID, this);
	}
	
	public ParticleEffect getEffect() {
		return effect;
	}
	
	private Integer quantity;
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
		DarwinParticlesMain.playerData.put(playerUUID, this);
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	private Long interval;
	
	public void setInterval(Long interval) {
		this.interval = interval;
		DarwinParticlesMain.playerData.put(playerUUID, this);
	}
	
	public Long getInterval() {
		return interval;
	}
}
