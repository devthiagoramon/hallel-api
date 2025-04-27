package br.api.hallel.integrationtests.ministerio.controller;

import br.api.hallel.integrationtests.config.TestConfig;
import br.api.hallel.integrationtests.ministerio.MinisterioIntegrationTest;
import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
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
public class DancaMinisterioCoordenadorControllerTest extends
        MinisterioIntegrationTest implements WithAssertions {

    static String danca_ministerio_id_1 = "";
    @Order(1)
    @Test
    void createDancaMinisterio() throws IOException {
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/danca/create")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        DancaMinisterioDTO dancaMinisterioDTO = new DancaMinisterioDTO();
        dancaMinisterioDTO.setMinisterioId(dummyMinisterioIds.get(0));
        dancaMinisterioDTO.setNome("Amor a Deus I");
        dancaMinisterioDTO.setDescricao("Amor a Deus I é uma dança para louvar ao Senhor Deus");
        dancaMinisterioDTO.setLinkVideo("link_video_1");



        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(dancaMinisterioDTO)
                                 .when().post()
                                 .then()
                                 .statusCode(201)
                                 .extract().body().asString();

        var response = mapper.readValue(content, DancaMinisterioResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getNome()).isEqualTo("Amor a Deus I");
        assertThat(response.getDescricao()).isEqualTo("Amor a Deus I é uma dança para louvar ao Senhor Deus");
        assertThat(response.getLinkVideo()).isEqualTo("link_video_1");
        danca_ministerio_id_1 = response.getId();
    }

    @Order(2)
    @Test
    void listAllDancasMinisterioByMinisterioId() throws IOException{
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0).getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/danca/list/"+dummyMinisterioIds.get(0))
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then().statusCode(200).extract().body().asString();
        var response = mapper.readValue(content, List.class);
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        assertThat(response).size().isEqualTo(1);
    }

    @Order(3)
    @Test
    void listDancasMinisterioById() throws IOException{
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0).getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/danca/list/id/"+ danca_ministerio_id_1)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .get()
                                 .then().statusCode(200).extract().body().asString();

        var response = mapper.readValue(content, DancaMinisterioResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getNome()).isEqualTo("Amor a Deus I");
        assertThat(response.getDescricao()).isEqualTo("Amor a Deus I é uma dança para louvar ao Senhor Deus");
        assertThat(response.getLinkVideo()).isEqualTo("link_video_1");

    }

    @Order(4)
    @Test
    void editDancaMinisterio() throws IOException{
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0).getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/danca/"+ danca_ministerio_id_1 +"/edit")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        DancaMinisterioDTOEdit dancaMinisterioDtoEdit = new DancaMinisterioDTOEdit();
        dancaMinisterioDtoEdit.setNome("Amor a Deus II");
        dancaMinisterioDtoEdit.setDescricao("Amor a Deus II é uma dança para louvar ao Senhor Deus");
        dancaMinisterioDtoEdit.setLinkVideo("link_video_2");


        var content = RestAssured.given()
                                 .spec(specification)
                                 .body(dancaMinisterioDtoEdit)
                                 .put()
                                 .then().statusCode(200).extract().body().asString();

        var response = mapper.readValue(content, DancaMinisterioResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMinisterioId()).isNotNull();
        assertThat(response.getMinisterioId()).isEqualTo(dummyMinisterioIds.get(0));
        assertThat(response.getNome()).isEqualTo("Amor a Deus II");
        assertThat(response.getDescricao()).isEqualTo("Amor a Deus II é uma dança para louvar ao Senhor Deus");
        assertThat(response.getLinkVideo()).isEqualTo("link_video_2");
    }

    @Order(5)
    @Test
    void listDancaMinnisterioAsMembroMinisterio() throws
            IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/danca/" + dummyMinisterioIds.get(0))
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
    void deleteDancaMinisterio() throws IOException{
        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0).getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/danca/"+ danca_ministerio_id_1 +"/delete")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .delete()
                                 .then().statusCode(204).extract().body().asString();

        RequestSpecification specification2 = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/danca/list/"+dummyMinisterioIds.get(0))
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content2 = RestAssured.given()
                                  .spec(specification2)
                                  .get()
                                  .then().statusCode(200).extract().body().asString();

        var response = mapper.readValue(content2, List.class);
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();

    }
}
