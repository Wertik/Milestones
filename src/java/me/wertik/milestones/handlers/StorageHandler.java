package me.wertik.milestones.handlers;

import me.wertik.milestones.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageHandler {

    private Main plugin = Main.getInstance();
    private YamlConfiguration storage;
    private File storageFile;
    private ConfigurationSection toggles;
    private ConfigurationSection items;

    public StorageHandler() {
    }

    public void setYamls() {

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
        items.set(name, itemStackToBase64(item));
        saveStorage();
        return true;
    }

    public List<String> getItemNames() {
        return new ArrayList<>(items.getKeys(false));
    }

    public ItemStack getItem(String name) {
        return itemStackFromBase64(storage.getString("Items." + name));
    }

    // Base64
    public static String itemStackToBase64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save every element in the list
            dataOutput.writeObject(item);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IllegalStateException e) {
            Bukkit.getLogger().warning("§cCould not save the item stack.. i've failed you.. :(");
        } catch (IOException e) {
            Bukkit.getLogger().warning("§cCould not save the item stack.. i've failed you.. :(");
        }
        return null;
    }

    public static ItemStack itemStackFromBase64(String data) {
        ItemStack item = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            // Read the serialized inventory
            item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("§cCould not get the item stack.. i've failed you.. :(");
        } catch (IOException e) {
            Bukkit.getLogger().warning("§cCould not get the item stack.. i've failed you.. :(");
        }
        return item;
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
