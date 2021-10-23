package com.airplane.seating.services;

import dto.MatrixItem;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SeatingService {

    public MatrixItem[][] seat(
//            List<List<Integer>> seatingArrangement,
            Integer numberOfPassengers) {

        List<Integer> firstSection = Arrays.asList(3,2);
        List<Integer> secondSection = Arrays.asList(4,3);
        List<Integer> thirdSection = Arrays.asList(2,3);
        List<Integer> fourthSection = Arrays.asList(3,4);

        Map<Integer, List<Integer>> maxDimensionsOfASection = new HashMap<>();
        maxDimensionsOfASection.put(0,firstSection);
        maxDimensionsOfASection.put(1,firstSection);
        maxDimensionsOfASection.put(2,firstSection);

        maxDimensionsOfASection.put(3,secondSection);
        maxDimensionsOfASection.put(4,secondSection);
        maxDimensionsOfASection.put(5,secondSection);
        maxDimensionsOfASection.put(6,secondSection);

        maxDimensionsOfASection.put(7,thirdSection);
        maxDimensionsOfASection.put(8,thirdSection);

        maxDimensionsOfASection.put(9,fourthSection);
        maxDimensionsOfASection.put(10,fourthSection);
        maxDimensionsOfASection.put(11,fourthSection);

        MatrixItem[][] matrices = new MatrixItem[4][12];
        int maxColumns = 12;

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

    private void printSeatLayoutToJavaConsole(MatrixItem[][] matrices) {
        // print seat layout in console
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

    private void addPassengersToSeats(Integer numberOfPassengers, List<MatrixItem> aisleList, List<MatrixItem> windowList, List<MatrixItem> centerList) {
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
