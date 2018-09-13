package friendfinder.net.config.security;

import friendfinder.net.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class CurrentUser extends org.springframework.security.core.userdetails.User{

    @Getter
    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(),user.getPassword(),AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }
}
