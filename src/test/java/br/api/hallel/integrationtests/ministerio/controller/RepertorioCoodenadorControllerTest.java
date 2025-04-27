package br.api.hallel.integrationtests.ministerio.controller;

import br.api.hallel.integrationtests.config.TestConfig;
import br.api.hallel.integrationtests.ministerio.MinisterioIntegrationTest;
import br.api.hallel.integrationtests.ministerio.dto.MinisterioDTO;
import br.api.hallel.integrationtests.ministerio.dto.MinisterioResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.model.DancaMinisterio;
import br.api.hallel.moduloAPI.model.MusicaMinisterio;
import br.api.hallel.moduloAPI.service.ministerio.DancaMinisterioService;
import br.api.hallel.moduloAPI.service.ministerio.MusicaMinisterioService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepertorioCoodenadorControllerTest extends
        MinisterioIntegrationTest implements WithAssertions {

    @Autowired
    private MusicaMinisterioService musicaMinisterioService;
    @Autowired
    private DancaMinisterioService dancaMinisterioService;

    static String repertorio_id_1 = "";
    static List<MusicaMinisterioResponse> musicasMinisterio = new ArrayList<>();
    static List<DancaMinisterioResponse> dancasMinisterio = new ArrayList<>();

    @Order(1)
    @Test
    void createRepertorio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/create")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioDTO repertorioDTO = new RepertorioDTO();
        repertorioDTO.setMinisterioId(dummyMinisterioIds.get(0));
        repertorioDTO.setNome("Repertorio 27");
        repertorioDTO.setDescricao("Repertorio 27 do ministerio da dança");

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(repertorioDTO)
                                 .when().post()
                                 .then()
                                 .statusCode(201)
                                 .extract().body().asString();

        var response = mapper.readValue(content, RepertorioResponse.class);

        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getNome()).isNotNull();
        assertThat(response.getNome()).isEqualTo("Repertorio 27");
        assertThat(response.getDescricao()).isNotEmpty();
        assertThat(response.getDescricao()).isEqualTo(repertorioDTO.getDescricao());
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        repertorio_id_1 = response.getId();
    }

    @Order(2)
    @Test
    void listAllRepertorioByMinisterioId() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/" + dummyMinisterioIds.get(0))
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then().statusCode(200).extract()
                                 .body().asString();
        var response = mapper.readValue(content, List.class);
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        assertThat(response).size().isEqualTo(1);
    }

    @Order(3)
    @Test
    void listRepertorioById() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/id/" + repertorio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, RepertorioResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(repertorio_id_1);
        assertThat(response.getNome()).isNotEmpty();
        assertThat(response.getNome()).isEqualTo("Repertorio 27");
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getDescricao()).isNotEmpty();

    }

    @Order(4)
    @Test
    void editRepertorio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/" + repertorio_id_1 + "/edit")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioDTOEdit repertorioDTOEdit = new RepertorioDTOEdit();
        repertorioDTOEdit.setNome("Repertorio 29");
        repertorioDTOEdit.setDescricao("Repertorio 29 e a sua descrição");

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(repertorioDTOEdit)
                                 .put()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, RepertorioResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(repertorio_id_1);
        assertThat(response.getNome()).isNotEmpty();
        assertThat(response.getNome()).isEqualTo("Repertorio 29");
        assertThat(response.getDescricao()).isNotEmpty();
        assertThat(response.getDescricao()).isEqualTo(repertorioDTOEdit.getDescricao());
    }

    @Order(5)
    @Test
    void deleteRepertorio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/" + repertorio_id_1 + "/delete")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .delete()
                                 .then().statusCode(204).extract()
                                 .body().asString();

        RequestSpecification specification2 = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/" + dummyMinisterioIds.get(0))
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content2 = RestAssured.given()
                                  .spec(specification2)
                                  .get()
                                  .then().statusCode(200).extract()
                                  .body().asString();

        var response = mapper.readValue(content2, List.class);
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

    MusicaMinisterioDTO mockMusic(int i) {
        MusicaMinisterioDTO musicaMinisterioDTO = new MusicaMinisterioDTO();
        musicaMinisterioDTO.setMinisterioId(dummyMinisterioIds.get(0));
        musicaMinisterioDTO.setTom("Tom " + i);
        musicaMinisterioDTO.setChaveHarmonica("Harmonica " + i);
        musicaMinisterioDTO.setTitulo("Titulo " + i);
        musicaMinisterioDTO.setEscala("Escala " + i);
        musicaMinisterioDTO.setCompasso("Compasso " + i);
        musicaMinisterioDTO.setDuracao(i + 250);
        return musicaMinisterioDTO;
    }

    DancaMinisterioDTO mockDance(int i) {
        DancaMinisterioDTO dancaMinisterioDTO = new DancaMinisterioDTO();
        dancaMinisterioDTO.setMinisterioId(dummyMinisterioIds.get(0));
        dancaMinisterioDTO.setLinkVideo("Link " + i);
        dancaMinisterioDTO.setDescricao("Descricao " + i);
        dancaMinisterioDTO.setNome("Nome " + i);
        return dancaMinisterioDTO;
    }

    @Order(6)
    @Test
    void createRepertorioForMusicAndDance() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/create")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioDTO repertorioDTO = new RepertorioDTO();
        repertorioDTO.setMinisterioId(dummyMinisterioIds.get(0));
        repertorioDTO.setNome("Repertorio 39");
        repertorioDTO.setDescricao("Repertorio 39 do ministerio da dança");

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(repertorioDTO)
                                 .when().post()
                                 .then()
                                 .statusCode(201)
                                 .extract().body().asString();

        var response = mapper.readValue(content, RepertorioResponse.class);

        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getNome()).isNotNull();
        assertThat(response.getNome()).isEqualTo("Repertorio 39");
        assertThat(response.getDescricao()).isNotEmpty();
        assertThat(response.getDescricao()).isEqualTo(repertorioDTO.getDescricao());
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        repertorio_id_1 = response.getId();
    }

    @Order(7)
    @Test
    void createDancesAndMusic() {
        for (int i = 0; i < 3; i++) {
            MusicaMinisterioDTO dto = mockMusic(i);
            MusicaMinisterioResponse musicaMinisterioResponse = musicaMinisterioService.createMusicaMinisterio(dto);
            musicasMinisterio.add(musicaMinisterioResponse);
        }
        for (int i = 0; i < 3; i++) {
            DancaMinisterioDTO dto = mockDance(i);
            DancaMinisterioResponse dancaMinisterioResponse = dancaMinisterioService.createDancaMinisterio(dto);
            dancasMinisterio.add(dancaMinisterioResponse);
        }
    }

    @Test
    @Order(8)
    void addMusicasIntoRepertorio() throws IOException {

        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/music/" + repertorio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioMusicDTO dto = new RepertorioMusicDTO();
        dto.setMusicIdsAdd(List.of(musicasMinisterio.get(0)
                                                    .getId(), musicasMinisterio.get(1)
                                                                               .getId()));

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(dto)
                                 .patch()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, RepertorioResponseWithInfos.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(repertorio_id_1);
        assertThat(response.getMusicasIds()).isNotNull();
        assertThat(response.getMusicasIds()).isNotEmpty();
        assertThat(response.getMusicasIds()).size().isEqualTo(2);
    }

    @Test
    @Order(9)
    void removeMusicasOfRepertorio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/music/" + repertorio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioMusicDTO dto = new RepertorioMusicDTO();
        dto.setMusicIdsRemove(List.of(musicasMinisterio.get(0)
                                                       .getId(), musicasMinisterio.get(1)
                                                                                  .getId()));
        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(dto)
                                 .patch()
                                 .then().statusCode(200).extract()
                                 .body().asString();
        var response = mapper.readValue(content, RepertorioResponseWithInfos.class);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(repertorio_id_1);
        assertThat(response.getMusicasIds()).isNotNull();
        assertThat(response.getMusicasIds()).isEmpty();

    }

    @Test
    @Order(10)
    void addAndRemoveMusicasOfRepertorio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/music/" + repertorio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioMusicDTO dto1 = new RepertorioMusicDTO();
        dto1.setMusicIdsAdd(List.of(musicasMinisterio.get(0)
                                                     .getId()));
        var content1 = RestAssured.given()
                                  .spec(specification)
                                  .body(dto1)
                                  .patch()
                                  .then().statusCode(200).extract()
                                  .body().asString();
        var response1 = mapper.readValue(content1, RepertorioResponseWithInfos.class);
        assertThat(response1).isNotNull();
        assertThat(response1.getId()).isEqualTo(repertorio_id_1);
        assertThat(response1.getMusicasIds()).isNotNull();
        assertThat(response1.getMusicasIds()).isNotEmpty();
        assertThat(response1.getMusicasIds()).size().isEqualTo(1);
        assertThat(response1.getMusicasIds()).contains(musicasMinisterio.get(0)
                                                                        .getId());

        RepertorioMusicDTO dto2 = new RepertorioMusicDTO();
        dto2.setMusicIdsRemove(List.of(musicasMinisterio.get(0)
                                                        .getId()));
        dto2.setMusicIdsAdd(List.of(musicasMinisterio.get(1)
                                                     .getId(), musicasMinisterio.get(2)
                                                                                .getId()));
        var content2 = RestAssured.given()
                                  .spec(specification)
                                  .body(dto2)
                                  .patch()
                                  .then().statusCode(200).extract()
                                  .body().asString();
        var response2 = mapper.readValue(content2, RepertorioResponseWithInfos.class);
        assertThat(response2).isNotNull();
        assertThat(response2.getId()).isEqualTo(repertorio_id_1);
        assertThat(response2.getMusicasIds()).isNotNull();
        assertThat(response2.getMusicasIds()).isNotEmpty();
        assertThat(response2.getMusicasIds()).size().isEqualTo(2);
        assertThat(response2.getMusicasIds()).doesNotContain(musicasMinisterio.get(0)
                                                                              .getId());
        assertThat(response2.getMusicasIds()).contains(musicasMinisterio.get(1)
                                                                        .getId(), musicasMinisterio.get(2)
                                                                                                   .getId());

    }

    @Test
    @Order(11)
    void addDanceIntoRepertorio() throws IOException {

        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/dance/" + repertorio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioDancaDTO dto = new RepertorioDancaDTO();
        dto.setDanceIdsAdd(List.of(dancasMinisterio.get(0)
                                                   .getId(), dancasMinisterio.get(1)
                                                                             .getId()));

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(dto)
                                 .patch()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, RepertorioResponseWithInfos.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(repertorio_id_1);
        assertThat(response.getDancaMinisterioIds()).isNotNull();
        assertThat(response.getDancaMinisterioIds()).isNotEmpty();
        assertThat(response.getDancaMinisterioIds()).size()
                                                    .isEqualTo(2);
    }

    @Test
    @Order(12)
    void removeDanceOfRepertorio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/dance/" + repertorio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioDancaDTO dto = new RepertorioDancaDTO();
        dto.setDanceIdsRemove(List.of(dancasMinisterio.get(0)
                                                      .getId(), dancasMinisterio.get(1)
                                                                                .getId()));
        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(dto)
                                 .patch()
                                 .then().statusCode(200).extract()
                                 .body().asString();
        var response = mapper.readValue(content, RepertorioResponseWithInfos.class);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(repertorio_id_1);
        assertThat(response.getDancaMinisterioIds()).isNotNull();
        assertThat(response.getDancaMinisterioIds()).isEmpty();

    }

    @Test
    @Order(13)
    void addAndRemoveDancesOfRepertorio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/dance/" + repertorio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RepertorioDancaDTO dto1 = new RepertorioDancaDTO();
        dto1.setDanceIdsAdd(List.of(dancasMinisterio.get(0)
                                                    .getId()));
        var content1 = RestAssured.given()
                                  .spec(specification)
                                  .body(dto1)
                                  .patch()
                                  .then().statusCode(200).extract()
                                  .body().asString();
        var response1 = mapper.readValue(content1, RepertorioResponseWithInfos.class);
        assertThat(response1).isNotNull();
        assertThat(response1.getId()).isEqualTo(repertorio_id_1);
        assertThat(response1.getDancaMinisterioIds()).isNotNull();
        assertThat(response1.getDancaMinisterioIds()).isNotEmpty();
        assertThat(response1.getDancaMinisterioIds()).size()
                                                     .isEqualTo(1);
        assertThat(response1.getDancaMinisterioIds()).contains(dancasMinisterio.get(0)
                                                                               .getId());

        RepertorioDancaDTO dto2 = new RepertorioDancaDTO();
        dto2.setDanceIdsRemove(List.of(dancasMinisterio.get(0)
                                                       .getId()));
        dto2.setDanceIdsAdd(List.of(dancasMinisterio.get(1)
                                                    .getId(), dancasMinisterio.get(2)
                                                                              .getId()));
        var content2 = RestAssured.given()
                                  .spec(specification)
                                  .body(dto2)
                                  .patch()
                                  .then().statusCode(200).extract()
                                  .body().asString();
        var response2 = mapper.readValue(content2, RepertorioResponseWithInfos.class);
        assertThat(response2).isNotNull();
        assertThat(response2.getId()).isEqualTo(repertorio_id_1);
        assertThat(response2.getDancaMinisterioIds()).isNotNull();
        assertThat(response2.getDancaMinisterioIds()).isNotEmpty();
        assertThat(response2.getDancaMinisterioIds()).size()
                                                     .isEqualTo(2);
        assertThat(response2.getDancaMinisterioIds()).doesNotContain(dancasMinisterio.get(0)
                                                                                     .getId());
        assertThat(response2.getDancaMinisterioIds()).contains(dancasMinisterio.get(1)
                                                                               .getId(), dancasMinisterio.get(2)
                                                                                                         .getId());

    }

    @Test
    @Order(14)
    void listRepertorioByIdWithInfos() throws IOException {

        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/id/" + repertorio_id_1 + "/infos")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, RepertorioResponseWithFullInfos.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(repertorio_id_1);
        int indexDanca = 1;
        for (DancaMinisterio danca : response.getDancasMinisterio()) {
            assertThat(danca.getId()).isEqualTo(dancasMinisterio.get(indexDanca)
                                                                .getId());
            assertThat(danca.getNome()).isEqualTo(dancasMinisterio.get(indexDanca)
                                                                  .getNome());
            assertThat(danca.getDescricao()).isEqualTo(dancasMinisterio.get(indexDanca)
                                                                       .getDescricao());
            assertThat(danca.getLinkVideo()).isEqualTo(dancasMinisterio.get(indexDanca)
                                                                       .getLinkVideo());
            indexDanca++;
        }
        int indexMusica = 1;
        for (MusicaMinisterio musica : response.getMusicas()) {
            assertThat(musica.getId()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                  .getId());
            assertThat(musica.getChaveHarmonica()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                              .getChaveHarmonica());
            assertThat(musica.getCompasso()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                        .getCompasso());
            assertThat(musica.getDuracao()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                       .getDuracao());
            assertThat(musica.getMinisterioId()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                            .getMinisterioId());
            assertThat(musica.getTitulo()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                      .getTitulo());
            assertThat(musica.getTom()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                   .getTom());
            assertThat(musica.getEscala()).isEqualTo(musicasMinisterio.get(indexMusica)
                                                                      .getEscala());
            indexMusica++;
        }

    }

    @Order(15)
    @Test
    void listRepertoriosAsMembroMinisterio() throws
            IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/repertorio/" + dummyMinisterioIds.get(0))
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, List.class);

        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
    }

    @Order(16)
    @Test
    void deleteMusicaWithMusicaLinkedInRepertorio() throws IOException{

        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/id/"+repertorio_id_1+"/infos")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content1 = RestAssured.given()
                                  .spec(specification)
                                  .get()
                                  .then().statusCode(200).extract()
                                  .body().asString();

        RepertorioResponseWithFullInfos repertorio = mapper.readValue(content1, RepertorioResponseWithFullInfos.class);

        assertThat(repertorio).isNotNull();
        assertThat(repertorio.getId()).isEqualTo(repertorio_id_1);
        List<String> musicasIds = repertorio.getMusicas().stream().map(MusicaMinisterio::getId).toList();
        assertThat(musicasIds).contains(musicasMinisterio.get(1).getId());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/" + musicasMinisterio.get(1).getId() + "/delete")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .delete()
                                 .then().statusCode(204).extract()
                                 .body().asString();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/id/"+repertorio_id_1+"/infos")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content2 = RestAssured.given()
                                  .spec(specification)
                                  .get()
                                  .then().statusCode(200).extract()
                                  .body().asString();

        RepertorioResponseWithFullInfos repertorio2 = mapper.readValue(content2, RepertorioResponseWithFullInfos.class);
        assertThat(repertorio2).isNotNull();
        assertThat(repertorio2.getId()).isEqualTo(repertorio_id_1);
        List<String> musicasIds2 = repertorio2.getMusicas().stream().map(MusicaMinisterio::getId).toList();
        assertThat(musicasIds2).doesNotContain(musicasMinisterio.get(1).getId());
    }

    @Order(17)
    @Test
    void deleteDancaWithDancaLinkedInRepertorio() throws IOException{

        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/id/"+repertorio_id_1+"/infos")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content1 = RestAssured.given()
                                  .spec(specification)
                                  .get()
                                  .then().statusCode(200).extract()
                                  .body().asString();

        RepertorioResponseWithFullInfos repertorio = mapper.readValue(content1, RepertorioResponseWithFullInfos.class);

        assertThat(repertorio).isNotNull();
        assertThat(repertorio.getId()).isEqualTo(repertorio_id_1);
        List<String> dancasIds = repertorio.getDancasMinisterio().stream().map(DancaMinisterio::getId).toList();
        assertThat(dancasIds).contains(dancasMinisterio.get(1).getId());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/danca/"+ dancasMinisterio.get(1).getId() +"/delete")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .delete()
                                 .then().statusCode(204).extract().body().asString();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/list/id/"+repertorio_id_1+"/infos")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content2 = RestAssured.given()
                                  .spec(specification)
                                  .get()
                                  .then().statusCode(200).extract()
                                  .body().asString();

        RepertorioResponseWithFullInfos repertorio2 = mapper.readValue(content2, RepertorioResponseWithFullInfos.class);
        assertThat(repertorio2).isNotNull();
        assertThat(repertorio2.getId()).isEqualTo(repertorio_id_1);
        List<String> dancasIds2 = repertorio2.getDancasMinisterio().stream().map(DancaMinisterio::getId).toList();
        assertThat(dancasIds2).doesNotContain(dancasMinisterio.get(1).getId());
    }
}
