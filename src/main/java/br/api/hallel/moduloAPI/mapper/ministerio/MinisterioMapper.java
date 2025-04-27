package br.api.hallel.moduloAPI.mapper.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioResponseV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioResponseV2;
import br.api.hallel.moduloAPI.mapper.MapperUtil;
import br.api.hallel.moduloAPI.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;

import java.util.ArrayList;
import java.util.List;

public class MinisterioMapper {

    private static final ModelMapper MAPPER = MapperUtil.modelMapper;

    public static Ministerio toModelMinisterio(
            MinisterioDTO ministerioDTO) {
        MAPPER.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT);
        return MAPPER.map(ministerioDTO, Ministerio.class);
    }

    public static Ministerio toModelMinisterioV2(
            MinisterioDTOV2 ministerioDTOV2
                                                ) {
        MAPPER.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT);
        return MAPPER.map(ministerioDTOV2, Ministerio.class);
    }

    public static MinisterioDTO toMinisterioDTO(
            Ministerio ministerio) {
        return MAPPER.map(ministerio, MinisterioDTO.class);
    }

    public static MinisterioResponse toMinisterioResponse(
            Ministerio ministerio) {
        return MAPPER.map(ministerio, MinisterioResponse.class);
    }

    public static MinisterioResponseV2 toMinisterioResponseV2(
            Ministerio ministerio) {
        return MAPPER.map(ministerio, MinisterioResponseV2.class);
    }

    public static List<MinisterioResponse> toMinisterioResponseList(
            List<Ministerio> all) {

        List<MinisterioResponse> responses = new ArrayList<>();
        all.forEach(m -> responses.add(toMinisterioResponse(m)));
        return responses;
    }

    public static FuncaoMinisterio toFuncaoMinisterio(
            FuncaoMinisterioDTO funcaoMinisterioDTO) {
        MAPPER.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT);
        return MAPPER.map(funcaoMinisterioDTO, FuncaoMinisterio.class);
    }

    public static MembroMinisterio toMembroMinisterio(
            AddMembroMinisterioDTO addMembroMinisterioDTO
                                                     ) {
        MAPPER.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT);

        return MAPPER.map(addMembroMinisterioDTO, MembroMinisterio.class);
    }

    public static NaoConfirmadoEscalaMinisterio toNaoConfirmadoEscalaMinisterio(
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO
                                                                               ) {
        return MAPPER.map(naoConfirmarEscalaDTO, NaoConfirmadoEscalaMinisterio.class);
    }

    public static EscalaMinisterio toEscalaMinisterio(
            EscalaMinisterioDTO escalaMinisterioDTO
                                                     ) {
        return MAPPER.map(escalaMinisterioDTO, EscalaMinisterio.class);
    }

    public static EscalaMinisterioResponse toEscalaMinisterioResponse(
            EscalaMinisterio escalaMinisterio
                                                                     ) {
        return MAPPER.map(escalaMinisterio, EscalaMinisterioResponse.class);
    }

    public static MinisterioPublicResponse toMinisterioPublicResponse(
            Ministerio ministerio) {
        return MAPPER.map(ministerio, MinisterioPublicResponse.class);
    }

    public static List<MinisterioPublicResponse> toMinisterioPublicResponseList(
            List<Ministerio> all) {
        List<MinisterioPublicResponse> response = new ArrayList<>();
        all.forEach(m -> response.add(toMinisterioPublicResponse(m)));
        return response;
    }

    public static Repertorio toModelRepertorio(
            RepertorioDTO repertorioDTO) {
        return MAPPER.map(repertorioDTO, Repertorio.class);
    }

    public static RepertorioResponse toResponseRepertorio(
            Repertorio repertorio) {
        return MAPPER.map(repertorio, RepertorioResponse.class);
    }

    public static List<RepertorioResponse> toListResponseRepertorio(
            List<Repertorio> all) {
        List<RepertorioResponse> response = new ArrayList<>();
        all.forEach(m -> response.add(toResponseRepertorio(m)));
        return response;
    }

    public static MusicaMinisterio toModelMusicaMinisterio(
            MusicaMinisterioDTO musicaMinisterioDTO) {
        return MAPPER.map(musicaMinisterioDTO, MusicaMinisterio.class);
    }

    public static MusicaMinisterioResponse toResponseMusicaMinisterioResponse(
            MusicaMinisterio musicaMinisterio) {
        return MAPPER.map(musicaMinisterio, MusicaMinisterioResponse.class);
    }

    public static MusicaMinisterioResponseV2 toResponseMusicaMinisterioV2(
            MusicaMinisterio musicaMinisterio) {
        return MAPPER.map(musicaMinisterio, MusicaMinisterioResponseV2.class);
    }

    public static List<MusicaMinisterioResponse> toListResponseMusicaMinisterio(
            List<MusicaMinisterio> all) {
        List<MusicaMinisterioResponse> response = new ArrayList<>();
        all.forEach(m -> response.add(toResponseMusicaMinisterioResponse(m)));
        return response;
    }

    public static List<MusicaMinisterioResponseV2> toListResponseV2MusicaMinisterio(
            List<MusicaMinisterio> all) {
        List<MusicaMinisterioResponseV2> response = new ArrayList<>();
        all.forEach(m -> response.add(toResponseMusicaMinisterioV2(m)));
        return response;
    }

    public static DancaMinisterio toModelDancaMinisterio(
            DancaMinisterioDTO dancaMinisterioDTO) {
        return MAPPER.map(dancaMinisterioDTO, DancaMinisterio.class);
    }

    public static DancaMinisterioResponse toResponseDancaMinisterioResponse(
            DancaMinisterio dancaMinisterio
                                                                           ) {
        return MAPPER.map(dancaMinisterio, DancaMinisterioResponse.class);
    }

    public static List<DancaMinisterioResponse> toListResponseDancaMinisterio(
            List<DancaMinisterio> all) {
        List<DancaMinisterioResponse> response = new ArrayList<>();
        all.forEach(m -> response.add(toResponseDancaMinisterioResponse(m)));
        return response;
    }

    public static RepertorioResponseWithInfos toRepertorioWithInfos(
            Repertorio model) {
        return MAPPER.map(model, RepertorioResponseWithInfos.class);
    }

    public static EnsaioMinisterioResponse toEnsaioMinisterioResponse(
            EnsaioMinisterio ensaio) {
        return MAPPER.map(ensaio, EnsaioMinisterioResponse.class);
    }

    public static List<EnsaioMinisterioResponse> toListEnsaioMinisterioResponse(
            List<EnsaioMinisterio> all) {
        List<EnsaioMinisterioResponse> response = new ArrayList<>();
        all.forEach(ensaio -> response.add(toEnsaioMinisterioResponse(ensaio)));
        return response;
    }

    public static ConvidadoEscalaMinisterioWithConviteResponse toConvidadoWithConviteResponse(ConvidadoEscalaMinisterio convidadoEscalaMinisterio){
        return MAPPER.map(convidadoEscalaMinisterio, ConvidadoEscalaMinisterioWithConviteResponse.class);
    }

    public static ConviteEscalaResponse toConviteEscalaResponse(ConviteEscalaMinisterio conviteEscala){
        return MAPPER.map(conviteEscala, ConviteEscalaResponse.class);
    }

    public static ConvidadoEscalaMinisterioWithInfos toConvidadoEscalaMinisterioWithInfos(ConvidadoEscalaMinisterio convidadoEscalaMinisterio){
        return MAPPER.map(convidadoEscalaMinisterio, ConvidadoEscalaMinisterioWithInfos.class);
    }


}
