package app.core.threads;

import app.core.entities.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

// Singleton components will be initialized on startup, therefore the thread will run on startup
@Component("couponExpirationThread")
@DependsOn({"couponExpirationService"})
@Scope("singleton")
public class CouponExpirationDailyJob implements Runnable {

    @Autowired
    private CouponExpirationService service;
    private final Thread t;
    private boolean quit = false;

    /**
     * Constructor and starter.
     */
    public CouponExpirationDailyJob() {
        this.t = new Thread(this);
        t.setDaemon(true); // Together with PreDestroy, thread will always stop properly when the program stops
        t.start();
    }

    /**
     * Thread Start.
     */
    @Override
    public void run() {

        System.out.println("CouponExpiration thread has started ");

        while (!quit) {

            ArrayList<Coupon> coupons = new ArrayList<>();

            try {
                coupons = (ArrayList<Coupon>) service.getAllCoupons();
            } catch (NullPointerException e) {

            }
            LocalDateTime now = LocalDateTime.now();

            try {

                for (Coupon coupon : coupons) {
                    if (coupon.getEndDate().isBefore(now))
                        service.deleteCouponById(coupon.getId());
                }
                long delay = delayUntilMidnight();
                Thread.sleep(delay);

            } catch (InterruptedException e) {

            }

        }
        System.out.println("CouponExpiration thread has stopped ");
    }

    // Will always close the thread properly
    @PreDestroy
    /**
     * Stops the thread
     */
    public void stop() {

        System.out.println("CouponExpiration thread closing properly... ");
        quit = true;
        t.interrupt();
    }

    /**
     * Gets how many milliseconds left until next midnight.
     */
    public static long delayUntilMidnight() {

        // Delay to work again at midnight
        Calendar now = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);

        if (now.after(midnight)) {
            midnight.add(Calendar.DATE, 1);
        }

        long delay = midnight.getTimeInMillis() - now.getTimeInMillis();
        return delay;
    }

}
