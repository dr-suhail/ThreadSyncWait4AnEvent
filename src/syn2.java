
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
 
/*
 * 	This class will use a lock, condition variable,
 * 	and a predicate variable to ensure synchronization
 *	of events (i-e event of displaying "ab" should occur
 *	before the event of displaying "cd"
 **/
 
 class Syn2DisplayThread implements Runnable {

	/* create a lock */
	ReentrantLock mylock = new ReentrantLock();
	
	/* get a condition variable */
	Condition myCond  = mylock.newCondition(); 
	
	/* creat the predicate variable and initialize to true */
	boolean turn = true; 

	
	public void display(String str) {
	
		int strlen=str.length();
		
		for (int i=0; i< strlen; i++) 
		{
			
			System.out.print(str.charAt(i));
			
			try{
				Thread.sleep(100);	
			}
			catch(InterruptedException e)
			{
				System.err.println("sleep() in DisplayThread.display() was interrupted.");	
				System.exit(0);
			}
	
		}
	}
	
	
	public void run(){
		
		String str = Thread.currentThread().getName();	
		
		/* thread1 will disply "ab" */
		if( str.compareTo("thread1")==0 )
		{
			
			for(int i=0;i<10;i++)
			{
				mylock.lock();
				try{
												
					while( turn == false )/* it is not turn of thread1 to display "ab" so wait untill signaled */
					{
						try{
							
							myCond.await();	
						}
						catch(InterruptedException ie){
							
							System.err.println("thread1 in DisplayThread.run() was interrupted.");	
							System.exit(0);
						}
					}
					
					/* call the display function */		
					display("ab");
					
					/* thread1 has done its turn */
					/* give the turn to thread2  */
					turn = false;
					
					/* wake up the waiting thread2 */
					myCond.signal();
				}
				finally{
					
					/* thread1 will release the lock*/
					/* finally block ensures to release the lock in any case */
					mylock.unlock();
				}
			}
					
		}
		else	/* thread2 will disply "cd" */
		{
			for(int i=0;i<10;i++)
			{
				mylock.lock();
					try{
						
						/* it is turn of thread1 to display "ab" so wait untill signaled */
						while( turn == true )
						{
							try{
								
								myCond.await();
							}
							catch(InterruptedException ie){
								System.err.println("thread2 in DisplayThread.run() was interrupted.");	
								System.exit(0);
							}
						}
						
						/* call the display function */
						display("cd\n");
						
						/* wake up the waiting thread1 */
						myCond.signal();
						
						/* give the turn to thread1 */
						turn = true;
					}
					finally{
						/* thread2 will release the lock*/
						/* finally block ensures to release the lock in any case */
						mylock.unlock();
					}
			}
				
		} 		/* end of thread2 code */	
	}
}


public class Syn2 {
    
	public static void main(String[] args) {

		Syn2DisplayThread dt = new Syn2DisplayThread();
		
		/* Create two threads */
		Thread thread1 = new Thread(dt, "thread1");
		Thread thread2 = new Thread(dt, "thread2");
		
		/* Start the threada */
		thread1.start();
		thread2.start();
		
		/* Wait for the threads to complete */
		try{
		
			thread1.join();
			thread2.join();
		}	
		catch(InterruptedException ie){
		
			System.out.println("Thread join in main() was interrupted");	
		}	
	}
}
