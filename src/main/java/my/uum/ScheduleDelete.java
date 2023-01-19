package my.uum;

import java.util.TimerTask;

/**
 * This class is to execute auto delete booking record in database
 */
public class ScheduleDelete extends TimerTask {
    public void run(){
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.autoDeleteBookRecord();
    }
}
