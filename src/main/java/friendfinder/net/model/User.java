package friendfinder.net.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import friendfinder.net.model.enums.ActivationType;
import friendfinder.net.model.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private int age;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private String profileImg;

    private String coverImg;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String token;

    @Enumerated(EnumType.STRING)
    private ActivationType activationType;

    @ManyToOne
    private City city;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_bookmark",joinColumns = @JoinColumn(name = "from_id"),
    inverseJoinColumns = @JoinColumn(name = "to_id"))
    private List<User> fromList;

    @JsonIgnore
    @ManyToMany(mappedBy = "fromList",cascade = CascadeType.MERGE)
    private List<User> toList;
}