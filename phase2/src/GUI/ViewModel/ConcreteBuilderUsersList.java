package GUI.ViewModel;
import HelperClasses.NotFoundException;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.*;
import UseCase.*;

/**
 * Implemented builder for a ListView of Users.
 * @author Richard Yin
 */
public class ConcreteBuilderUsersList implements BuilderUsersList {
    private List<ImageView> i;
    private List<Label> l;
    private List<AnchorPane> a;
    private List<String> usernames;
    private List<Byte> banStatuses;
    private final UserManager users;

    /**
     * Constructor for ConcreteBuilderUsersList, which acts as a setter.
     * @param users The UserManager to use.
     */
    public ConcreteBuilderUsersList(UserManager users) {
        this.users = users;
    }

    /** Generate the usernames of all regular users. */
    @Override
    public void buildUsernames() {
        usernames = users.getAllRegularUsernames();
    }

    /** Generate the ban statuses of all generated usernames. */
    @Override
    public void buildBanStatuses() {
        banStatuses = new ArrayList<>();
        for (String username : usernames) {
            try {
                banStatuses.add(users.isBanned(username) ? (byte) 1 : (byte) 0);
            } catch (NotFoundException e) {
                banStatuses.add((byte) -1);
            }
        }
    }

    /** Build all displayable AnchorPanes that each contain a Label and ImageView. */
    @Override
    public void buildAnchorPanes() {
        a = new ArrayList<>();
        for (int n = 0; n < banStatuses.size(); n++) {
            AnchorPane anch = new AnchorPane();
            anch.setPrefWidth(680);
            anch.setPrefHeight(40);
            anch.getChildren().add(l.get(n));
            anch.getChildren().add(i.get(n));
            a.add(anch);
        }
    }

    /** Build all Labels (that show username). */
    @Override
    public void buildLabels() {
        l = new ArrayList<>();
        for (int i = 0; i < banStatuses.size(); i++) {
            Label lab = new Label();
            lab.setText(usernames.get(i));
            lab.setLayoutX(9);
            lab.setLayoutY(5);
            if (banStatuses.get(i) == -1) {
                lab.setFont(Font.font(Font.getDefault().toString(), FontWeight.LIGHT, 18));
                lab.setText(lab.getText() + " (error)");
            } else if (banStatuses.get(i) == 1)
                lab.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD, 18));
            else
                lab.setFont(Font.font(Font.getDefault().toString(), 18));
            l.add(lab);
        }
    }

    /** Build all ImageViews (that show a picture of a ban-hammer). */
    @Override
    public void buildImageViews() {
        i = new ArrayList<>();
        for (Byte banned : banStatuses) {
            ImageView img = new ImageView("GUI\\assets\\ban.png");
            img.setFitWidth(27);
            img.setFitHeight(25);
            img.setLayoutX(653);
            img.setLayoutY(5);
            img.setVisible(banned == 1);
            i.add(img);
        }
    }

    /** Get the built AnchorPane.
     * @return the built AnchorPane. */
    @Override
    public List<AnchorPane> getResult() {
        return a;
    }
}
