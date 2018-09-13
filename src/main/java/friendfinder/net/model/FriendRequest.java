package friendfinder.net.model;

import friendfinder.net.model.enums.RequestStatus;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User from;

    @ManyToOne
    private User to;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
}