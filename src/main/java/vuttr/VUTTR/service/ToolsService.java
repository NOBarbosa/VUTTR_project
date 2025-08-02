package vuttr.VUTTR.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vuttr.VUTTR.dto.ToolsCreateDto;
import vuttr.VUTTR.entity.Tools;
import vuttr.VUTTR.exception.NotFoundException;
import vuttr.VUTTR.repository.ToolsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ToolsService {
    @Autowired

    private ToolsRepository toolsRepository;

    public List<Tools> findAll(){ return toolsRepository.findAll();}

    public Optional<Tools> findOneById(long id){

        return Optional.ofNullable(toolsRepository.findById(id).orElseThrow(() -> new NotFoundException("Não encontramos ferramenta com o id: " + id)));
    }


    public Tools createTools(ToolsCreateDto createDto){
        Tools tools = new Tools();
            tools.setTitle(createDto.getTitle());
            tools.setLink(createDto.getLink());
            tools.setDescription(createDto.getDescription());
            tools.setTags(createDto.getTags());

        return toolsRepository.save(tools);
    }
}
