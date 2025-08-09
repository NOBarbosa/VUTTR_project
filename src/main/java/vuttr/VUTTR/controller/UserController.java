package vuttr.VUTTR.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vuttr.VUTTR.dto.ToolsCreateDto;
import vuttr.VUTTR.dto.UserCreateDto;
import vuttr.VUTTR.dto.UserCreateResponseDTo;
import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.entity.User;
import vuttr.VUTTR.service.ToolsService;
import vuttr.VUTTR.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    private ToolsService toolsService;

    public UserController(UserService userService, ToolsService toolsService) {
        this.userService = userService;
        this.toolsService = toolsService;
    }

    @GetMapping
    public List<UserCreateResponseDTo> findAll(){
        return userService.findAll();
    }

    @PostMapping
    public UserCreateResponseDTo createUser(@RequestBody UserCreateDto userDto){
         User user = userService.createUser(userDto);
        return new UserCreateResponseDTo(user.getId(), user.getName(), user.getEmail(), user.getTools());

    }

    @PostMapping("/{userId}/tools")
    public ResponseEntity<Void> createTool(@PathVariable("userId") Long userId, @RequestBody ToolsCreateDto toolsDto) {
        userService.createTool(userId, toolsDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/tools/{toolId}")
    public ResponseEntity<Tools> findOneById(
            @PathVariable Long userId,
            @PathVariable Long toolId
    ) {
        Tools tool = toolsService.findOneByUser(userId, toolId);
        return ResponseEntity.ok(tool);
    }

    @GetMapping("/{userId}/tools")
    public ResponseEntity<List<Tools>> listByUser(
            @PathVariable Long userId,
            @RequestParam(name = "tag", required = false) String tag
    ) {
        var result = toolsService.listByUserAndOptionalTag(userId, tag);
        return ResponseEntity.ok(result);
    }
}
