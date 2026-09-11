package edu.hbuas.campustodo.service;

import edu.hbuas.campustodo.model.Task;
import edu.hbuas.campustodo.model.Task.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TaskService} 的基线测试。
 *
 * <p>起始版本至少覆盖两类场景：
 * <ol>
 *   <li>新增任务：成功创建并分配 id、标题正确、初始 completed 为 false。</li>
 *   <li>空标题校验：null、空串、纯空白串均应抛出 {@link IllegalArgumentException}。</li>
 * </ol>
 *
 * <p>后续 Issue 中，开发者应在本类或新增测试类中补充对应功能的测试，
 * 并保持基线测试持续通过。
 *
 * @author CampusTodo Lab
 */
class TaskServiceTest {

    private TaskService service;

    @BeforeEach
    void setUp() {
        // 每个测试方法使用全新的 service，避免相互影响
        service = new TaskService();
    }

    // ================================================================
    // 新增任务
    // ================================================================

    @Test
    @DisplayName("addTask：合法标题应返回带 id 与标题的任务，且 completed 为 false")
    void addTask_withValidTitle_returnsTaskWithIdAndTitle() {
        Task task = service.addTask("Review pull request");

        assertNotNull(task, "addTask 不应返回 null");
        assertTrue(task.getId() > 0, "新增任务的 id 应为正数");
        assertEquals("Review pull request", task.getTitle(), "标题应与传入值一致");
        assertFalse(task.isCompleted(), "新建任务默认未完成");
    }

    @Test
    @DisplayName("addTask：连续新增任务时 id 应自增")
    void addTask_incrementsIdSequentially() {
        Task first = service.addTask("First task");
        Task second = service.addTask("Second task");

        assertEquals(first.getId() + 1, second.getId(), "第二个任务的 id 应比第一个大 1");
    }

    @Test
    @DisplayName("addTask：标题前后空白应被 trim，存储为干净标题")
    void addTask_trimsSurroundingWhitespace() {
        Task task = service.addTask("  Submit report  ");

        assertEquals("Submit report", task.getTitle(), "标题前后空白应被去除");
    }

    // ================================================================
    // 空标题校验
    // ================================================================

    @Test
    @DisplayName("addTask：null 标题应抛出 IllegalArgumentException")
    void addTask_withNullTitle_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addTask(null),
                "null 标题应被拒绝");
    }

    @Test
    @DisplayName("addTask：空字符串标题应抛出 IllegalArgumentException")
    void addTask_withEmptyTitle_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addTask(""),
                "空字符串标题应被拒绝");
    }

    @Test
    @DisplayName("addTask：纯空白标题应抛出 IllegalArgumentException")
    void addTask_withBlankTitle_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addTask("   "),
                "纯空白标题应被拒绝");
    }

    // ================================================================
    // 列出任务
    // ================================================================

    @Test
    @DisplayName("listAll：无任务时应返回空列表")
    void listAll_whenNoTasks_returnsEmptyList() {
        List<Task> all = service.listAll();

        assertNotNull(all, "listAll 不应返回 null");
        assertTrue(all.isEmpty(), "初始状态下任务列表应为空");
    }

    @Test
    @DisplayName("listAll：新增任务后应按插入顺序返回全部任务")
    void listAll_afterAddingTasks_returnsAllTasksInOrder() {
        service.addTask("Task A");
        service.addTask("Task B");
        service.addTask("Task C");

        List<Task> all = service.listAll();

        assertEquals(3, all.size(), "应返回 3 个任务");
        assertEquals("Task A", all.get(0).getTitle());
        assertEquals("Task B", all.get(1).getTitle());
        assertEquals("Task C", all.get(2).getTitle());
    }

    @Test
    @DisplayName("listAll：返回的列表不可修改")
    void listAll_returnsUnmodifiableList() {
        service.addTask("Task A");

        List<Task> all = service.listAll();

        assertThrows(UnsupportedOperationException.class,
                () -> all.add(new Task(99, "Injected")),
                "返回的列表应不可修改，防止外部绕过 addTask 改变内部状态");
    }

    // ================================================================
    // 优先级筛选（Issue #1）
    // ================================================================

    @Test
    @DisplayName("addTask：新建任务默认优先级为 MEDIUM")
    void addTask_defaultPriorityIsMedium() {
        Task task = service.addTask("Default priority task");

        assertEquals(Priority.MEDIUM, task.getPriority(), "新建任务默认优先级应为 MEDIUM");
    }

    @Test
    @DisplayName("filterByPriority：只返回指定优先级的任务，保持插入顺序")
    void filterByPriority_returnsMatchingTasksInOrder() {
        Task high = service.addTask("High task");
        high.setPriority(Priority.HIGH);
        Task medium = service.addTask("Medium task");
        Task low = service.addTask("Low task");
        low.setPriority(Priority.LOW);
        Task high2 = service.addTask("Another high task");
        high2.setPriority(Priority.HIGH);

        List<Task> highTasks = service.filterByPriority(Priority.HIGH);

        assertEquals(2, highTasks.size(), "应返回 2 个 HIGH 优先级任务");
        assertEquals("High task", highTasks.get(0).getTitle(), "第一个 HIGH 任务应为 High task");
        assertEquals("Another high task", highTasks.get(1).getTitle(), "第二个 HIGH 任务应为 Another high task");
    }

    @Test
    @DisplayName("filterByPriority：无匹配优先级时返回空列表")
    void filterByPriority_noMatch_returnsEmptyList() {
        service.addTask("Only medium task");

        List<Task> highTasks = service.filterByPriority(Priority.HIGH);

        assertNotNull(highTasks, "filterByPriority 不应返回 null");
        assertTrue(highTasks.isEmpty(), "无匹配任务时应返回空列表");
    }

    @Test
    @DisplayName("filterByPriority：空任务库时返回空列表")
    void filterByPriority_emptyRepository_returnsEmptyList() {
        List<Task> result = service.filterByPriority(Priority.LOW);

        assertNotNull(result, "filterByPriority 不应返回 null");
        assertTrue(result.isEmpty(), "空任务库筛选应返回空列表");
    }

    @Test
    @DisplayName("filterByPriority：null 优先级应抛出 IllegalArgumentException")
    void filterByPriority_withNullPriority_throwsException() {
        service.addTask("Some task");

        assertThrows(IllegalArgumentException.class,
                () -> service.filterByPriority(null),
                "null 优先级应被拒绝");
    }

    @Test
    @DisplayName("filterByPriority：返回的列表不可修改")
    void filterByPriority_returnsUnmodifiableList() {
        Task high = service.addTask("High task");
        high.setPriority(Priority.HIGH);

        List<Task> filtered = service.filterByPriority(Priority.HIGH);

        assertThrows(UnsupportedOperationException.class,
                () -> filtered.add(new Task(99, "Injected")),
                "筛选结果列表应不可修改");
    }

    @Test
    @DisplayName("filterByPriority：各优先级均能正确筛选")
    void filterByPriority_allPrioritiesWork() {
        Task high = service.addTask("H");
        high.setPriority(Priority.HIGH);
        Task medium = service.addTask("M");
        Task low = service.addTask("L");
        low.setPriority(Priority.LOW);

        assertEquals(1, service.filterByPriority(Priority.HIGH).size(), "HIGH 应有 1 个");
        assertEquals(1, service.filterByPriority(Priority.MEDIUM).size(), "MEDIUM 应有 1 个");
        assertEquals(1, service.filterByPriority(Priority.LOW).size(), "LOW 应有 1 个");
    }

    // ================================================================
    // 判空保护（Review 反馈）
    // ================================================================

    @Test
    @DisplayName("filterByPriority：空任务列表时对所有优先级筛选均不抛出 NullPointerException")
    void filterByPriority_emptyTaskList_doesNotThrowNpe() {
        // 任务列表初始为空，验证 stream() 调用不会因空列表而 NPE
        assertDoesNotThrow(() -> service.filterByPriority(Priority.HIGH),
                "空任务列表下筛选 HIGH 不应抛出异常");
        assertDoesNotThrow(() -> service.filterByPriority(Priority.MEDIUM),
                "空任务列表下筛选 MEDIUM 不应抛出异常");
        assertDoesNotThrow(() -> service.filterByPriority(Priority.LOW),
                "空任务列表下筛选 LOW 不应抛出异常");
    }

    @Test
    @DisplayName("filterByPriority：null 优先级抛出 IllegalArgumentException 而非 NullPointerException")
    void filterByPriority_nullPriority_throwsIllegalArgumentNotNpe() {
        service.addTask("Some task");

        // 验证输入 null 时不会因调用 stream() 或 equals() 而抛出 NPE，
        // 而是被前置判空拦截，抛出语义更明确的 IllegalArgumentException
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.filterByPriority(null),
                "null 优先级应抛出 IllegalArgumentException");
        assertNotNull(ex.getMessage(), "异常信息不应为空");
    }

    @Test
    @DisplayName("filterByPriority：任务中存在 null 优先级元素时不崩溃，仅排除该元素")
    void filterByPriority_taskWithNullPriority_doesNotCrash() {
        Task normal = service.addTask("Normal task");
        Task nullPriorityTask = service.addTask("Null priority task");
        nullPriorityTask.setPriority(null);

        // 筛选 MEDIUM 时，null 优先级的任务应被排除，不应抛出 NPE
        List<Task> mediumTasks = assertDoesNotThrow(
                () -> service.filterByPriority(Priority.MEDIUM),
                "存在 null 优先级任务时筛选不应崩溃");

        assertEquals(1, mediumTasks.size(), "应只返回优先级为 MEDIUM 的任务");
        assertEquals("Normal task", mediumTasks.get(0).getTitle(),
                "null 优先级的任务应被排除");
    }
}
