package assignmentthree.utilities;

import java.util.TimerTask;

public class SampleTask extends TimerTask{

	Thread myThreadObj;
	
	 public SampleTask (Thread t){
	   this.myThreadObj=t;
	  }
	
	@Override
	public void run() {
		myThreadObj.start();
	}

}
