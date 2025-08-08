package vuttr.VUTTR.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vuttr.VUTTR.dto.UserCreateDto;
import vuttr.VUTTR.dto.UserCreateResponseDTo;
import vuttr.VUTTR.entity.User;
import vuttr.VUTTR.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserCreateResponseDTo> findAll(){
        return userService.findAll();
    }

    @PostMapping
    public UserCreateResponseDTo createUser(@RequestBody UserCreateDto userDto){
         User user = userService.createUser(userDto);
        return new UserCreateResponseDTo(user.getId(), user.getName(), user.getEmail());

    }
}
