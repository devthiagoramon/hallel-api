package br.api.hallel.moduloAPI.service;

import br.api.hallel.ApiHallelApplication;
import br.api.hallel.moduloAPI.service.ministerio.TelegramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ApiHallelApplication.class)
public class TelegramServiceTest {

    @Autowired
    private TelegramService telegramService;

    @Test
    public void sendMessageToTelegram() {
        String number = "+5592994794878";

        telegramService.sendMessageWithEventToContact(number, null, null);
    }

    @Test
    public void sendMessageToMe(){
        String message = "Olá, você foi convidado para uma escala... Info escala... Localização...";
        telegramService.sendMessageToMe(message);
    }
}
