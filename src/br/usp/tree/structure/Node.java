package br.usp.tree.structure;
import br.usp.util.DataManipulator;

import java.util.ArrayList;
import java.util.HashSet;

public class Node {

    private boolean isLeaf = false;
    private Branch fatherBranch;
    private ArrayList<String[]> nodeDataset;
    private String nodeAttribute;
    private boolean[] availableAttributes;
    private HashSet<String> branchValues;
    private HashSet<Branch> nextBranches = new HashSet<>();
    private String nodeAnswer = null;

    public Node(Branch fatherBranch, ArrayList<String[]> nodeDataset, boolean[] availableAttributes) {
        this.fatherBranch = fatherBranch;
        this.nodeDataset = nodeDataset;
        this.availableAttributes = availableAttributes;
    }

    public Node(Branch fatherBranch, ArrayList<String[]> nodeDataset, boolean[] availableAttributes, Boolean isLeaf, DataManipulator dataManipulator, int selectedAttrIndex) {
        this.fatherBranch = fatherBranch;
        this.nodeDataset = nodeDataset;
        this.availableAttributes = availableAttributes;
        this.isLeaf = isLeaf;
        this.nodeAttribute = dataManipulator.getAttributeString(selectedAttrIndex);
        this.branchValues = dataManipulator.getAttributeValues(selectedAttrIndex);
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setNodeAttribute(String nodeAttribute){
        this.nodeAttribute = nodeAttribute;
    }

    public HashSet<String> getBranchValues() {
        return branchValues;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public Branch getFatherBranch() {
        return fatherBranch;
    }

    public ArrayList<String[]> getNodeDataset(){
        return nodeDataset;
    }


    public String getNodeAttribute() {
        return nodeAttribute;
    }

    public boolean[] getAvailableAttributes() {
        return availableAttributes;
    }

    public void setNextBranches(Branch branch) {
        nextBranches.add(branch);
    }

    public void setNextBranchesStructures(HashSet<Branch> nextBranches) {
        this.nextBranches = nextBranches;
    }

    public void setBranchValues(HashSet<String> branchValues) {
        this.branchValues = branchValues;
    }

    public HashSet<Branch> getNextBranches() {
        return nextBranches;
    }

    public String getNodeAnswer() {
        return nodeAnswer;
    }

    public void setNodeAnswer(String nodeAnswer) {
        this.nodeAnswer = nodeAnswer;
    }

}
