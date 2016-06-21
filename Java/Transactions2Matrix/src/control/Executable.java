package control;

import model.Operator;
import view.*;

public class Executable {

	public static void main(String[] args)
	{
		boolean next = true;
		
		while(next)
		{
			try
			{
				
				boolean ga = ((int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Using Genetic Algorithm (0 = No | 1 = Yes)?")) == 1 ? true: false;
				
				int transactionsColumn = -1;
				int payerColumn = -1;
				int receiverColumn = -1;
				
				transactionsColumn = (int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Transactions column (starting from 0)?");
			
				
				payerColumn = (int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Payer column (starting from 0)?");
				
				receiverColumn = (int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Receiver column (starting from 0)?");
				
				boolean removeQuotes = ((int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Remove quotes from strings (0 = No | 1 = Yes)?")) == 1 ? true: false;
				
				Operator.TransformFile(ViewManager.getFile(), transactionsColumn, payerColumn, receiverColumn, removeQuotes, ga);
				
				ViewManager.showInformationMessage("Done!", "Work complete");
				
			}catch(Exception e)
			{
				ViewManager.showErrorMessage(e.getMessage(), "Error");
			}
			catch(Error er)
			{
				ViewManager.showErrorMessage(er.getMessage(), "Several Error");
			}
			finally{
				
				try
				{
				
					int userResponse = (int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Would you like to continue (0 = No | 1 = Yes)?");
					
					if(userResponse == 0)
					{
						next = false;
					}
						
				}catch(NumberFormatException e)
				{
					ViewManager.showErrorMessage(e.getMessage(), "Error");
				}
				
			}
		}
	}
	
}
