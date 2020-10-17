package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.KillCondition;

public class KillActionListener extends AbstractActionListener {

    public KillActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getKiller() != null) {
            ActionContext context = new ActionContext();
            context.fromPlayer(entity.getKiller())
                    .add(entity.getType())
                    .add(entity.getLocation());
            handle("kill", entity.getKiller(), context);
        }
    }

    @Override
    public void registerConditionLoaders(ConditionRegistry registry) {
        registry.setInstanceCreator("kill", KillCondition::new);
    }
}
