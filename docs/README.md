# YapGPT User Guide

![Showcase of YapGPT user interface](/docs/Ui.png)

YapGPT is your friendly, robot-themed task management chatbot with a
modern JavaFX interface. It helps you keep track of all your tasks, 
including todos, deadlines, and events.

You can interact with YapGPT either via the Command-Line Interface (CLI) 
or through the Graphical User Interface (GUI).

---

## Feature Showcase

### 1. Add different type of tasks

Add a todo, deadline and event task for yapGPT to keep track of!
```
// Adds a todo task
todo borrow a book from the library

// Adds a deadline task
deadline read the book /by 2025-08-31

// Adds an event task
event attend a book club /from 2025-09-01 1200 /to 2025-09-01 1400
```

### 2. View your list of tasks

See your current list of tasks using the `list` command.
```
list
```
### 3. Mark or unmark your tasks accordingly

Mark or unmark tasks using the `mark` and `unmark` command respectively.
```
// Marks tasks 1 and 2
mark 1
mark 2

// Unmarks tasks 2
unmark 2
```

### 4. Delete tasks 

Delete tasks from the task list using the `delete` command.
````
// Deletes tasks 3 
delete 3
````
### 5. Search for tasks that occur on a specific date

Find tasks from the task list that occur on a specific date using the `on` command.
````
// Returns all tasks that occur on 2025-08-31
on 2025-08-31
````

### 6. Search for tasks that contain certain keywords

Search for tasks from the task list that contain specific keywords using the `find` command.
````
// Returns all tasks that contains the word "book"
find book 

// Returns all tasks that contains the words "borrow" and "book"
find borrow book
````

### 7. Extension feature: Statistics

Check how many tasks of each type (todo, deadline, event) there are using the `stats` command.
````
// Returns the count of each type of tasks.
stats
````

### 8. Exiting the programme

Exit the programme using the `bye` command. 
````
// Displays goodbye message and closes the programme.
bye
````
---
### That's all for the user guide. Happy chatting! 