package vuttr.VUTTR.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.entity.User;

import java.util.List;

public class UserCreateResponseDTo {
    @NotBlank
    @NotNull
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String email;

    private List<Tools> tools;

    public UserCreateResponseDTo(Long id, String name, String email, List tools) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tools = tools;
    }

    public List<Tools> getTools() {
        return tools;
    }

    public void setTools(List<Tools> tools) {
        this.tools = tools;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
