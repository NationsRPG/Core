package com.nationsrpg.plugin.core.models.profile;

import me.byteful.lib.datastore.api.model.ModelStructure;
import me.byteful.lib.datastore.api.model.ProcessedModel;
import me.byteful.lib.datastore.api.model.impl.JSONProcessedModel;
import me.lucko.helper.gson.GsonProvider;
import org.jetbrains.annotations.NotNull;

public class ProfileStructure implements ModelStructure<Profile> {
  @Override
  public @NotNull ProcessedModel serialize(@NotNull Profile profile) {
    return new JSONProcessedModel(GsonProvider.standard().toJson(profile), GsonProvider.standard());
  }

  @Override
  public @NotNull Profile deserialize(@NotNull ProcessedModel processedModel) {
    if (processedModel instanceof JSONProcessedModel) {
      return GsonProvider.standard()
          .fromJson(((JSONProcessedModel) processedModel).toJSON(), Profile.class);
    }

    throw new IllegalStateException("Bad data store.");
  }

  @Override
  public @NotNull Class<Profile> getModelType() {
    return Profile.class;
  }
}
