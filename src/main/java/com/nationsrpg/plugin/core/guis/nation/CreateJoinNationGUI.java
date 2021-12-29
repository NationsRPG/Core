package com.nationsrpg.plugin.core.guis.nation;

import com.nationsrpg.plugin.core.helpers.InventoryUtils;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CreateJoinNationGUI extends Gui {
  private static final MenuScheme LAYOUT =
      new MenuScheme()
          .mask("000000000")
          .mask("000000000")
          .mask("000101000")
          .mask("000000000")
          .mask("000000000")
          .mask("000000000");

  private CreateJoinNationGUI(Player player, int lines, String title) {
    super(player, lines, title);
  }

  public static void open(@NotNull Player player) {
    new CreateJoinNationGUI(player, 6, "&f&lCreate or join a nation.").open();
  }

  @Override
  public void redraw() {
    final List<Item> items =
        Arrays.asList(
            // Create a nation
            ItemStackBuilder.of(Material.ANVIL)
                .build(
                    () -> {
                      close();

                      CreateNationGUI.open(getPlayer());
                    }),

            // Join a nation
            ItemStackBuilder.of(Material.RED_WOOL)
                .build(
                    () -> {
                      close();

                      JoinNationGUI.open(getPlayer());
                    }));

    InventoryUtils.populateBackground(this);

    final MenuPopulator populator = LAYOUT.newPopulator(this);
    items.forEach(populator::acceptIfSpace);

    InventoryUtils.populateTools(this, 6);
  }

  private static class CreateNationGUI extends Gui {
    private CreateNationGUI(Player player, int lines, String title) {
      super(player, lines, title);
    }

    protected static void open(@NotNull Player player) {
      new CreateNationGUI(player, 6, "&f&lCreate a nation.");
    }

    @Override
    public void redraw() {

    }
  }

  private static class JoinNationGUI extends Gui {
    private JoinNationGUI(Player player, int lines, String title) {
      super(player, lines, title);
    }

    protected static void open(@NotNull Player player) {
      new JoinNationGUI(player, 6, "&f&lJoin a nation.");
    }

    @Override
    public void redraw() {

    }
  }
}
