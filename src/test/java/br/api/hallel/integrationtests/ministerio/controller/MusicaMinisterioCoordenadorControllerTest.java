package br.api.hallel.integrationtests.ministerio.controller;

import br.api.hallel.integrationtests.config.TestConfig;
import br.api.hallel.integrationtests.ministerio.MinisterioIntegrationTest;
import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioResponseV2;
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

import java.io.IOException;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MusicaMinisterioCoordenadorControllerTest extends
        MinisterioIntegrationTest implements WithAssertions {

    static String musica_ministerio_id_1 = "";

    @Order(1)
    @Test
    void createMusicaMinisterio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/create")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        MusicaMinisterioDTO musicaMinisterioDTO = new MusicaMinisterioDTO();
        musicaMinisterioDTO.setMinisterioId(dummyMinisterioIds.get(0));
        musicaMinisterioDTO.setCompasso("4/4");
        musicaMinisterioDTO.setDuracao(263);
        musicaMinisterioDTO.setEscala("Menor");
        musicaMinisterioDTO.setTitulo("Louvou ao senhor");
        musicaMinisterioDTO.setTom("C#m");
        musicaMinisterioDTO.setChaveHarmonica("C#m Menor");

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(musicaMinisterioDTO)
                                 .when().post()
                                 .then()
                                 .statusCode(201)
                                 .extract().body().asString();

        var response = mapper.readValue(content, MusicaMinisterioResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getCompasso()).isEqualTo("4/4");
        assertThat(response.getDuracao()).isEqualTo(263);
        assertThat(response.getEscala()).isEqualTo("Menor");
        assertThat(response.getTitulo()).isEqualTo("Louvou ao senhor");
        assertThat(response.getTom()).isEqualTo("C#m");
        assertThat(response.getChaveHarmonica()).isEqualTo("C#m Menor");
        musica_ministerio_id_1 = response.getId();
    }

    @Order(2)
    @Test
    void listAllMusicasMinisterioByMinisterioId() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/list/" + dummyMinisterioIds.get(0))
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
    void listMusicasMinisterioById() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/list/id/" + musica_ministerio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, MusicaMinisterioResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getCompasso()).isEqualTo("4/4");
        assertThat(response.getDuracao()).isEqualTo(263);
        assertThat(response.getEscala()).isEqualTo("Menor");
        assertThat(response.getTitulo()).isEqualTo("Louvou ao senhor");
        assertThat(response.getTom()).isEqualTo("C#m");
        assertThat(response.getChaveHarmonica()).isEqualTo("C#m Menor");

    }

    @Order(4)
    @Test
    void editMusicaMinisterio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/" + musica_ministerio_id_1 + "/edit")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        MusicaMinisterioDTOEdit musicaMinisterioDTOEdit = new MusicaMinisterioDTOEdit();
        musicaMinisterioDTOEdit.setTitulo("Meu amor por Deus");
        musicaMinisterioDTOEdit.setDuracao(354);
        musicaMinisterioDTOEdit.setTom("Bb");
        musicaMinisterioDTOEdit.setEscala("Maior");
        musicaMinisterioDTOEdit.setCompasso("4/4");
        musicaMinisterioDTOEdit.setChaveHarmonica("Bb Maior");

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(musicaMinisterioDTOEdit)
                                 .put()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, MusicaMinisterioResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(musica_ministerio_id_1);
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getCompasso()).isEqualTo("4/4");
        assertThat(response.getDuracao()).isEqualTo(354);
        assertThat(response.getTom()).isEqualTo("Bb");
        assertThat(response.getEscala()).isEqualTo("Maior");
        assertThat(response.getChaveHarmonica()).isEqualTo("Bb Maior");
        assertThat(response.getTitulo()).isEqualTo("Meu amor por Deus");
    }

    @Order(5)
    @Test
    void listMusicaMinnisterioAsMembroMinisterio() throws
            IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/musica/" + dummyMinisterioIds.get(0))
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

    @Order(6)
    @Test
    void deleteMusicaMinisterio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/" + musica_ministerio_id_1 + "/delete")
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
                .setBasePath("/api/membros/ministerio/coordenador/musica/list/" + dummyMinisterioIds.get(0))
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

    @Test
    @Order(7)
    void createMusicaMinisterioWithLetra() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/v2/create")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        MusicaMinisterioDTOV2 musicaMinisterioDTOV2 = new MusicaMinisterioDTOV2();
        musicaMinisterioDTOV2.setMinisterioId(dummyMinisterioIds.get(0));
        musicaMinisterioDTOV2.setCompasso("4/4");
        musicaMinisterioDTOV2.setDuracao(263);
        musicaMinisterioDTOV2.setEscala("Menor");
        musicaMinisterioDTOV2.setTitulo("Louvou ao senhor");
        musicaMinisterioDTOV2.setTom("C#m");
        musicaMinisterioDTOV2.setChaveHarmonica("C#m Menor");
        musicaMinisterioDTOV2.setLetra("Vento sopra e leva a dor,\n" +
                "O sol renasce com calor.\n" +
                "Caminho firme, sem temor,\n" +
                "Espalhando luz e amor.");

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(musicaMinisterioDTOV2)
                                 .when().post()
                                 .then()
                                 .statusCode(201)
                                 .extract().body().asString();

        var response = mapper.readValue(content, MusicaMinisterioResponseV2.class);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getCompasso()).isEqualTo("4/4");
        assertThat(response.getDuracao()).isEqualTo(263);
        assertThat(response.getEscala()).isEqualTo("Menor");
        assertThat(response.getTitulo()).isEqualTo("Louvou ao senhor");
        assertThat(response.getTom()).isEqualTo("C#m");
        assertThat(response.getChaveHarmonica()).isEqualTo("C#m Menor");
        assertThat(response.getLetra()).isEqualTo("Vento sopra e leva a dor,\n" +
                "O sol renasce com calor.\n" +
                "Caminho firme, sem temor,\n" +
                "Espalhando luz e amor.");
        musica_ministerio_id_1 = response.getId();

    }

    @Test
    @Order(8)
    void editMusicaMinisterioWithLetra() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/musica/v2/" + musica_ministerio_id_1 + "/edit")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        MusicaMinisterioDTOV2 musicaMinisterioDTOEdit = new MusicaMinisterioDTOV2();
        musicaMinisterioDTOEdit.setTitulo("Meu amor por Deus");
        musicaMinisterioDTOEdit.setDuracao(354);
        musicaMinisterioDTOEdit.setTom("Bb");
        musicaMinisterioDTOEdit.setEscala("Maior");
        musicaMinisterioDTOEdit.setCompasso("4/4");
        musicaMinisterioDTOEdit.setChaveHarmonica("Bb Maior");
        musicaMinisterioDTOEdit.setLetra("No céu azul vou me perder,\n" +
                "Sonhos prontos pra viver.\n" +
                "Cada passo, um novo olhar,\n" +
                "O mundo espera, é só amar.");

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(musicaMinisterioDTOEdit)
                                 .put()
                                 .then().statusCode(200).extract()
                                 .body().asString();

        var response = mapper.readValue(content, MusicaMinisterioResponseV2.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(musica_ministerio_id_1);
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getCompasso()).isEqualTo("4/4");
        assertThat(response.getDuracao()).isEqualTo(354);
        assertThat(response.getTom()).isEqualTo("Bb");
        assertThat(response.getEscala()).isEqualTo("Maior");
        assertThat(response.getChaveHarmonica()).isEqualTo("Bb Maior");
        assertThat(response.getTitulo()).isEqualTo("Meu amor por Deus");
        assertThat(response.getLetra()).isEqualTo("No céu azul vou me perder,\n" +
                "Sonhos prontos pra viver.\n" +
                "Cada passo, um novo olhar,\n" +
                "O mundo espera, é só amar.");
    }
}
