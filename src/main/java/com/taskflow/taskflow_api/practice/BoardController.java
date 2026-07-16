@RestController
public class BoardController{
    @GetMapping("/board"){
        public String getBoards(){
            return "board list";
        }
    }
}
