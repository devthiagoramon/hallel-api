package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.model.EnsaioMinisterio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EnsaioMinisterioRepository extends MongoRepository<EnsaioMinisterio, String> {

    List<EnsaioMinisterio> findAllByIdMinisterio(String idMinisterio);
}
