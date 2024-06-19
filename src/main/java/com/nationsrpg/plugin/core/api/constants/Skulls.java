package com.nationsrpg.plugin.core.api.constants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.nationsrpg.plugin.core.helpers.InventoryUtils.skull;

public interface Skulls {
  ItemStack OAK_WOOD_REFRESH =
      skull(
          "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg4N2NjMzg4YzhkY2ZjZjFiYThhYTVjM2MxMDJkY2U5Y2Y3YjFiNjNlNzg2YjM0ZDRmMWMzNzk2ZDNlOWQ2MSJ9fX0=");
  ItemStack OAK_WOOD_ARROW_LEFT =
      skull(
          "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
  ItemStack OAK_WOOD_ARROW_RIGHT =
      skull(
          "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");

  Args1<Player> PLAYER = (p) -> skull(p.getName());

  interface Args1<A0> {
    ItemStack build(A0 arg0);
  }
}
