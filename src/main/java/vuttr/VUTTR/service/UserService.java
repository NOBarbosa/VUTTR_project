package vuttr.VUTTR.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vuttr.VUTTR.dto.ToolsCreateDto;
import vuttr.VUTTR.dto.UserCreateDto;
import vuttr.VUTTR.dto.UserCreateResponseDTo;
import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.entity.User;
import vuttr.VUTTR.exception.NotFoundException;
import vuttr.VUTTR.exception.UniqueEmailException;
import vuttr.VUTTR.repository.ToolsRepository;
import vuttr.VUTTR.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ToolsRepository toolsRepository;

    @Autowired
    public UserService(UserRepository userRepository, ToolsRepository toolsRepository) {
        this.userRepository = userRepository;
        this.toolsRepository = toolsRepository;
    }

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
    @Transactional
    public Tools createTool(Long userId, ToolsCreateDto toolsDto) {
       var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Tools tool = new Tools();
        tool.setTitle(toolsDto.getTitle());
        tool.setLink(toolsDto.getLink());
        tool.setDescription(toolsDto.getDescription());
        tool.setTags(toolsDto.getTags());
        tool.setUser(user);

        return toolsRepository.save(tool);
    }

    @Transactional
    public void deleteUser(Long id){
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        userRepository.deleteById(id);

    }


}
