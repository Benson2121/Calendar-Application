package UseCase.DisplayPlans;

import Entities.Plan;
import java.util.List;

/** AllTasksPlanDisplayer displays all Tasks, implements PlanDisplayer.
 */
public class AllTasksPlanDisplayer implements PlanDisplayer {

    /** Displays a list of all Tasks
     * @param plans list of Plans
     */
    @Override
    public String display(List<Plan> plans) {
        StringBuilder str = new StringBuilder();
        for (Plan p : plans) {
            if (p.isTask()) {
                str.append(p).append("\n");
            }
        }
        return str.toString();
    }
}
