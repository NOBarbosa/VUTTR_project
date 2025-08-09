package vuttr.VUTTR.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vuttr.VUTTR.dto.ToolsCreateDto;
import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.service.ToolsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tools")
public class ToolsController {
    @Autowired
    private ToolsService toolsService;
}
