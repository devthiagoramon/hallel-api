package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.dto.v1.ministerio.SimpleEscalaMinisterioResponse;
import br.api.hallel.moduloAPI.dto.v2.ministerio.EscalaMinisterioResponseWithInfosV2;
import br.api.hallel.moduloAPI.model.EscalaMinisterio;
import br.api.hallel.moduloAPI.repository.custom.CustomEscalaMinisterioRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EscalaMinisterioRepository
        extends MongoRepository<EscalaMinisterio, String>,
        CustomEscalaMinisterioRepository {

    List<EscalaMinisterio> findByEventoId(String idEvento);

    List<EscalaMinisterio> findByRepertorioIdsContaining(
            String repertorioId);

    @Query(value = "{'ministerioId': ?0, 'date': {$gte:  ?1, $lte:  ?2}}",
           fields = "{'_id': 1, 'date': 1}")
    List<SimpleEscalaMinisterioResponse> findAllIdsByMinisterioIdAndRangeDate(
            String ministerioId, LocalDateTime start,
            LocalDateTime end);

    @Query(value = "{'membrosMinisterioConvidadosIds': ?0, 'date':  {$gte: ?1, $lte: ?2}}",
           fields = "{'_id': 1, 'date':  1}")
    List<SimpleEscalaMinisterioResponse> findEscalaMinisterioIdsByMembroIdCanPaticipate(
            String membroId,
            LocalDateTime start,
            LocalDateTime end);

    @Query(value = "{'membrosMinisterioConfimadoIds': ?0, 'date': {$gte: ?1, $lte: ?2}}",
           fields = "{'_id': 1, 'date': 1}")
    List<SimpleEscalaMinisterioResponse> findEscalaMinisterioIdsByMembroIdParticipate(
            String membroId, LocalDateTime start, LocalDateTime end);

    @Query(value = "{'_id': ?0}")
    EscalaMinisterioResponseWithInfosV2 findByIdWithAllInfos(
            String idEscalaMinisterio);

    Optional<EscalaMinisterio> findByEnsaioMinisterioId(
            String ensaioId);

    @Query(value = "{'isEnsaio':  false, ministerioId: ?0 ,'date': {'$gte': ?1}}",
           sort = "{'date': 1}",
           fields = "{'_id':  1, 'date':  1}")
    List<SimpleEscalaMinisterioResponse> findAllEscalasMinisterioCanAddIntoEnsaioOfMinisterioFromDate(
            String idMinisteiro,
            LocalDateTime from);
}
