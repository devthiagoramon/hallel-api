package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.model.DancaMinisterio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DancaMinisterioRepository
        extends MongoRepository<DancaMinisterio, String> {
    List<DancaMinisterio> findAllByMinisterioId(String ministerioId);
}
