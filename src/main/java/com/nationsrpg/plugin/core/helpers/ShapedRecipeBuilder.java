package com.nationsrpg.plugin.core.helpers;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShapedRecipeBuilder {
  @NotNull private final NamespacedKey key;

  private ItemStack result;
  private String[] shape;
  private Map<Character, Object> ingredients;

  private ShapedRecipeBuilder(@NotNull NamespacedKey key) {
    this.key = key;
  }

  @NotNull
  public static ShapedRecipeBuilder of(@NotNull NamespacedKey key) {
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
    if (result == null || shape == null || ingredients == null) {
      throw new IllegalStateException("Not all builder arguments were fulfilled.");
    }

    final ShapedRecipe recipe = new ShapedRecipe(key, result).shape(this.shape);
    recipe.setIngredient(' ', Material.AIR);

    ingredients.forEach(
        (k, v) -> {
          if (v instanceof Material) {
            recipe.setIngredient(k, (Material) v);
          } else if (v instanceof RecipeChoice) {
            recipe.setIngredient(k, (RecipeChoice) v);
          } else if (v instanceof ItemStack) {
            recipe.setIngredient(k, (ItemStack) v);
          }
        });

    return recipe;
  }
}
