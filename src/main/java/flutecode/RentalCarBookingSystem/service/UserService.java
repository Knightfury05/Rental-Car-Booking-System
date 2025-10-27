package flutecode.RentalCarBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import flutecode.RentalCarBookingSystem.entity.UserEntity;
import flutecode.RentalCarBookingSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity fcUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        //List<SimpleGrantedAuthority> grantedAuthorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        log.info("User {} has logged in", fcUser.getEmail());
        return new User(fcUser.getEmail(), fcUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
