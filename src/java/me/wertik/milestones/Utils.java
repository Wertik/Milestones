package me.wertik.milestones;

import me.wertik.milestones.objects.Milestone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public Utils() {
    }

    // This method.. just.. can't ignore durability while comparing item stacks and have to do this, fvck Spigot tho.
    public static boolean compareItemStacks(ItemStack a, ItemStack b) {

        // ah... fml...
        if (a.getType() != b.getType())
            return false;

        if (a.getAmount() != b.getAmount())
            return false;

        if (!a.getEnchantments().equals(b.getEnchantments()))
            return false;

        if (!a.getItemMeta().equals(b.getItemMeta()))
            return false;

        Bukkit.broadcastMessage("Did pass itemstack compare");
        return true;
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String parse(String string, Milestone milestone) {
        string = string.replace("%milestoneName%", milestone.getName());
        string = string.replace("%milestoneDisplay%", milestone.getDisplayName());
        if (milestone.isGlobal())
            string = string.replace("%milestoneScore%", String.valueOf(Main.getInstance().getDataHandler().getGlobalScore(milestone.getName())));
        return string;
    }

    public static String parse(String string, Player player, Milestone milestone) {
        string = parse(string, milestone);
        string = parse(string, player);
        if (!milestone.isGlobal())
            string = string.replace("%milestoneScore%", String.valueOf(Main.getInstance().getDataHandler().getScore(player.getName(), milestone.getName())));
        return string;
    }

    public static String parse(String string, Player player) {
        string = string.replace("%player%", player.getName());
        string = string.replace("%playerDisplay%", player.getDisplayName());
        string = string.replace("%playerSuffix%", Main.getChat().getPlayerSuffix(player));
        string = string.replace("%playerPrefix%", Main.getChat().getPlayerPrefix(player));
        string = string.replace("%playerMaxHP%", String.valueOf(player.getMaxHealth()));
        string = string.replace("%playerHP%", String.valueOf(player.getHealth()));
        string = string.replace("%playerX%", String.valueOf((int) player.getLocation().getX()));
        string = string.replace("%playerY%", String.valueOf((int) player.getLocation().getY()));
        string = string.replace("%playerZ%", String.valueOf((int) player.getLocation().getZ()));
        string = string.replace("%playerWorld%", String.valueOf(player.getLocation().getWorld()));
        string = string.replace("%playerFood%", String.valueOf(player.getFoodLevel()));
        string = string.replace("%playerLevel%", String.valueOf(player.getExpToLevel()));
        return string;
    }

    public static String checkString(String string) {
        if (string == null)
            string = "";
        return string;
    }

    public static List<String> checkStringList(List<String> list) {
        if (list == null)
            return new ArrayList<>();
        else
            return list;
    }

    public static String toBTypeString(String string) {
        byte data = 0;
        Material material;

        try {
            material = Material.valueOf(string.split(";")[0]);
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("Error in config. " + string.split(";")[0] + " is not a valid material.");
            return null;
        }

        if (string.contains(";"))
            data = Byte.valueOf(string.split(";")[1]);

        return material.toString() + ";" + data;
    }

    public static String toBTypeString(Material material, byte data) {
        return material.toString() + ";" + data;
    }

    public static List<ItemStack> listToArray(ItemStack[] list) {
        List<ItemStack> outPut = new ArrayList<>();
        for (ItemStack item : list) {
            outPut.add(item);
        }
        return outPut;
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
