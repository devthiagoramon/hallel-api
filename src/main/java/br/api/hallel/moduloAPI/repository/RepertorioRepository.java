package br.api.hallel.moduloAPI.repository;

import br.api.hallel.moduloAPI.dto.v1.ministerio.RepertorioResponseWithFullInfos;
import br.api.hallel.moduloAPI.model.Repertorio;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepertorioRepository
        extends MongoRepository<Repertorio, String> {
    List<Repertorio> findAllByMinisterioId(String ministerioId);

    @Aggregation(pipeline = {
            "{$addFields: {idsDancas: {$map: {input: '$dancaMinisterioIds',as: 'stringId',in: {$toObjectId: '$$stringId'}}} ,idsMusica:{$map: {input: '$musicasIds',as: 'stringId',in: {$toObjectId: '$$stringId'}}}}}",
            "{$lookup:  {from: 'musicaMinisterio', localField: 'idsMusica', foreignField:  '_id', as: 'musicas'}}",
            "{$lookup: {from: 'dancaMinisterio', localField: 'idsDancas', foreignField:  '_id', as: 'dancasMinisterio'}}",
            "{$match: {'_id':  ?0}}",
    })
    RepertorioResponseWithFullInfos findByIdWithDanceAndMusicInfos(
            String idRepertorio);

    @Aggregation(pipeline = {
            "{$addFields: {idsDancas: {$map: {input: '$dancaMinisterioIds',as: 'stringId',in: {$toObjectId: '$$stringId'}}} ,idsMusica:{$map: {input: '$musicasIds',as: 'stringId',in: {$toObjectId: '$$stringId'}}}}}",
            "{$lookup:  {from: 'musicaMinisterio', localField: 'idsMusica', foreignField:  '_id', as: 'musicas'}}",
            "{$lookup: {from: 'dancaMinisterio', localField: 'idsDancas', foreignField:  '_id', as: 'dancasMinisterio'}}",
            "{$match: {'ministerioId':  ?0}}",
    })
    List<RepertorioResponseWithFullInfos> findAllByMinisterioIdWithDanceAndMusicInfos(
            String idMinisterio);

    List<Repertorio> findByDancaMinisterioIdsContains(
            String dancaMinisterioId);

    List<Repertorio> findByMusicasIdsContains(String musicaId);

}
