@RestController
@RequestMapping("/api/boards")
public class BoardController{
    private final BoardRepository1 boardRepository1;
    public class BoardController(BoardRepository1 boardRepository1){
        this.boardRepository1=boardRepository1;
    }
    @GetMapping
    public List<Board> getAllBoards(){
        return BoardRepository1.findAll();
    }
}