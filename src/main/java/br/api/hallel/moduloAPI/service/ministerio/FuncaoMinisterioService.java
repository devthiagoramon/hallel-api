package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.DefineFunctionsDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.FuncaoMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.MembroMinisterioWithInfosResponse;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.FuncaoMinisterio;
import br.api.hallel.moduloAPI.model.MembroMinisterio;
import br.api.hallel.moduloAPI.repository.FuncaoMinisterioRepository;
import br.api.hallel.moduloAPI.repository.MembroRepository;
import br.api.hallel.moduloAPI.repository.MinisterioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class FuncaoMinisterioService implements FuncaoMinisterioInterface{

    @Autowired
    private MinisterioRepository ministerioRepository;
    @Autowired
    private MembroRepository membroRepository;

    private MinisterioMapper ministerioMapper;

    @Autowired
    private FuncaoMinisterioRepository funcaoMinisterioRepository;
    @Autowired
    private MembroMinisterioService membroMinisterioService;

    @Override
    public FuncaoMinisterio createFuncaoMinisterio(
            FuncaoMinisterioDTO funcaoMinisterioDTO) {
        log.info("Creating função ministerio...");
        FuncaoMinisterio funcaoMinisterio = MinisterioMapper.toFuncaoMinisterio(funcaoMinisterioDTO);
        return this.funcaoMinisterioRepository.insert(funcaoMinisterio);
    }

    @Override
    public List<FuncaoMinisterio> listFuncaoOfMinisterio(
            String idMinisterio) {
        log.info("Listing funções ministerio from ministerio " + idMinisterio + "...");
        return this.funcaoMinisterioRepository.findAllByMinisterioId(idMinisterio);
    }

    @Override
    public FuncaoMinisterio listFuncaoMinisterioById(
            String idFuncaoMinisterio) {
        log.info("Listing funcao ministerio by id " + idFuncaoMinisterio + "...");
        Optional<FuncaoMinisterio> optional = this.funcaoMinisterioRepository.findById(idFuncaoMinisterio);
        if (optional.isEmpty()) {
            log.info("Error listing função ministerio: Can't find this id");
            throw new RuntimeException("Can't find função ministerio by this id");
        }
        return optional.get();
    }

    @Override
    public FuncaoMinisterio editFuncaoMinisterio(
            String idFuncaoMinisterio,
            FuncaoMinisterioDTO funcaoMinisterioDTO) {
        FuncaoMinisterio funcaoMinisterio = listFuncaoMinisterioById(idFuncaoMinisterio);
        log.info("Editing função ministerio " + funcaoMinisterio.getNome() + "...");
        FuncaoMinisterio funcaoMinisterioNew = MinisterioMapper.toFuncaoMinisterio(funcaoMinisterioDTO);
        funcaoMinisterio.setNome(funcaoMinisterioNew.getNome());
        funcaoMinisterio.setCor(funcaoMinisterioNew.getCor());
        funcaoMinisterio.setDescricao(funcaoMinisterioNew.getDescricao());
        funcaoMinisterio.setIcone(funcaoMinisterioNew.getIcone());
        log.info("Função ministerio " + funcaoMinisterio.getId() + " edited!");
        return this.funcaoMinisterioRepository.save(funcaoMinisterio);
    }

    @Override
    public void deleteFuncaoMinisterio(String idFuncaoMinisterio) {
        log.info("Delete função ministerio " + idFuncaoMinisterio + "...");
        FuncaoMinisterio funcaoMinisterio = listFuncaoMinisterioById(idFuncaoMinisterio);
        this.funcaoMinisterioRepository.delete(funcaoMinisterio);
        log.info("Função ministerio " + funcaoMinisterio.getNome() + " deleted!");
    }

    @Override
    public MembroMinisterioWithInfosResponse defineFunctionsToMembroMinisterio(
            DefineFunctionsDTO defineFunctionsDTO) {
        log.info("Defining/Deleting functions of membro ministerio...");
        String idMembroMinisterio = defineFunctionsDTO.getIdMinisterioMembro();
        MembroMinisterio membroMinisterio = membroMinisterioService.getMembroMinisterioById(idMembroMinisterio);
        if (membroMinisterio.getFuncaoMinisterioIds() == null) {
            membroMinisterio.setFuncaoMinisterioIds(new ArrayList<>());
        }
        if (defineFunctionsDTO.getIdsFuncaoMinisterioAdd() != null) {
            defineFunctionsDTO.getIdsFuncaoMinisterioAdd()
                              .forEach(idAdds -> {
                                  if (!membroMinisterio.getFuncaoMinisterioIds()
                                                       .contains(idAdds)) {
                                      membroMinisterio.getFuncaoMinisterioIds()
                                                      .add(idAdds);
                                  }
                              });
        }
        if (defineFunctionsDTO.getIdsFuncaoMinisterioRemove() != null) {
            defineFunctionsDTO.getIdsFuncaoMinisterioRemove()
                              .forEach(idRemove -> {
                                  if (membroMinisterio.getFuncaoMinisterioIds()
                                                      .contains(idRemove)) {
                                      membroMinisterio.getFuncaoMinisterioIds()
                                                      .removeIf(item -> item.equals(idRemove));
                                  }
                              });
        }
        membroMinisterioService.membroMinisterioRepository.save(membroMinisterio);
        return membroMinisterioService.listMembroMinisterioById(idMembroMinisterio);
    }
}
