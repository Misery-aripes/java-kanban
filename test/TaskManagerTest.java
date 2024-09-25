import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    @Test
    public void shouldImplementTaskManagerInterface() {
        TaskManager manager = new InMemoryTaskManager();
        assertNotNull(manager, "Менеджер задач не должен быть null");
    }
}