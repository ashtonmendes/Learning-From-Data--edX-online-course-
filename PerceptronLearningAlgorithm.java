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
	static Random randomNo = new Random();

	public static void generateTargetFunction()
	{
		//choose any random line as the target function
		double X1 = randomNo.nextDouble()*assignRandomSign();
		double Y1 = randomNo.nextDouble()*assignRandomSign();
		double X2 = randomNo.nextDouble()*assignRandomSign();
		double Y2 = randomNo.nextDouble()*assignRandomSign();
				
		double m = (Y2-Y1)/(X2-X1);
		double c = Y1 - m*X1;
				
		targetFunction = new Line(m,c);
				
		System.out.println("Target Function: y = "+targetFunction.m+"*x + "+targetFunction.c);
	}
	
	public static DataPoint[] generateTrainingSet(int N)
	{	
		DataPoint trainingSet[] = new DataPoint[N];
		
		for(int i=0; i<N; i++)
		{
			double x = randomNo.nextDouble()*assignRandomSign();
			double y = randomNo.nextDouble()*assignRandomSign();
			
			trainingSet[i] = new DataPoint(x, y, classifyPoint(x, y, targetFunction));
		}
		return trainingSet;
	}
		
	public static int classifyPoint(double x, double y, Line targetFunction)
	{
			double temp = (y - targetFunction.c) / targetFunction.m;
			if (x > temp)
				return 1;
			else
				return -1;				
	}
		
	public static int assignRandomSign()
	{
			switch (randomNo.nextInt(2))
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
	
	public static void main(String[] args) 
	{ 
		int N = 10;
		int loopLimit = 100;
		DataPoint trainingSet[];
		double[] weight;
		ArrayList<DataPoint> misclassified;
	    double condition=0;
		double classifiedWrongly = 0.0;
		double iterations = 0;
		double disagreement = 0; 
		int loop = 0;
		
		while(loop<loopLimit)
		{
			loop++;
				
			generateTargetFunction();
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
			for (DataPoint point : trainingSet)
			{
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
				
				int calculatedResult = 0;
				
				if(condition > 0)
					calculatedResult = 1;
				else
					calculatedResult = -1;
				 
				if(calculatedResult != point.result)
				{
					weight[0] +=  (point.result * point.x0);
					weight[1] +=  (point.result * point.x1);
					weight[2] +=  (point.result * point.x2);
				}				
				
				misclassified.removeAll(misclassified); 
				
				for (int i = 0; i < trainingSet.length; i++)
				{ 
					condition = weight[0]*trainingSet[i].x0 + weight[1]*trainingSet[i].x1 +	weight[2]*trainingSet[i].x2;
									
					if(condition > 0)
						calculatedResult = 1;
					else
						calculatedResult = -1;
					 
					if(calculatedResult != trainingSet[i].result)
					{
						misclassified.add(trainingSet[i]);
					}					
				}
			}
			
			System.out.println("Final Hypothesis: y = "+ (-1*weight[1])/weight[2] + "* x  +  "+ (-1*weight[0])/weight[2]);
					 
			 //Test on 1000 random points
			DataPoint[] test = new DataPoint[1000]; 
			
			test = generateTrainingSet(1000);			

			double count = 0.0;
			
			for(int k1 = 0; k1 < test.length; k1++)
			{
				double dotProd = (weight[0] + (weight[1] * test[k1].x1) + (weight[2] * test[k1].x2));
				double calc = 0.0;
				if(dotProd>0)
					calc = 1.0;
				else
					calc = -1.0;
				if(test[k1].result != calc)
					count++; 
			 }
			
			classifiedWrongly += (count/1000.0);		
			
		 } 
		 
		System.out.println("Avg. No. Of Iterations : "+	iterations/(double)loopLimit);
		disagreement = (double)classifiedWrongly/(double)(loopLimit);
		System.out.println("Avg. Probability of disagreement : ("+classifiedWrongly+"/"+(loopLimit)+") = "+disagreement);
		}
}
