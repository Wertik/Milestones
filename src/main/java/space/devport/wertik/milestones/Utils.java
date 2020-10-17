package space.devport.wertik.milestones;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Utils {

    public static boolean compareItemStacks(ItemStack a, ItemStack b) {
        return !(a.getType() != b.getType() || a.getAmount() != b.getAmount() || !a.getEnchantments().equals(b.getEnchantments()) || !a.getItemMeta().equals(b.getItemMeta()));
    }

    public static List<ItemStack> listToArray(ItemStack[] list) {
        return new ArrayList<>(Arrays.asList(list));
    }

    @Nullable
    public static <E extends Enum<E>> E parseEnum(String str, Class<E> clazz) {
        try {
            return E.valueOf(clazz, str);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    public static boolean isListNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
