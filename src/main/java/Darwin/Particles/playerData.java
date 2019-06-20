package Darwin.Particles;

import org.spongepowered.api.effect.particle.ParticleType;

public class playerData {
	private ParticleType effect;
	
	public void setEffect(ParticleType type) {
		this.effect = type;
	}
	
	public ParticleType getType() {
		return effect;
	}
	
	private int quantity;
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
}
