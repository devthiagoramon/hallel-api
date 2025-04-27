package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.model.NaoConfirmadoEscalaMinisterio;
import br.api.hallel.moduloAPI.repository.custom.CustomNaoConfirmadoEscalaMinisterioRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


// Repository to custom
public interface NaoConfirmadoEscalaMinisterioRepository extends
        MongoRepository<NaoConfirmadoEscalaMinisterio, String>,
        CustomNaoConfirmadoEscalaMinisterioRepository {
    List<NaoConfirmadoEscalaMinisterio> findAllByIdMembroMinisterio(
            String idMembroMinisterio);

    Optional<NaoConfirmadoEscalaMinisterio> findByIdMembroMinisterioAndIdEscalaMinisterio(String idMembro, String idEscala);
}
