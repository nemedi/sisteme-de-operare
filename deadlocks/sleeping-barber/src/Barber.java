import java.util.concurrent.Semaphore;

public class Barber implements Runnable {

    private FreeChairs freeChairs;
    private Semaphore clients;
    private Semaphore barberReady;
    private Semaphore chair;

    public Barber(FreeChairs freeChairs,
        Semaphore clients,
        Semaphore barberReady,
        Semaphore chair) {
        this.freeChairs = freeChairs;
        this.clients = clients;
        this.barberReady = barberReady;
        this.chair = chair;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Barber is looking for client.");
                clients.acquire();
                freeChairs.increment();
                barberReady.release();
                System.out.println("Barber is ready to start cutting client's hair.");
                chair.release();
                cutClientHair();
            } catch (InterruptedException e) {
            }
            
        }
    }

    private void cutClientHair() throws InterruptedException {
        System.out.println("Barber is cutting the client's hair.");
        Thread.sleep(500);
    }
    
}
