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

    public void loadYamls() {

        // Other data
        storageFile = new File(plugin.getDataFolder() + "/datastorage.yml");

        if (!storageFile.exists()) {
            plugin.saveResource("datastorage.yml", false);
            plugin.getServer().getConsoleSender().sendMessage("§aGenerated default §f" + storageFile.getName());
        }

        storage = YamlConfiguration.loadConfiguration(storageFile);

        if (storage.contains("Toggles"))
            toggles = storage.getConfigurationSection("Toggles");
        else
            toggles = storage.createSection("Toggles");
        if (storage.contains("Items"))
            items = storage.getConfigurationSection("Items");
        else
            items = storage.createSection("Items");

        saveStorage();
    }

    public List<ItemStack> parseForItemPlaceholders(List<String> list) {
        List<ItemStack> outPut = new ArrayList<>();

        for (String row : list) {
            if (containsItemHolder(row)) {
                String itemName = parseForItemName(row);
                if (items.contains(itemName))
                        outPut.add(getItem(itemName));
                else
                    plugin.getServer().getLogger().warning("Item " + parseForItemName(row) + " is not saved in the data storage, cannot use it.");
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
        if (msg.contains("%item_"))
            return true;
        return false;
    }

    public String parseForItemName(String msg) {
        msg = msg.replace("%item_", "");
        msg = msg.replace("%", "");
        return msg;
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
        if (milestoneName.equals("*"))
            storage.set("Toggles", "*");
        else if (worldName.equals("*"))
            toggles.set(milestoneName, "*");
        else if (playerName.equals("*"))
            toggles.set(milestoneName + "." + worldName, "*");
        else {
            if (!toggles.contains(milestoneName))
                toggles.createSection(milestoneName);
            if (!toggles.getConfigurationSection(milestoneName).contains(worldName))
                toggles.set(milestoneName + "." + worldName, new ArrayList<>());

            List<String> players = toggles.getStringList(milestoneName + "." + worldName);

            if (!toggles.getStringList(milestoneName + "." + worldName).contains(playerName))
                players.add(playerName);
            else
                players.remove(playerName);

            toggles.set(milestoneName + "." + worldName, players);
        }
    }

    public boolean isToggled(String playerName, String milestoneName, String worldName) {
        if (!toggles.contains(milestoneName))
            return false;
        if (!toggles.getConfigurationSection(milestoneName).contains(worldName))
            return false;
        if (!toggles.getStringList(milestoneName + "." + worldName).contains(playerName))
            return false;
        if (toggles.get(milestoneName).equals("*"))
            return false;
        if (toggles.get(milestoneName + "." + worldName).equals("*"))
            return false;
        if (toggles.equals("*"))
            return false;
        return true;
    }
}
