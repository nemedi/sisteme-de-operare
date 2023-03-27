package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worker implements Runnable {
	
	private static List<Integer> list = new ArrayList<Integer>();
	private int index;
	private boolean lock;
	
	public Worker(int index, boolean lock) {
		this.index = index;
		this.lock = lock;
	}

	@Override
	public void run() {
		if (lock) {
			synchronized (list) {
				list.add(index);
				list.add(index);
				work();
				list.remove(list.size() - 1);
			}
		} else {
			list.add(index);
			list.add(index);
			work();
			list.remove(list.size() - 1);			
		}
	}

	private void work() {
		try {
			Random random = new Random();
			Thread.sleep(200 + random.nextInt(500));
		} catch (InterruptedException e) {
		}
	}
	
	public static List<Integer> getList() {
		return list;
	}

}
