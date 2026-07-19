@RestController
@RequestMapping("api/boards/{boardId}/lists")
public class TaskListCantroller{
    private final TaskListService taskListService;
    public TaskListController(TaskListService taskListService){
        this.taskListController=taskListController;
    }
    @GetMapping
    public List<TaskList> getLists(@PathVariable Long BoardId){
        return taskListService.getListsForBoard(boardId);
    }
    @PostMapping
    public TaskList createList(@PathVariable Long boardId,@RequestBody CreateListRequest request){
        return taskListService.createList(boardId,request.getName());
    }
}