package tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import command.Command;
import storage.Storage;

public class TaskListTest {

    private final Storage store = new Storage("test.txt");
    private final TaskList taskList = new TaskList(store);

    @Test
    public void addTask() {
        String actResult = this.taskList.addTask(Command.TODO, "borrow book");
        String expResult = "Got it. MUG has added this task:\n"
                + "[T][✘] borrow book"
                + "\nNow you have "
                + 3
                + " tasks in the list.";
        assertEquals(expResult, actResult);
    }

    @Test
    public void taskDone() {
        String actResult = this.taskList.taskDone(3);
        String expResult = "Congratz! MUG has marked this task as done:\n"
                + "[T][✓] borrow book";
        assertEquals(expResult, actResult);
    }

    @Test
    public void deleteTask() {
        String actResult = this.taskList.deleteTask(3);
        String expResult = "Noted. MUG has removed this task:\n"
                + "[T][✓] borrow book"
                + "\nNow you have "
                + 2
                + " tasks in the list.";
        assertEquals(expResult, actResult);
    }

    @Test
    public void readList() {
        String actResult = this.taskList.readList();
        String expResult = "Here is your tasks:\n"
                + "1. [T][✘] read book\n"
                + "2. [T][✘] return book\n"
                + "3. [T][✘] borrow book";
        assertEquals(expResult, actResult);
    }

    @Test
    public void searchTask() {
        String actResult = this.taskList.searchTask("read");
        String expResult = "Here is the result:\n"
                + "1. [T][✘] read book";
        assertEquals(expResult, actResult);
    }

}
