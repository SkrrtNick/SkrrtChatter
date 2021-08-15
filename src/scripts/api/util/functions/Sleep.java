package scripts.api.util.functions;

import org.tribot.api.Timing;

import java.util.function.BooleanSupplier;

public class Sleep {

    public static boolean until(BooleanSupplier waitCondition) {
        return Timing.waitCondition(waitCondition, 6000);
    }

    public static boolean until(BooleanSupplier waitCondition, long timeout) {
        return Timing.waitCondition(waitCondition, timeout);
    }

}
