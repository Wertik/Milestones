package space.devport.wertik.milestones;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Utils {

    @Nullable
    public <E extends Enum<E>> E parseEnum(String str, Class<E> clazz) {
        try {
            return E.valueOf(clazz, str);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
