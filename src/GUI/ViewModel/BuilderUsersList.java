package GUI.ViewModel;
import javafx.scene.layout.*;
import java.util.List;

/**
 * Builder for a ListView of Users.
 * @author Richard Yin
 */
public interface BuilderUsersList {
    /** Generate the usernames of all regular users. */
    void buildUsernames();

    /** Generate the ban statuses of all generated usernames. */
    void buildBanStatuses();

    /** Build all Labels (that show username). */
    void buildLabels();

    /** Build all ImageViews (that show a picture of a ban-hammer). */
    void buildImageViews();

    /** Build all displayable AnchorPanes that each contain a Label and ImageView. */
    void buildAnchorPanes();

    /** Get the built AnchorPane.
     * @return the built AnchorPane. */
    List<AnchorPane> getResult();
}
