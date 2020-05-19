package br.usp.util;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class DataManipulator {

    private ArrayList<String[]> dataSet = new ArrayList<>();
    private String[] dataSetHeaders;
    private HashSet<String>[] attributesValues;
    private ArrayList<String[]> trainingData = new ArrayList<>();
    private ArrayList<String[]> validationData = new ArrayList<>();
    private ArrayList<String[]> testingData = new ArrayList<>();
    private String dataSetName;

    public DataManipulator(String dataSetName, boolean isPrunning) {
        this.dataSetName = dataSetName;
        String dataFramePath;
        if (dataSetName.equals("tennis")) {
            dataFramePath = "resources//tennis.csv";
        } else {
            dataFramePath = "resources//adults.csv";
        }

        try (CSVReader reader = new CSVReader(new FileReader(dataFramePath))) {
            String[] line;
            dataSetHeaders = reader.readNext();
            while ((line = reader.readNext()) != null) {
                this.dataSet.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        attributesValues = new HashSet[this.dataSet.get(0).length];
        for (int i = 0; i < attributesValues.length; i++) {
            attributesValues[i] = new HashSet<>();
        }

        for (int i = 0; i < attributesValues.length; i++) {
            for (String[] strings : this.dataSet) {
                attributesValues[i].add(strings[i]);
            }
        }

        if (isPrunning) {
            int total = this.dataSet.size();
            int trainingIndex = (int) (Math.random() * total);
            int validationIndex = trainingIndex + total / 3;
            int testingIndex = validationIndex + total / 3;

            for (int i = trainingIndex; i < validationIndex; i++) trainingData.add(this.dataSet.get(i % total));
            for (int i = validationIndex; i < testingIndex; i++) validationData.add(this.dataSet.get(i % total));
            for (int i = testingIndex; i < testingIndex + total / 3; i++) testingData.add(this.dataSet.get(i % total));
        } else {
            trainingData.addAll(dataSet);
        }
    }


    public ArrayList<String[]> getDataSet() {
        return dataSet;
    }

    public String getAttributeString(int selectedAttrIndex) {
        return dataSetHeaders[selectedAttrIndex];
    }

    public HashSet<String> getAttributeValues(int attributeIndex) {
        return attributesValues[attributeIndex];
    }

    public int getAttributeIndex(String attribute) {
        for (int i = 0; i < dataSetHeaders.length; i++) {
            if (dataSetHeaders[i].equals(attribute)) return i;
        }
        return -1;
    }

    public int getAttributesAmount() {
        return dataSetHeaders.length;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public ArrayList<String[]> getTrainingData() {
        return trainingData;
    }

    public ArrayList<String[]> getValidationData() {
        return validationData;
    }

    public ArrayList<String[]> getTestingData() {
        return testingData;
    }

    public void setTrainingData(ArrayList<String[]> trainingData) {
        this.trainingData = trainingData;
    }

    public void setTestingData(ArrayList<String[]> testingData) {
        this.testingData = testingData;
    }
}
