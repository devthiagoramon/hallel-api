package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.dto.v1.ministerio.EventosShortResponse;
import br.api.hallel.moduloAPI.model.Eventos;
import br.api.hallel.moduloAPI.payload.resposta.EventosVisualizacaoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventosRepository extends MongoRepository<Eventos, String> {
    Optional<Eventos> findByTitulo(String titulo);
    List<Eventos> findAllByDestaqueEquals(Boolean isTrue);
    List<Eventos> findAllByOrderByTituloAsc(Pageable pageable);

    List<Eventos> findAllByOrderByDateAsc();

    List<EventosShortResponse> findAllByMinisteriosAssociadosContains(String idMinisterio);

    @Query(value = "{'_id': ?0}", fields = "{'_id': 1, 'titulo':  1, 'date':  1, 'fileImageUrl':  1, 'banner': 1}")
    Optional<EventosShortResponse> findByIdShort(String idEvento);


    List<EventosVisualizacaoResponse> findAllByTituloContainingIgnoreCase(String titulo);
}
