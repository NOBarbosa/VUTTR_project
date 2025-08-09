package vuttr.VUTTR.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vuttr.VUTTR.repository.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

@Override
public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
 var user = userRepository.findByEmail(userName)
         .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

 return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
         .password(user.getPassword())
         .authorities("USER")
         .build();
}

}

