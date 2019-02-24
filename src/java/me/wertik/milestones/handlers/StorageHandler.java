package me.wertik.milestones.handlers;

import me.wertik.milestones.Main;
import me.wertik.milestones.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageHandler {

    private Main plugin;
    private YamlConfiguration storage;
    private File storageFile;
    private ConfigurationSection toggles;
    private ConfigurationSection items;

    public StorageHandler() {
        plugin = Main.getInstance();
    }

    public void setYamls() {

        // Other data
        storageFile = new File(plugin.getDataFolder() + "/datastorage.yml");

        if (!storageFile.exists()) {
            plugin.saveResource("datastorage.yml", false);
            plugin.getServer().getConsoleSender().sendMessage("§aGenerated default §f" + storageFile.getName());
        }

        storage = YamlConfiguration.loadConfiguration(storageFile);

        toggles = storage.getConfigurationSection("Toggles");

        items = storage.getConfigurationSection("Items");

        for (String milestoneName : Main.getInstance().getConfigLoader().getMileNames()) {
            if (!toggles.contains(milestoneName)) {
                ConfigurationSection section = toggles.createSection(milestoneName);
                section.set("global-enabled", true);
            }
            for (String worldName : Main.getInstance().getWorldNames()) {
                if (!toggles.getConfigurationSection(milestoneName).contains(worldName)) {
                    toggles.getConfigurationSection(milestoneName).set(worldName, new ArrayList<>());
                }
            }
        }

        saveStorage();
    }

    public List<ItemStack> parseForItemPlaceholders(List<String> list) {
        List<ItemStack> outPut = new ArrayList<>();

        for (String row : list) {
            if (containsItemHolder(row)) {
                if (storage.getConfigurationSection("Items").contains(row.replace("%item_", "").replace("%", "")))
                    outPut.add(parseForItem(row));
                else
                    plugin.getServer().getLogger().warning("Item " + row.replace("%item_", "").replace("%", "") + " is not saved in the data storage, cannot use it.");
                continue;
            } else
                outPut.add(new ItemStack(Material.valueOf(row.toUpperCase()), -1));
        }
        return outPut;
    }

    public void saveStorage() {
        try {
            storage.save(storageFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("§cCould not save the storage.. idk what to do, send help.");
            return;
        }
    }

    // TOGGLES SECTION

    // ITEM DATA STORAGE

    // %item_(name)%
    public boolean containsItemHolder(String msg) {
        if (msg.contains("%item_")) {
            return true;
        }
        return false;
    }

    public String parseForItemName(String msg) {
        msg = msg.replace("%item_", "");
        msg = msg.replace("%", "");
        return msg;
    }

    public ItemStack parseForItem(String msg) {
        return getItem(parseForItemName(msg));
    }

    public boolean saveItem(String name, ItemStack item) {
        items.set(name, Utils.itemStackToBase64(item));
        saveStorage();
        return true;
    }

    public List<String> getItemNames() {
        return new ArrayList<>(items.getKeys(false));
    }

    public ItemStack getItem(String name) {
        return Utils.itemStackFromBase64(storage.getString("Items." + name));
    }

    public void toggleLogger(String playerName, String milestoneName, String worldName) {

        if (worldName.equalsIgnoreCase("*")) {

        }

        boolean contains = toggles.getConfigurationSection(milestoneName + "." + worldName).contains(playerName);

        if (contains)
            toggles.set(milestoneName + "." + worldName, toggles.getStringList(milestoneName + "." + worldName).remove(playerName));
        else
            toggles.set(milestoneName + "." + worldName, toggles.getStringList(milestoneName + "." + worldName).add(playerName));
    }

    public boolean isToggled(String playerName, String milestoneName, String worldName) {
        return !toggles.getConfigurationSection(milestoneName + "." + worldName).contains(playerName);
    }
}
