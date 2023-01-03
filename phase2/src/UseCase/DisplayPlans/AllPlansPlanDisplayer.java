package UseCase.DisplayPlans;

import Entities.Plan;
import java.util.List;

/** AllPlansPlanDisplayer displays all Plans, implements PlanDisplayer.
 */
public class AllPlansPlanDisplayer implements PlanDisplayer {

    /** Displays a list of all Plans
     * @param plans list of Plans
     */
    @Override
    public String display(List<Plan> plans) {
        StringBuilder s = new StringBuilder();
        for (Plan p:plans){
            s.append(p);
            s.append("\n");
        }
        return s.toString();
    }
}
