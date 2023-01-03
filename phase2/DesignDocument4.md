# Design Document 4: Universal Design

There are many ways our program’s GUI has incorporated universal design.

Firstly, we had simple and intuitive use in mind designing the logged out menu, which follows conventions for
its menu type − username field at top, password field below, and submit button at bottom.

The logged in menu essentially copies the interfaces of other storage/editing programs like Google Drive and Dropbox,
so people familiar with similar programs can easily adapt. The consistent organization and location of the main left tab
helps minimize trouble in navigating and physical effort from looking around for the right buttons to click. CSS is used
to give slight changes in shade when hovering and clicking over buttons; combined with ban-hammer icons and bold
usernames to indicate bans, or success/error labels to indicate the result of an action, this provides an element of
responsiveness and user feedback. Lastly, icons are displayed next to buttons so users can more intuitively guess their
function before clicking them.

Many other efforts have been taken to ensure information is perceptible: for instance, the light background and dark
text is high-contrast, and there are no animations/flashing colours. Colour is used to accentuate − showing unsaved
changes, a selected tap, or an important button − but it does not convey essential information, making it colour-blind
friendly. There is no audio, so no issue for the hard-of-hearing.

A few places where our GUI could have improved is in flexibility of use − due to time constraints, we could not
implement light/dark/custom colour themes, so those who have trouble with bright screens may have trouble with our
program.

While the save button flashes yellow to remind users of unsaved changes, there was no time to create a popup menu asking
the user to confirm logging out with unsaved changes (though this was implemented in the Shell), making toleration for
error slightly weaker − the lack of “confirm your selection” messages due to time constraints was in fact a common
problem throughout our GUI. However, we do manage to hide admin-only features when regular users are logged in, helping
to minimize potential error.

Finally, our GUI unfortunately does not support audio readers for the visually-impaired, which we felt was too difficult
to implement with our skill level and time constraints.
