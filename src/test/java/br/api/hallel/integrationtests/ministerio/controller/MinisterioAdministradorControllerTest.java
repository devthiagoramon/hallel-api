package br.api.hallel.integrationtests.ministerio.controller;

import br.api.hallel.integrationtests.config.TestConfig;
import br.api.hallel.integrationtests.ministerio.MinisterioIntegrationTest;
import br.api.hallel.integrationtests.ministerio.dto.MinisterioDTO;
import br.api.hallel.integrationtests.ministerio.dto.MinisterioResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EditCoordMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioResponseV2;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MinisterioAdministradorControllerTest extends
        MinisterioIntegrationTest implements WithAssertions {

    private static String id_ministerio1 = "";
    private static String id_ministerio_v2_1 = "";

    @Order(1)
    @Test
    void createMinisterio() throws IOException {
        MinisterioDTO ministerioDTO1 = new MinisterioDTO();
        ministerioDTO1.setNome("Dança");
        ministerioDTO1.setCoordenadorId(membrosTest.get(0).getId());
        ministerioDTO1.setViceCoordenadorId(membrosTest.get(1)
                                                       .getId());
        ministerioDTO1.setImagem("...");
        ministerioDTO1.setDescricao("Ministerio de dança da comunidade!");
        ministerioDTO1.setObjetivos(List.of("Animar a comunidade", "Divertir a comunidade"));

        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio")
                .setPort(TestConfig.SERVER_PORT)
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(ministerioDTO1)
                                 .when().post()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();
        MinisterioResponse ministerio = mapper.readValue(content, MinisterioResponse.class);

        assertThat(ministerio.getId()).isNotNull();
        assertThat(ministerio.getNome()).isNotEmpty();
        assertThat(ministerio.getNome()).isEqualTo("Dança");
        assertThat(ministerio.getCoordenadorId()).isNotEmpty();
        assertThat(ministerio.getCoordenadorId()).isEqualTo(membrosTest.get(0)
                                                                       .getId());
        assertThat(ministerio.getViceCoordenadorId()).isNotEmpty();
        assertThat(ministerio.getViceCoordenadorId()).isEqualTo(membrosTest.get(1)
                                                                           .getId());
        assertThat(ministerio.getImagem()).isNotEmpty();
        assertThat(ministerio.getDescricao()).isNotEmpty();
        assertThat(ministerio.getObjetivos()).isNotEmpty();
        assertThat(ministerio.getImagem()).isEqualTo("...");
        assertThat(ministerio.getDescricao()).isEqualTo("Ministerio de dança da comunidade!");
        assertThat(ministerio.getObjetivos()).hasSize(2);
        assertThat(ministerio.getObjetivos()).contains("Animar a comunidade", "Divertir a comunidade");
        id_ministerio1 = ministerio.getId();
    }

    @Order(2)
    @Test
    void listMinisterio() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio")
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .when().get()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();

        var ministerioCoords = mapper.readValue(content, List.class);

        assertThat(ministerioCoords).isNotNull();
        assertThat(ministerioCoords.get(0)).isNotNull();

    }


    @Order(3)
    @Test
    void listMinisterioById() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio/" + id_ministerio1)
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .when().get()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();

        var ministerioResponse = mapper.readValue(content, MinisterioResponse.class);

        assertThat(ministerioResponse.getId()).isNotNull();
        assertThat(ministerioResponse.getNome()).isNotEmpty();
        assertThat(ministerioResponse.getNome()).isEqualTo("Dança");
        assertThat(ministerioResponse.getId()).isEqualTo(id_ministerio1);
        assertThat(ministerioResponse.getCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponse.getCoordenadorId()).isEqualTo(membrosTest.get(0)
                                                                               .getId());
        assertThat(ministerioResponse.getViceCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponse.getViceCoordenadorId()).isEqualTo(membrosTest.get(1)
                                                                                   .getId());

    }


    @Order(4)
    @Test
    void editarMinisterio() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio/" + id_ministerio1 + "/edit")
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        MinisterioDTO ministerioDTOEdit = new MinisterioDTO();
        ministerioDTOEdit.setNome("Ministerio da Dança");
        ministerioDTOEdit.setCoordenadorId(membrosTest.get(0)
                                                      .getId());
        ministerioDTOEdit.setViceCoordenadorId(membrosTest.get(1)
                                                          .getId());

        ministerioDTOEdit.setImagem("...");
        ministerioDTOEdit.setDescricao("Ministerio de dança da comunidade!");
        ministerioDTOEdit.setObjetivos(List.of("Animar a comunidade", "Divertir a comunidade"));

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(ministerioDTOEdit)
                                 .put()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();

        var ministerioResponse = mapper.readValue(content, MinisterioResponse.class);

        assertThat(ministerioResponse.getId()).isNotNull();
        assertThat(ministerioResponse.getNome()).isNotEmpty();
        assertThat(ministerioResponse.getNome()).isEqualTo("Ministerio da Dança");
        assertThat(ministerioResponse.getCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponse.getCoordenadorId()).isEqualTo(membrosTest.get(0)
                                                                               .getId());
        assertThat(ministerioResponse.getViceCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponse.getViceCoordenadorId()).isEqualTo(membrosTest.get(1)
                                                                                   .getId());
        assertThat(ministerioResponse.getImagem()).isNotEmpty();
        assertThat(ministerioResponse.getDescricao()).isNotEmpty();
        assertThat(ministerioResponse.getObjetivos()).isNotEmpty();
        assertThat(ministerioResponse.getImagem()).isEqualTo("...");
        assertThat(ministerioResponse.getDescricao()).isEqualTo("Ministerio de dança da comunidade!");
        assertThat(ministerioResponse.getObjetivos()).hasSize(2);
        assertThat(ministerioResponse.getObjetivos()).contains("Animar a comunidade", "Divertir a comunidade");

    }

    @Order(5)
    @Test
    void alterarCoordenadores() throws IOException {

        EditCoordMinisterioDTO editCoordMinisterioDTO = new EditCoordMinisterioDTO();
        editCoordMinisterioDTO.setCoordenadorId(membrosTest.get(1)
                                                           .getId());
        editCoordMinisterioDTO.setViceCoordenadorId(membrosTest.get(2)
                                                               .getId());

        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio/" + id_ministerio1 + "/edit/coordenadores")
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(editCoordMinisterioDTO)
                                 .patch()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();

        var ministerioResponse = mapper.readValue(content, MinisterioResponse.class);
        assertThat(ministerioResponse.getId()).isNotNull();
        assertThat(ministerioResponse.getNome()).isNotEmpty();
        assertThat(ministerioResponse.getNome()).isEqualTo("Ministerio da Dança");
        assertThat(ministerioResponse.getCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponse.getCoordenadorId()).isEqualTo(membrosTest.get(1)
                                                                               .getId());
        assertThat(ministerioResponse.getViceCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponse.getViceCoordenadorId()).isEqualTo(membrosTest.get(2)
                                                                                   .getId());
    }

    @Order(6)
    @Test
    void deleteMinisterio() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio/" + id_ministerio1)
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        RestAssured.given().spec(specification)
                   .delete()
                   .then()
                   .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Order(7)
    @Test
    void adicionarMinisterioV2() throws IOException {
        MinisterioDTOV2 ministerioDTOV2 = new MinisterioDTOV2();
        ministerioDTOV2.setNome("Dança");
        ministerioDTOV2.setCoordenadorId(membrosTest.get(0).getId());
        ministerioDTOV2.setViceCoordenadorId(membrosTest.get(1)
                                                        .getId());
        ministerioDTOV2.setFileImageUrl("...");
        ministerioDTOV2.setDescricao("Ministerio de dança da comunidade!");
        ministerioDTOV2.setObjetivos(List.of("Animar a comunidade", "Divertir a comunidade"));

        ministerioDTOV2.setHasDance(true);
        ministerioDTOV2.setHasRepertorio(true);
        ministerioDTOV2.setHasMusic(false);

        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio/v2/create")
                .setPort(TestConfig.SERVER_PORT)
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(ministerioDTOV2)
                                 .when().post()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();
        MinisterioResponseV2 ministerio = mapper.readValue(content, MinisterioResponseV2.class);

        assertThat(ministerio.getId()).isNotNull();
        assertThat(ministerio.getNome()).isNotEmpty();
        assertThat(ministerio.getNome()).isEqualTo("Dança");
        assertThat(ministerio.getCoordenadorId()).isNotEmpty();
        assertThat(ministerio.getCoordenadorId()).isEqualTo(membrosTest.get(0)
                                                                       .getId());
        assertThat(ministerio.getViceCoordenadorId()).isNotEmpty();
        assertThat(ministerio.getViceCoordenadorId()).isEqualTo(membrosTest.get(1)
                                                                           .getId());
        assertThat(ministerio.getFileImageUrl()).isNotEmpty();
        assertThat(ministerio.getDescricao()).isNotEmpty();
        assertThat(ministerio.getObjetivos()).isNotEmpty();
        assertThat(ministerio.getFileImageUrl()).isEqualTo("...");
        assertThat(ministerio.getDescricao()).isEqualTo("Ministerio de dança da comunidade!");
        assertThat(ministerio.getObjetivos()).hasSize(2);
        assertThat(ministerio.getObjetivos()).contains("Animar a comunidade", "Divertir a comunidade");

        assertThat(ministerio.isHasDance()).isTrue();
        assertThat(ministerio.isHasRepertorio()).isTrue();
        assertThat(ministerio.isHasMusic()).isFalse();

        id_ministerio_v2_1 = ministerio.getId();
    }

    @Order(8)
    @Test
    void editarMinisterioV2() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio/" + id_ministerio_v2_1 + "/v2/edit")
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        MinisterioDTOV2 ministerioDTOV2Edit = new MinisterioDTOV2();
        ministerioDTOV2Edit.setNome("Ministerio da Dança");
        ministerioDTOV2Edit.setCoordenadorId(membrosTest.get(0)
                                                        .getId());
        ministerioDTOV2Edit.setViceCoordenadorId(membrosTest.get(1)
                                                            .getId());

        ministerioDTOV2Edit.setFileImageUrl("...");
        ministerioDTOV2Edit.setDescricao("Ministerio de dança da comunidade!");
        ministerioDTOV2Edit.setObjetivos(List.of("Animar a comunidade", "Divertir a comunidade"));

        ministerioDTOV2Edit.setHasMusic(true);
        ministerioDTOV2Edit.setHasRepertorio(false);
        ministerioDTOV2Edit.setHasDance(false);

        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(ministerioDTOV2Edit)
                                 .put()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();

        var ministerioResponseV2 = mapper.readValue(content, MinisterioResponseV2.class);

        assertThat(ministerioResponseV2.getId()).isNotNull();
        assertThat(ministerioResponseV2.getNome()).isNotEmpty();
        assertThat(ministerioResponseV2.getNome()).isEqualTo("Ministerio da Dança");
        assertThat(ministerioResponseV2.getCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponseV2.getCoordenadorId()).isEqualTo(membrosTest.get(0)
                                                                                 .getId());
        assertThat(ministerioResponseV2.getViceCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponseV2.getViceCoordenadorId()).isEqualTo(membrosTest.get(1)
                                                                                     .getId());
        assertThat(ministerioResponseV2.getFileImageUrl()).isNotEmpty();
        assertThat(ministerioResponseV2.getDescricao()).isNotEmpty();
        assertThat(ministerioResponseV2.getObjetivos()).isNotEmpty();
        assertThat(ministerioResponseV2.getFileImageUrl()).isEqualTo("...");
        assertThat(ministerioResponseV2.getDescricao()).isEqualTo("Ministerio de dança da comunidade!");
        assertThat(ministerioResponseV2.getObjetivos()).hasSize(2);
        assertThat(ministerioResponseV2.getObjetivos()).contains("Animar a comunidade", "Divertir a comunidade");

        assertThat(ministerioResponseV2.isHasDance()).isFalse();
        assertThat(ministerioResponseV2.isHasRepertorio()).isFalse();
        assertThat(ministerioResponseV2.isHasMusic()).isTrue();
    }

    @Order(9)
    @Test
    void listarMinisterioV2PeloId() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/administrador/ministerio/v2/" + id_ministerio_v2_1)
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, adm_login_token)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then()
                                 .statusCode(200)
                                 .extract().body().asString();

        var ministerioResponseV2 = mapper.readValue(content, MinisterioResponseV2.class);

        assertThat(ministerioResponseV2.getId()).isNotNull();
        assertThat(ministerioResponseV2.getNome()).isNotEmpty();
        assertThat(ministerioResponseV2.getNome()).isEqualTo("Ministerio da Dança");
        assertThat(ministerioResponseV2.getCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponseV2.getCoordenadorId()).isEqualTo(membrosTest.get(0)
                                                                                 .getId());
        assertThat(ministerioResponseV2.getViceCoordenadorId()).isNotEmpty();
        assertThat(ministerioResponseV2.getViceCoordenadorId()).isEqualTo(membrosTest.get(1)
                                                                                     .getId());
        assertThat(ministerioResponseV2.getFileImageUrl()).isNotEmpty();
        assertThat(ministerioResponseV2.getDescricao()).isNotEmpty();
        assertThat(ministerioResponseV2.getObjetivos()).isNotEmpty();
        assertThat(ministerioResponseV2.getFileImageUrl()).isEqualTo("...");
        assertThat(ministerioResponseV2.getDescricao()).isEqualTo("Ministerio de dança da comunidade!");
        assertThat(ministerioResponseV2.getObjetivos()).hasSize(2);
        assertThat(ministerioResponseV2.getObjetivos()).contains("Animar a comunidade", "Divertir a comunidade");

        assertThat(ministerioResponseV2.isHasDance()).isFalse();
        assertThat(ministerioResponseV2.isHasRepertorio()).isFalse();
        assertThat(ministerioResponseV2.isHasMusic()).isTrue();
    }
}

