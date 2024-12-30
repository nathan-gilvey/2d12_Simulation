public class DualRoll{
    D12 X;
    D12 Y;
    int numRolls;
    double critValueChi_21 = 32.671; //21 degrees of freedom for 0.05 WITH CRITS && WITHOUT ROLLING 2 OR 4
    double critValueChi_22 = 33.924; //22 degrees of freedom for 0.05
    double experimentalChi;
    double experimentalChiWithCritHits;
    int[] freq;
    int[] freqWithCrits;
    int[] expected = {0,0,1,2,3,4,5,6,7,8,9,10,11,12,11,10,9,8,7,6,5,4,3,2,1};
    int[] expectedWithCrits = {0,12,0,2,2,4,4,6,6,8,8,10,10,12,10,10,8,8,6,6,4,4,2,2,0};
    /**
     *  @param freq -- tracks how many times the sum of each number 2-24 is rolled as the sum of the output of 2d12
     *  @param freqWithCrits -- does mostly the same thing as freq[], but instead when a critical success is rolled, it is added to index 1
     *      *Results of each 2d12 will be entered into each array at the index equal to the result of the roll
     * 
     *  @param expected
     *  @param expectedWithCrits
     *      *Each hold the expected number of times each number is produced as the output of rolling 2d12 and taking the sum of the results
     *      *The number at each index an integer equal to 144*P(X=k)
     *          *In other words, the expected number of times that roll occurs among 144 rolls
     *          *The integers in this array will be scaled to use for later statistical analysis in a Chi-Squared test
     *      *expected[] and expectedWithCrits[] are parallel to freq[] and freqWithCrits[] respectively, with each index corresponding to the same possible 2d12 roll output
     */

    //constructor
    public DualRoll(){
        this.X = new D12();
        this.Y = new D12();
        this.freq = new int[25];
        this.freqWithCrits = new int[25];
        this.experimentalChi = 0.0;
        this.experimentalChiWithCritHits = 0.0;
        this.numRolls=0;
    }

    //getters
    public D12 getX(){
        return this.X;
    }

    public D12 getY(){
        return this.Y;
    }

    public int[] getFrequencyArray(){
        return this.freq;
    }
    
    public int[] getFrequencyArrayWithCrits(){
        return this.freqWithCrits;
    }

    public int getNumRolls(){
        return this.numRolls;
    }

    public int[] getExpectedArray(){
        return this.expected;
    }

    public int[] getExpectedArrayWithCrits(){
        return this.expectedWithCrits;
    }

    //operators
    public boolean conductDualityDieExperiment(int rollsToMake, String fileName1, String fileName2){
        for(int i = 0; i<rollsToMake; i++){
            //makes the rolls and calculates the sum
            int x = X.makeRoll();
            int y = Y.makeRoll();
            int sum = x+y;

            //updates frequency arrays
            freq[sum]++;
            if(x==y) freqWithCrits[1]++; //if x=y, it's a crit, therefore freqWithCrits[] at index 1 should increase by 1
            else freqWithCrits[sum]++;

            //iterates numRolls
            numRolls++;
        }
        return X.chiSquaredTest(fileName1) && Y.chiSquaredTest(fileName2);
    }

    public void printStats(String fileName){
        StdOut.setFile(fileName);


        //print the freq table
        String bars = "";
        String row1 = "";
        String row2 = "";
        int cushionVar = (int)Math.log10(maxVal(freq))+1;
        for(int i = 2; i<freq.length; i++){

            int currMag = (int)Math.log10(freq[i])+1;

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
                }else {row1+=" ";}
            }
            for(int j = 0; j<cushionVar-currMag+3; j++){
                if(j==(cushionVar-currMag+3)/2){
                    row2+=freq[i]; 
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


        boolean success = conductChiSquaredTestWithoutCritHits();

        StdOut.println("\n\nChi-Squared test on 2d12 WITHOUT counting critical successes separately:\n");
        StdOut.println("For 23 possible outputs, there are 22 degrees of freedom and a p value of 0.05, the critical value is " + critValueChi_22 + ".\n\n");
        StdOut.println("The calculated Chi-Squared Value is " + experimentalChi + "\n");

        if(success){
            StdOut.println("Since the calculated Chi Squared value is less than the critical value, this simulation accurately represents the output of rolling 2d12");
        } else {
            StdOut.println("SIMULATION FAILURE");
            StdOut.println("Since the calculated Chi Squared value is greater than the critical value, this simulation does not accurately represent the output of rolling 2d12");
        }


        StdOut.println("\n\n\n\n");

        bars = "|_______________";
        row1 = "| CRITICAL HITS ";
        row2 = "|";
        cushionVar = (int)Math.log10(maxVal(freqWithCrits))+1;
        int critMag = (int)Math.log10(freqWithCrits[1])+1;
        for(int j = 0; j<16-critMag; j++){
            if(j==(16-critMag)/2){
                row2+=freqWithCrits[1]; 
            }else {row2+=" ";}
        }
        for(int i = 2; i<freqWithCrits.length; i++){

            int currMag = 1;
            if(freqWithCrits[i]!=0) currMag+=(int)Math.log10(freqWithCrits[i]);

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
                }else {row1+=" ";}
            }
            for(int j = 0; j<cushionVar-currMag+3; j++){
                if(j==(cushionVar-currMag+3)/2){
                    if(freqWithCrits[i]!=0)row2+=freqWithCrits[i]; 
                    else row2+=0;
                }else {row2+=" ";}
            }
        }
        bars+="|";
        row1+="|";
        row2+="|";

        StdOut.println("Rolls for 2d12, counting critical hits separately:");
        StdOut.println(bars.replace('|', '_'));
        StdOut.println(row1);
        StdOut.println(bars);
        StdOut.println(row2);
        StdOut.println(bars);

        success = conductChiSquaredTestWithCritHits();

        StdOut.println("\n\nChi-Squared test on 2d12 counting critical successes separately:\n");
        StdOut.println("For 22 possible outputs, there are 21 degrees of freedom and a p value of 0.05, the critical value is " + critValueChi_21 + ".\n\n");
        StdOut.println("The calculated Chi-Squared Value is " + experimentalChiWithCritHits + "\n");

        if(success){
            StdOut.println("Since the calculated Chi Squared value is less than the critical value, this simulation accurately represents the output of rolling 2d12");
        } else {
            StdOut.println("SIMULATION FAILURE");
            StdOut.println("Since the calculated Chi Squared value is greater than the critical value, this simulation does not accurately represent the output of rolling 2d12");
        }

    }
    //helper for printStats
    public int maxVal(int[] arr){
        int max = arr[0];
        for(int i = 0; i<arr.length; i++){
            if(arr[i]>max){max=arr[i];}
        }
        return max;
    }

    public boolean conductChiSquaredTestWithoutCritHits(){
        for(int i = 2; i<freq.length; i++){
            double expectation= ((expected[i]*numRolls)/144.0);
            experimentalChi+=((freq[i]-expectation)*(freq[i]-expectation))/expectation;
        }
        return experimentalChi<critValueChi_22; //22 degrees of freedom for 23 possible outputs
    }

    public boolean conductChiSquaredTestWithCritHits(){
        for(int i = 1; i<freqWithCrits.length-1; i++){
            if(i==2) continue;
            double expectation = ((expectedWithCrits[i]*numRolls)/144.0);
            experimentalChiWithCritHits+=((freqWithCrits[i]-expectation)*(freqWithCrits[i]-expectation))/expectation;
        }
        return experimentalChiWithCritHits<critValueChi_21; //21 degrees of freedom for 22 possible outputs
    }


}