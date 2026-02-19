package catbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import catbot.task.Deadline;
import catbot.task.Event;
import catbot.task.Task;
import catbot.task.Todo;

public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void addTask_singleTask_increasesSize() {
        taskList.addTask(new Todo("read book"));
        assertEquals(1, taskList.getSize());
    }

    @Test
    public void addTask_multipleTasks_increasesSize() throws CatbotException {
        taskList.addTask(new Todo("read book"));
        taskList.addTask(new Deadline("submit report", "2024-10-01 1800"));
        taskList.addTask(new Event("project meeting", "2024-08-06 1400", "2024-08-06 1600"));
        assertEquals(3, taskList.getSize());
    }

    @Test
    public void list_emptyList_returnsEmptyMessage() {
        assertEquals("No tasks in the list.", taskList.list());
    }

    @Test
    public void list_singleTask_returnsFormattedTask() {
        taskList.addTask(new Todo("read book"));
        String result = taskList.list();
        assertTrue(result.contains("Here are the tasks in your list:"));
        assertTrue(result.contains("1. [T][ ] read book"));
    }

    @Test
    public void list_multipleTasks_returnsAllTasks() {
        taskList.addTask(new Todo("read book"));
        taskList.addTask(new Todo("do homework"));
        String result = taskList.list();
        assertTrue(result.contains("1. [T][ ] read book"));
        assertTrue(result.contains("2. [T][ ] do homework"));
    }

    @Test
    public void mark_validIndex_marksTaskAsDone() throws CatbotException {
        taskList.addTask(new Todo("read book"));
        Task result = taskList.mark(0);
        assertTrue(result.toString().contains("[T][X] read book"));
    }

    @Test
    public void mark_invalidIndex_throwsException() {
        taskList.addTask(new Todo("read book"));
        CatbotException exception = assertThrows(CatbotException.class, () -> taskList.mark(5));
        assertEquals("Invalid task number.", exception.getMessage());
    }

    @Test
    public void mark_negativeIndex_throwsException() {
        taskList.addTask(new Todo("read book"));
        CatbotException exception = assertThrows(CatbotException.class, () -> taskList.mark(-1));
        assertEquals("Invalid task number.", exception.getMessage());
    }

    @Test
    public void unmark_validIndex_unmarksDone() throws CatbotException {
        taskList.addTask(new Todo("read book"));
        taskList.mark(0);
        Task result = taskList.unmark(0);
        assertTrue(result.toString().contains("[T][ ] read book"));
    }

    @Test
    public void unmark_invalidIndex_throwsException() {
        taskList.addTask(new Todo("read book"));
        CatbotException exception = assertThrows(CatbotException.class, () -> taskList.unmark(5));
        assertEquals("Invalid task number.", exception.getMessage());
    }

    @Test
    public void delete_validIndex_removesTask() throws CatbotException {
        taskList.addTask(new Todo("read book"));
        taskList.addTask(new Todo("do homework"));
        Task result = taskList.delete(0);
        assertTrue(result.toString().contains("[T][ ] read book"));
        assertEquals(1, taskList.getSize());
    }

    @Test
    public void delete_invalidIndex_throwsException() {
        taskList.addTask(new Todo("read book"));
        CatbotException exception = assertThrows(CatbotException.class, () -> taskList.delete(5));
        assertEquals("Invalid task number.", exception.getMessage());
    }

    @Test
    public void delete_negativeIndex_throwsException() {
        taskList.addTask(new Todo("read book"));
        CatbotException exception = assertThrows(CatbotException.class, () -> taskList.delete(-1));
        assertEquals("Invalid task number.", exception.getMessage());
    }

    @Test
    public void find_matchingKeyword_returnsMatchingTasks() {
        taskList.addTask(new Todo("read book"));
        taskList.addTask(new Todo("buy book"));
        taskList.addTask(new Todo("do homework"));
        String result = taskList.find("book");
        assertTrue(result.contains("Here are the matching tasks in your list:"));
        assertTrue(result.contains("1. [T][ ] read book"));
        assertTrue(result.contains("2. [T][ ] buy book"));
    }

    @Test
    public void find_noMatchingKeyword_returnsNoMatchMessage() {
        taskList.addTask(new Todo("read book"));
        taskList.addTask(new Todo("do homework"));
        String result = taskList.find("xyz");
        assertEquals("No matching tasks found.", result);
    }

    @Test
    public void find_emptyList_returnsNoMatchMessage() {
        String result = taskList.find("book");
        assertEquals("No matching tasks found.", result);
    }

    @Test
    public void update_validIndex_updatesTask() throws CatbotException {
        taskList.addTask(new Todo("read book"));
        Task result = taskList.update(0, new Todo("read magazine"));
        assertTrue(result.toString().contains("[T][ ] read magazine"));
    }

    @Test
    public void update_invalidIndex_throwsException() {
        taskList.addTask(new Todo("read book"));
        CatbotException exception = assertThrows(CatbotException.class, () ->
                taskList.update(5, new Todo("read magazine")));
        assertEquals("Invalid task number.", exception.getMessage());
    }

    @Test
    public void getSize_emptyList_returnsZero() {
        assertEquals(0, taskList.getSize());
    }

    @Test
    public void getSize_afterMultipleOperations_returnsCorrectSize() throws CatbotException {
        taskList.addTask(new Todo("task 1"));
        taskList.addTask(new Todo("task 2"));
        assertEquals(2, taskList.getSize());
        taskList.delete(0);
        assertEquals(1, taskList.getSize());
    }
}
