package br.api.hallel.moduloAPI.repository;


import br.api.hallel.moduloAPI.model.MusicaMinisterio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicaMinisterioRepository extends MongoRepository<MusicaMinisterio, String> {
    List<MusicaMinisterio> findAllByMinisterioId(String ministerioId);
}
