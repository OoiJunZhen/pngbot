package  my.uum;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.Calendar;
import java.util.Timer;

public class App {
    public static void main(String[] args) throws ClassNotFoundException {

        //A part for Telegram Bot registration
        try {
            App app = new App();

            TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
            botApi.registerBot(new PNG_bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 1);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        //setting timer as a daemon thread
        Timer timer = new Timer(true);
        ScheduleDelete task = new ScheduleDelete();
        timer.schedule(task, date.getTime(), 24 * 60 * 60 * 1000);

    }
}
