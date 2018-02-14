package com.nowcoder.wenda;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread{
	private int tid ;
	
	public MyThread(int tid) {
		this.tid = tid;
	}
	@Override
	public void run() {
		try {
			for(int i =0;i<10;i++) {
				Thread.sleep(1000);
				System.out.println(String.format("%d:%d", tid,i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
public class MultiThreadTests {
	private static void testThread() {
		for(int i = 0;i<7;i++) {  
//			new MyThread(i).start();
		}
		
		for(int i =0;i<7;i++) {
//			接口式的匿名内部类是实现了一个接口的匿名类。
//			class Anomymous implements Runnable { public void run() { } } Runnable rn = new Anomymous();
//			匿名内部类(Anonymous Inner Class)，在创建实例的同时给出类的定义，所有这些在一个表达式中完成。
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for(int i =0;i<10;i++) {
							Thread.sleep(1000);
							System.out.println(String.format("t2 %d:" ,i));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}).start();
		}
	}
	
	private static Object obj = new Object();
	
	public static void testSynchronized1() {
		synchronized(new Object()) {
			try {
				for(int i =0;i<10;i++) {
					Thread.sleep(1000);
					System.out.println(String.format("t3 %d",i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void testSynchronized2() {
		synchronized(new Object()) {
			try {
				for(int i =0;i<10;i++) {
					Thread.sleep(1000);
					System.out.println(String.format("t4 %d",i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void testSynchronized() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					testSynchronized1();
					testSynchronized2();
				}
			}).start();
	}
//	实现异步处理
	static class Consumer implements Runnable{
		private BlockingQueue<String> q;
		public Consumer(BlockingQueue<String> q ) {
			this.q = q;
		}
//		不断取数据
		@Override
		public void run() {
			try {
				while(true) {
					System.out.println(Thread.currentThread().getName() + ":" + q.take());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
//	将某个内部类修饰为静态类，才能在静态类调用 该内部类的成员变量和成员方法
	static class Producer implements Runnable{
		private BlockingQueue<String> q;
		public Producer(BlockingQueue<String> q ) {
			this.q = q;
		}
//		不断插入数据
		@Override
		public void run() {
			try {
				for(int i = 0;i<100;i++) {
					Thread.sleep(1000);
					q.put(String.valueOf(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	private static  ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
	private static int userId;
//	使用本地线程变量，创建10个线程，答应出每个线程的id
	/*public static void testThreadLocal() {
		
		for(int i = 0;i< 10;i++) {
			final int finalId = i;
//			thread方法的参数为传入实现runnable接口
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						threadLocalUserIds.set(finalId);
						Thread.sleep(1000);
						System.out.println("ThreadLocalUserIds:" + threadLocalUserIds.get());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}).start();
		}
		
		for(int i = 0;i< 10;i++) {
			final int finalId = i;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						userId = finalId;
						Thread.sleep(1000);
						System.out.println("UserId:" + userId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}).start();
		}
	}
	}*/
	
	public static void testBlockingQueue() {
//		阻塞队列实现类三种：
//		有界：由数组支持arrayblockingqueue；
//		无界：基于已链接节点，范围任意的blockingqueue
//		同步移交：put和take串行执行。
		BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
		new Thread(new Producer(q)).start();
		new Thread(new Consumer(q) ,"Consumer1").start();
		new Thread(new Consumer(q),"Consumer2").start();
		
	}
	
	public static int counter = 0;
//	原子操作的静态变量
	public static AtomicInteger atomicInteger = new AtomicInteger(0);
	public static void testWithoutAtomic() {
		for(int i = 0;i < 10;i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						for(int i =0;i<10;i++) {
							counter++;
							System.out.println(counter);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
	}
	
	public static void testWithAtomic() {
		for(int i = 0;i < 10;i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						for(int i =0;i<10;i++) {
							System.out.println(atomicInteger.getAndIncrement());
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
	}
	
	public static void testFuture() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<Integer> future = service.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000);
				return 1;
			}
		
		});
		service.shutdown();
		try {
//			System.out.println(future.get());
			System.out.println(future.get(1000, TimeUnit.MILLISECONDS));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		testWithoutAtomic();
//		testWithAtomic();
		testFuture();
	}
}
