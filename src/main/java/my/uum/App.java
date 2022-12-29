package my.uum;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    public static void main(String[] args) throws ClassNotFoundException {

        //A part for Telegram Bot registration
        try {
            App app = new App();

            TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
            botApi.registerBot(new PNG_Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}