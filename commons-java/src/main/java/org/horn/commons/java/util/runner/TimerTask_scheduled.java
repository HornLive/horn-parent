package org.horn.commons.java.util.runner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledExecutorService是从Java SE 5的java.util.concurrent里，做为并发工具类被引进的，这是最理想的定时任务实现方式。
 * 相比于上两个方法，它有以下好处：
 * 相比于Timer的单线程，它是通过线程池的方式来执行任务的 可以很灵活的去设定第一次执行任务delay时间 提供了良好的约定，以便设定执行的时间间隔
 * 下面是实现代码，我们通过ScheduledExecutorService#scheduleAtFixedRate展示这个例子，通过代码里参数的控制，
 * 首次执行加了delay时间。
 * 
 * @author lihongen
 *
 */
public class TimerTask_scheduled {
	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
			public void run() {
				// task to run goes here
				System.out.println("Hello !!");
			}
		};
		Runnable runnable1 = new Runnable() {
			public void run() {
				// task to run goes here
				System.out.println("Hello弟弟顶顶顶顶顶 !!");
			}
		};
		rrr rr = new rrr();
		
		ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);//延迟和间隔
		service.scheduleAtFixedRate(rr, 1, 5, TimeUnit.SECONDS);//延迟和间隔
	}
}


class rrr implements Runnable{

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("fffffffffasdfasdfasdfasdf");
	}
	
}