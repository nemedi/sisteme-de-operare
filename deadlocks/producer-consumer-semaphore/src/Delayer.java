import java.util.Random;

public class Delayer {
    
    private static Random random = new Random();

    public static void delay() {
        try {
            Thread.sleep(1000 + (random.nextInt(1000) % 10) * 100);
        } catch (InterruptedException e) {
        }
    }
}
