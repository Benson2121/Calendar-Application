# Specification

## Domain
Organization/Productivity App

## Program Description
"Calendar" is an application designed for scheduling events/tasks and
reminding users of then. It can be used both personally or by a company.

Users can create accounts/log in/out, and schedule tasks/events on the many
calendars they can create, which will be saved onto their account.
These tasks/events can last for a timeframe or occur at a single-time,
be auto-generated every day/week/month, be given descriptions, be visually
displayed as a checklist or on a calendar, and be given thematic labels
(eg. “project1”, “new features”, “bugs”) for filtering. Tasks can be divided
into subtasks, given progress labels (eg. “incomplete”, “in progress”,
“needs help”, “paused”, “completed”), and have comments added. Furthermore,
the history of a task can be tracked, and reminders can be auto-generated about
upcoming tasks.

Users can additionally share their calendars with other users, who can
access the calendar given a calendarID, and be given different permissions
for that calendar:
- **Editor:** Can only change progress labels and add comments to tasks/events
- **Planner:** Can do what the editor does, but can also create/edit tasks/events.
they can assign editors to "groups", which collectively can be assigned different
tasks or be given different visibilities for different tasks/events. Planners
cannot edit tasks/events created by other planners, and can restrict visibility
for other planners
- **Owner:** The default setting for the calendar owner, but can be assigned to
others. Has the same functionality as the planner, but has access to everything.

### Entities
- **Label** - stores name, whether it's a thematic/progress label
- **Storable** - abstract class. stores name, description, the timeframe/
single-time/pattern where the task/event occurs (eg every monday), owner, list of
userIDs/groupIDs with visibility access to this Storable
  - **Task** - stores a list of Tasks (ie. subtasks), Labels, comments
  - **Event** - stores thematic Labels only
- **Calendar** - stores calendarID, name, description, owner, list of
co-owners/planners/editors, mapping of users to groupIDs, set of Storables, and
a mapping of timestamps to History
- **History** - stores userID and a description of an action
- **User** - stores userID, personal information, list of owned calendars

There are also getters/setters for the entities when appropriate.

### Use Cases
- **StorableManager** - methods for adding/editing/deleting tasks or events or
their properties - timeframes, labels, descriptions, comments, subtasks, etc. Can
also get tasks/events based on filtering by time/label
- **GroupManager** - methods for adding/editing/deleting co-owners/planners/editors,
groups, manages task/event visibility, checking the user's authority
- **HistoryManager** - updates the timestamp to history mapping in the Calendar object
- **UserManager** - methods for logging in/out, account creation/deletion, creating
calendars, etc.
- **DateManager** - methods for checking deadlines/dates, finding overdue/passed
events/tasks, returning upcoming events/tasks to be reminded of

### Front-End
The front-end should consist of a fully working UI with username/password buttons to
login, a viewable list of calendars to choose to edit, and options to display calendars
either in calendar form or as a list of items.

## Other Target Features
- Push notifications for upcoming tasks, specifying reminder date
- Prompts to reschedule a task if overdue and incomplete
- Check whether scheduled times overlap
- Generating a report from task completion times
- Searching for individual tasks
- Get today's tasks