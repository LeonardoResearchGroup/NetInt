package control;

import model.Operator;
import view.MainWindow;

public class Executable {

	public static void main(String[] args)
	{
		MainWindow window = new MainWindow(new Operator());
		window.setVisible(true);
	}
	
	
}
