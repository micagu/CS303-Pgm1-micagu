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

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Integer> numbers1 = readFile("data1.txt");
        ArrayList<Integer> numbers2 = readFile("data2.txt");
        ArrayList<Integer> numbers3 = readFile("data3.txt");

        SortStats stats1 = radixSort(numbers1);
        SortStats stats2 = radixSort(numbers2);
        SortStats stats3 = radixSort(numbers3);

        stats1.fileName = "1 (random)";
        stats2.fileName = "2 (ascending)";
        stats3.fileName = "3 (descending)";

        PrintWriter output = new PrintWriter("output.txt");

        output.printf("%-15s %-20s %-15s %-10s %-10s %-15s\n",
            "Sort Name",
            "File Name",
            "Comparisons",
            "Swaps",
            "Loops",
            "Runtime Estimate (ns)");

        printStats(output, stats1);
        printStats(output, stats2);
        printStats(output, stats3);

        output.close();

    }

    public static void printStats(PrintWriter output, SortStats stats) {
        output.printf("%-15s %-20s %-15s %-10s %-10s %-15s\n",
                stats.sortName,
                stats.fileName,
                stats.swaps,
                stats.comparisons,
                stats.loops,
                stats.timeNano);
    }
        
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

    public static SortStats radixSort(ArrayList<Integer> numbers) {

        SortStats stats = new SortStats();
        stats.sortName = "Radix Sort";
        stats.comparisons = 0;
        stats.swaps = 0;
        stats.loops = 0;

        long startTime = System.nanoTime();

        for (int place = 1; place <= 1000; place *= 10) {
            stats.loops++;
            ArrayList<ArrayList<Integer>> buckets = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                buckets.add(new ArrayList<Integer>());
            }
        
            for (int i = 0; i < numbers.size(); i++) {
                stats.loops++;
                int number = numbers.get(i);
                int digit = (number/place) % 10;
                buckets.get(digit).add(number);
                stats.swaps++;
            }

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
        long endTime = System.nanoTime();
        stats.timeNano = endTime - startTime;
        return stats;
    }
}

