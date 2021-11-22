
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TestTimer {

    public static void main(String[] args) {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Run my Task " + new Date());
//            }
//        }, 0, 10 * 1000);

        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        today.set(Calendar.HOUR_OF_DAY, 19);
        today.set(Calendar.MINUTE, 57);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Run my Task " + new Date());
            }
        }, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

}
