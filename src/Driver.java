public class Driver{

    public static void CDF(String fileName){
        StdOut.setFile(fileName);

        int[] expectedWithCrits = {0,12,0,2,2,4,4,6,6,8,8,10,10,12,10,10,8,8,6,6,4,4,2,2,0};
        int probSuccess = 12; //to account for baseline probability that you crit
        double[] CDF = new double[25];
        StdOut.println("Sort of a Cumulative Distribution Function, which is typically P(X<k) for all possible k.");
        StdOut.println("This table demonstrates the probability that you succeed a skill check for a given DC.\n\n");
        StdOut.println("Let X and Y be random variables each representing the output of rolling a d12.");
        StdOut.println("This table represents the probability that you succeed a roll with a given DC: k, given by the expression:\nP(X+Y>=k) + P(X=Y)");
        StdOut.println("This means that you hit or surpass the DC or roll a critical success.\n\n");

        for(int i = 24; i>1; i--){
            probSuccess+=expectedWithCrits[i];
            CDF[i]=round2(probSuccess/144.0);
        }

        String bars = "";
        String row1 = "";
        String row2 = "";
        int cushionVar = 6;
        for(int i = 2; i<CDF.length; i++){

            String curr = "";
            curr+=CDF[i] + "%";
            int currMag=curr.length();

            bars+="|";
            row1+="|";
            row2+="|";

            for(int j = 1; j<=cushionVar+2; j++){   //each iteration, bars has cushionVar+3 characters
                bars+="_";
            }
            for(int j = 1; j<=cushionVar+2; j++){   //same idea here
                if(j==(cushionVar+2)/2){
                    row1+=i;
                    if(i>=10) j++;                  //if i has two characters, it skips an iteration for consistent padding
                }else {row1+=" ";}
            }
            for(int j = 0; j<cushionVar-currMag+3; j++){
                if(j==(cushionVar-currMag+3)/2){
                    row2+=curr; 
                }else {row2+=" ";}
            }
        }
        bars+="|";
        row1+="|";
        row2+="|";

        StdOut.println("P(X+Y>=k) + P(X=Y):");
        StdOut.println(bars.replace('|', '_'));
        StdOut.println(row1);
        StdOut.println(bars);
        StdOut.println(row2);
        StdOut.println(bars);
    }
    public static double round2(double a){
        int round = (int)(a*10000);
        return round/100.0;
    }
    public static void main(String[] args){
        DualRoll test = new DualRoll();
        String fileName1 = "d12_1.out";
        String fileName2 = "d12_2.out";
        String fileName3 = "2d12.out";
        if(test.conductDualityDieExperiment(1200, fileName1, fileName2)){
            System.out.println("both dice are valid");
        }
        test.printStats(fileName3);

        CDF("CDF.out");
    }
}