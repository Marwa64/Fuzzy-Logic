import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


class Point {
    private int x;
    private int y;
    
    public void set_x(int _x) { x = _x; }
    public void set_y(int _y) { y = _y; }
    
    public int get_x() { return x; }
    public int get_y() { return y; }
}

public class FuzzyLogic {
	
	// Membership function
	public static List<Double> fuzzification(List<List<Integer>> fuzzySets, int input) {
		List<Double> memberships = new ArrayList<Double>();

		for (int j = 0; j < fuzzySets.size(); j++) {
			for (int i = fuzzySets.get(j).size()-1; i > 0; i--) {
				// If the exact input is in the set
				if (input == fuzzySets.get(j).get(i) && i != fuzzySets.get(j).size()-1) {
					memberships.add(1.0);
					break;
				// If the input is found in a range between 2 values in the set
				} else if (input > fuzzySets.get(j).get(i-1) && input < fuzzySets.get(j).get(i)) {
					Point point1 = new Point();
					Point point2 = new Point();
					
					if (i-1 != fuzzySets.get(j).size()-1 && i-1 != 0) {
						point1.set_x(fuzzySets.get(j).get(i-1));
						point1.set_y(1);
					} else {
						point1.set_x(fuzzySets.get(j).get(i-1));
						point1.set_y(0);
					}
					
					if (i != fuzzySets.get(j).size()-1 && i != 0) {
						point2.set_x(fuzzySets.get(j).get(i));
						point2.set_y(1);
					} else {
						point2.set_x(fuzzySets.get(j).get(i));
						point2.set_y(0);
					}
					// Line Equation
					double m = (double)(point2.get_y() - point1.get_y()) / (point2.get_x() - point1.get_x());
					double c = point2.get_y() - (m * point2.get_x());
					double membership = (m * input) + c;
					memberships.add(membership);
					break;
				// If the input is not in the set at all
				} else if (i == 1) {
					memberships.add(0.0);
				}
			}
		}
		return memberships;
	}
	
	
	// Applying the rules
	public static List<Double> inference(List<Double> funding, List<Double> experience) {
		List<Double> fuzzyValues = new ArrayList<Double>();
		
		fuzzyValues.add(Math.max(funding.get(3), experience.get(2))); // Low
		fuzzyValues.add(Math.max(Math.min(funding.get(2), experience.get(1)), experience.get(0))); // Normal
		fuzzyValues.add(Math.max(funding.get(0), Math.min(funding.get(1), experience.get(0)))); // High
		
		return fuzzyValues;
	}
	
	// Predict the output using the Weighted Average Method
	public static void defuzzification(List<List<Integer>> risk, List<Double> fuzzyValues) {
		List<Double> centroids = new ArrayList<Double>();
		double centroid;
		for (int i = 0; i < risk.size(); i++) {
			double sum = 0;
			for (int j = 0; j < risk.get(i).size(); j++) {
				sum += risk.get(i).get(j);
			}
			centroid = sum / risk.get(i).size();
			centroids.add(centroid);
		}
		
		double top = 0, bottom = 0, max_membership = -1;
		int max_index = -1;
		String risk_type = "";
		
		for (int i = 0; i < fuzzyValues.size(); i++) {
			top += (fuzzyValues.get(i) * centroids.get(i));
			bottom += fuzzyValues.get(i);
			if (max_membership < fuzzyValues.get(i)) {
				max_membership = fuzzyValues.get(i);
				max_index = i;
			}
		}
		double crisp = top / bottom;
		System.out.println("Predicted Value (Risk) = " + crisp);
		
		switch(max_index) {
			case 0:
				risk_type = "Low";
				break;
			case 1:
				risk_type = "Normal";
				break;
			case 2:
				risk_type = "High";
				break;
		}
		System.out.println("Risk will be " + risk_type);
	}
	
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int fundingInput, experienceInput;
		
		System.out.print("Project Fund: ");
		fundingInput = in.nextInt();
		System.out.print("Experience Level: ");
		experienceInput = in.nextInt();
		
		System.out.println("\n------------------------\n");
		
		List<Integer> veryLow = Arrays.asList(0, 0, 10, 30);
		List<Integer> low = Arrays.asList(10, 30, 40, 60);
		List<Integer> medium = Arrays.asList(40, 60, 70, 90);
		List<Integer> high = Arrays.asList(70, 90, 100, 100);
		
		List<List<Integer>> funding = Arrays.asList(veryLow, low, medium, high);
		
		List<Integer> beginner = Arrays.asList(0, 15, 30);
		List<Integer> intermediate = Arrays.asList(10, 30, 45);
		List<Integer> expert = Arrays.asList(30, 60, 60);
		
		List<List<Integer>> experience = Arrays.asList(beginner, intermediate, expert);
		
		List<Double> funding_memberships = fuzzification(funding, fundingInput);
		List<Double> experience_memberships = fuzzification(experience, experienceInput);
		
		List<Double> fuzzyValues = inference(funding_memberships, experience_memberships);
		
		List<Integer> risk_low = Arrays.asList(0, 25, 50);
		List<Integer> risk_normal = Arrays.asList(25, 50, 75);
		List<Integer> risk_high = Arrays.asList(50, 100, 100);
		
		List<List<Integer>> risk = Arrays.asList(risk_low, risk_normal, risk_high);
		
		defuzzification(risk, fuzzyValues);
	}
	
}
