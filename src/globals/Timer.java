package globals;
public class Timer{

	Main p5;

	int savedTime;
	int totalTime;
	int currentTime;

	Timer(){
		p5 = getP5();
		totalTime = 0 ;  
	}
	
	public void setDuration(int tempTotalTime){
		totalTime = tempTotalTime * 1000; 
	}

	public void start(){
		savedTime = p5.millis(); 
	}

	public boolean isFinished(){
		currentTime = p5.millis() - savedTime;
		//p5.println(currentTime);
		if(currentTime > totalTime){
			return true; 
		} else {
			return false;
		}

	}
	
	public int getTotalTime(){
		return totalTime;
	}
	public int getCurrentTime(){
		return currentTime;
	}
	
	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}



