package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import co.edu.icesi.tics.sis.vertical_fragmentation.genetic_algorithm.model.AlgoritmoGenetico;
import co.edu.icesi.tics.sis.vertical_fragmentation.genetic_algorithm.model.CargadorMUA;
import co.edu.icesi.tics.sis.vertical_fragmentation.genetic_algorithm.model.RespuestaAlgoritmo;

public class Executable {

	private static final int INITIAL_NUM_COMMUNITIES = 15;
	private static final int INITIAL_NUM_CHROMOSOMES = 100;
	private static final double INITIAL_CROSS_PROB = 0.85;
	private static final double INITIAL_MUT_PROB = 0.02;
	private static final double INITIAL_INTER_PROB = 0.40;
	
	public static void main(String[] args)
	{
		try {
			
			int suggestedGenerations = findGenerations(20, 2000, 5);
			int suggestedChromosomes = findChromosomesNumber(10, 1000, 5, suggestedGenerations);
			double suggestedCrossProb = findCrossProbability(0.50, 0.02, 5, suggestedGenerations, suggestedChromosomes);
			double suggestedMutProb = findMutationProbability(0.015, 0.01, 5, suggestedGenerations, suggestedChromosomes, suggestedCrossProb);
			double suggestedInterProb = findInterProbability(0.40, 0.02, 5, suggestedGenerations, suggestedChromosomes, suggestedCrossProb, suggestedMutProb);
			
			HashMap<Integer, Double> dictionary = findCommunitiesNumber(1, 10, suggestedGenerations, suggestedChromosomes, suggestedCrossProb, suggestedMutProb, suggestedInterProb);
			saveCSV(dictionary, suggestedGenerations, suggestedChromosomes, suggestedCrossProb, suggestedMutProb, suggestedInterProb);
			showMessage(1, "Done!", ".");
			
		} catch (Exception e) {
			
			showMessage(0, e.getMessage(), "Exception");
			
		} catch (Error e)
		{
			showMessage(0, e.getMessage(), "Error");
		}
	}
	
	private static double executeGeneticAlgorithm(int communities2Find, int numChromosomes,  double crossProb, double mutProb, int numGenerations, double interProb) throws Exception
	{
		
		String[] communities = new String[communities2Find];
		
		for(int i = 0; i< communities.length ; i++)
		{
			communities[i] = "C" + (i+1);
		}

		int numGens = 92;
		
		String path = "C://Users//Fell//OneDrive//Icesi//Maestría//Semestre 1//Machine Learning//Proyecto del Curso//ML - Communities//Matrix_2016_06_18_12_13_49//Matrix_2016_06_18_12_13_49.txt";
		File MUAFile = new File(path);
		int numRows=CargadorMUA.darNumFilas(MUAFile);
		int numCol=CargadorMUA.darNumCol(MUAFile);
		int[][] MUA=CargadorMUA.cargarMUA(numRows, numCol, MUAFile);
		
		AlgoritmoGenetico ag = new AlgoritmoGenetico(communities, numChromosomes, numGens, crossProb, mutProb, numGenerations, MUA, interProb);
		RespuestaAlgoritmo response = ag.iniciarAlgoritmo();

		return response.getValorFitness();
		
	}
	
	private static void showMessage(int type, String message, String title)
	{
		type = type == 1 ? javax.swing.JOptionPane.INFORMATION_MESSAGE : javax.swing.JOptionPane.ERROR_MESSAGE;
		javax.swing.JOptionPane.showMessageDialog(null, message, title, type);
	}
	
	private static int findGenerations(int sumFactor, int maxGenerations, int generationExecutions) throws Exception{
		
		int factor = sumFactor;
		int max = maxGenerations;
		int executions = generationExecutions;
		
		Integer suggested = null;
		
		int bestGenerations = -1;
		double bestMean = 1;
		
		for(suggested = new Random(System.currentTimeMillis()).nextInt(10)+1 ; suggested <= max ; suggested += factor )
		{
			
			double sum = 0;
			
			for(int i = 0; i < executions; i++)
			{
				sum += executeGeneticAlgorithm(INITIAL_NUM_COMMUNITIES, INITIAL_NUM_CHROMOSOMES, INITIAL_CROSS_PROB, INITIAL_MUT_PROB, suggested, INITIAL_INTER_PROB);
			}
			
			double mean = sum / executions;
			
			if(mean < bestMean)
			{
				bestMean = mean;
				bestGenerations = suggested;
			}
			
			System.out.println("Finished executions for "+suggested+" generations. Mean: "+mean);
		}
		
		System.out.println("Suggested generations: "+bestGenerations);
		
		return bestGenerations;
	}

	private static int findChromosomesNumber(int sumFactor, int maxChromosomes, int chromosomesExecutions, int numGenerations) throws Exception{
		
		int factor = sumFactor;
		int max = maxChromosomes;
		int executions = chromosomesExecutions;
		
		Integer suggested = null;
		
		int bestChromosomes = -1;
		double bestMean = 1;
		
		for(suggested = new Random(System.currentTimeMillis()).nextInt(10)+50 ; suggested <= max ; suggested += factor )
		{
			
			double sum = 0;
			
			for(int i = 0; i < executions; i++)
			{
				sum += executeGeneticAlgorithm(INITIAL_NUM_COMMUNITIES, suggested, INITIAL_CROSS_PROB, INITIAL_MUT_PROB, numGenerations, INITIAL_INTER_PROB);
			}
			
			double mean = sum / executions;
			
			if(mean < bestMean)
			{
				bestMean = mean;
				bestChromosomes = suggested;
			}
			
			System.out.println("Finished executions for "+suggested+" chromosomes. Mean: "+mean);
		}
		
		System.out.println("Suggested chromosomes: "+bestChromosomes);
		
		return bestChromosomes;
	}
	
	private static double findCrossProbability(double initialProb, double sumFactor, int probExecutions, int numGenerations, int numChromosomes) throws Exception{
		
		double factor = sumFactor;
		double max = 1;
		int executions = probExecutions;
		
		Double suggested = null;
		
		double bestProb = -1;
		double bestMean = 1;
		
		for(suggested = initialProb ; suggested <= max ; suggested += factor )
		{
			
			double sum = 0;
			
			for(int i = 0; i < executions; i++)
			{
				sum += executeGeneticAlgorithm(INITIAL_NUM_COMMUNITIES, numChromosomes, suggested, INITIAL_MUT_PROB, numGenerations, INITIAL_INTER_PROB);
			}
			
			double mean = sum / executions;
			
			if(mean < bestMean)
			{
				bestMean = mean;
				bestProb = suggested;
			}
			
			System.out.println("Finished executions for "+suggested+" cross probability. Mean: "+mean);
		}
		
		System.out.println("Suggested Cross Probability: "+bestProb);
		
		return bestProb;
	}

	private static double findMutationProbability(double initialProb, double sumFactor, int probExecutions, int numGenerations, int numChromosomes, double crossProb) throws Exception{
		
		double factor = sumFactor;
		double max = 0.1;
		int executions = probExecutions;
		
		Double suggested = null;
		
		double bestProb = -1;
		double bestMean = 1;
		
		for(suggested = initialProb ; suggested <= max ; suggested += factor )
		{
			
			double sum = 0;
			
			for(int i = 0; i < executions; i++)
			{
				sum += executeGeneticAlgorithm(INITIAL_NUM_COMMUNITIES, numChromosomes, crossProb, suggested, numGenerations, INITIAL_INTER_PROB);
			}
			
			double mean = sum / executions;
			
			if(mean < bestMean)
			{
				bestMean = mean;
				bestProb = suggested;
			}
			
			System.out.println("Finished executions for "+suggested+" mutation probability. Mean: "+mean);
		}
		
		System.out.println("Suggested Mutation Probability: "+bestProb);
		
		return bestProb;
	}
	
	private static double findInterProbability(double initialProb, double sumFactor, int probExecutions, int numGenerations, int numChromosomes, double crossProb, double mutProb) throws Exception{
		
		double factor = sumFactor;
		double max = 1;
		int executions = probExecutions;
		
		Double suggested = null;
		
		double bestProb = -1;
		double bestMean = 1;
		
		for(suggested = initialProb ; suggested <= max ; suggested += factor )
		{
			
			double sum = 0;
			
			for(int i = 0; i < executions; i++)
			{
				sum += executeGeneticAlgorithm(INITIAL_NUM_COMMUNITIES, numChromosomes, crossProb, mutProb, numGenerations, suggested);
			}
			
			double mean = sum / executions;
			
			if(mean < bestMean)
			{
				bestMean = mean;
				bestProb = suggested;
			}
			
			System.out.println("Finished executions for "+suggested+" inter probability. Mean: "+mean);
		}
		
		System.out.println("Suggested Inter Probability: "+bestProb);
		
		return bestProb;
	}
	
	private static HashMap<Integer, Double>  findCommunitiesNumber(int sumFactor, int communitiesExecutions, int numGenerations, int numChromosomes, double crossProb, double mutProb, double interProb) throws Exception{
		
		int factor = sumFactor;
		int max = 92;
		int executions = communitiesExecutions;
		
		Integer suggested = 2;
		
		boolean next = true;
		
		HashMap<Integer, Double> dictionary = new HashMap<Integer, Double>();
		
		while(next)
		{
			
			double sum = 0;
			
			for(int i = 0; i < executions; i++)
			{
				sum += executeGeneticAlgorithm(suggested, numChromosomes, crossProb, mutProb, numGenerations, interProb);
			}
			
			double mean = sum / executions;
			
			dictionary.put(suggested, mean);
			
			System.out.println("Finished executions for "+suggested+" communities. Mean: "+mean);
			
			if(suggested >= max)
			{
				next = false;
			}
			else
			{
				suggested += factor;
			}
			
			
		}
	
		
		return dictionary;
	}
	
	private static void saveCSV(HashMap<Integer, Double> dictionary, int suggestedGenerations, int suggestedChromosomes, double suggestedCrossProb, double suggestedMutProb, double suggestedInterProb) throws IOException
	{
		
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		
		String folderName = "CommunitiesNumber_"+dateFormat.format(date);
		
		File folder = new File((desktopPath + "\\" + folderName));
		folder.mkdirs();
		
		String fileName = "CommunitiesNumber_"+dateFormat.format(date)+".csv";
		
		File out = new File(folder.getAbsolutePath() + "\\" + fileName);
		
		out.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(out);
		 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
		boolean firstTimeTitle = true;
		
		for (Integer k:dictionary.keySet()) {
			
			if(firstTimeTitle)
			{
				bw.write("Communities;FitnessValue");
				firstTimeTitle = false;
			}
			else
			{
				bw.newLine();
				bw.write(k + ";" + dictionary.get(k));
			}
		}
		
		bw.flush();
		bw.close();
		
		//----------------------------------------------------------
		
		String fileName2 = "GeneticParameters"+dateFormat.format(date)+".txt";
		
		File out2 = new File(folder.getAbsolutePath() + "\\" + fileName2);
		
		out2.createNewFile();
		
		FileOutputStream fos2 = new FileOutputStream(out2);
		 
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));
	 
		bw2.write("Suggested number of generations: "+suggestedGenerations);
		bw2.newLine();
		bw2.write("Suggested number of chromosomes: "+suggestedChromosomes);
		bw2.newLine();
		bw2.write("Suggested cross probability: "+suggestedCrossProb);
		bw2.newLine();
		bw2.write("Suggested mutation probability: "+suggestedMutProb);
		bw2.newLine();
		bw2.write("Suggested inter probability: "+suggestedInterProb);
		
		bw2.flush();
		bw2.close();
	}
}
