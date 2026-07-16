@GetMapping ("/boards")
public BoardResponseDTO getBoard(){
  return new BoardResponseDTO(1L,"myboard",3);
}