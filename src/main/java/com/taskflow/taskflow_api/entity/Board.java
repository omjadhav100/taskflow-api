@Entity
public class Board extends BaseEntity{
    private String title;
    @oneToMany(mappedBy="board",cascade=CascadeType.ALL)
    private list<TaskList> lists=new ArrayList();
}
map<String,List<Task>> tasksBysStatus=tasks.stream();
   .collect(Collectors.groupingBy(Task::getStatus));