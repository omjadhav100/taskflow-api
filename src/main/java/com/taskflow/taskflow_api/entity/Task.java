@Entity
public class Task extends BaseEntity{
    private String title;
     private String description;
    private String status;       
    private LocalDate dueDate;
    private Long listId;         
    private int position;  
}