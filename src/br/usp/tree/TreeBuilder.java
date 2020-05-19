package br.usp.tree;

import br.usp.tree.calculators.InfoGainCalculator;
import br.usp.tree.structure.Branch;
import br.usp.tree.structure.Node;
import br.usp.util.DataManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class TreeBuilder {

    private DataManipulator dataManipulator;
    private Boolean isLeaf = false;
    private ID3Utils id3Utils = new ID3Utils();
    public static int nodeCount;

    public TreeBuilder(DataManipulator dataManipulator) {
        this.dataManipulator = dataManipulator;
    }

    public Node createTree() {
        boolean[] availableAttributes = new boolean[dataManipulator.getAttributesAmount()];

        Arrays.fill(availableAttributes, true);

        return createNode(null, dataManipulator.getTrainingData(), availableAttributes);
    }

    private Node createNode(Branch fatherBranch, ArrayList<String[]> nodeDataset, boolean[] availableAttributes) {
        nodeCount++;
        int selectedAttrIndex = decideAttribute(nodeDataset, availableAttributes);

        if (selectedAttrIndex == -1) {
            return createLeafNode(fatherBranch, nodeDataset);
        }

        Node newNode = new Node(fatherBranch, nodeDataset, availableAttributes, isLeaf, dataManipulator, selectedAttrIndex);

        availableAttributes = availableAttributes.clone();
        availableAttributes[selectedAttrIndex] = false;

        HashSet<String> branchValues = newNode.getBranchValues();
        createNextBranches(nodeDataset, availableAttributes, newNode, branchValues);

        Node rootNode = newNode;
        rootNode = findRootNode(rootNode, fatherBranch);

        ID3Utils.nodeCount = 0;
        ID3Utils.countNodes(rootNode);

        return newNode;
    }

    private Node findRootNode(Node rootNode, Branch auxBranch) {
        if (auxBranch != null) {

            while (auxBranch.getPreviousNode().getFatherBranch() != null) {
                auxBranch = auxBranch.getPreviousNode().getFatherBranch();
            }

            rootNode = auxBranch.getPreviousNode();
        }
        return rootNode;
    }

    private void createNextBranches(ArrayList<String[]> nodeDataset, boolean[] availableAttributes, Node newNode, HashSet<String> branchValues) {
        branchValues.forEach(branchValue ->
                newNode.setNextBranches(createBranch(branchValue, newNode, nodeDataset, availableAttributes)));
    }

    Node createLeafNode(Branch fatherBranch, ArrayList<String[]> nodeDataset) {
        Node newNode = new Node(fatherBranch, nodeDataset, null);
        newNode.setLeaf(true);
        id3Utils.defineLeafNodeAnswer(newNode);
        if (fatherBranch != null) {
            fatherBranch.getPreviousNode().setLeaf(false);
        }
        return newNode;
    }

    Branch createBranch(String branchValue, Node previousNode, ArrayList<String[]> dataSet, boolean[] availableAttributes) {
        ArrayList<String[]> nodeDataset = ID3Utils.createSubSet(dataSet, dataManipulator.getAttributeIndex(previousNode.getNodeAttribute()), branchValue);
        Branch newBranch = new Branch(previousNode, branchValue);

        if (nodeDataset.isEmpty() || previousNode.isLeaf()) {
            newBranch.setNextNode((createLeafNode(newBranch, nodeDataset)));
        } else {
            newBranch.setNextNode(createNode(newBranch, nodeDataset, availableAttributes));
        }

        return newBranch;
    }

    Branch prunningCreateBranch(String branchValue, Node previousNode, ArrayList<String[]> dataSet) {
        ID3Utils.createSubSet(dataSet, dataManipulator.getAttributeIndex(previousNode.getNodeAttribute()), branchValue);
        Branch newBranch = new Branch(previousNode, branchValue);
        newBranch.setNextNode(createLeafNode(newBranch, previousNode.getNodeDataset()));
        return newBranch;
    }

    private int decideAttribute(ArrayList<String[]> nodeDataset, boolean[] availableAttributes) {
        double biggestGain = 0;
        int chosenAttribute = -1;

        for (int i = 0; i < availableAttributes.length - 1; i++) {

            if (availableAttributes[i]) {
                InfoGainCalculator gain = new InfoGainCalculator(nodeDataset, i);
                double value = gain.getInfoGain();

                if (value > biggestGain) {
                    biggestGain = value;
                    chosenAttribute = i;

                    if (biggestGain == 1) {
                        isLeaf = true;
                        break;
                    }
                }
            }
        }
        return chosenAttribute;
    }

    DataManipulator getDataManipulator() {
        return dataManipulator;
    }
}
