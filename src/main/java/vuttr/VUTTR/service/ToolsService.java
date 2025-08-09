package vuttr.VUTTR.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vuttr.VUTTR.dto.ToolsCreateDto;
import org.springframework.data.domain.Page;

import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.exception.NotFoundException;
import vuttr.VUTTR.repository.ToolsRepository;
import vuttr.VUTTR.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ToolsService {
    @Autowired

    private ToolsRepository toolsRepository;
    private  UserRepository userRepository;

    public ToolsService(ToolsRepository toolsRepository, UserRepository userRepository) {
        this.toolsRepository = toolsRepository;
        this.userRepository = userRepository;
    }
    public Tools findOneByUser(Long userId, Long toolId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return toolsRepository.findByIdAndUserId(toolId, userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Ferramenta não encontrada para userId=%d e toolId=%d"
                                        .formatted(userId, toolId)
                        )
                );
    }

    public List<Tools> listByUserAndOptionalTag(Long userId, String tag) {
        if (tag == null || tag.isBlank()) {
            return toolsRepository.findAllByUserId(userId);
        }
        return toolsRepository.findByUserIdAndTagIgnoreCase(userId, tag.trim());
    }
}
