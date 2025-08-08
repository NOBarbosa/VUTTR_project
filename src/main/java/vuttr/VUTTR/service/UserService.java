package vuttr.VUTTR.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vuttr.VUTTR.dto.ToolsCreateDto;
import vuttr.VUTTR.dto.UserCreateDto;
import vuttr.VUTTR.dto.UserCreateResponseDTo;
import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.entity.User;
import vuttr.VUTTR.exception.UniqueEmailException;
import vuttr.VUTTR.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired

    private UserRepository userRepository;

    public List<UserCreateResponseDTo> findAll(){
    return  userRepository.findAll().stream().map(user -> new UserCreateResponseDTo(user.getId(), user.getName(), user.getEmail(), user.getTools()))
            .collect(Collectors.toList());

    }


    public User createUser(UserCreateDto userDto){

        boolean exists = userRepository.findByEmail(userDto.getEmail()).isPresent();
        if (exists) {
            throw new UniqueEmailException("E-mail já cadastrado: " + userDto.getEmail());
        }
        User user = new User();


        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());


        return userRepository.save(user);
    }
}
