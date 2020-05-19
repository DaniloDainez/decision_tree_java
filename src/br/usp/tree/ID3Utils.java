package br.usp.tree;

import br.usp.tree.structure.Branch;
import br.usp.tree.structure.Node;
import br.usp.util.DataManipulator;

import java.util.ArrayList;

//TODO: O nome dessa classe ainda n ta bom e tem coisa q ta aqui q nao precisava estar
public class ID3Utils {

    public static int nodeCount;

    public static ArrayList<String[]> createSubSet(ArrayList<String[]> dataSet, int attributeIndex, String targetValue) {
        ArrayList<String[]> newSubset = new ArrayList<>();

        dataSet.forEach(current -> {
            if (current[attributeIndex].equals(targetValue)) {
                newSubset.add(current);
            }
        });

        return newSubset;
    }

    public double calculateTreeAccuracy(DataManipulator dataManipulator, Node root) {
        ArrayList<String[]> dataSet = dataManipulator.getTestingData();

        double numberOfRightAnswers =
                (double) dataSet.stream()
                        .filter(tuple -> answerIsCorrect(tuple, root, dataManipulator))
                        .count();

        return numberOfRightAnswers / dataSet.size();
    }

    private boolean answerIsCorrect(String[] data, Node root, DataManipulator dataManipulator) {
        Node auxNode = root;

        while (!auxNode.isLeaf()) {
            int index = dataManipulator.getAttributeIndex(auxNode.getNodeAttribute());
            String indexValue = data[index];

            for (Branch branch : auxNode.getNextBranches()) {
                if (branch.getBranchAttribute().equals(indexValue)) {
                    auxNode = branch.getNextNode();
                    break;
                }
            }

        }
        return auxNode.getNodeAnswer().equals(data[data.length - 1]);
    }

    void defineLeafNodeAnswer(Node node) {
        int positiveCounter = 0;
        int negativeCounter = 0;

        ArrayList<String[]> nodeDataset = node.getNodeDataset();

        for (String[] line : nodeDataset) {
            String lineAnswer = line[line.length - 1];
            if (lineAnswer.equals("positive")) positiveCounter++;
            else negativeCounter++;
        }

        if (positiveCounter > negativeCounter) node.setNodeAnswer("positive");
        else node.setNodeAnswer("negative");
    }

    public static int countNodes(Node node) {
        nodeCount++;

        node.getNextBranches().forEach(branch -> {
            if (!node.isLeaf()) {
                nodeCount = countNodes(branch.getNextNode());
            }
        });

        return nodeCount;
    }
}
