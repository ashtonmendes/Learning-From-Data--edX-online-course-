package algorithms;

import java.util.ArrayList;
import java.util.Random;

class DataPoint
{
	double x0;
	double x1;
	double x2;	
	int result;
	
	public DataPoint(double x1, double x2, int result) 
	{
		super();
		this.x0 = 1;
		this.x1 = x1;
		this.x2 = x2;
		this.result = result;
	}
}
class Line
{
	double m;
	double c;
	
	public Line(double m, double c)
	{
		this.m = m;
		this.c = c;
	}
}

public class PerceptronLearningAlgorithm
{
	static Line targetFunction;
	public static DataPoint[] generateTrainingSet(int N)
	{
		//choose any random line as the target function
		double X1 = Math.random()*assignRandomSign();
		double Y1 = Math.random()*assignRandomSign();
		double X2 = Math.random()*assignRandomSign();
		double Y2 = Math.random()*assignRandomSign();
		
		double m = (Y2-Y1)/(X2-X1);
		double c = Y1 - m*X1;
		
		targetFunction = new Line(m,c);
		
		System.out.println("Target Function: y = "+targetFunction.m+"*x + "+targetFunction.c);
		
		DataPoint trainingSet[] = new DataPoint[N];
		
		for(int i=0; i<N; i++)
		{
			double x = Math.random()*assignRandomSign();
			double y = Math.random()*assignRandomSign();
			
			trainingSet[i] = new DataPoint(x, y, classifyPoint(x, y, targetFunction));
		}
		
		return trainingSet;
	}
	
	public static int classifyPoint(double x, double y, Line targetFunction)
	{
		double temp = (y-targetFunction.c)/targetFunction.m;
		if(x>temp)
			return 1;
		if(x<temp)
			return -1;
		if(x==temp)
			return assignRandomSign();
		return 0;
	}
	
	public static int assignRandomSign()
	{
		switch(new Random().nextInt(2))
		{		
			case 1:
			{
				return -1;				
			}
			case 0:
			default:
			{
				return 1;
			}
		}
	}
	
	public static void main(String[] args) {	
		
		int N = 100;
		int loopLimit = 1000;
		DataPoint trainingSet[];
		double[] weight;
		ArrayList<DataPoint> misclassified;
		Random randomNo = new Random();
		double condition=0;
		int classifiedWrongly = 0;
		double iterations = 0;
		double disagreement = 0;		
		int loop = 0;
		
		while(loop<loopLimit)
		{
		loop++;
		
		System.out.println(loop);
			
		trainingSet = generateTrainingSet(N);
		
		System.out.println("******************\nTraining Set Points:");
		for(DataPoint point: trainingSet)
			System.out.println(point.x1+","+point.x2);
		System.out.println("******************");	
        		
		weight = new double[3];
		weight[0] = 0;
		weight[1] = 0;
		weight[2] = 0;
		
		misclassified = new ArrayList<DataPoint>();
		
		for (DataPoint point : trainingSet) {
			misclassified.add(point);
		}	
		
		//PLA	
		condition = 0;
		while(!misclassified.isEmpty())
		{		
			iterations++;
			
			int pick = randomNo.nextInt(misclassified.size());			
			
			DataPoint point = misclassified.get(pick);		
			
			condition = weight[0]*point.x0 + weight[1]*point.x1 + weight[2]*point.x2;
			
			if(condition > 0 && point.result == -1)
			{
				weight[0] -= point.x0;
				weight[1] -= point.x1;
				weight[2] -= point.x2;
			}
			else if(condition < 0 && point.result == 1)
			{
				weight[0] += point.x0;
				weight[1] += point.x1;
				weight[2] += point.x2;
			}	
			else if(condition == 0)
			{
				switch(new Random().nextInt(2))
				{
					case 0:
					{
						weight[0] -= point.x0;
						weight[1] -= point.x1;
						weight[2] -= point.x2;
						break;
					}
					case 1:
					{
						weight[0] += point.x0;
						weight[1] += point.x1;
						weight[2] += point.x2;
						break;
					}
				}
			}
				
			misclassified.removeAll(misclassified);					
			
			for (int i = 0; i < trainingSet.length; i++)
			{	
				condition = weight[0]*trainingSet[i].x0 + weight[1]*trainingSet[i].x1 + weight[2]*trainingSet[i].x2;
				
				if( (condition > 0 && trainingSet[i].result == -1) ||
					(condition < 0 && trainingSet[i].result == 1) ||
					(condition == 0) )
				{
					misclassified.add(trainingSet[i]);
				}
			}
		}
				
		System.out.println("y = "+ weight[1]/weight[2] + "* x  +  "+ weight[0]/weight[2]);
		
		Line finalHypothesis = new Line(weight[1]/weight[2], weight[0]/weight[2]);		 
		for(int i=0; i<trainingSet.length; i++)
		{
			if(classifyPoint(trainingSet[i].x1, trainingSet[i].x2, targetFunction) != classifyPoint(trainingSet[i].x1, trainingSet[i].x2, finalHypothesis))
			{
				classifiedWrongly++;
			}
		}
		
		}		
		
		System.out.println("Avg. No. Of Iterations : "+ iterations/(double)loopLimit);
		disagreement = (double)classifiedWrongly/(double)(N*loopLimit);
		System.out.println("Avg. Probability of disagreement : ("+classifiedWrongly+"/"+(N*loopLimit)+") = "+disagreement);
	}
}
