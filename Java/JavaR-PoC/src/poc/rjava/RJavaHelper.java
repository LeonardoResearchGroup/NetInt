package poc.rjava;

import org.rosuda.JRI.RBool;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.Rengine;

public class RJavaHelper {

	private static final RJavaHelper INSTANCE = new RJavaHelper();
	private Rengine engine;
	
	private RJavaHelper()
	{
		//Start Rengine.
        engine = new Rengine(new String[] { "--no-save" }, false, null);
	}
	
	public static RJavaHelper getInstance()
	{
		return INSTANCE;
	}
	
	public void assignVariable(String variableName, String value)
	{
		engine.eval(variableName + "=" + value);
	}
	
	public void assignVariableFromFunction(String variableName, String functionName, String functionInput)
	{
		engine.eval(variableName + "=" + functionName + "(" + functionInput+  ")");
	}
	
	public String obtainValueFromVariableAsString(String variableName)
	{
		return engine.eval(variableName).asString();
	}
	
	public int obtainValueFromVariableAsInt(String variableName)
	{
		return engine.eval(variableName).asInt();
	}
	
	public double obtainValueFromVariableAsDouble(String variableName)
	{
		return engine.eval(variableName).asDouble();
	}
	
	public RBool obtainValueFromVariableAsBoolean(String variableName)
	{
		return engine.eval(variableName).asBool();
	}
	
	public RList obtainValueFromVariableAsList(String variableName)
	{
		return engine.eval(variableName).asList();
	}
	
	public double[][] obtainValueFromVariableAsMatrix(String variableName)
	{
		return engine.eval(variableName).asMatrix();
	}

	public RVector obtainValueFromVariableAsVector(String variableName)
	{
		return engine.eval(variableName).asVector();
	}

}
