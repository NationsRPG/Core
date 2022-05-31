package com.nationsrpg.plugin.core.api.constants;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.paginated.PageInfo;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.menu.scheme.StandardSchemeMappings;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

public interface PaginatedSettings {
  MenuScheme LAYOUT =
      new MenuScheme(StandardSchemeMappings.STAINED_GLASS)
          .mask("111111111")
          .scheme(7, 7, 7, 7, 7, 7, 7, 7, 7)
          .maskEmpty(5);

  List<Integer> ITEM_INDEXES =
      new MenuScheme()
          .mask("000000000")
          .mask("111111111")
          .mask("111111111")
          .mask("111111111")
          .mask("111111111")
          .mask("111111111")
          .getMaskedIndexes();

  Function<PageInfo, ItemStack> NEXT_PAGE_ITEM =
      pageInfo ->
          ItemStackBuilder.of(Skulls.OAK_WOOD_ARROW_RIGHT)
              .name("&f&lNext")
              .lore("&7Switch to the next page.")
              .lore("")
              .lore(
                  "&7Currently viewing page &b"
                      + pageInfo.getCurrent()
                      + "&3/&b"
                      + pageInfo.getSize()
                      + ".")
              .build();

  Function<PageInfo, ItemStack> PREVIOUS_PAGE_ITEM =
      pageInfo ->
          ItemStackBuilder.of(Skulls.OAK_WOOD_ARROW_LEFT)
              .name("&f&lPrevious")
              .lore("&7Switch to the previous page.")
              .lore("")
              .lore(
                  "&7Currently viewing page &b"
                      + pageInfo.getCurrent()
                      + "&3/&b"
                      + pageInfo.getSize()
                      + ".")
              .build();

  int PREVIOUS_ITEM_SLOT = 2;
  int NEXT_ITEM_SLOT = 6;
  int LINES = 6;
}
