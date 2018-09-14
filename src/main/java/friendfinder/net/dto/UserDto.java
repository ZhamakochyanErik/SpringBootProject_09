package friendfinder.net.dto;

import friendfinder.net.model.City;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private int id;

    private String name;

    private String surname;

    private String email;

    private int age;

    private String profileImg;

    private String coverImg;

    private City city;
}