package com.nationsrpg.plugin.core.guis.misc;

import com.nationsrpg.plugin.core.api.constants.Skulls;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.paginated.PaginatedGuiBuilder;
import me.lucko.helper.text3.Text;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.nationsrpg.plugin.core.api.constants.PaginatedSettings.*;

public class PlayersGUI {
  public static void open(@NotNull Player openFor, @NotNull Consumer<Player> callback) {
    PaginatedGuiBuilder.create()
        .title(Text.colorize("&fPlayers"))
        .lines(LINES)
        .scheme(LAYOUT)
        .itemSlots(ITEM_INDEXES)
        .nextPageSlot(NEXT_ITEM_SLOT)
        .previousPageSlot(PREVIOUS_ITEM_SLOT)
        .nextPageItem(NEXT_PAGE_ITEM)
        .previousPageItem(PREVIOUS_PAGE_ITEM)
        .build(
            openFor,
            (gui) -> {
              final List<Item> items = new ArrayList<>();

              Players.forEach(
                  player -> {
                    final Item button =
                        ItemStackBuilder.of(Skulls.PLAYER.build(player))
                            .name("&e" + player.getName())
                            .lore("&7Left click me to", "&7select " + player.getName() + ".")
                            .hideAttributes()
                            .build(() -> callback.accept(player));
                    items.add(button);
                  });

              return items;
            })
        .open();
  }
}
