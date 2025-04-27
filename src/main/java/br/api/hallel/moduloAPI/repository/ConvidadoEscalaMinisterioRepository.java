package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.dto.v1.ministerio.ConvidadoEscalaMinisterioUserResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.ConvidadoEscalaMinisterioWithConviteResponse;
import br.api.hallel.moduloAPI.model.ConvidadoEscalaMinisterio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvidadoEscalaMinisterioRepository extends
        MongoRepository<ConvidadoEscalaMinisterio, String> {

    @Query(value = "{escala_ministerio_id: ?0}", fields = "{'_id': 1, 'nome': 1, 'email': 1}")
    public List<ConvidadoEscalaMinisterioUserResponse> listAllConvidadosUserInfosByEscalaId(String idEscala);

    @Query(value = "{escala_ministerio_id: ?0}", fields = "{'_id': 1, 'nome': 1, 'email': 1, 'convite_escala_id': 1}")
    public List<ConvidadoEscalaMinisterioWithConviteResponse> listAllConvidadosWithConvitesInfosByEscalaId(String idEscalaMinisterio);


}
