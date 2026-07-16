@RestController
@RequestMapping("/api/boards")
public class BoardController{
    @GetMapping
    public List<BoardResponseDTO> getAllBoards() {...}
    @GetMapping({"/id"})
    public BoardResponseDTO getBoard(@PathVariable Long id){...}
    @PostMapping
    public BoardResponseDTO createBoard(@RequestBody CreateBoardRequest request){...}
    @PutMapping({"/id"})
    public BoardResponseDTO updateBoard(@PathVariable Long id,@RequestBody CreateBoardRequest request){...}
    @DeleteMapping({"/id"})
    public BoardResponseDTO deleteBoard("/id") {...}
    public void delteBoard(@PathVariable Long id){...}

}

