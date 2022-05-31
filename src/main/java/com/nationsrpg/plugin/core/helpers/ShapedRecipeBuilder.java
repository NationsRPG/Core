package com.nationsrpg.plugin.core.helpers;

import com.google.common.base.Preconditions;
import com.nationsrpg.plugin.core.NationsRPGPlugin;
import com.nationsrpg.plugin.core.api.addon.AbstractItemStackAddon;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShapedRecipeBuilder {
  @NotNull private final NamespacedKey key;

  private ItemStack result;
  private String[] shape;
  private Map<Character, Object> ingredients;

  private ShapedRecipeBuilder(@NotNull NamespacedKey key) {
    this.key = key;
  }

  @NotNull
  public static ShapedRecipeBuilder builder(@NotNull NamespacedKey key) {
    return new ShapedRecipeBuilder(key);
  }

  @NotNull
  public ShapedRecipeBuilder result(@NotNull ItemStack result) {
    this.result = result;

    return this;
  }

  @NotNull
  public ItemStack result() {
    return result;
  }

  @NotNull
  public ShapedRecipeBuilder shape(
      @NotNull String line1, @NotNull String line2, @NotNull String line3) {
    this.shape = new String[] {line1, line2, line3};

    return this;
  }

  @NotNull
  public String[] shape() {
    return shape;
  }

  @NotNull
  public ShapedRecipeBuilder ingredients(@NotNull Object... map) {
    this.ingredients = new HashMap<>();

    if (map.length % 2 != 0) {
      throw new IllegalArgumentException("Invalid size map: " + Arrays.toString(map));
    }

    for (int i = 0; i < map.length; i += 2) {
      ingredients.put((Character) map[i], map[i + 1]);
    }

    return this;
  }

  @NotNull
  public ShapedRecipe build() {
    Preconditions.checkNotNull(result, "Result was not set.");
    Preconditions.checkNotNull(shape, "Shape was not set.");
    Preconditions.checkArgument(shape.length == 3, "Shape is not fully set.");
    Preconditions.checkNotNull(ingredients, "Ingredients were not set.");

    final ShapedRecipe recipe = new ShapedRecipe(key, result).shape(this.shape);
    if (shape[0].contains(" ") || shape[1].contains(" ") || shape[2].contains(" ")) {
      recipe.setIngredient(' ', Material.AIR);
    }

    ingredients.forEach(
        (k, v) -> {
          if (v instanceof Material) {
            recipe.setIngredient(k, new ItemStack((Material) v, 1));
          } else if (v instanceof RecipeChoice) {
            recipe.setIngredient(k, (RecipeChoice) v);
          } else if (v instanceof ItemStack) {
            recipe.setIngredient(k, (ItemStack) v);
          } else if (v instanceof String) {
            final AbstractItemStackAddon addon =
                (AbstractItemStackAddon)
                    NationsRPGPlugin.getInstance().getAddonManager().getAddons().get(v);
            recipe.setIngredient(k, Objects.requireNonNull(addon).buildItemStack());
          }
        });

    return recipe;
  }
}
