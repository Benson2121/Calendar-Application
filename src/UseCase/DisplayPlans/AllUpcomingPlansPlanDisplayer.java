package UseCase.DisplayPlans;

import java.time.LocalDateTime;
import java.util.List;
import Entities.Plan;

/** AllUpcomingPlansPlanDisplayer displays all upcoming Plans, implements PlanDisplayer.
 */
public class AllUpcomingPlansPlanDisplayer implements PlanDisplayer {

    /** Displays a list of all upcoming Plans
     * @param plans list of Plans
     */
    @Override
    public String display(List<Plan> plans) {
        StringBuilder s = new StringBuilder();
        for (Plan p : plans) {
            if (p.getEndTime() != null && p.getEndTime().isAfter(LocalDateTime.now()))
                s.append(p).append("\n");
        }
        return s.toString();
    }
}
