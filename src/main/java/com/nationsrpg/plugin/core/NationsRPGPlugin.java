package com.nationsrpg.plugin.core;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import com.nationsrpg.plugin.core.api.map.SpawnMap;
import com.nationsrpg.plugin.core.commands.NationCommand;
import com.nationsrpg.plugin.core.managers.AddonManager;
import com.nationsrpg.plugin.core.managers.NationManager;
import com.nationsrpg.plugin.core.managers.UserManager;
import com.nationsrpg.plugin.core.models.nation.NationStructure;
import com.nationsrpg.plugin.core.models.user.UserStructure;
import me.byteful.lib.datastore.api.ModelManager;
import me.byteful.lib.datastore.api.data.DataStore;
import me.byteful.lib.datastore.mysql.MySQLDataStore;
import me.lucko.helper.config.ConfigurationNode;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.utils.Log;

import java.lang.reflect.InvocationTargetException;

public final class NationsRPGPlugin extends ExtendedJavaPlugin {
  private static NationsRPGPlugin instance;

  private DataStore dataStore;
  private ConfigurationNode settings;
  private SpawnMap spawn;
  private AddonManager addonManager;
  private BukkitCommandManager commandManager;
  private NationManager nationManager;
  private UserManager userManager;

  public static NationsRPGPlugin getInstance() {
    return instance;
  }

  @Override
  protected void enable() {
    instance = this;

    settings = loadConfigNode("config.yml");
    Log.info("Loaded configuration...");

    loadDataStore();
    Log.info("Connected to data storage servers...");

    nationManager = new NationManager(this);
    userManager = new UserManager(this);
    Log.info("Initialized user and nation managers...");

    loadSpawnMap();
    Log.info("Loaded spawn map...");

    addonManager = new AddonManager(this);
    Log.info("Loaded addons...");

    commandManager = new PaperCommandManager(this);
    commandManager.enableUnstableAPI("brigadier");
    commandManager.enableUnstableAPI("help");
    registerCommands();
    Log.info("Registered commands...");

    Log.info("Successfully started NationsRPG Core Plugin. All core services are now available.");
  }

  @Override
  protected void disable() {
    commandManager.unregisterCommands();
    Log.info("Unregistered commands...");

    Log.info(
        "Successfully stopped NationsRPG Core Plugin. All core services are no longer available.");

    instance = null;
  }

  private void loadDataStore() {
    final ConfigurationNode mysql = settings.getNode("mysql");
    dataStore =
        new MySQLDataStore(
            mysql.getNode("host").getString("localhost"),
            mysql.getNode("port").getInt(3306),
            mysql.getNode("user").getString("root"),
            mysql.getNode("pass").getString("password"),
            mysql.getNode("database").getString("NationsRPG"));
    bind(dataStore);

    ModelManager.registerModelStructure(new NationStructure(this));
    ModelManager.registerModelStructure(new UserStructure(this));
  }

  private void registerCommands() {
    commandManager.registerCommand(new NationCommand());
  }

  private void loadSpawnMap() {
    final String mapClass =
        settings.getNode("map").getString("com.nationsrpg.plugin.core.maps.ClassicSpawnMap");

    try {
      spawn = Class.forName(mapClass).asSubclass(SpawnMap.class).getConstructor().newInstance();
      spawn.initialize();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException
        | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public DataStore getDataStore() {
    return dataStore;
  }

  public ConfigurationNode getSettings() {
    return settings;
  }

  public SpawnMap getSpawn() {
    return spawn;
  }

  public AddonManager getAddonManager() {
    return addonManager;
  }

  public BukkitCommandManager getCommandManager() {
    return commandManager;
  }

  public NationManager getNationManager() {
    return nationManager;
  }

  public UserManager getUserManager() {
    return userManager;
  }
}
