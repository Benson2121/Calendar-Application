# Running This Program
This program can be run through the Main class's public static void main function.

If previous data does not exist (in the form of a file called CSC207Data.ser in the src folder), the program should
ask you to create a username/password for an admin account, because admin accounts cannot be created out of thin air.

Then, a command line interface will show as specified in Phase 1, where entering numbers correspond to different commands,
which may sometimes ask for string inputs which must be entered.

## Moving Through Menus
You can freely move back and forth through menus through this logic:
Logged out <-> User/Admin-user logged in menu <-> Calendar edit menu <-> Task/Event edit menu <-> Label edit menu

## Menus
Users have only one Calendar (for phase 1) which they can access with the Calendar Edit command in the logged in menu.
The following commands perform the following:
- View Schedule - print out all events/tasks
- View Schedule By Label - you will be prompted to specify labels. Type "" to stop the prompter. The program will print all 
events/tasks that contain all the specified label names.
- View Upcoming - print out all events/tasks with end times/deadlines that have still not passed yet

You can also access the Event/Task edit menu for the calendar. Duplicate event/task names are allowed, but in exchange,
unique IDs are generated for each event/task. The shell will show you the IDs assigned to each name, and you must input IDs
to modify event/tasks with the commands below:
- Add event/task
- Change event/task name, description, and time (for an event, this means start/end times. for a task, this means an end time)
    - The shell guides you on writing input date/times that it can parse.
- Delete/clear event/tasks
- View events/tasks (the same as ViewSchedule, but separates events/tasks)
- Nest/unnest events/tasks (if you display events/tasks with subevents/tasks, they will show up below the parent)
- Add subevent/subtask (combines add & nest event/task into one command)

Finally, you can access the Edit Label menu for an event/task. Duplicate label names are not allowed; if you enter a
duplicate, it will replace the old label for a new label of the same name (which does nothing).

We will need to differentiate between **thematic labels**, examples of which being "project1", "canada", or "math", and
**progress labels**, examples of which being "in progress", "complete", or "not started". Only tasks can contain
progress labels. This feature hasn't been totally implemented for phase 1, so it currently only supports:
- Adding/deleting labels
- Changing label names
- Viewing all labels (by printing them)