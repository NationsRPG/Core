package com.nationsrpg.plugin.core.guis.misc;

import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.promise.Promise;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ConfirmGUI extends Gui {
  private final Promise<Boolean> promise;
  private final ItemStack showcase;

  private ConfirmGUI(
      Player player,
      int lines,
      String title,
      @NotNull Promise<Boolean> promise,
      ItemStack showcase) {
    super(player, lines, title);

    this.promise = promise;
    this.showcase = showcase;
  }

  public static void open(
      @NotNull Player player, @NotNull ItemStack showcase, @NotNull Consumer<Boolean> callback) {
    final ConfirmGUI gui = new ConfirmGUI(player, 3, "&fConfirmation", Promise.empty(), showcase);
    gui.promise.thenAcceptSync(callback);

    gui.open();
  }

  @Override
  public void redraw() {
    if (isFirstDraw()) {
      bind(
          () -> {
            if (!promise.isDone()) {
              promise.supply(false);
            }
          });

      final Item accept =
          ItemStackBuilder.of(Material.GREEN_STAINED_GLASS_PANE)
              .build(
                  () -> {
                    promise.supply(true);
                    close();
                  });

      final Item showcaseButton = Item.builder(showcase.clone()).bind(() -> {}).build();

      final Item deny =
          ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE)
              .build(
                  () -> {
                    promise.supply(false);
                    close();
                  });

      InventoryUtils.populateBackground(this);

      InventoryUtils.setSlot(this, 5, 2, showcaseButton);

      InventoryUtils.fill(this, 1, 1, 4, 3, accept);
      InventoryUtils.fill(this, 6, 1, 9, 3, deny);
    }
  }
}
