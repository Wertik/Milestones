package space.devport.wertik.milestones.system.action.struct;

import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.struct.Context;

public class ActionContext extends Context {

    @Nullable
    public <T> T get(Class<T> clazz) {
        for (Object value : getValues()) {
            if (clazz.isAssignableFrom(value.getClass()))
                return clazz.cast(value);
        }
        return null;
    }

    public Context fromBlock(Block block) {
        return add(block).add(block.getLocation());
    }
}