package mutex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.function.Function;

public class FileMutex {

	private String path;

	public FileMutex(final String path) {
		this.path = path;
	}

	public <T, R> R doWithLock(Function<T, R> function, T argument) throws FileNotFoundException, IOException {
		try (RandomAccessFile file = new RandomAccessFile(path, "rw")) {
			System.out.print("Acquiring lock...");
			FileLock lock = file.getChannel().lock();
			System.out.println("done.");
			R result = function.apply(argument);
			System.out.print("Releasing lock...");
			lock.release();
			System.out.println("done.");
			return result;
		}
	}
}
