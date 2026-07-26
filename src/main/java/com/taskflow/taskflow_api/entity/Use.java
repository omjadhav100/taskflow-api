@Entity
public class Use extends BaseEntity{
    @column(unique=true,nullable=false)
    private String email;
    @Column(nullable=false)
    private String password;

    private String name;
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,orphanRemoval=true)
    private List<Board> boards=new ArrayList<>();
}