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
				
				boolean transactions = ga ? true : ((int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Include transactions (0 = No | 1 = Yes)?")) == 1;
				
				int transactionsColumn = -1;
				int payerColumn = -1;
				int receiverColumn = -1;
				
				if(transactions)
				{
					transactionsColumn = (int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Transactions column (starting from 0)?");
				}
				
				payerColumn = (int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Payer column (starting from 0)?");
				
				receiverColumn = (int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Receiver column (starting from 0)?");
				
				boolean removeQuotes = ((int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Remove quotes from strings (0 = No | 1 = Yes)?")) == 1 ? true: false;
				
				boolean columnShuffle = ((int) ViewManager.getInput(ViewManager.INPUT_TYPE_INT, "Column shuffle (0 = No | 1 = Yes)?")) == 1 ? true: false;
				
				Operator.TransformFile(ViewManager.getFile(), transactions, transactionsColumn, payerColumn, receiverColumn, removeQuotes, columnShuffle , ga);
				
				ViewManager.showInformationMessage("Done!", "Work complete");
				
			}catch(Exception e)
			{
				ViewManager.showErrorMessage(e.getMessage(), "Error");
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
