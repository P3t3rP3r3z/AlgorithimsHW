import java.util.Random;
import java.util.Scanner;

//This project uses some functionality as outlined in:
// http://www.java2s.com/Tutorial/Java/0140__Collections/Quicksortwithmedianofthreepartitioning.htm
// www.geeksforgeeks.org/insertion-sort

public class PeterPerezHW1 {

    private static long elapsedTime;
    private static int [] arrayToSort;
    private static long averageElapsedTime;

    public static void main(String[] args) {

        System.out.println("Please enter the size of the arrays to sort.");
        Scanner keyboard = new Scanner(System.in);
        int size= keyboard.nextInt();
        System.out.println("Please enter the amount of times to run the quicksort experiment.");
        int numberOfTimesToRun = keyboard.nextInt();
        System.out.println("Please enter 1 for median of three experiment, 2 for insertion sort.");
        int whichToRun = keyboard.nextInt();

        if(whichToRun == 1){
            for(int i=0; i<numberOfTimesToRun; i++){
                fillArray(size);
                medianOfThreeSort(arrayToSort);
                averageElapsedTime += elapsedTime;
            }
            System.out.println("Average time out of "+numberOfTimesToRun+" iterations: " + averageElapsedTime/10 + "ms \n\n");

            averageElapsedTime = 0;

            for(int i=0; i<numberOfTimesToRun; i++){
                fillArray(size);
                standardQuickSort(arrayToSort);
                averageElapsedTime += elapsedTime;
            }
            System.out.println("Average time out of "+numberOfTimesToRun+" iterations: "  + averageElapsedTime/10 + "ms\n\n");
        }

        if(whichToRun ==2){
            for(int i=0; i<numberOfTimesToRun; i++){
                fillArray(size);
                medianOfThreeSortWInsertionLimit(arrayToSort);
                averageElapsedTime += elapsedTime;
            }
            System.out.println("Average time out of "+numberOfTimesToRun+" iterations: " + averageElapsedTime/10 + "ms \n\n");

            averageElapsedTime = 0;

            for(int i=0; i<numberOfTimesToRun; i++){
                fillArray(size);
                standardQuickSort(arrayToSort);
                averageElapsedTime += elapsedTime;
            }
            System.out.println("Average time out of "+numberOfTimesToRun+" iterations: "  + averageElapsedTime/10 + "ms\n\n");
        }

    }

    private static void startTimer(){
        elapsedTime = System.nanoTime();
    }

    private static void endTimer(){
        long endTime = System.nanoTime();
        elapsedTime = endTime - elapsedTime;
    }

    private static void fillArray(int size){
        arrayToSort = new int[size];
        for(int i=0;i<arrayToSort.length;i++)
        {
            arrayToSort[i] = randomFill();
        }
    }

    private static int randomFill(){
        Random rand = new Random();
        return rand.nextInt(50);
    }

    private static void medianOfThreeSort(int[] unsorted){
        System.out.print("Unsorted array:");
        printArray(unsorted);
        startTimer();
        m3QuickSort(unsorted);
        endTimer();
        System.out.print("Sorted array using Median of Three:");
        printArray(unsorted);
        System.out.println("Time to Median of Three Sort: " + elapsedTime + "ms");
    }

    private static void medianOfThreeSortWInsertionLimit(int[] unsorted){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please enter sub-array size limit for insertion sort");
        int subSize = keyboard.nextInt();
        System.out.print("Unsorted array:");
        printArray(unsorted);
        startTimer();
        m3QuickSortWInsertion(unsorted, subSize);
        endTimer();
        System.out.print("Sorted array using quick sort with insertion sort cap:");
        printArray(unsorted);
        System.out.println("Time to Sort: " + elapsedTime + "ms");
    }

    private static void standardQuickSort(int[] unsorted){
        int left = 0;
        int right = unsorted.length-1;
        System.out.print("Unsorted array:");
        printArray(unsorted);
        startTimer();
        stdQuickSort(left, right, unsorted);
        endTimer();
        System.out.print("Sorted array using Standard Quick Sort:");
        printArray(unsorted);
        System.out.println("Time to Quick Sort: " + elapsedTime + "ms");
    }

    private static void stdQuickSort(int left, int right, int[] unsorted){
        if(left >= right)
            return;

        // For the simplicity, we took the right most item of the array as a pivot
        int pivot =unsorted[right];
        int partition = partition(left, right, pivot, unsorted);

        // Recursively, calls the quicksort with the different left and right parameters of the sub-array
        stdQuickSort(0, partition-1,unsorted);
        stdQuickSort(partition+1, right, unsorted);

    }

    private static int partition(int left,int right,int pivot, int[] unsorted){
        int leftCursor = left-1;
        int rightCursor = right;
        while(leftCursor < rightCursor){
            while(unsorted[++leftCursor] < pivot);
            while(rightCursor > 0 && unsorted[--rightCursor] > pivot);
            if(leftCursor >= rightCursor){
                break;
            }else{
                swap(unsorted, leftCursor, rightCursor);
            }
        }
        swap(unsorted, leftCursor, right);
        return leftCursor;
    }

    private static void partitionAlgorithExperiment(int[] unsorted){
        startTimer();

        endTimer();
        System.out.println("Time to Median of Three Sort: "+ elapsedTime);
    }

    private static void m3QuickSort(int[] intArray) {
        recursiveQuickSort(intArray, 0, intArray.length - 1);
    }

    private static void m3QuickSortWInsertion(int[] intArray, int subSize) {
        recursiveQuickSortWInsertion(intArray, 0, intArray.length - 1, subSize);
    }

    private static void recursiveQuickSort(int[] intArray, int left, int right) {
        int size = right - left + 1;
        if (size <= 3)
            manualSort(intArray, left, right);
        else {
            double median = medianOf3(intArray, left, right);
            int partition = partitionIt(intArray, left, right, median);
            recursiveQuickSort(intArray, left, partition - 1);
            recursiveQuickSort(intArray, partition + 1, right);
        }
    }

    private static void recursiveQuickSortWInsertion(int[] intArray, int left, int right, int subSize) {
        int size = right - left + 1;
        if (size <= 3)
            manualSort(intArray, left, right);
        else {
            double median = medianOf3WithInsertionSort(intArray, left, right, subSize);
            int partition = partitionIt(intArray, left, right, median);
            recursiveQuickSortWInsertion(intArray, left, partition - 1, subSize);
            recursiveQuickSortWInsertion(intArray, partition + 1, right, subSize);
        }
    }

    private static int medianOf3(int[] intArray, int left, int right) {
        int center = (left + right) / 2;

        if (intArray[left] > intArray[center])
            swap(intArray, left, center);

        if (intArray[left] > intArray[right])
            swap(intArray, left, right);

        if (intArray[center] > intArray[right])
            swap(intArray, center, right);

        swap(intArray, center, right - 1);
        return intArray[right - 1];
    }

    private static int medianOf3WithInsertionSort(int[] intArray, int left, int right, int subSize) {
        int center = (left + right) / 2;

        if(center <= subSize){
            insertionSort(intArray);
        }

        if (intArray[left] > intArray[center])
            swap(intArray, left, center);

        if (intArray[left] > intArray[right])
            swap(intArray, left, right);

        if (intArray[center] > intArray[right])
            swap(intArray, center, right);

        swap(intArray, center, right - 1);
        return intArray[right - 1];
    }

    private static void insertionSort(int[] intArray) {
        int n = intArray.length;
        for (int i = 1; i < n; ++i) {
            int key = intArray[i];
            int j = i - 1;

            while (j >= 0 && intArray[j] > key) {
                intArray[j + 1] = intArray[j];
                j = j - 1;
            }
            intArray[j + 1] = key;
        }
    }

    private static void swap(int[] intArray, int dex1, int dex2) {
        int temp = intArray[dex1];
        intArray[dex1] = intArray[dex2];
        intArray[dex2] = temp;
    }

    private static int partitionIt(int[] intArray, int left, int right, double pivot) {
        int leftPtr = left;
        int rightPtr = right - 1;

        while (true) {
            while (intArray[++leftPtr] < pivot)
                ;
            while (intArray[--rightPtr] > pivot)
                ;
            if (leftPtr >= rightPtr)
                break;
            else
                swap(intArray, leftPtr, rightPtr);
        }
        swap(intArray, leftPtr, right - 1);
        return leftPtr;
    }

    private static void manualSort(int[] intArray, int left, int right) {
        int size = right - left + 1;
        if (size <= 1)
            return;
        if (size == 2) {
            if (intArray[left] > intArray[right])
                swap(intArray, left, right);
            return;
        } else {
            if (intArray[left] > intArray[right - 1])
                swap(intArray, left, right - 1);
            if (intArray[left] > intArray[right])
                swap(intArray, left, right);
            if (intArray[right - 1] > intArray[right])
                swap(intArray, right - 1, right);
        }
    }

    private static void printArray(int[] arrayToPrint){
        int n = arrayToPrint.length;
        System.out.print("\tarray values are = [ ");
        for (int i1 : arrayToPrint) {

            System.out.print(i1 + " ");

        }
        System.out.print("]\n");
    }
}
