package com.nationsrpg.plugin.core.guis.misc;

import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.promise.Promise;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ConfirmGUI extends Gui {
  private final Promise<Boolean> promise;

  private ConfirmGUI(Player player, int lines, String title, @NotNull Promise<Boolean> promise) {
    super(player, lines, title);

    this.promise = promise;
  }

  public static void open(@NotNull Player player, @NotNull Consumer<Boolean> callback) {
    final ConfirmGUI gui = new ConfirmGUI(player, 3, "&f&lConfirmation", Promise.empty());
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
                    close();
                    promise.supply(true);
                  });

      final Item deny =
          ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE)
              .build(
                  () -> {
                    close();
                    promise.supply(false);
                  });

      InventoryUtils.populateBackground(this);

      InventoryUtils.fill(this, 1, 1, 4, 3, accept);
      InventoryUtils.fill(this, 6, 1, 9, 3, deny);
    }
  }
}
