package crunch.darwin.particles;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashMap;

public class ParticleItemStack extends HashMap<String, ItemType> {

    public ParticleItemStack getAll() {
        // Sponge made me do this, I know it's horrible
        put("ambient_mob_spell", ItemTypes.ENDER_PEARL);
        put("angry_villager", ItemTypes.ENDER_EYE);
        put("barrier", ItemTypes.BARRIER);
        put("block_crack", ItemTypes.STONEBRICK);
        put("block_dust", ItemTypes.FLINT);
        put("break_block", ItemTypes.IRON_PICKAXE);
        put("cloud", ItemTypes.QUARTZ);
        put("critical_hit", ItemTypes.GOLDEN_SWORD);
        put("damage_indicator", ItemTypes.NETHER_WART);
        put("dragon_breath", ItemTypes.DRAGON_BREATH);
        put("dragon_breath_attack", ItemTypes.DRAGON_EGG);
        put("drip_lava", ItemTypes.LAVA_BUCKET);
        put("drip_water", ItemTypes.WATER_BUCKET);
        put("enchanting_glyphs", ItemTypes.ENCHANTED_BOOK);
        put("end_rod", ItemTypes.END_ROD);
        put("ender_teleport", ItemTypes.ENDER_PEARL);
        put("explosion", ItemTypes.TNT);
        put("falling_dust", ItemTypes.SAND);
        put("fertilizer", ItemTypes.DIRT);
        put("fire_smoke", ItemTypes.FIRE_CHARGE);
        put("fireworks", ItemTypes.FIREWORKS);
        put("fireworks_spark", ItemTypes.FIREWORK_CHARGE);
        put("flame", ItemTypes.FLINT_AND_STEEL);
        put("footstep", ItemTypes.CLAY_BALL);
        put("happy_villager", ItemTypes.EMERALD);
        put("heart", ItemTypes.RED_FLOWER);
        put("huge_explosion", ItemTypes.BLAZE_POWDER);
        put("instant_spell", ItemTypes.MAGMA_CREAM);
        put("item_crack", ItemTypes.CHORUS_FRUIT_POPPED);
        put("large_explosion", ItemTypes.PRISMARINE_SHARD);
        put("large_smoke", ItemTypes.COOKED_FISH);
        put("lava", ItemTypes.LAVA_BUCKET);
        put("magic_critical_hit", ItemTypes.BLAZE_ROD);
        put("mob_spell", ItemTypes.GHAST_TEAR);
        put("mobspawner_flames", ItemTypes.NETHER_STAR);
        put("note", ItemTypes.NOTEBLOCK);
        put("portal", ItemTypes.END_PORTAL_FRAME);
        put("redstone_dust", ItemTypes.REDSTONE);
        put("slime", ItemTypes.SLIME_BALL);
        put("smoke", ItemTypes.RABBIT_HIDE);
        put("snow_shovel", ItemTypes.STONE_SHOVEL);
        put("snowball", ItemTypes.SNOWBALL);
        put("spell", ItemTypes.SPECKLED_MELON);
        put("splash_potion", ItemTypes.SPLASH_POTION);
        put("suspended", ItemTypes.PRISMARINE_SHARD);
        put("suspended_depth", ItemTypes.SHULKER_SHELL);
        put("sweep_attack", ItemTypes.DIAMOND_SWORD);
        put("town_aura", ItemTypes.ANVIL);
        put("water_bubble", ItemTypes.GLASS_BOTTLE);
        put("water_drop", ItemTypes.POTION);
        put("water_splash", ItemTypes.LINGERING_POTION);
        put("water_wake", ItemTypes.EXPERIENCE_BOTTLE);
        put("witch_spell", ItemTypes.SPIDER_EYE);

        return this;
    }

}
