package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.model.ConviteEscalaMinisterio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConviteEscalaMinisterioRepository extends
        MongoRepository<ConviteEscalaMinisterio, String> {
}
