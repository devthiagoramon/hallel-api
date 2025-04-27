package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.model.Eventos;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@Log4j2
public class TelegramService {
    private Client client;


    private static final String API_ID = "29287658";

    private static final String API_HASH = "e739b089c47e8415f4d688d78c18ade6";

    private static final String PHONE_NUMBER = "+5592991533437";

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH'h'mm");

//    public TelegramService() {
//        try {
//            Client.execute(new TdApi.SetLogVerbosityLevel(0));
//            client = Client.create(this::handleUpdate, null, null);
//
//        } catch (Exception e) {
//            log.error(e);
//            log.error("Telegram Service error: {}", e.getMessage());
//        }
//    }

    private TdApi.SetTdlibParameters getTdLibParams() {
        TdApi.SetTdlibParameters params = new TdApi.SetTdlibParameters();
        params.apiHash = API_HASH;
        params.databaseDirectory = "tdlib";
        params.useMessageDatabase = true;
        params.useSecretChats = false;
        params.apiId = Integer.parseInt(API_ID);
        params.systemLanguageCode = "en";
        params.deviceModel = "Spring Boot API";
        params.applicationVersion = "1.0.0";
        return params;
    }


    private void handleAuthorization(TdApi.Object object) {
        if (object instanceof TdApi.UpdateAuthorizationState) {
            TdApi.AuthorizationState state = ((TdApi.UpdateAuthorizationState) object).authorizationState;

            if (state instanceof TdApi.AuthorizationStateWaitPhoneNumber) {
                client.send(new TdApi.SetAuthenticationPhoneNumber(PHONE_NUMBER, null), this::handleAuthorization, ex -> log.error(ex.getMessage()));
            } else if (state instanceof TdApi.AuthorizationStateWaitCode) {
                System.out.println("Insert code telegram has to be delivered! ");
                client.send(new TdApi.CheckAuthenticationCode("42645"), this::handleAuthorization, ex -> log.error(ex.getMessage()));
            } else if (state instanceof TdApi.AuthorizationStateReady) {
                System.out.println("Telegram authorization succeed");
            } else if (state instanceof TdApi.AuthorizationStateWaitTdlibParameters) {
                client.send(getTdLibParams(), this::handleAuthorization);
            }
        }
    }

    private void handleUpdate(TdApi.Object object) {
        if (object instanceof TdApi.User user) {
            log.info("HANDLER UPDATE COM USER: {}", user);
        }
        if (object instanceof TdApi.UpdateAuthorizationState) {
            log.info("Update telegram: {}", object);
            handleAuthorization(object);
        }
    }

    private void createChatAndSendMessage(long userId,
                                          String message) {
        CountDownLatch latch = new CountDownLatch(1);
        client.send(new TdApi.CreatePrivateChat(userId, false), chatResult -> {
            log.info("CHAT RESULT: {}", chatResult);
            sendMessage(userId, message);
            latch.countDown();
        }, chatException -> log.error("Erro ao criar chat", chatException));
    }

    private void sendMessage(long chatId, String message) {
        CountDownLatch latch = new CountDownLatch(1);
        TdApi.SendMessage sendMessage = new TdApi.SendMessage();
        sendMessage.chatId = chatId;
        sendMessage.inputMessageContent = new TdApi.InputMessageText(
                new TdApi.FormattedText(message, new TdApi.TextEntity[0]), null, false
        );
        log.debug("SEND MESSAGE: {}", sendMessage);

        client.send(sendMessage, response -> {
            log.info("Mensagem enviada para mim mesmo: {}", response.toString());
            latch.countDown();
        }, exception -> log.error("Erro ao enviar mensagem", exception));
    }

    public void sendMessageToMe(String message) {
        log.info("Sending message to me...");
        CountDownLatch latch = new CountDownLatch(1);
        client.send(new TdApi.GetMe(), result -> {
            if (result instanceof TdApi.User user) {
                long myUserId = user.id; // Pegando meu próprio userId
                createChatAndSendMessage(myUserId, message);
            }
            latch.countDown();
        }, exception -> log.error("Erro ao obter meu userId", exception));
        try {
            latch.await(); // Aguarda até o callback ser executado
        } catch (InterruptedException e) {
            log.error(e);
        }
    }


    public boolean sendMessageWithEventToContact(String phoneDestination,
                                                 String conviteMessage,
                                                 Eventos evento) {
        log.info("Send message with event to contact {}...", phoneDestination);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean response = new AtomicBoolean(false);

        String messageTemplate = """
                ⛪COMUNIDADE CATÓLICA HALLEL⛪
                
                Olá, você foi convidado para uma escala de um evento da nossa comunidade, segue as informações sobre o evento:
                
                🍾 Evento %s
                📅 Data: %s
                🚩 Localização: %s
                
                """.formatted(evento.getTitulo(), dateFormat.format(evento.getDate()), evento.getLocalEvento()
                                                                                             .getLocalizacao());

        client.send(new TdApi.SearchUserByPhoneNumber(phoneDestination, false), update -> {
            if (update instanceof TdApi.User user) {
                createChatAndSendMessage(user.id, messageTemplate);
                if (conviteMessage != null) {
                    sendMessage(user.id, conviteMessage);
                }
                response.set(true);
                log.info("Message with event sended to contact {}", user.phoneNumber);
            }
            latch.countDown();
        }, exception -> log.error("Erro ao obter o userId do contato", exception));

        try {
            latch.await(); // Aguarda até o callback ser executado
        } catch (InterruptedException e) {
            log.error(e);
        }

        return response.get();
    }

    public boolean sendMessageToContact(String phoneDestination, String message){
        log.info("Send message to contact {}...", phoneDestination);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean response = new AtomicBoolean(false);


        client.send(new TdApi.SearchUserByPhoneNumber(phoneDestination, false), update -> {
            if (update instanceof TdApi.User user) {
                createChatAndSendMessage(user.id, message);

                response.set(true);
                log.info("Message sended to contact {}", user.phoneNumber);
            }
            latch.countDown();
        }, exception -> log.error("Erro ao obter o userId do contato", exception));

        try {
            latch.await(); // Aguarda até o callback ser executado
        } catch (InterruptedException e) {
            log.error(e);
        }

        return response.get();
    }

    public boolean sendMessageUninvite(String phoneDestination,
                                       String conviteMessage,
                                       Eventos evento) {
        log.info("Send message to uninvite {}...", phoneDestination);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean response = new AtomicBoolean(false);

        String messageTemplate = """
                ⛪COMUNIDADE CATÓLICA HALLEL⛪
                
                ! AVISO !
                
                Você foi desconvidado para participar da escala do evento:
                🍾 Evento %s
                📅 Data: %s
                🚩 Localização: %s
                """.formatted(evento.getTitulo(), dateFormat.format(evento.getDate()), evento.getLocalEvento()
                                                                                             .getLocalizacao());
        client.send(new TdApi.SearchUserByPhoneNumber(phoneDestination, false), update -> {
            if (update instanceof TdApi.User user) {
                createChatAndSendMessage(user.id, messageTemplate);
                if (conviteMessage != null) {

                    sendMessage(user.id, conviteMessage);
                }
                response.set(true);
                log.info("Message sended to contact {}", user.phoneNumber);
            }
            latch.countDown();
        }, exception -> log.error("Erro ao obter o userId do contato", exception));

        try {
            latch.await(); // Aguarda até o callback ser executado
        } catch (InterruptedException e) {
            log.error(e);
        }

        return response.get();
    }

}
