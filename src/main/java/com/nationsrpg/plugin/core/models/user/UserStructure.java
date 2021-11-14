package com.nationsrpg.plugin.core.models.user;

import me.byteful.lib.datastore.api.model.ModelStructure;
import me.byteful.lib.datastore.api.model.ProcessedModel;
import me.byteful.lib.datastore.api.model.impl.JSONProcessedModel;
import me.lucko.helper.gson.GsonProvider;
import org.jetbrains.annotations.NotNull;

public class UserStructure implements ModelStructure<User> {
  @Override
  public @NotNull ProcessedModel serialize(@NotNull User user) {
    return new JSONProcessedModel(GsonProvider.standard().toJson(user), GsonProvider.standard());
  }

  @Override
  public @NotNull User deserialize(@NotNull ProcessedModel processedModel) {
    if (processedModel instanceof JSONProcessedModel) {
      return GsonProvider.standard()
          .fromJson(((JSONProcessedModel) processedModel).toJSON(), User.class);
    }

    throw new IllegalStateException("Bad data store.");
  }

  @Override
  public @NotNull Class<User> getModelType() {
    return User.class;
  }
}
