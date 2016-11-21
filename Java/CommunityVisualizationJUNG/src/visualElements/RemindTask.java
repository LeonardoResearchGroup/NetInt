package visualElements;

import java.util.TimerTask;

/**
 * It indicates when the canvas is not being zoomed anymore.
 * 
 * @author Loaiza Quintana
 *
 */
class RemindTask extends TimerTask {
//	private Canvas c;

//	public RemindTask(Canvas c) {
//		this.c = c;
//	}
	
	public RemindTask() {

	}

	public void run() {
		Canvas.canvasBeingZoomed = false;
		System.out.println("Canvas> RemindTask> Time's up!");
	}
}

