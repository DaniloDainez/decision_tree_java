package br.usp.tree.structure;

public class Branch {

    private Node previousNode;
    private Node nextNode;
    private String branchAttribute;

    public Branch(Node previousNode, String branchAttribute) {
        this.previousNode = previousNode;
        this.branchAttribute = branchAttribute;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public Node getPreviousNode(){
        return this.previousNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public void setBranchAttribute(String branchAttribute) {
        this.branchAttribute = branchAttribute;
    }

    public Node getNextNode() { return this.nextNode; }

    public String getBranchAttribute() {
        return branchAttribute;
    }
}
