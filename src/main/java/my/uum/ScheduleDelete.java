package my.uum;

import java.util.TimerTask;

public class ScheduleDelete extends TimerTask {
    public void run(){
        DatabaseManager databaseManager = new DatabaseManager();
        //databaseManager.autoDeleteBookRecord();

        System.out.println("It's time");
    }
}
