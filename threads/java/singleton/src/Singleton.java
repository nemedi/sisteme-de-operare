public class Singleton {
    
    private static Singleton instance;

    private Singleton() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (!Singleton.class.getName().equals(stackTrace[2].getClassName())
            || !"getInstance".equals(stackTrace[2].getMethodName())) {
                throw new InstantiationError("Constructor was called by another method than getgInstance.");
            }
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
