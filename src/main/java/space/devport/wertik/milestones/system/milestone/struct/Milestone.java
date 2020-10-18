package space.devport.wertik.milestones.system.milestone.struct;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;
import space.devport.wertik.milestones.system.milestone.struct.reward.MilestoneRewards;

public class Milestone {

    @Getter
    private final String name;

    @Getter
    private final String actionName;

    @Getter
    @Setter
    private String displayName;

    @Getter
    @Setter
    private AbstractCondition condition;

    @Getter
    @Setter
    private MilestoneRewards rewards;

    public Milestone(String name, String actionName) {
        this.name = name;
        this.actionName = actionName;
    }

    @Nullable
    public static Milestone load(Configuration configuration, String path) {
        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            ConsoleOutput.getInstance().warn("Could not load milestone at " + configuration.getFile().getName() + "@" + path);
            return null;
        }

        String type = section.getString("type");
        if (!MilestonesPlugin.getInstance().getActionRegistry().isRegistered(type)) {
            ConsoleOutput.getInstance().warn("Action " + type + " is not valid, cannot load milestone at " + configuration.getFile().getName() + "@" + path);
            return null;
        }

        Milestone milestone = new Milestone(path, type);

        AbstractCondition condition = MilestonesPlugin.getInstance().getConditionRegistry().load(type, configuration, path + ".conditions");
        ConsoleOutput.getInstance().debug("Condition: " + condition.getClass().getSimpleName());
        milestone.setCondition(condition);

        MilestoneRewards rewards = MilestoneRewards.load(configuration, path + ".rewards", false);
        milestone.setRewards(rewards);

        //TODO Load the rest

        return milestone;
    }
}