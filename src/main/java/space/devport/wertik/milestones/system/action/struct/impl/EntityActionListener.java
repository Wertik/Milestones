package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.KillCondition;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.TradeCondition;

import java.util.Arrays;
import java.util.List;

public class EntityActionListener extends AbstractActionListener {

    public EntityActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<String> getRegisteredActions() {
        return Arrays.asList("kill", "trade");
    }

    @Override
    public void registerConditions(@NotNull ConditionRegistry registry) {
        registry.setInstanceCreator("kill", KillCondition::new);
        registry.setInstanceCreator("trade", TradeCondition::new);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(EntityDeathEvent event) {

        final LivingEntity entity = event.getEntity();

        if (entity.getKiller() == null)
            return;

        final Player player = entity.getKiller();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromPlayer(player)
                    .add(entity.getType())
                    .add(entity.getLocation());
            handle("kill", player, context);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTrade(InventoryClickEvent event) {

        if (event.isCancelled() || event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.MERCHANT)
            return;

        MerchantInventory merchantInventory = (MerchantInventory) event.getClickedInventory();

        // Only clicks on the result item
        if (event.getSlot() != 2)
            return;

        Merchant merchant = merchantInventory.getMerchant();

        if (!(merchant instanceof Villager))
            return;

        final Player player = (Player) event.getWhoClicked();

        final Villager villager = (Villager) merchant;
        final MerchantRecipe recipe = merchantInventory.getSelectedRecipe();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromPlayer(player)
                    .add(recipe)
                    .add(villager.getProfession());
            handle("trade", player, context);
        });
    }
}
