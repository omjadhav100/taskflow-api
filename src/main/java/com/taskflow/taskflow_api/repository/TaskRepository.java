@Repository
public class TaskRepository extends GenericInMemoryRepository<Task>{
    public List<Task> findByListId(Long listId){
        return findAll().stream()
        .filter(t->t.getListId().equals(listId))
        .collect(Collectors.toList());
    }
}