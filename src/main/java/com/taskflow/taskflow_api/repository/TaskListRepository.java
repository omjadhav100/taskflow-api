@Repository
public class TaskListRepository extends GenericInMemeoryRepository<TaskList>{
    public List<TaskList> findByBoardId(Long boardId){
       return findAll().stream()
       .filter(l->l.getBoardId().equals(boardId))
       .collect(Collectors.toList()); 
    }
}