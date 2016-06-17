package model.entity;

import java.util.ArrayList;
import java.util.Random;

public class Matrix {

	private ArrayList<String> columns;
	private ArrayList<Cell> cells;
	private int numRows;
	private boolean first;
	private boolean geneticAlgorithm;
	private ArrayList<Integer> transactionNumbers;
	
	public Matrix(boolean geneticAlgorithm)
	{
		columns = new ArrayList<String>();
		cells = new ArrayList<Cell>();
		numRows = 0;
		first = true;
		this.geneticAlgorithm = geneticAlgorithm;
		
		if(geneticAlgorithm)
		{
			transactionNumbers = new ArrayList<Integer>();
		}
		
	}
	
	public void appendRow(String payer, String receiver, int transactions)
	{
		
		int transactionsCopy = transactions;
		transactions = geneticAlgorithm ? -1 : transactions;
		
		if(first)
		{
			
			if(geneticAlgorithm)
			{
			cells.add(new Cell(numRows, 0, 1));
			cells.add(new Cell(numRows, 1, 1));
			}
			else
			{
				for(int i = 0; i < transactions ; i++)
				{
					cells.add(new Cell(numRows, 0, 1));
					cells.add(new Cell(numRows, 1, 1));
					numRows++;
				}
			}
			
			columns.add(payer);
			columns.add(receiver);
			first = false;
			
		}
		else
		{
			int payerColumn = getColumn(payer);
			int receiverColumn = getColumn(receiver);
			
			if(payerColumn==-1)
			{
				columns.add(payer);
			}
			
			payerColumn = payerColumn != -1 ? payerColumn : columns.size()-1;
			
			if(receiverColumn==-1)
			{
				columns.add(receiver);
			}
			
			receiverColumn = receiverColumn != -1 ? receiverColumn : columns.size()-1;
			
			if(geneticAlgorithm)
			{
			cells.add(new Cell(numRows, payerColumn, 1));
			cells.add(new Cell(numRows, receiverColumn, 1));
			}
			else
			{
				for(int i = 0; i < transactions ; i++)
				{
					cells.add(new Cell(numRows, payerColumn, 1));
					cells.add(new Cell(numRows, receiverColumn, 1));
					numRows++;
				}
			}
			
			
		}
		
		if(geneticAlgorithm)
		{
			transactionNumbers.add(transactionsCopy);
			numRows++;
		}
		
		
		
	}
	
	private int getColumn(String name)
	{
		boolean seguir = true;
		int position = -1;
		
		for(int i=0; i<columns.size() && seguir ; i++)
		{
			String actual = columns.get(i);
			
			if(actual.equals(name))
			{
				position = i;
				seguir = false;
			}
		}
		
		return position;
		
	}
	
	public ArrayList<String> getColumns()
	{
		return columns;
	}
	
	public int[][] toIntMatrix(boolean rowShuffle, boolean columnShuffle)
	{
		int[][] matrix = null;
		
		if(!geneticAlgorithm)
		{
			matrix = new int[numRows][columns.size()];
		}
		else
		{
			matrix = new int[numRows][columns.size()+1];
		}
		
		for(Cell c : cells)
		{
			matrix[c.getRow()][c.getColumn()] = c.getValue();
		}
		
		if(geneticAlgorithm)
		{
			for(int i = 0; i<transactionNumbers.size();i++)
			{
				Integer number = transactionNumbers.get(i);
				matrix[i][matrix[0].length-1] = number;
			}
		}
		
		if(rowShuffle){
			
			int[][] matrixCopy = new int[matrix.length][matrix[0].length];
			
			for (int i = 0; i < matrix.length; i++) {
			    for (int j = 0; j < matrix[0].length; j++) {
			        matrixCopy[i][j] = matrix[i][j];
			    }
			}
			
			ArrayList<String> rowPositions = new ArrayList<String>();
			
			for(int i=0;i<numRows;i++)
			{
				rowPositions.add(""+i);
			}
			
			for(int i=0;i<numRows;i++)
			{
				Random r = new Random(System.currentTimeMillis());
				int selected = r.nextInt(rowPositions.size());
				int rowSelected = Integer.parseInt(rowPositions.get(selected));
				
				for(int j=0;j<matrix[0].length;j++)
				{
					matrix[i][j] = matrixCopy[rowSelected][j];
				}
				
				rowPositions.remove(selected);
			}
		}
		
		if(columnShuffle)
		{
			
			ArrayList<String> selectedColumns = new ArrayList<String>();
			
			int[][] matrixCopy = new int[matrix.length][matrix[0].length];
			
			for (int i = 0; i < matrix.length; i++) {
			    for (int j = 0; j < matrix[0].length; j++) {
			        matrixCopy[i][j] = matrix[i][j];
			    }
			}
			
			ArrayList<String> columnPositions = new ArrayList<String>();
			
			for(int i=0;i<columns.size();i++)
			{
				columnPositions.add(""+i);
			}
			
			for(int i=0;i<columns.size();i++)
			{
				Random r = new Random(System.currentTimeMillis());
				int selected = r.nextInt(columnPositions.size());
				int columnSelected = Integer.parseInt(columnPositions.get(selected));
				
				selectedColumns.add(""+columnSelected);
				
				for(int j=0;j<numRows;j++)
				{
					matrix[j][i] = matrixCopy[j][columnSelected];
				}
				
				columnPositions.remove(selected);
			}
			
			ArrayList<String> temp = new ArrayList<String>();
			
			for(String col : selectedColumns)
			{
				int selected = Integer.parseInt(col);
				temp.add(columns.get(selected));
			}
			
			columns = temp;
			
		}
	
		
		return matrix;
	}
	
	
}
