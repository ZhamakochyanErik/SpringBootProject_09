package friendfinder.net.model;

import friendfinder.net.model.enums.MessageStatus;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User from;

    @ManyToOne
    private User to;

    private String message;

    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
}