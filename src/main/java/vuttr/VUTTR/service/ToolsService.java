package vuttr.VUTTR.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vuttr.VUTTR.dto.ToolEditDto;
import vuttr.VUTTR.dto.ToolsCreateDto;
import org.springframework.data.domain.Page;

import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.exception.NonEmptyException;
import vuttr.VUTTR.exception.NotFoundException;
import vuttr.VUTTR.repository.ToolsRepository;
import vuttr.VUTTR.repository.UserRepository;

import java.util.*;

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
    @Transactional
    public Optional<Void> deleteOneByUser(Long userId, Long toolId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        toolsRepository.findById(toolId).orElseThrow(() -> new NotFoundException("Ferramenta não encontrada"));
        if (!toolsRepository.existsByIdAndUserId(toolId, userId)) {
            throw new NotFoundException("Ferramenta não pertence ao usuário");
        }

       return  toolsRepository.deleteByIdAndUserId(toolId, userId);
    }


    @Transactional
    public Tools updateTool(Long userId, Long toolId, ToolEditDto dto) {
        Tools tool = toolsRepository.findByIdAndUserId(toolId, userId)
                .orElseThrow(() -> new NotFoundException("Ferramenta não encontrada para este usuário"));

        if (dto.getTitle() != null) {
            if (dto.getTitle().isBlank()) {
                throw new NonEmptyException("title não pode ser vazio");
            }
            tool.setTitle(dto.getTitle().trim());
        }

        if (dto.getLink() != null) {
            if (dto.getLink().isBlank()) {
                throw new NonEmptyException("link não pode ser vazio");
            }
            tool.setLink(dto.getLink().trim());
        }

        if (dto.getDescription() != null) {
            if (dto.getDescription().isBlank()) {
                throw new NonEmptyException("description não pode ser vazio");
            }
            tool.setDescription(dto.getDescription().trim());
        }

        if (dto.getTags() != null) {
            if (dto.getTags().stream().anyMatch(tag -> tag == null || tag.isBlank())) {
                throw new NonEmptyException("tags não pode conter itens vazios");
            }
            tool.setTags(new ArrayList<>(dto.getTags()));
        }

        return toolsRepository.save(tool);
    }

}
