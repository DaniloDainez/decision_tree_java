package br.usp.util;

// Essa classe é responsável pelo uso da técnica de validação cruzada

import br.usp.tree.structure.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TenFoldCrossValidator {
    private HashSet<DataManipulator> dataManipulators = new HashSet<>();
    private HashSet<Tree> models = new HashSet<>();

    public TenFoldCrossValidator(String dataSetName) {
        for (int i = 0; i < 10; i++) {
            DataManipulator newDataManipulator = new DataManipulator(dataSetName, false);
            dataManipulators.add(newDataManipulator);
            makeFolds(newDataManipulator, i);
        }
    }

    public void createModels() {
        for (DataManipulator dataManipulator : dataManipulators) {
            Tree tree = new Tree(dataManipulator);
            models.add(tree);
        }
    }

    public HashMap<Tree, Double> getModelsEvaluation() {
        HashMap<Tree, Double> evaluations = new HashMap<>();
        for (Tree model : models) {
            double accuracy = model.getId3Utils().calculateTreeAccuracy(model.getDataManipulator(), model.getRoot());
            evaluations.put(model, accuracy);
        }
        return evaluations;
    }

    private double getEvalsMean(HashMap<Tree, Double> evaluations) {
        double sum = 0;
        for (Double accuracy : evaluations.values()) {
            sum += accuracy;
        }
        return sum / evaluations.values().size();
    }

    private double getStandardError(HashMap<Tree, Double> evaluations) {
        double mean = getEvalsMean(evaluations);
        return Math.sqrt((mean * (1 - mean)) / evaluations.values().size());
    }

    public String getConfidenceInterval() {
        HashMap<Tree, Double> evaluations = getModelsEvaluation();
        double mean = getEvalsMean(evaluations);
        double stdError = getStandardError(evaluations);
        double lowerBound = mean - 1.96 * stdError;
        double upperBound = mean + 1.96 * stdError;
        if (upperBound > 1) upperBound = 1;
        if (lowerBound < 0) lowerBound = 0;
        return "Em 95% dos modelos criados a partir de um 10-fold cross-validation com esse " +
                "conjunto de dados, a acurácia x desse modelo estará no intervalo abaixo: \n" +
                lowerBound + " <= x <= " + upperBound;
    }

    /*
     * essa funcao recebe como parametros:
     * data: o conjunto de dados
     * validation_index: indice do fold que sera utilizado como conjunto de teste
     * listOfFolds: esse parametro eh uma lista vazia que ao final da funcao tera na sua primeira posicao
     * o fold que sera usado para teste e na segunda posicao tera os folds de treina
     *
     * A ideia eh que essa funcao seja utilizada num laço de 10 iterações.
     *
     * Considera-se que trainingSet acabou de ser declarado e está vazio (será retornado via ref,
     * já que o conj de validacao será o retorno do metodo).
     */

    private void makeFolds(DataManipulator dataManipulator, int testingIndex) {
        ArrayList<String[]> testingSet = new ArrayList<>();
        ArrayList<String[]> trainingSet = new ArrayList<>();

        trainingSet.addAll(dataManipulator.getDataSet());

        int foldSize = dataManipulator.getTrainingData().size() / 10;
        int validationStartIndex = foldSize * testingIndex;

        while (testingSet.size() < foldSize) testingSet.add(trainingSet.remove(validationStartIndex));

        dataManipulator.setTestingData(testingSet);
        dataManipulator.setTrainingData(trainingSet);
    }
}
