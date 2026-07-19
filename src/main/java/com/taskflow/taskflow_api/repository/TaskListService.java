@Service 
public class TaskListService{
    private final TaskListRepository tasklistRepository;
    private final BoardRepository boardRepository;

    public TaskListService(TaskListRepository taskListRepository,BoardRepository boardRepository){
        this.taskListRepository=taskListRepository;
        this.boardRepository=boardRepository;
    }
    public TaskList createList(Long boardId,String name){
        boardRepository.findById(boardId)
        .orElseThrow() -> new BoardNotFoundException(boardId);

        TaskList list=new TaskList();
        list.setName(name);
        list.setBoardId(boardId);
    }
    public List<TaskList> getListsForBoard(Long boardId){
        return TaskListRepository.findByBoardId(boardId);
    }
}