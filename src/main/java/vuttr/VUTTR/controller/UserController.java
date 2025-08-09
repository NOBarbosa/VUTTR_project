package vuttr.VUTTR.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vuttr.VUTTR.dto.ToolEditDto;
import vuttr.VUTTR.dto.ToolsCreateDto;
import vuttr.VUTTR.dto.UserCreateDto;
import vuttr.VUTTR.dto.UserCreateResponseDTo;
import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.entity.User;
import vuttr.VUTTR.service.ToolsService;
import vuttr.VUTTR.service.UserService;

import java.util.List;

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

    @DeleteMapping("/{userId}/tools/{toolId}")
    public ResponseEntity<Void> deleteToolById(
            @PathVariable Long userId,
            @PathVariable Long toolId
    ){
         toolsService.deleteOneByUser(userId, toolId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();

    }

    @PatchMapping("/{userId}/tools/{toolId}")
    public ResponseEntity<Tools> patchTool(
            @PathVariable Long userId,
            @PathVariable Long toolId,
            @RequestBody ToolEditDto body
    ) {
        Tools updated = toolsService.updateTool(userId, toolId, body);


        return ResponseEntity.ok(updated);
    }
}
