import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Callback;
public interface CLibrary extends Library {
    interface SignalHandler extends Callback {
        void apply(int signal);
    }
	CLibrary INSTANCE = Native.load("c", CLibrary.class);
	int SIGINT = 2;
    int kill(int pid, int sig);
    SignalHandler signal(int signum, SignalHandler handler);
}
