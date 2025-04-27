package br.api.hallel.integrationtests.ministerio.controller;

import br.api.hallel.integrationtests.config.TestConfig;
import br.api.hallel.integrationtests.ministerio.MinisterioIntegrationTest;
import br.api.hallel.moduloAPI.dto.v1.evento.EventoDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EscalaMinisterioWithEventoInfoResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EscalaRepertorioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.RepertorioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.RepertorioResponse;
import br.api.hallel.moduloAPI.dto.v2.ministerio.EscalaMinisterioResponseWithInfosV2;
import br.api.hallel.moduloAPI.model.LocalEvento;
import br.api.hallel.moduloAPI.service.eventos.EventosService;
import br.api.hallel.moduloAPI.service.ministerio.EscalaService;
import br.api.hallel.moduloAPI.service.ministerio.RepertorioService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EscalaMinisterioMembroControllerTest extends
        MinisterioIntegrationTest implements WithAssertions {

    @Autowired
    EscalaService escalaService;

    @Autowired
    RepertorioService repertorioService;

    public static RepertorioResponse repertorioResponse;

    static EventoDTO mockEvento(int i, String... ministerioId) {
        EventoDTO evento = new EventoDTO();
        evento.setTitulo("Teste " + i);
        evento.setDescricao("Teste " + i);
        evento.setDate(new Date());
        evento.setBanner("Teste " + i);
        evento.setLocalEvento(new LocalEvento());
        evento.setFileImageUrl("Teste " + i);
        evento.setMinisteriosAssociados(List.of(ministerioId));
        return evento;
    }

    RepertorioDTO mockRepertorio(int i) {
        RepertorioDTO repertorio = new RepertorioDTO();
        repertorio.setMinisterioId(dummyMinisterioIds.get(0));
        repertorio.setNome("Repertorio " + i);
        repertorio.setDescricao("Repertorio " + i);
        return repertorio;
    }

    @BeforeAll
    public static void setUpBeforeClass(
            @Autowired EscalaService escalaService, @Autowired
    EventosService eventosService) throws Exception {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO = mockEvento(1, dummyMinisterioIds.get(0), dummyMinisterioIds.get(1));
//        eventosService.createEvento(eventoDTO);
        EventoDTO eventoDTO2 = mockEvento(2, dummyMinisterioIds.get(0), dummyMinisterioIds.get(1));
//        eventosService.createEvento(eventoDTO2);
    }

    @Test
    @Order(1)
    public void associarRepertorio() throws IOException {


        RepertorioResponse repertorio1 = repertorioService.createRepertorio(mockRepertorio(1));
        RepertorioResponse repertorio2 = repertorioService.createRepertorio(mockRepertorio(2));
        repertorioResponse = repertorio2;

        List<EscalaMinisterioWithEventoInfoResponse> escalasMinisterios =
                escalaService.listEscalaMinisterioRangeDateByMinisterioId(dummyMinisterioIds.get(0),
                        LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2025, 1, 31, 0, 0, 0));


        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/escala/repertorio/" + escalasMinisterios.get(0)
                                                                                                          .getId())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        EscalaRepertorioDTO escalaRepertorioDTO = new EscalaRepertorioDTO();
        escalaRepertorioDTO.setRepertorioIdsAdd(List.of(repertorio1.getId()));


        var content1 = RestAssured.given()
                                  .spec(specification)
                                  .body(escalaRepertorioDTO)
                                  .when().patch()
                                  .then()
                                  .statusCode(200)
                                  .extract().body().asString();

        var response1 = mapper.readValue(content1, EscalaMinisterioResponseWithInfosV2.class);

        assertThat(response1).isNotNull();
        assertThat(response1.getRepertorioIds()).isNotNull();
        assertThat(response1.getRepertorioIds()).hasSize(1);
        assertThat(response1.getRepertorioIds()
                            .get(0)).isEqualTo(repertorio1.getId());
        assertThat(response1.getRepertorioIds()).contains(repertorio1.getId());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/escala/repertorio/" + escalasMinisterios.get(0)
                                                                                                          .getId())
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        EscalaRepertorioDTO escalaRepertorioDTO2 = new EscalaRepertorioDTO();
        escalaRepertorioDTO2.setRepertorioIdsRemove(List.of(repertorio1.getId()));
        escalaRepertorioDTO2.setRepertorioIdsAdd(List.of(repertorio2.getId()));


        var content2 = RestAssured.given()
                                  .spec(specification)
                                  .body(escalaRepertorioDTO2)
                                  .when().patch()
                                  .then()
                                  .statusCode(200)
                                  .extract().body().asString();

        var response2 = mapper.readValue(content2, EscalaMinisterioResponseWithInfosV2.class);

        assertThat(response2).isNotNull();
        assertThat(response2.getRepertorioIds()).isNotNull();
        assertThat(response2.getRepertorioIds()).hasSize(1);
        assertThat(response2.getRepertorioIds()
                            .get(0)).isEqualTo(repertorio2.getId());
        assertThat(response2.getRepertorioIds()).contains(repertorio2.getId());

    }

    @Order(2)
    @Test
    public void deleteRepertorioLinkedWithEscala() throws
            IOException {

        List<EscalaMinisterioWithEventoInfoResponse> escalasMinisterios =
                escalaService.listEscalaMinisterioRangeDateByMinisterioId(dummyMinisterioIds.get(0),
                        LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2025, 1, 31, 0, 0, 0));

        EscalaMinisterioResponseWithInfosV2 escalaMinisterio1 = escalaService.listEscalaMinisterioByIdWithInfosV2(escalasMinisterios.get(0)
                                                                                                                                    .getId());
        assertThat(escalaMinisterio1).isNotNull();
        assertThat(escalaMinisterio1.getRepertorioIds()).contains(repertorioResponse.getId());

        System.out.println(escalaMinisterio1.getRepertorioIds().toString());

        String coordMinisterio1Token = generateCoordToken(membrosTest.get(0)
                                                                     .getToken(), dummyMinisterioIds.get(0), membrosTest.get(0)
                                                                                                                        .getId());
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_CONTENT_TYPE, TestConfig.APPLICATION_JSON)
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, membrosTest.get(0)
                                                                             .getToken())
                .addHeader(TestConfig.HEADER_PARAM_COORDENADOR_TOKEN, coordMinisterio1Token)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/membros/ministerio/coordenador/repertorio/" + repertorioResponse.getId() + "/delete")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = RestAssured.given()
                                 .spec(specification)
                                 .delete()
                                 .then().statusCode(204).extract()
                                 .body().asString();

        EscalaMinisterioResponseWithInfosV2 escalaMinisterio2 = escalaService.listEscalaMinisterioByIdWithInfosV2(escalasMinisterios.get(0)
                                                                                                                                    .getId());
        assertThat(escalaMinisterio2).isNotNull();
        assertThat(escalaMinisterio2.getRepertorioIds()).doesNotContain(repertorioResponse.getId());
        System.out.println(escalaMinisterio2.getRepertorioIds().toString());

    }

}
