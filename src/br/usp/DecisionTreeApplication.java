package br.usp;

import br.usp.tree.ID3Utils;
import br.usp.tree.structure.Tree;
import br.usp.tree.TreePrunner;
import br.usp.tree.TreeBuilder;
import br.usp.util.DataManipulator;
import br.usp.util.Rule;
import br.usp.util.TenFoldCrossValidator;

import java.util.Map;

public class DecisionTreeApplication {
    public static void main(String [] args){
       // Testando o conjunto Play Tennis
        testPlayTennis();

        //10-fold para adults
        TenFoldCrossValidator tenFoldCrossValidator = getTenFoldCrossValidator();

        StringBuilder accuracies = new StringBuilder("\n\nAcuracias para a validacao cruzada de 10 particoes sobre o conjunto adults.csv\n\n\n");

        int treeCounter = 1;
        for (Map.Entry<Tree,Double> entry : tenFoldCrossValidator.getModelsEvaluation().entrySet()) {
            accuracies.append("Arvore numero ").append(treeCounter).append(" : ").append(entry.getValue()).append("\n");
            treeCounter++;
        }

        System.out.println(accuracies);
        System.out.println(tenFoldCrossValidator.getConfidenceInterval());

        Tree pruneTree = new Tree(new DataManipulator("adults", true));
        System.out.println();
        ID3Utils.nodeCount = 0;
        ID3Utils.countNodes(pruneTree.getRoot());
        System.out.println("Numero de Nós antes da Poda: " + ID3Utils.nodeCount);
        System.out.printf("Acuracia da arvore antes da Poda: %.2f%%\n\n", pruneTree.getId3Utils().calculateTreeAccuracy(pruneTree.getDataManipulator(), pruneTree.getRoot()) * 100);
        TreePrunner treePrunner = new TreePrunner(pruneTree);
        treePrunner.pruneTree(pruneTree.getRoot(), pruneTree.getRoot());
        ID3Utils.nodeCount = 0;
        ID3Utils.countNodes(pruneTree.getRoot());
        System.out.println("Numeros de Nos depois da Poda: " + ID3Utils.nodeCount);
        System.out.printf("Acuracia apos a poda: %.2f%%\n\n", pruneTree.getId3Utils().calculateTreeAccuracy(pruneTree.getDataManipulator(), pruneTree.getRoot()) * 100);

        if (args.length > 0 && args[0].equals("-ranking")) {
            System.out.println("Regras geradas para o modelo descrito acima:\n");
            new Rule(pruneTree.getRoot(), new String());
        }
    }

    private static TenFoldCrossValidator getTenFoldCrossValidator() {
        TenFoldCrossValidator tenFoldCrossValidator = new TenFoldCrossValidator("adults");
        tenFoldCrossValidator.createModels();
        return tenFoldCrossValidator;
    }

    private static void testPlayTennis() {
        Tree tennisTree = new Tree(new DataManipulator("tennis", false));

        System.out.println("\n\nModelo gerado a partir do conjunto tennis.csv tomando o conjunto inteiro como conjunto de treinamento:\n\n");

        new Rule(tennisTree.getRoot(), new String());

        System.out.println("Nodes: " + TreeBuilder.nodeCount);
    }
}
