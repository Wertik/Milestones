package space.devport.wertik.milestones.system.action.struct.impl;

import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.KillCondition;

public class KillActionListener extends AbstractActionListener {

    public KillActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void registerConditionLoaders(ConditionRegistry registry) {
        registry.setInstanceCreator("kill", KillCondition::new);
    }
}
