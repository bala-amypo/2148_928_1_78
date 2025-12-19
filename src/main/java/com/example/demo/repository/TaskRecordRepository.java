@Entity
public class TaskRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskCode; // ✅ add this
    private String title;
    private String description;
    private String requiredSkill;
    private String requiredLevel;
    private String status;

    // getters and setters
}
