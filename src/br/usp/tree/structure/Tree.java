package br.usp.tree.structure;

import br.usp.tree.ID3Utils;
import br.usp.tree.TreeBuilder;
import br.usp.util.DataManipulator;
import br.usp.tree.structure.Node;

public class Tree {
    private Node root;
    private DataManipulator dataManipulator;
    private ID3Utils id3Utils = new ID3Utils();
    private TreeBuilder creation;

    public Tree(DataManipulator dataManipulator){
        this.dataManipulator = dataManipulator;
        this.creation = new TreeBuilder(dataManipulator);
        root = creation.createTree();
    }

    public ID3Utils getId3Utils() {
        return id3Utils;
    }

    public DataManipulator getDataManipulator() {
        return dataManipulator;
    }

    public Node getRoot() {
        return root;
    }

    public TreeBuilder getCreation() {
        return creation;
    }
}
