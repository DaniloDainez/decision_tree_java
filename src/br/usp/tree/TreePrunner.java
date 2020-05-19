package br.usp.tree;

import br.usp.tree.structure.Branch;
import br.usp.tree.structure.Node;
import br.usp.tree.structure.Tree;
import br.usp.util.DataManipulator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TreePrunner {

    private Tree tree;
    private TreeBuilder creation;
    private DataManipulator dataManipulator;
    private HashMap<Node, Double> pruneCandidatesAccuracies = new HashMap<>();

    public TreePrunner(Tree tree) {
        this.tree = tree;
        this.creation = tree.getCreation();
        dataManipulator = new DataManipulator(creation.getDataManipulator().getDataSetName(), true);
    }

    public void pruneTree(Node node, Node rootNode) {
        double candidateAccuracy = 1;
        double initialAccuracy = 0;

        while (candidateAccuracy > initialAccuracy) {
            initialAccuracy = tree.getId3Utils().calculateTreeAccuracy(dataManipulator, rootNode);
            for (Branch branch : node.getNextBranches()) {
                if (node != rootNode && branch.getNextNode().isLeaf()) {
                    testPrunning(node.getFatherBranch().getPreviousNode(), rootNode, initialAccuracy);
                } else {
                    pruneTree(branch.getNextNode(), rootNode);
                    testPrunning(node, rootNode, initialAccuracy);
                }
            }
            Node candidateNode = null;
            candidateAccuracy = initialAccuracy;
            for (Map.Entry<Node, Double> entry : pruneCandidatesAccuracies.entrySet()) {
                if (entry.getValue() >= candidateAccuracy) {
                    candidateNode = entry.getKey();
                    candidateAccuracy = entry.getValue();
                }
            }
            if (candidateNode != null) {
                executePrunning(candidateNode);

                ID3Utils.nodeCount = 0;
                ID3Utils.countNodes(rootNode);
                double newAccuracy = tree.getId3Utils().calculateTreeAccuracy(dataManipulator, rootNode);
                System.out.println("Numero de nos : " + ID3Utils.nodeCount + "; Acuracia validation set: " + newAccuracy);

            }
        }
    }

    public void executePrunning(Node node) {
        node.setNextBranchesStructures(new HashSet<>());
        node.setBranchValues(dataManipulator.getAttributeValues(dataManipulator.getAttributeIndex(node.getNodeAttribute())));
        for (String branchValue : node.getBranchValues()) {
            node.setNextBranches(creation.prunningCreateBranch(branchValue, node, node.getNodeDataset()));
        }
        pruneCandidatesAccuracies = new HashMap<>();
    }

    public void testPrunning(Node node, Node rootNode, double accuracy) {
        HashSet<Branch> oldSons = node.getNextBranches();
        node.setNextBranchesStructures(new HashSet<>());
        //node.setBranchValues(dataManipulator.getAttributeValues(dataManipulator.getAttributeIndex(node.getNodeAttribute())));

        for (String branchValue : node.getBranchValues()) {
            node.setNextBranches(creation.prunningCreateBranch(branchValue, node, node.getNodeDataset()));
        }
        double newAccuracy = tree.getId3Utils().calculateTreeAccuracy(dataManipulator, rootNode);
        if (newAccuracy >= accuracy) {
            pruneCandidatesAccuracies.put(node, newAccuracy);
        }
        node.setNextBranchesStructures(oldSons);
    }
}

