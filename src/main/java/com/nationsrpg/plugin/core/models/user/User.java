package com.nationsrpg.plugin.core.models.user;

import com.nationsrpg.plugin.core.NationsRPGPlugin;
import me.byteful.lib.datastore.api.data.StoredGroup;
import me.byteful.lib.datastore.api.model.Model;
import me.byteful.lib.datastore.api.model.impl.JSONModelId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@StoredGroup("users")
public record User(@NotNull NationsRPGPlugin plugin, @NotNull UUID uuid, @NotNull UserSettings settings, @Nullable UUID nationUUID,
                   @NotNull Double balance) implements Model {
  @NotNull
  public static User create(@NotNull NationsRPGPlugin plugin, @NotNull UUID uuid) {
    final User user = new User(plugin, uuid, UserSettings.defaultSettings(), null, 0.0D);
    user.update();

    return user;
  }

  public void update() {
    plugin.getDataStore().set(JSONModelId.of("id", uuid.toString()), this);
  }

  public void delete() {
    plugin.getDataStore().delete(User.class, JSONModelId.of("id", uuid.toString()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return uuid.equals(user.uuid) && balance.equals(user.balance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, balance);
  }

  @Override
  public String toString() {
    return "User{" +
        "uuid=" + uuid +
        ", balance=" + balance +
        '}';
  }
}
