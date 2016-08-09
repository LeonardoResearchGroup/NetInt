package main;

import poc.rjava.RJavaHelper;

public class Executable {

	public static void main(String[] args)
	{
		testrJava();
		//testrServe();
	}
	
	private static void testrJava()
	{
		//http://www.codophile.com/how-to-integrate-r-with-java-using-rjava/
		
		RJavaHelper.getInstance().assignVariable("vector", "c(1,2,3,4,5)");
		RJavaHelper.getInstance().assignVariableFromFunction("mean", "mean", "vector");
		double mean = RJavaHelper.getInstance().obtainValueFromVariableAsDouble("mean");
		System.out.println("Mean: "+mean);
	}
	
	private static void testrServe()
	{
		//http://www.codophile.com/how-to-integrate-r-with-java-using-rserve/
	}
}
