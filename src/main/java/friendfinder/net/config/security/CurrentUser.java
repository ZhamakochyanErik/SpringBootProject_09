package friendfinder.net.config.security;

import friendfinder.net.model.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User{

    @Getter
    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(),user.getPassword(),AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public CurrentUser(User user,boolean isActive) {
        super(user.getEmail(),user.getPassword(), true, true, true,
                isActive,AuthorityUtils.createAuthorityList(user.getRole().name()) );
        this.user = user;
    }
}
