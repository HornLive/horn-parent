package org.horn.commons.java.util.runner;

/**
 * Copyright (c), 2017 , 南京途牛科技有限公司
 * 
 * 设计模型：生产者消费者模型(线程安全)
 * @author Horn Live
 * @date 创建时间： 2018年9月16日 上午12:12:56
 * @version 3.0
 */
public class ProducerConsumer {
	public static void main(String[] args) {
		SyncStack ss = new SyncStack();
		Producer p = new Producer(ss);
		Consumer c = new Consumer(ss);
		new Thread(p).start();
		new Thread(p).start();
		new Thread(p).start();
		new Thread(c).start();
	}
}

class WoTou {
	long id;

	WoTou(long l) {
		this.id = l;
	}

	public String toString() {
		return "WoTou : " + id;
	}
}

class SyncStack {
	int index = 0;
	WoTou[] arrWT = new WoTou[6];

	public synchronized void push(WoTou wt) {
		while (index == arrWT.length) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.notifyAll();
		arrWT[index] = wt;
		index++;
	}

	public synchronized WoTou pop() {
		while (index == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.notifyAll();
		index--;
		return arrWT[index];
	}
}

class Producer implements Runnable {
	SyncStack ss = null;

	Producer(SyncStack ss) {
		this.ss = ss;
	}

	public void run() {
//		for (int i = 0; i < 20; i++) {
			WoTou wt = new WoTou(System.currentTimeMillis());
			ss.push(wt);
			System.out.println("生产了" + wt);
			try {
				Thread.sleep((int) (Math.random() * 200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//	}
}

class Consumer implements Runnable {
	SyncStack ss = null;

	Consumer(SyncStack ss) {
		this.ss = ss;
	}

	public void run() {
//		for (int i = 0; i < 20; i++) {
			WoTou wt = ss.pop();
			System.out.println("消费了: " + wt);
			try {
				Thread.sleep((int) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//	}
}