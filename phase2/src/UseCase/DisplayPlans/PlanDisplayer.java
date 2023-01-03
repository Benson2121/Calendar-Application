package UseCase.DisplayPlans;

import Entities.Plan;
import java.util.List;

/** PlanDisplayer interface, all displayers of subsets of Plans must have display method.
 */
public interface PlanDisplayer {

    /** Displays a list of Plans
     * @param plans list of Plans
     */
    public String display(List<Plan> plans);

}
