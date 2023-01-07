package UseCase.DisplayPlans;

import Entities.Plan;
import java.util.List;

/** AllEventsPlanDisplayer displays all Events, implements PlanDisplayer.
 */
public class AllEventsPlanDisplayer implements PlanDisplayer {

    /** Displays a list of all Events
     * @param plans list of Plans
     */
    @Override
    public String display(List<Plan> plans) {
        StringBuilder str = new StringBuilder();
        for (Plan p : plans) {
            if (!p.isTask()) {
                str.append(p).append("\n");
            }
        }
        return str.toString();
    }
}
