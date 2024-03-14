public class Sender {

	public static void main(String[] arguments) {
		if (arguments.length == 1) {
			int targetProcessId = Integer.parseInt(arguments[0]);
			CLibrary.INSTANCE.kill(targetProcessId, CLibrary.SIGINT);
			System.out.println("Signal sent successfully to process.");
		}
	}

}
