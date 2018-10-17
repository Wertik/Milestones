package me.wertik.milestones.handlers;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import org.bukkit.Bukkit;
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
    private static YamlConfiguration storage;
    private static File storageFile;
    private static ConfigurationSection toggles;
    private static ConfigurationSection items;

    public StorageHandler() {
    }

    public void setYamls() {

        // Other data
        storageFile = new File(plugin.getDataFolder() + "/datastorage.yml");

        if (!storageFile.exists()) {
            plugin.saveResource("datastorage.yml", false);
            storage = YamlConfiguration.loadConfiguration(storageFile);
            storage.options().copyDefaults(true);
            try {
                storage.save(storageFile);
            } catch (IOException e) {
                plugin.getServer().getConsoleSender().sendMessage("§cCould not save the file, that's bad tho.");
            }
            plugin.getServer().getConsoleSender().sendMessage("§aGenerated default §f" + storageFile.getName());
        }

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

    public void saveStorage() {
        try {
            storage.save(storageFile);
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage("§cCould not save the storage.. idk what to do, send help.");
            return;
        }
    }

    // TOGGLES SECTION

    // ITEM DATA STORAGE

    // %item_(name)%
    public boolean lookForItem(String msg) {
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
}
