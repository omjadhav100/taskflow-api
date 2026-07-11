public abstract class BaseEntity{
    @id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private localDateTime createdAt;
    private localDateTime updatedAt;
    //getter means when to locked box and setter open
}
@Entity
public class Board extends BaseEntity{
    private String title; //means the code for not repation of values in code 
} 