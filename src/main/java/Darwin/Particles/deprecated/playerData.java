package Darwin.Particles.deprecated;

import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;

public class playerData {
	private ParticleEffect effect;
	
	public void setEffect(ParticleEffect type) {
		this.effect = type;
	}
	
	public ParticleEffect getEffect() {
		return effect;
	}
	
	private Integer quantity;
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	private Long interval;
	
	public void setInterval(Long interval) {
		this.interval = interval;
	}
	
	public Long getInterval() {
		return interval;
	}
}
