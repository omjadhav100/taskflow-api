public class GenericInMemoryRepository<T extends BaseEntity>{
    private final Map<Long,T> storage=new HashMap<>();
    private final AtomicLong idCounter=new AtomicLong(1);
    public T save (T entity){
        if(entity.getId()==null){
            entity.setId(IdCounter.getAndIcrement());
        }
        storage.put(entity.getId(),entity);
        return entity;
    }
    public Optimal<T> findbyId(Long id){
        return Optimal.ofNullable(storage.get(id));
    }
    public List<T> findbyId(){
        return new ArrayList<>(storage.values());
    }
    public void deleteById(Long id){
        storage.remove(id);
    }
}