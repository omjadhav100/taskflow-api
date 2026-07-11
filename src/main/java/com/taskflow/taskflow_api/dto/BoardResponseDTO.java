@Entity
public class Board extends BaseEntity{
    private String title;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy="board")
    private List<TaskList> lists;
}
public class BoardResponseDTO{
    private Long id;
    private String title;
    private int ListCount;
    public BoardResponseDTO(Board board){
        this.id=board.getId();
        this.title=board.getTitle();
        this.ListCount=board.getLists().size();
    }
}