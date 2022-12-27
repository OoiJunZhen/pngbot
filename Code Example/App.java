package my.uum;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * This is the main class for DaBooking Bot
 *
 * @author Ang Toon Ph'ng
 */
public class App {

    public static void main(String[] args) throws ClassNotFoundException {

        //A part for Telegram Bot registration
        try {

            App app = new App();

            TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
            botApi.registerBot(new DaBooking_bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
