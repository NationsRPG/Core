package com.nationsrpg.plugin.core.models.user;

import me.byteful.lib.datastore.api.model.ModelStructure;
import me.byteful.lib.datastore.api.model.ProcessedModel;
import me.byteful.lib.datastore.api.model.ProcessedModelFieldType;
import me.byteful.lib.datastore.api.model.impl.JSONProcessedModel;
import me.lucko.helper.gson.GsonProvider;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class UserStructure implements ModelStructure<User> {
  @Override
  public @NotNull ProcessedModel serialize(@NotNull User user) {
    return new JSONProcessedModel(GsonProvider.standard())
        .append("id", ProcessedModelFieldType.UNIQUE_INDEXED, user.uuid())
        .append("balance", ProcessedModelFieldType.NORMAL, user.balance());
  }

  @Override
  public @NotNull User deserialize(@NotNull ProcessedModel processedModel) {
    if (processedModel.has("id") && processedModel.has("balance")) {
      return new User(processedModel.get("id", UUID.class).orElseThrow(), processedModel.get("balance", Double.class).orElseThrow());
    }

    throw new IllegalArgumentException("Outdated model structure! Please modify database manually.");
  }

  @Override
  public @NotNull Class<User> getModelType() {
    return User.class;
  }
}
