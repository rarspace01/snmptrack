package org.dh.usertrack.snmptest;

class RunnableThread implements Runnable{
	
	Thread runner;
	public RunnableThread() {
	}
	
	public RunnableThread(String threadName) {
        runner = new Thread(this, threadName);   // (1) Create a new thread.
        System.out.println(runner.getName());
        runner.start();                          // (2) Start the thread.
	}
	
	public void run(){
		//Display info about this particular thread
		System.out.println(Thread.currentThread());
	}
	
	
}

public class RunnableExample{
  
	public static void main(String[] args){

		HelperClass.sleeping(5);
		
	    Thread thread1 =  new Thread(new RunnableThread(),"thread1");
	    HelperClass.sleeping(5);
	    Thread thread2 =  new Thread(new RunnableThread(),"thread2");
	    HelperClass.sleeping(5);
	    RunnableThread thread3 = new RunnableThread("thread3");
	    HelperClass.sleeping(5);
//Start the threads
	    thread1.start();
	    thread2.start();
	    HelperClass.sleeping(5);
	    try{
	      //delay for one second
	      Thread.currentThread().sleep(1000);
	    }catch(InterruptedException e){}
	
	    //Display info about the main thread    
	    System.out.println(Thread.currentThread());        

	}
}
