public class D12{

    int[] frequency;
    int numRolls;
    double expectedVal;
    double experimentalChi;
    private static final double CRIT_VALUE_CHI = 19.675; //11 degrees of freedom for p<0.05

    //ititializer
    public D12(){
        this.frequency= new int[13]; //int array with indices 0-12, do not use index 0. Try to roll a 0 on a d12, like come on
        this.expectedVal = 0.0; //the expected value of rolling any distinct number on a d12 is uniform
        this.numRolls = 0;
    }


    //getters
    public int[] getFrequencyArray(){
        return this.frequency;
    }

    public int getNumRolls(){
        return this.numRolls;
    }

    public double getExpectedVal(){
        return this.expectedVal;
    }

    public double getExperimentalChiVal(){
        return experimentalChi;
    }


    
    //operators
    public int makeRoll(){
        int roll = (int)(12*Math.random() + 1); //generates a random number between 1 and 12

        numRolls++;
        frequency[roll]++; //updates relevant values

        return roll;
    }
    public void conductExperiment(int rollsToMake){
        for(int i = 0; i<rollsToMake; i++) makeRoll();
    }

    public boolean chiSquaredTest(String fileName){
        calculateExpectedValue();

        double chiSum = 0;
        for(int i = 1; i<frequency.length; i++){
            chiSum+=((frequency[i]-expectedVal)*(frequency[i]-expectedVal))/expectedVal;
        }
        this.experimentalChi=chiSum;
        printStats(fileName);
        return experimentalChi<CRIT_VALUE_CHI;
    }

    public void calculateExpectedValue(){
        this.expectedVal=numRolls/12.0; //takes the number of rolls and divides by 12, as the number of rolls for each number on a fair d12 is evenly distributed
    }

    public void printStats(String fileName){

        StdOut.setFile(fileName);

        String bars = "";
        String row1 = "";
        String row2 = "";
        int cushionVar = (int)Math.log10(maxVal(frequency))+1;
        for(int i = 1; i<frequency.length; i++){

            int currMag = (int)Math.log10(frequency[i])+1;

            bars+="|";
            row1+="|";
            row2+="|";

            for(int j = 1; j<=cushionVar+2; j++){   //each iteration, bars has cushionVar+3 characters
                bars+="_";
            }
            for(int j = 1; j<=cushionVar+2; j++){   //same idea here
                if(j==(cushionVar+3)/2){
                    row1+=i;
                    if(i>=10) j++;                  //if i has two characters, it skips an iteration for consistent padding
                }
                else {
                    row1+=" ";
                }
            }
            for(int j = 0; j<cushionVar-currMag+3; j++){
                if(j==(cushionVar-currMag+3)/2){
                    row2+=frequency[i]; 
                }else {row2+=" ";}
            }
        }
        bars+="|";
        row1+="|";
        row2+="|";

        StdOut.println("Rolls for 2d12, NOT counting critical hits separately:");
        StdOut.println(bars.replace('|', '_'));
        StdOut.println(row1);
        StdOut.println(bars);
        StdOut.println(row2);
        StdOut.println(bars);
        StdOut.println("\n\n\n");

        StdOut.print("Chi-Squared test on this d12:\n");
        StdOut.println("For 11 degrees of freedom and a p value of 0.05, the critical value is " + CRIT_VALUE_CHI + ".\n\n");
        StdOut.println("The calculated Chi-Squared Value is " + experimentalChi + "\n");
        if(experimentalChi<CRIT_VALUE_CHI){
            StdOut.println("Since the calculated Chi-Squared value is less than the critical value, it can be said with confidence that this accurately simulates a d12.");
        } else {
            StdOut.println("SIMULATION FAILURE");
            StdOut.println("Since the calculated Chi-Squared value is greater than the critical value, it can not be said with confidence that this accurately simulates a d12.");
        }

    }
    //helper method for printStats()
    public int maxVal(int[] arr){
        int max = arr[0];
        for(int i = 0; i<arr.length; i++){
            if(arr[i]>max){max=arr[i];}
        }
        return max;
    }
}