public interface Boardrepository extends Jparepository<Board,Long>{
    List<Board>findByUserId(long userId);
}