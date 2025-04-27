package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.model.Membro;
import br.api.hallel.moduloAPI.payload.resposta.MembroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembroRepository extends MongoRepository<Membro, String> {

    Optional<Membro> findByEmailAndSenha(String email, String senha);

    Optional<Membro> findByEmail(String email);

    Optional<Membro> findByNome(String username);

    boolean existsByEmail(String email);

    boolean existsByNome(String nome);

    Optional<Membro> findByNomeAndEmail(String nome, String email);

    Optional<Membro> findByToken(String token);

    @Aggregation(pipeline = {
            "{$addFields: {idIntoString: {$toString: '$_id'}}}",
            "{$lookup: {from: 'membroMinisterio', localField: 'idIntoString', foreignField: 'membroId', as: 'membroMinisterio' }}",
            "{$match:  {'membroMinisterio.ministerioId': {$ne:  ?0}}}"
    })
    Slice<MembroResponse> findMembrosWithNoParticipationInThisMinisterio(String idMinisterio, Pageable pageable);
    Page<Membro> findAllByOrderByStatusMembroDescNomeAsc(Pageable pageable);

    List<MembroResponse> findAllByNomeContainingIgnoreCase(String name);
}
