import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;

//PRE: The program must read 3 files that contain integers and then sort them using radix sort. 
//It will count the runtime estimate, loops, comparisons, and swap counts for each sort and then 
//print the results to an output file.

//POST: The program outputs the results of the radix sort for each of the 3 files to an output file called "output.txt". 
//The results are formated in a table that includes the sort name, file name, # of comparisons, # of swaps, # of loops, 
//and the runtime estimate in nanoseconds.

public class Main{
    
    //PRE: The 3 files must exist and contain integers. They are then read.
    //POST: The 3 files are sorted using radix sort and the results are written in the otuput.txt.
    public static void main(String[] args) throws FileNotFoundException {
       
        //the integers are read from each file
        ArrayList<Integer> numbers1 = readFile("data1.txt");
        ArrayList<Integer> numbers2 = readFile("data2.txt");
        ArrayList<Integer> numbers3 = readFile("data3.txt");

        //Radix sort is performed and the results are stored in SortStats objects
        SortStats stats1 = radixSort(numbers1);
        SortStats stats2 = radixSort(numbers2);
        SortStats stats3 = radixSort(numbers3);

        //This labels each file based on the order they were read in and on the type of data they had.
        stats1.fileName = "1 (random)";
        stats2.fileName = "2 (ascending)";
        stats3.fileName = "3 (descending)";

        //This creates the output file
        PrintWriter output = new PrintWriter("output.txt");

        //This prints the header for the output file
        output.printf("%-15s %-20s %-15s %-10s %-10s %-15s\n",
            "Sort Name",
            "File Name",
            "Comparisons",
            "Swaps",
            "Loops",
            "Runtime Estimate (ns)");

        //This prints the count comparisons, loops, swaps, and track time for each file
        printStats(output, stats1);
        printStats(output, stats2);
        printStats(output, stats3);

        //Closes the output file
        output.close();

    }

    //PRE: The output file has to be open and stats must have the sorting statistics.
    //POST: the sorting statistics are written to the output file as a single row in the table.
    public static void printStats(PrintWriter output, SortStats stats) {
        output.printf("%-15s %-20s %-15s %-10s %-10s %-15s\n",
                stats.sortName,
                stats.fileName,
                stats.swaps,
                stats.comparisons,
                stats.loops,
                stats.timeNano);
    }
        
    //PRE: The file must exist and contain integers.
    //POST: An ArrayList of integers read from the file is returned.
    public static ArrayList<Integer> readFile(String fileName)
        throws FileNotFoundException {
        File file = new File(fileName);
        Scanner input = new Scanner(file);
        input.useDelimiter(",|\\s+");
        ArrayList<Integer> numbers = new ArrayList<>();
        while (input.hasNextInt()) {
            int number = input.nextInt();
            numbers.add(number);
        }
        input.close();
        return numbers;

        }

    //PRE: The list of integers that need to be sorted must be provided.
    //POST: The list is sorted using radix sort and the statistics are returned.
    public static SortStats radixSort(ArrayList<Integer> numbers) {

        //Creates an object that holds the sorting statistics.
        SortStats stats = new SortStats();
        stats.sortName = "Radix Sort";
        stats.comparisons = 0;
        stats.swaps = 0;
        stats.loops = 0;

        //starts the runtime estimate timer
        long startTime = System.nanoTime();

        //loops through the digits of the numbers, it starts with the least significant digit and goes to the most significant digit.
        //(sorts by ones, tens, hundreds, and thousands place)
        for (int place = 1; place <= 1000; place *= 10) {
            stats.loops++;
            //creates 10 buckets for each digit (0-9)
            ArrayList<ArrayList<Integer>> buckets = new ArrayList<>();
            
            for (int i = 0; i < 10; i++) {
                buckets.add(new ArrayList<Integer>());
            }
        
            //loops through the numbers and places them into a bucket based on the current digit. 
            for (int i = 0; i < numbers.size(); i++) {
                stats.loops++;
                int number = numbers.get(i);
                //finds the digit thats currently being sorted and places the number in the correct bucket.
                int digit = (number/place) % 10;
                buckets.get(digit).add(number);
                stats.swaps++;
            }

            //clears the original list and move the numbers from the buckets back into the Arraylist in the correct order.
            numbers.clear();

            for (int i = 0; i < 10; i++) {
                stats.loops++;
                for (int j = 0; j < buckets.get(i).size(); j++) {
                    stats.loops++;
                    numbers.add(buckets.get(i).get(j));
                    stats.swaps++;
                }
            }
        }

        //stops the runtime estimate timer and calculates the total runtime in nanoseconds. 
        long endTime = System.nanoTime();
        stats.timeNano = endTime - startTime;
        return stats;
    }
}

