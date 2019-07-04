package crunch.darwin.particles;

import com.mcsimonflash.sponge.teslalibs.inventory.Action;
import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class UserInterface {

  static PluginContainer container =
      Sponge.getPluginManager().getPlugin("darwinparticles").orElse(null);

  private static ParticleItemStack particleItemStack = new ParticleItemStack().getAll();

  private static List particles = new ArrayList(particleItemStack.keySet());

  public UserInterface(Player player) {
    // Layout builder
    Layout.Builder builder = new Layout.Builder().dimension(InventoryDimension.of(9, 6));

    // Construct the particles
    particleItemStack.entrySet().forEach(entry -> constructAndPutItem(entry, builder, player));

    // Construct the layout and archetype
    Layout layout = builder.build();

    // Construct and open the View
    View view =
        View.builder()
            .archetype(InventoryArchetypes.DOUBLE_CHEST)
            .property(InventoryTitle.of(Text.of(TextColors.AQUA, "Particles")))
            .build(container)
            .define(layout);
    view.open(player);
  }

  private void constructAndPutItem(
      Map.Entry<String, ItemType> entry, Layout.Builder builder, Player player) {
    int index = particles.indexOf(entry.getKey());
    ItemStack itemStack = ItemStack.builder().itemType(entry.getValue()).build();

    String particleName = entry.getKey();
    String[] displayParts = particleName.replaceAll("_", " ").split(" ");
    IntStream.range(0, displayParts.length)
        .forEach(
            i -> {
              String part = displayParts[i];
              displayParts[i] =
                  part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
            });
    String displayName = String.join(" ", displayParts);
    itemStack.offer(Keys.DISPLAY_NAME, Text.of(TextColors.DARK_AQUA, displayName));

    List<Text> lore = new ArrayList<>();
    lore.add(Text.of(TextColors.AQUA, "Click to select particle type"));
    itemStack.offer(Keys.ITEM_LORE, lore);

    Consumer<Action.Click> action =
        a -> new QuantityInterface(particleName, displayName, itemStack, player);

    Element element = Element.of(itemStack, action);
    builder.set(element, index);
  }

  private static class QuantityInterface {

    private int maxQuantity;
    private int currentQuantity = 1;

    private long minInterval = 1;
    private long currentInterval = 1;

    private Layout.Builder builder = new Layout.Builder().dimension(InventoryDimension.of(9, 3));
    private ItemStack offerStack;
    private List<Text> lore = new ArrayList<>();
    private Player player;
    private String particleName;

    QuantityInterface(
        String particleName, String particleDisplayName, ItemStack particleObject, Player player) {
      this.particleName = particleName;
      this.player = player;

      offerStack =
          ItemStack.builder()
              .itemType(particleObject.getType())
              .build(); // Prevent copying keys from previous screens
      offerStack.offer(
          Keys.DISPLAY_NAME, Text.of(TextColors.DARK_AQUA, "Click to summon particles"));
      lore.add(0, Text.of(TextColors.AQUA, "Particle : " + particleDisplayName));
      lore.add(1, Text.of(TextColors.AQUA, "Quantity : " + currentQuantity));
      lore.add(2, Text.of(TextColors.AQUA, "Interval : " + currentInterval + " seconds"));
      offerStack.offer(Keys.ITEM_LORE, lore);

      maxQuantity = PlotParticles.getPlayerQuantityLimit(player);

      addIntervalButton(-10, 1);
      addIntervalButton(-5, 2);
      addIntervalButton(-1, 3);

      addIntervalButton(1, 5);
      addIntervalButton(5, 6);
      addIntervalButton(10, 7);

      addIncrementButton(-10, 10);
      addIncrementButton(-5, 11);
      addIncrementButton(-1, 12);

      addIncrementButton(1, 14);
      addIncrementButton(5, 15);
      addIncrementButton(10, 16);

      updateView();
    }

    private void addIntervalButton(int increment, int index) {
      ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.BRICK).build();
      itemStack.offer(
          Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Update interval by : " + increment));
      builder.set(Element.of(itemStack, a -> countInterval(increment)), index);
    }

    private void addIncrementButton(int increment, int index) {
      ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.BARRIER).build();
      itemStack.offer(
          Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "Update quantity by : " + increment));
      builder.set(Element.of(itemStack, a -> countQuantity(increment)), index);
    }

    private void countQuantity(int increment) {
      currentQuantity =
          currentQuantity + increment <= maxQuantity
              ? currentQuantity + increment
              : currentQuantity;
      lore.remove(1);
      lore.add(1, Text.of(TextColors.AQUA, "Quantity : " + currentQuantity));
      offerStack.remove(Keys.ITEM_LORE);
      offerStack.offer(Keys.ITEM_LORE, lore);
      updateView();
    }

    private void countInterval(int increment) {
      currentInterval =
          currentInterval + increment >= minInterval
              ? currentInterval + increment
              : currentInterval;
      lore.remove(2);
      lore.add(2, Text.of(TextColors.AQUA, "Interval : " + currentInterval + " seconds"));
      offerStack.remove(Keys.ITEM_LORE);
      offerStack.offer(Keys.ITEM_LORE, lore);
      updateView();
    }

    private void updateView() {
      builder.set(
          Element.of(
              offerStack,
              a ->
                  callback(
                      particleName,
                      player,
                      player.getLocation(),
                      currentQuantity,
                      currentInterval)),
          22);
      Layout layout = builder.build();
      View view =
          View.builder()
              .archetype(InventoryArchetypes.CHEST)
              .property(InventoryTitle.of(Text.of(TextColors.AQUA, "Particles")))
              .build(container)
              .define(layout);
      view.open(player);
    }

    private void callback(
        String particleName, Player player, Location location, int quantity, Long interval) {
      ParticleEffect effect = GetParticleFromString.get(particleName, quantity);
      PlayerData pd = new PlayerData();
      if (PlotParticles.getPlayerQuantityLimit(player) >= quantity) {
    	  player.sendMessage(Text.of(DarwinParticlesMain.particlesDefault, "That quantity is above your limit of ", PlotParticles.getPlayerQuantityLimit(player)));
    	  return;
      }
      pd.setEffect(effect);
      
      pd.setQuantity(quantity);
      pd.setInterval(interval);
      DarwinParticlesMain.playerData.put(player.getUniqueId(), pd);
      DarwinParticlesMain.addNewParticle(location, player);

      System.out.printf(
          "> Constructed : %n\tParticle : %s%n\tPlayer : %s%n\tLocation : %s%n\tQuantity : %d%n\tInterval : %d seconds",
          particleName,
          player.getName(),
          (location.getBlockX()
              + ", "
              + location.getBlockY()
              + ", "
              + location.getBlockZ()
              + " in "
              + ((World) location.getExtent()).getName()),
          quantity,
          interval);
    }
  }
}
