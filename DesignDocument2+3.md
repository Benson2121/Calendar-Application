# Design Document 2: Design Patterns

## Facade
*ControllerMenuLoggedIn, HandlerAdminSettings, HandlerCalendars, HandlerMainTab, HandlerSettings, HandlerTables*

- ControllerMenuLogged is the Facade class. It delegates methods to the classes HandlerAdminSettings, HandlerCalendars,
  HandlerMainTab, HandlerSettings, and HandlerTables.

- The GUI was designed so that each menu had a corresponding controller class that contained a presenter class.

- The controller class handled all user requests that required the retrieval or editing of data from Use Case classes,
  while the presenter class contained all the code necessary to work with an fxml file. Since user requests
  came via buttons, whose clicks could only be linked to a method call in the presenter class, the presenter had to
  delegate methods to the controller.

- The controller class implemented an interface, which allowed the presenter to do this via dependency inversion.

- The logged in menu was becoming bloated because it had to handle user requests from all possible tabs, which each had
  separate, self-contained functionalities and called separate methods (and helper methods). These are the “actors”.

- I did not want to split up the tabs into separate fxml files, because it would increase complexity significantly
  (more controllers, more interfaces, I didn’t like the idea of one menu having multiple fxml files, and more maybe
  repetitive code).

- As such, it began to make more sense for pieces of code “responsible” to different actors/tabs to have their own
  classes, and combine them into a facade controller class so there would only need to be one controller interface.

## Builder
*BuilderCalendarTable, ConcreteBuilderCalendarTable, DirectorCalendarTable, BuilderHistoryTable,
ConcreteBuilderHistoryTable, BuilderUsersList, ConcreteBuilderUsersList*

- These are three different builder classes I implemented for very similar reasons.

- BuilderCalendarTable, BuilderHistoryTable, and BuilderUsersList are implemented respectively by
  ConcreteBuilderCalendarTable, ConcreteBuilderHistoryTable, and ConcreteBuilderUsersList. The DirectorCalendarTable
  works with BuilderCalendarTable and ConcreteBuilderCalendarTable.

- In each of these cases, I needed to build up a TableView/ListView for the GUI, but this process (a) involved many
  lines of code, including specific helper methods, (b) involved working with special EntryCalendar/EntryHistory classes
  specifically designed for the TableView/ListView, (c) involved calling methods in a careful order, and (d) contributed
  to bloating in a menu.

- To hide the complex internal representation of building TableView/ListView items from the actual method to load a
  TableView/ListView, I implemented builder objects.

- I did not use a Director class for History and UsersList because there was only one way to build them. I used a
  Director class for CalendarTable because Builder CalendarTable generated a TableView of calendars for both the “my
  calendars” and “shared with me” tabs, and this involved calling similar code. To hide any potential confusion over
  what to call exactly, I used a Director class to handle generating a TableView for either one tab or the other.

## Observer
*Saver, Saveable*

- Saveable is a parent class containing an instance of interface Saver. Saveables have a method called setSaver() and
  makechange(), which calls a method from Saver to notify that an unsaved change was made.

- A feature we were interested in implementing was for unsaved changes made in a program to be tracked such that when a
  user logs out, if there were unsaved changes, they'd be warned - this is a common features in most professional editor
  programs.

- Previously, this was achieved with a problematic ShellData class that sidestepped these issues, but after deleting it,
  another way was needed.

- The saveable manager classes needed to call some makeChange() method and some way to store that information when the
  Shell later wanted to log out.

- It would be a bad idea to create a class for this one hyper-specific and limited functionality, and a static variable
  was to be avoided if possible, so the idea was to integrate it into an existing saving-related class − the ShellSaver
  gateway, which already extended a Saver interface to allow dependency inversion. The manager classes would extend
  Saveable and call makeChange(), which changes a boolean in the Saveable’s associated Saver.

- This, combined with the dependency inversion, prevented the use case classes from depending on ShellSaver.

## Strategy Design Pattern
*PlanDisplayer, AllEventPlanDisplayer, AllPlansPlanDisplayer, AllTasksPlanDisplayer, AllUpcomingPlansPlanDisplayer,
PlanManager*

This pattern was used because we want to separate the ways of displaying Plans to avoid the bloater code smell and have
more ways of displaying Plans, like displaying the plans for a specific day or month. We implemented a PlanDisplayer
interface, and each way of displaying Plans would implement the PlanDisplayer interface. In the PlanManager, we added a
PlanDisplayer attributes, and had set method for the PlanDisplayer. We could switch the way of displaying Plans by
setting the PlanDisplayer.




# Design Document 3: From Phase 1 to Phase 2

## Refactoring

#### ShellData

In accordance with TA suggestions, ShellData, which gave too much access and passed around too many methods, was
completely removed. The Controller class now passes all Use Case classes as arguments into the Menu classes instead
of just one ShellData class, which has resulted in methods with many parameters.

To overcome the problematic cast outside a gateway (which happened in ShellData), an Observer design pattern was
implemented as described in Design Document 2.

#### Duplicate ShellPresenter Code

In accordance with TA suggestions, to separate the printing of the strings from the content of the string, a method
prompter() was created in ShellPresenter which took in unlimited String inputs, formatted them, and printed them out.
The content to be printed out was then moved to each of the individual Shell menu classes.

#### Success Messages

In accordance with TA suggestions, the String success message returned by Use Case classes were removed, replaced with
a void return on success and exceptions on failure. The success message was moved up to the menu classes instead.

#### Unchanged

It was suggested to use a Singleton design pattern to eliminate the need for ShellData, however after consultation with
professor Shorser, we concluded that it would be more problematic since it would involve static variables. Instead, we
used dependency injection to pass the use case classes around in necessary methods.

Another suggestion was to use the Decorator design pattern in place of NameDuplicatable, however after professor Shorser
explained Decorators in more detail, we thought they were better-used when there are many possible functionalities one
can assign to a class − but assigning an ID was the only functionality that was needed, so we concluded the Decorator
was too powerful to suit our needs and we’d find somewhere else to implement a design pattern.

Thirdly, there was a suggestion to use a Facade design pattern in ShellPresenter to make it easier for controllers to
build menus. However, after coding the method prompter(), we realized that was the only method we needed to properly
display a menu. The code ended up simpler than thought, so we decided it wasn’t necessary to use a Facade.

Lastly, it was said that ideally, all controllers, managers, and gateways should implement abstract interfaces, which
would help keep code separated and allow dependency inversion. We talked to professor Shorser about this approach, and
while she said she agreed it was an ideal, she also noted it wasn’t always necessary because it also added considerable
complexity and more classes to the program. Considering that we have 7 manager classes and many shell menu classes, we
decided abstract interfaces were not worth increasing the bulkiness of our progress, and we’d instead implement them as
the program needed.



## Changes and New Features

#### Different Label Functionality

We changed progress labels to be an enum because it made more sense - having multiple progress indicators would be
confusing, and having a pre-described set of labels to choose from (eg. not started, in progress) seemed appropriate.
This involved changing Label, LabelManager, and ShellMenuLabel. We also split ShellMenuEditLabel into two menu classes:
ShellMenuEditLabelEvent, which is extended by ShellMenuEditLabelTask.

#### Changing Username and Password

We added functionality to change passwords, which was relatively simple, and change usernames, which was a bit more
difficult as other manager classes used usernames as a way to organize ownership of data. This involved adding setters
in User, methods to handle username changes in some Manager classes, and more commands in ShellMenuUser.

#### More Powerful History Tracker

We made History track more than just logging in and out - creating new accounts, banning/unbanning users, changing
usernames and password, and creating/editing/deleting/sharing calendars. This involved editing with menu methods to call
HistoryManager.addHists(). In order to make History able to record specific details about an action (eg. when changing
usernames, history will log the old and new usernames), we refactored the action field from an enum to a String to allow
this.

#### Smarter Saving

There are some changes that should be auto-saved without saving the whole program simultaneously, like tracking logging
history, banning/unbanning, changing usernames/passwords, and deleting accounts. In order to implement this, we added
more Saveable.makeChange() calls around various Manager methods. Also, previously, Saveable.save() would save the
Saveable AND tell the Saver that there are no unsaved changes, even though it only saved for the particular class that
called save(). We removed this and created a new method Saveable.saveAll() that does not save but tells the Saver that
there are no more unsaved changes.

#### Multiple Calendars

We added the ability for users to own multiple calendars - this involved assigning IDs to calendars, as well as extra
functionality like editing many different calendar names and descriptions, deleting calendars, clearing calendars, etc.
This involved greatly expanding ShellMenuEditCalendar, more methods in CalendarManager to do the above functions, and
editing the user entity to contain a list of owned calendars.

#### Calendar Sharing

We implemented the ability for users to share calendars with each other, giving either edit access (where events/tasks
can be created), view access (where calendars can only be viewed), or no access. This involved creating more fields in
User which store owned, editable, and viewable calendarIDs, UserManager methods to handle adding/deleting these, and it
made more complex the methods in the shell that deleted users or calendars (as it also involved finding where else
calendarIDs are stored).

#### Comments

We implemented the ability to leave comments on events/tasks. This involved creating a Comment entity, a Comment Manager,
and a new ShellMenu for editing comments.

The Comment entity records the comment text, the username of the poster, the initial post date, and the last edited date
(if the comment has been edited), as well as any replies (a recursive list of comments). The CommentManager contains
most methods to manipulate with Comments, including adding comments, replying to comments, deleting comments, viewing
comments, etc. Finally, ShellMenuEditComment is the shell menu that displays all possible commands for comments and
executes them.

#### GUI

The biggest new addition to our program was the new GUI, which is contained in a package of the same name. This involved
creating fxml files for the screen, a new set of controller classes for, and a new set of presenter classes. The logged
out and logged in (both RegularUser and AdminUser) menus were created, which mostly do the same as their Shell equivalent,
however the logged in menu has some calendar viewing features. There are a few features still missing, however:
- Creating a new admin user when no previous data is detected
- Creating a new admin user as an admin user
- Popup screen when a user is logging out with unsaved changes
- Sharing calendars
- Opening calendars and editing plans/labels/comments

Nevertheless, the GUI currently supports:
- Creating new regular accounts
- Logging in/out
- Changing username/password
- Deleting own accounts
- Deleting other accounts
- Banning/unbanning
- Viewing history
- Viewing calendars shared with the user
- Viewing owned calendars
- Creating new calendars
- Changing calendar name/description
- Deleting calendars
- Removing the own user from a shared calendar
- Tracking unsaved changes
- Save button