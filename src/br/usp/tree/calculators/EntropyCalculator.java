package br.usp.tree.calculators;

import java.util.ArrayList;

class EntropyCalculator {

    private int[] frequencies = new int[2];
    private int total;

    // Estou considerando que essa função vai receber o dataset apenas com o atributo alvo, sem os demais atributos
    EntropyCalculator(ArrayList<String[]> dataSet) {
        frequencies[0] = 0;
        frequencies[1] = 0;
        int length = dataSet.get(0).length;
        for (String[] line: dataSet) {
            // Estou considerando que vamos adequar ambos datasets para que a ultima coluna seja "positive" || "negative"
            if(line[length-1].equals("positive"))  frequencies[0] += 1;
            else frequencies[1] += 1;
        }
        total = dataSet.size();
    }

    double getEntropy() {
        double entropy = 0;

        for (int freq : frequencies) {
            double probability = (double) freq/total;
            if(probability != 0) entropy -= probability * (Math.log(probability) / Math.log(2));
        }

        return entropy;
    }
}
