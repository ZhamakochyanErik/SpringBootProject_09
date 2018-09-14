package friendfinder.net.config.security;

import friendfinder.net.model.User;
import friendfinder.net.model.enums.ActivationType;
import friendfinder.net.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Qualifier("USD")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw new UsernameNotFoundException("");
        }else if(optionalUser.get().getActivationType() == ActivationType.INACTIVE){
            return new CurrentUser(optionalUser.get(),false);
        }else {
            return new CurrentUser(optionalUser.get());
        }
    }
}
