package br.usp.tree.calculators;

import br.usp.tree.ID3Utils;

import java.util.ArrayList;
import java.util.HashSet;

public class InfoGainCalculator {

    private HashSet<String> attributeValues;
    private ArrayList<String[]> dataset;
    private int attributeIndex;

    public InfoGainCalculator(ArrayList<String[]> dataset, int attributeIndex) {
        HashSet<String> attributeValues = new HashSet<>();
        for (String[] line:dataset) {
            String current = line[attributeIndex];
            attributeValues.add(current);
        }

        this.attributeValues = attributeValues;
        this.dataset = dataset;
        this.attributeIndex = attributeIndex;
    }

    public double getInfoGain() {
        double[] entropies = new double[attributeValues.size()];
        int[] frequencies = new int[attributeValues.size()];

        int i = 0;
        for (String value : attributeValues) {
            ArrayList<String[]> subset = ID3Utils.createSubSet(dataset, attributeIndex, value);
            if(subset.isEmpty()) {
                entropies[i] = 1;
                frequencies[i] = 0;
            } else {
                EntropyCalculator valueEntropyCalculator = new EntropyCalculator(subset);
                entropies[i] = valueEntropyCalculator.getEntropy();
                frequencies[i] = subset.size();
            }
            i++;
        }

        EntropyCalculator setEntropyCalculator = new EntropyCalculator(dataset);
        double gain = setEntropyCalculator.getEntropy();
        int total = dataset.size();

        for (int j = 0; j < entropies.length; j++) {
            double probability = (double) frequencies[j] / total;
            gain -= probability * entropies[j];
        }

        return gain;
    }
}
