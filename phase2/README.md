# Program Structure

The SRC folder contains the following packages, whose division mostly had the layers of Clean Architecture in mind:
- **Entities**: All entity classes
- **UseCase**: All use case/manager classes
- **Gateway**: Gateway-related classes
- **GUI**: GUI-related classes (contains the controllers and presenters)
- **Shell**: Shell-related classes (contains the controllers and presenters)
- **HelperClasses**: Contain custom exceptions thrown in our program
- **lib**: The library folder for javafx for the convenient access of anyone who needs it

The Test folder contains the following packages:
- **EntityTest**: Tests for all entities
- **UseCaseTest**: Tests for all use cases.

### Entities

User is an abstract class extended by RegularUser and AdminUser.

Plan is an abstract class extended by Event and Task. By default, Plans have no start/end times or deadlines.

NameDuplicatable is an abstract class extended by everything that can have duplicate names: Plan, Calendar, and Comment.
In exchange, they are assigned unique IDs, which will be used to differentiate them. Note that Labels cannot have
duplicate names. There is an "is a" relationship here, so we felt a parent class was appropriate.

History is an instance of a record action.

A Label can either be a progress label (an enum like "in progress", "complete", "not started") or a thematic label
(eg. "math", "project1", "afternoon", "important", "priority"). Events cannot contain progress labels. By default, a
task's progress label is "Not Started".

TimeParser is a helper class that can parse String datetimes and turn them into LocalDateTime objects storable by this
program. It is called by a use case class, PlanManager.

None of the entities depend on each other or store instances of each other.

### UseCase

All manager classes only store their associated entities. For instance, UserManager stores Users, CommentManager stores
Comments, and LabelManager stores Labels. While these classes can depend on other entities, any stored references to
other entities (eg. knowing the plan a label is associated with) come in the form of IDs (eg. CalendarIDs, PlanIDs).

Most manager classes also store instances of a current object. For instance, CalendarManager stores the current
calendarID and PlanManager stores the current PlanID. This is for the convenience of shell menus, which need to know what
is the current calendar/plan being selected when they perform a command.

The abstract class Saveable represents a manager class that can be saved. It interacts with Saver inside the Gateway and
is extended by CalendarManager, CommentManager, HistoryManager, LabelManager, UserManager, and PlanManager.

The inner package DisplayPlans contains the classes relevant to a Strategy design pattern used by the PlanManager when
choosing the format/display all plans of a given calendar.

### Gateway
Contains the InterfaceSaver (for dependency inversion), which is implemented by Saver. Manager classes are saved via
serialization, and should produce six .ser files titled "CSC207_calendars.ser", "CSC207_comments.ser",
"CSC207_histories.ser", "CSC207_labels.ser", "CSC207_plans.ser", and "CSC207_users.ser" when saved.

### HelperClasses
Contains four exception classes:
- **DuplicateUsernameException**: Called when a user is being created and their username already exists
- **InvalidCommandException**: Called when a command cannot be called, most likely because inputs break the command's logic.
- **NoPermissionException**: Called when a command cannot be called because the user doesn't have permissions to edit/change something
- **NotFoundException**: Called when a command cannot be called because the input item can't be found.

### Shell
The Shell consists of two packages:

Shell.Presenter contains the ShellPresenter, which contains basic methods for presenting possible commands on a command
line (prompter()), getting inputs from the user (getArguments()), and printing results (displayResult()).

Shell.Controller contains the ShellMenu classes, which are responsible for performing commands and requesting inputs.

ShellMenu is an abstract class extended by all other classes in the same package starting with ShellMenu. It uses a map
of numbers (that the user must input) to anonymous functions in order to handle commands.

ShellController initializes all manager classes and contains a map of all MenuStates (an enum) to an instance of a
corresponding MenuClass. Depending on the MenuState, ShellController.run() calls MenuClass.run() for some MenuClass,
which executes one instance of the menu (ie. one user request). ShellController.run() must be repeated forever on a
loop in the main file to keep the shell running.

The ShellController passes all of its manager classes down into the various ShellMenu classes via dependency injection.

### GUI
The GUI consists of four packages:

GUI.Assets contains all images, CSS files, and FXML files.

GUI.Controller contains all controller classes. The abstract class Controller is extended by ControllerMenuLoggedIn
and ControllerMenuLoggedOut; all three of these classes implement the respective interfaces of the same name (for
dependency inversion), which also follow the same inheritance hierarchy. ControllerMenuLoggedIn is a facade class; all
its code is inside the classes whose names begin with Handler. All controllers contain instances of a presenter class
of the same name.

GUI.Menu contains all menu presenter classes. These classes directly work with the FXML files and as a result delegate
methods to Controller classes via dependency inversion. Menu is an abstract class extended by MenuLoggedIn and
MenuLoggedOut.

GUI.ViewModel contains all the Entity-like objects that exist solely for the purposes of the GUI. The TableView and
ListView classes in javafx are parameterized, but entering entity objects into these would break dependency rules. As a
result, the data container classes EntryCalendar and EntryHistory were created to solve this issue. The remaining
classes consist of three builder classes - for Calendars (in my calendars, shared with me tabs), Histories, and Users
(in admin settings) - for these Entry classes.





# Setting Up and Starting

### Project Configurations:
Our project JDK was Azul Zulu 11, but the project should work with any Java 11 JDK.

Our project used javafx for the GUI. In order to add javafx to IntelliJ and configure it, we did the following:
1) We downloaded the latest version of javafx from https://gluonhq.com/products/javafx/. However, since we included the
javafx lib folder inside this project, downloading shouldn't be necessary.

2) In IntelliJ, we accessed the menus File -> Project Structure -> Libraries and added the library for javafx by
copying in our computer's path to the "lib" file from the downloaded javafx file.

3) In IntelliJ, we accessed the menus Run -> Edit Configurations... -> GUI.Main -> Modify options -> Add VM options,
and when the prompter asked for text to enter, we went to https://openjfx.io/openjfx-docs/ and accessed the tabs
JavaFX and IntelliJ -> Non-modular from IDE -> 4. Add VM options, pasting the right code based on our operating system.
If this is not done, then on launch of the GUI, there may be the error "JavaFX runtime components are missing".



### How to Start:
There are two ways to run this program, via running the public static void main functions in two different places:

1) src/Main.java, which will launch the Shell with a command line interface
2) src/GUI/Main.java, which will launch the GUI interface

The GUI only has partial functionality, while the Shell is complete. 

On first launch, it is recommended that the user loads the Shell, as it directs the user to create an admin account.
This function is not yet implemented on the GUI, so if data is saved on the GUI, it will be impossible to create an
admin account without deleting the .ser files and restarting from the Shell. After the admin account is created, it is
acceptable to quit and load the GUI.


# Program Instructions

### Shell-Specific
For all menus, information is presented in the form of a command line interface, where the user is given a list of all
commands and asked to enter numbers which correspond to their desired command. Depending on the command, the user may
be asked to input additional information before executing them.

### GUI-Specific
In the logged out menu, there are two buttons - "sign up" and "sign in". Both buttons take the user to a submenu
consisting of a text field and password field indicating username and password, plus a coloured register/confirm button
at the bottom (and a back button to go back to the previous menu). The user should enter text and press the
register/confirm button to create an account or log in.

The logged in menu consists of a main tab, on the left side of the screen, and the contents of that tab, on the right
side of the screen. Users can navigate between tabs by clicking between them on the main tab. There are also buttons to
save and log out in the main tab. As for the contents of each tab:

- The tabs "My calendar" and "Shared with me" respectively contain a table of calendars that the user owns and has been
  given access to. The buttons at the top of the table can perform various operations to a calendar that is selected by
  the user.
- The tab "History" similarly contains a table of history entries, but cannot be edited, only viewed.
- The tab "Admin settings" contains a list of all non-admin users, multiple of which can be selected at a time using
  default controls (ctrl + click, shift + click). The above buttons perform various operations to all selected users,
  including unbanning/banning and deleting their accounts. This tab will not show up if a regular user is logged in.
- The tab "Settings" contains a set of text/password fields and adjacent buttons that executes a command according to
  the field.

### Moving Through Menus (GUI)
The logic connecting menus is shown below:

Logged out <-> User/Admin-user logged in menu <-> Calendar edit menu <-> Task/Event edit menu

One can move back and forth between task/event edit and label edit menus. 
One can move back and forth between task/event edit and comment edit menus.
One cannot between label edit and comment edit menus.

### Menu-Specific Commands (GUI):

Commands should return a message "Successfully ..." if they succeed. Otherwise, they'll create an error message which
wil hopefully show what went wrong.

##### Logged Out
- **Log In**: Log into an account by entering the right username and password.
- **New User**: Create a new account by entering a username and password. You cannot choose already-existing usernames
- **Exit**: Exit the program.

##### Logged In, Regular User
- **View User History**: Print a list of all trackable actions the user has performed, timestamps included.
- **Log Out**: Log out. You will be notified of unsaved changes and be asked to confirm logging out if so.
- **Delete Own Account**: Wipe the user's account and logs out. You will be asked to confirm this. Cannot be reversed.
- **Edit Calendars**: Go to the Calendar Edit menu.
- **Change Username**: Enter a new username to change to. You cannot choose already-existing usernames. Will be auto-saved.
- **Change Password**: Enter a new password to change to. Will be auto-saved.
- **Save**: Save the program. Six .ser files should load in the src folder.

##### Logged In, Admin User
- **Create Admin User**: Create a new admin user by entering a new username and password. Will be auto-saved.
- **Ban User**: Ban a non-admin user only by entering their username. Will be auto-saved.
- **Unban User**: Unban a non-admin user only by entering their username. Will be auto-saved.
- **Delete Non-Admin Account**: Delete a non-admin user's account. Cannot be reversed.

##### Calendar Edit
- **Back**: Go back to the user menu.
- **View Schedule**: Given a calendarID, print all events/tasks in the calendar.
- **View Schedule by Label**: You will be asked for a calendarID and for labels. The shell will keep asking you for
labels until you press enter without typing. The program will then print all events/tasks containing all inputted labels
- **View Upcoming**: Given a calendarID, print all events/tasks in the calendar with dates that have not yet arrived.
- **Create Calendar**: Given a calendar name and optional description, creates a new calendar and prints its new ID. You
can only refer to this calendar by its ID in following commands. If you forget the ID, you can find it again with the
command View My Calendars. This problem is obviously mitigated in the GUI.
- **Delete Calendar**: Given a calendar ID, deletes it.
- **Clear Calendars**: Deletes all owned calendars. You will be asked to confirm this command.
- **Edit Calendar Name**: Given a calendar ID and new name, changes the name of the calendar.
- **Edit Calendar Description**: Given a calendar ID and new description, changes the description of the calendar 
- **Edit Sharing Permissions**: The program will ask for a calendar ID to share, a username to share to, and different
levels of sharing access (either enter exactly "view", "edit", or "none"). This will share a calendar with another user.
In view access, the command Edit Events/Tasks will not work, while for edit access, it will work. Calendars shared with
a user cannot be shared to someone else by that user, only the owner.
- **View My Calendars**: Print all calendars (and their IDs) owned by the user
- **View Calendars Shared With Me**: Print all calendars (and their IDs) shared with the user
- **Edit Events/Tasks**: Go to the event/task edit menu for a given calendar ID.

##### Event/Task Edit
- **Back**: Go back to the calendar edit menu.
- **Add Event**: Create a new event with a name and optional description. IDs are auto-printed out, also viewable in View All Events. 
- **Add Task**: Create a new task with a name and optional description. IDs are auto-printed out, also viewable in View All Tasks.
- **Add Subevent**: Effectively combines Add Event and Nest Event/Task. Asks for a name, optional description, and event/task to nest under.
- **Add Subtask**:Effectively combines Add Task and Nest Event/Task. Asks for a name, optional description, and event/task to nest under.
- **Edit Name**: Given an event/task ID and new name, edits the edit/task's name.
- **Edit Description**: Given an event/task ID and new description, edits the edit/task's description.
- **Edit Labels**: Given an event/task ID, enters the label edit menu.
- **Edit Time**: Given an event ID, it asks for a start/end time to set to. Given a plan ID, it asks for a deadline to set to.
Guidelines for formatting are given in the prompt, but can be viewed explictly in the TimeParser class.
- **Nest Event/Task**: Nest one event/task under another, given a parent and child event/task ID. Nested event/tasks will
show up indented and below the parent in View All Events/Tasks. 
- **Un-nest Event/Task**: Unnest one event/task from another, given a parent and child event/task ID.
- **Delete Event/Task**: Given an event/task ID, deletes it.
- **Clear Events**: Clears all events of a calendar. You will be asked to confirm this.
- **Clear Tasks**: Clears all tasks of a calendar. You will be asked to confirm this.
- **View All Events**: Print out all events (and their IDs).
- **View All Tasks**: Print out all tasks (and their IDs).
- **Edit Comments**: Given an event/task ID, enters the comment edit menu.

##### Label Edit
- **Back**: Go back to the event/task edit menu.
- **View Labels**: View all thematic labels for the event/task.
- **Edit Label**: Enter a new name of a thematic label to change to, given its old name. Duplicates will be automatically deleted.
- **Delete Label**: Delete a thematic label, given its name.
- **Clear Labels**: Clear all thematic labels.
- **Add Thematic Label**: Add a new thematic label.
- **Set Progress Label**: Only appears for tasks. Change the progress label to a predefined set of allowed inputs.

##### Comment Edit
- **Back**: Go back to the event/task edit menu.
- **View Comments**: View all comments (and their IDs) plus their replies.
- **Add Comment**: Add a new comment, which will also assign an ID to them.
- **Edit Comment**: Edit a comment, given a commentID.
- **Delete Comment**: Delete a comment, given a commentID. 
- **Clear Comments**: Delete all comments. You will be asked to confirm this.
- **Reply to Comment**: Reply to a comment, given a commentID