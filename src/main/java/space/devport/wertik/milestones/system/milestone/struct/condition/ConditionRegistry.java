package space.devport.wertik.milestones.system.milestone.struct.condition;

import org.jetbrains.annotations.NotNull;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.BaseCondition;

import java.util.HashMap;
import java.util.Map;

public class ConditionRegistry {

    private final MilestonesPlugin plugin;

    private final ConditionInstanceCreator defaultLoader = BaseCondition::new;

    private final Map<String, ConditionInstanceCreator> registeredLoaders = new HashMap<>();

    public ConditionRegistry(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public AbstractCondition load(String name, Configuration configuration, String path) {
        ConditionInstanceCreator loader = getLoaderOrDefault(name);

        return loader.createInstance().load(configuration, path);
    }

    public ConditionInstanceCreator getLoaderOrDefault(String name) {
        return this.registeredLoaders.getOrDefault(name, defaultLoader);
    }

    public ConditionInstanceCreator getLoader(String name) {
        return this.registeredLoaders.get(name);
    }

    public void setInstanceCreator(String name, ConditionInstanceCreator loader) {
        this.registeredLoaders.put(name, loader);
    }
}