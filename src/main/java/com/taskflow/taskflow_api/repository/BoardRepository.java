@Repository
public class BoardRepository extends GenericInMemoryRepository{
    public List<Board> findByUserId(Long userId){
        return findAll().stream()
        .filter(b->b.getUserId().equals(userId))
        .collect(Collectors.toList());
    }
}