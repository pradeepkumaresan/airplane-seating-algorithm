package com.airplane.seating.services;

import dto.MatrixItem;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SeatingService {

    /**
     * gets seating arrangement in the form of a list and number of passengers from UI
     * prints the numbers in seating arrangement in java console
     * @param seatingArrangement
     * @param numberOfPassengers
     * @return
     */
    public MatrixItem[][] seat(
            List<Integer> seatingArrangement,
            Integer numberOfPassengers) {

        List<List<Integer>> masterList = new ArrayList<>();
        int maxColumns = getMaxColumnsAndPopulateMasterList(seatingArrangement, masterList);

        Map<Integer, List<Integer>> maxDimensionsOfASection =
                populateMaxDimensionsMap(masterList, maxColumns);

        MatrixItem[][] matrices = new MatrixItem[4][12];

        List<MatrixItem> aisleList = new ArrayList<>();
        List<MatrixItem> windowList = new ArrayList<>();
        List<MatrixItem> centerList = new ArrayList<>();

        for(int currentRow=0; currentRow<matrices.length; currentRow++) {
            int previousSectionColumnLength = 0;
            boolean isStartingAisleFlag = false;
            for(int currentColumn=0; currentColumn<matrices[currentRow].length; currentColumn++) {
                MatrixItem item = new MatrixItem();
                matrices[currentRow][currentColumn] = item;
                int maxRowsForThisSection = maxDimensionsOfASection.get(currentColumn).get(1);
                int maxColumnsForThisSection = maxDimensionsOfASection.get(currentColumn).get(0);
                boolean isEndingAisle =
                        (currentColumn + 1) - previousSectionColumnLength == maxColumnsForThisSection;
                if (currentRow < maxRowsForThisSection) {
                    item.setValidCell(true);
                    if (currentColumn == 0 || currentColumn == maxColumns-1){
                        item.setPosition("window");
                        windowList.add(item);
                    }
                    else if(isEndingAisle){
                        previousSectionColumnLength = previousSectionColumnLength + maxColumnsForThisSection;
                        item.setPosition("aisle");
                        isStartingAisleFlag = true;
                        aisleList.add(item);
                    }
                    else if(isStartingAisleFlag) {
                        item.setPosition("aisle");
                        isStartingAisleFlag = false;
                        aisleList.add(item);
                    }
                    else {
                        item.setPosition("center");
                        centerList.add(item);
                    }
                }
                else {
                    item.setValidCell(false);
                    if(isEndingAisle) {
                        previousSectionColumnLength = previousSectionColumnLength + maxColumnsForThisSection;
                        isStartingAisleFlag = true;
                    }
                }
            }
        }

        addPassengersToSeats(numberOfPassengers, aisleList, windowList, centerList);
        printSeatLayoutToJavaConsole(matrices);
        return matrices;
    }

    /**
     * returns maximum columns in the seating layout
     * @param seatingArrangement
     * @param masterList
     * @return
     */
    private int getMaxColumnsAndPopulateMasterList(List<Integer> seatingArrangement,
                                                   List<List<Integer>> masterList) {
        int maxColumns = 0;

        for(int i = 0; i < seatingArrangement.size(); i += 2) {
            List<Integer> sectionList = new ArrayList<>();
            sectionList.add(seatingArrangement.get(i));
            sectionList.add(seatingArrangement.get(i+1));
            masterList.add(sectionList);
            maxColumns += seatingArrangement.get(i);
        }
        return maxColumns;
    }

    /**
     * populates a helper map, which is used to determine the max number of rows and
     * columns in a seating section
     * @param masterList
     * @param maxColumns
     * @return
     */
    private Map<Integer, List<Integer>> populateMaxDimensionsMap(
            List<List<Integer>> masterList,
            int maxColumns) {
        Map<Integer, List<Integer>> maxDimensionsOfASection = new HashMap<>();
        int counter = 1;
        int counter1 = 0;
        for(int i = 0; i< maxColumns; i++){
            maxDimensionsOfASection.put(i, masterList.get(counter1));
            if(counter == masterList.get(counter1).get(0)){
                counter1 += 1;
                counter = 0;
            }
            counter++;
        }
        return maxDimensionsOfASection;
    }

    /**
     * prints seat layout to java console
     * @param matrices
     */
    private void printSeatLayoutToJavaConsole(MatrixItem[][] matrices) {
        for (MatrixItem[] matrix : matrices) {
            for (MatrixItem matrixItem : matrix) {
                String value = matrixItem.getValue();
                if (value == null){
                    System.out.print("    ");
                }
                else {
                    System.out.print(value + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * logic to add passengers to their respective seats
     * @param numberOfPassengers
     * @param aisleList
     * @param windowList
     * @param centerList
     */
    private void addPassengersToSeats(Integer numberOfPassengers,
                                      List<MatrixItem> aisleList,
                                      List<MatrixItem> windowList,
                                      List<MatrixItem> centerList) {
        int countOfPassengersAdded = 0;
        for (MatrixItem item: aisleList) {
            if(countOfPassengersAdded <= numberOfPassengers) {
                countOfPassengersAdded++;
                item.setHasValue(true);
                addpassengersToSeats(countOfPassengersAdded, item);
            }
        }
        for (MatrixItem item: windowList) {
            if(countOfPassengersAdded <= numberOfPassengers) {
                countOfPassengersAdded++;
                item.setHasValue(true);
                addpassengersToSeats(countOfPassengersAdded, item);
            }
        }
        for (MatrixItem item: centerList) {
            if(countOfPassengersAdded < numberOfPassengers) {
                countOfPassengersAdded++;
                item.setHasValue(true);
                addpassengersToSeats(countOfPassengersAdded, item);
            }
        }
    }

    /**
     * logic to add passengers to their respective seats
     * @param countOfPassengersAdded
     * @param item
     */
    private void addpassengersToSeats(int countOfPassengersAdded, MatrixItem item) {
        String val = "";
        if (countOfPassengersAdded <= 9)
            val = "00" + countOfPassengersAdded;
        else if (countOfPassengersAdded <= 99)
            val = "0" + countOfPassengersAdded;
        else
            val = String.valueOf(countOfPassengersAdded);
        item.setValue(val);
    }
}
