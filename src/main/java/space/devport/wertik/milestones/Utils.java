package space.devport.wertik.milestones;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static boolean compareItemStacks(ItemStack a, ItemStack b) {
        return !(a.getType() != b.getType() || a.getAmount() != b.getAmount() || !a.getEnchantments().equals(b.getEnchantments()) || !a.getItemMeta().equals(b.getItemMeta()));
    }

    public static List<ItemStack> listToArray(ItemStack[] list) {
        return new ArrayList<>(Arrays.asList(list));
    }
}
