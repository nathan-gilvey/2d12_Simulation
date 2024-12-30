
Class File Descriptions:

    StdIn.java and StdOut.java{
        Provided by Princeton University, credit to the authors included in files.
    }

    d12.java{
        Performs simulated random rolls of a 12-sided die (henceforth referred to as a d12).
        Internally counts the frequency of each possible output.
        Performs a Chi-Squared Test of Goodness of Fit under the assumtion that the probability of rolling any given number is uniformly distributed.

        Prints data into a dynamically formatted table.
        Prints the results of Chi-Squared test.
    }

    dualRoll.java{
        Performs simulated random rolls of two 12-sided die (henceforth referred to as 2d12), the result of which is the sum of both d12 outputs.
            Employs the use of the d12 datatype
        Internally counts the frequency of all possible output in two arrays:
            Frequency array where only possible outputs 2-24 are counted.
            Frequency array which counts possible outputs 2-24 in which both d12 do not roll the same number, 
                and counts the events in which both d12 DO roll the same number (henceforth referred to as a critical success) separately.
        Performs a Chi-Sqared Test of Goodness of Fit on both arrays, each under a unique assumption about their respective distributions.

        Prints data from both frequency arrays into a dynamically formatted table.
        Prints the results of the Chi-Squared tests.
    }

    driver.java{
        Simulates an experiment using the dualRoll datatype:
            Roll 2d12 Z number of times and keep track of each outcome.

            Print the experimental distribution for each indivual d12.
            Verify that each d12 fits the distribution of a fair d12 with a Chi-Squared Test.

            Print the experimental distribution of 2d12.
            Verify that this 2d12 fits the distribution of the sum of two d12's expected output.

        Calculate and print a modified Cumulative Distribution Function for succeeding a "skill check":
            Succeeding a skill check requires one of two conditions being met: 
                The output of rolling 2d12 meets or exceeds some prescribed number, k.
                d12_1 and d12_2 output the same number, which constitutes a "critical success"
    }