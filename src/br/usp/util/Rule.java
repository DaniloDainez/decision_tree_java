package br.usp.util;

import br.usp.tree.structure.Branch;
import br.usp.tree.structure.Node;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Rule {

    private ArrayList<String> rule = new ArrayList<>();
    private ArrayList<Double> accuracy = new ArrayList<>();
    private ArrayList<Integer> total = new ArrayList<>();
    private ArrayList<Integer> correctCases = new ArrayList<>();

    public Rule(Node rootNode, String sb) {
        getRules(rootNode, sb, this);
        printRules();
    }

    ArrayList<String> getRule() {
        return rule;
    }

    private ArrayList<Double> getRuleAccuracy() {
        return accuracy;
    }

    private ArrayList<Integer> getTotal() {
        return total;
    }

    private ArrayList<Integer> getCorrectCases() {
        return correctCases;
    }

    public void setRule(ArrayList<String> rule) {
        this.rule = rule;
    }

    public void setRuleAccuracy(ArrayList<Double> accuracy) {
        this.accuracy = accuracy;
    }

    public void setTotal(ArrayList<Integer> total) {
        this.total = total;
    }

    public void setCorrectCases(ArrayList<Integer> correctCases) {
        this.correctCases = correctCases;
    }

    private void printRules() {
        DecimalFormat df = new DecimalFormat("#.00");
        StringBuilder rules = new StringBuilder();
        int aux;
        Double biggestAccuracy;
        int biggestTotal;

        for (int i = 0; i < accuracy.size(); i++) {
            biggestAccuracy = -1.0;
            biggestTotal = -1;
            aux = 0;
            for (int j = 0; j < accuracy.size(); j++) {
                double auxFreq = accuracy.get(j);
                if (auxFreq > biggestAccuracy) {
                    biggestAccuracy = accuracy.get(j);
                    biggestTotal = total.get(j);
                    aux = j;
                } else if (biggestAccuracy == auxFreq) {
                    if (total.get(j) > biggestTotal) {
                        biggestAccuracy = accuracy.get(j);
                        biggestTotal = total.get(j);
                        aux = j;
                    }
                }
            }
            if (accuracy.get(aux) > 0.0) {
                rules.append(rule.get(aux)).append("; Frequencia: ").append(df.format(accuracy.get(aux))).append("%; Total de Recorrencias: ").append(correctCases.get(aux)).append("/").append(total.get(aux)).append(".\n");
            } else {
                rules.append(rule.get(aux)).append("; Frequencia: 0,00%; Total de Recorrencias: ").append(correctCases.get(aux)).append("/").append(total.get(aux)).append(".\n");
            }

            accuracy.set(aux, -1.0);
            total.set(aux, -1);

        }
        System.out.println(rules);
    }

    private void getRules(Node node, String rule, Rule ruleList) {
        for (Branch branch : node.getNextBranches()) {
            String newRule = "";
            double accuracy = 0.0;
            int cases;
            int total = 0;
            if (branch.getNextNode().isLeaf()) {
                if (rule.length() != 0) {
                    newRule = "IF " + rule + " AND " + node.getNodeAttribute() + ": " + branch.getBranchAttribute() + " THEN " + branch.getNextNode().getNodeAnswer();
                } else {
                    newRule = "IF " + node.getNodeAttribute() + ": " + branch.getBranchAttribute() + " THEN " + branch.getNextNode().getNodeAnswer();
                }
                ruleList.getRule().add(newRule);

                if (branch.getNextNode().getNodeAnswer().equals("positive")) {
                    cases = 0;
                    total = branch.getNextNode().getNodeDataset().size();
                    if (total != 0) {
                        int resultInd = branch.getNextNode().getNodeDataset().get(0).length - 1;
                        for (int i = 0; i < total; i++) {
                            if (branch.getNextNode().getNodeDataset().get(i)[resultInd].equals("positive")) {
                                cases += 1;
                            }
                        }
                        accuracy = (double) cases / total * 100;
                    }
                    ruleList.getCorrectCases().add(cases);
                    ruleList.getRuleAccuracy().add(accuracy);
                    ruleList.getTotal().add(total);
                } else {
                    cases = 0;
                    total = branch.getNextNode().getNodeDataset().size();
                    if (total != 0) {
                        int resultInd = branch.getNextNode().getNodeDataset().get(0).length - 1;
                        for (int i = 0; i < total; i++) {
                            if (branch.getNextNode().getNodeDataset().get(i)[resultInd].equals("negative")) {
                                cases += 1;
                            }
                        }
                        accuracy = (double) cases / total * 100;
                    }
                    ruleList.getCorrectCases().add(cases);
                    ruleList.getRuleAccuracy().add(accuracy);
                    ruleList.getTotal().add(total);
                }
            } else {
                if (rule.length() != 0)
                    newRule = rule + " AND " + node.getNodeAttribute() + ": " + branch.getBranchAttribute();
                else newRule = rule + node.getNodeAttribute() + ": " + branch.getBranchAttribute();
                getRules(branch.getNextNode(), newRule, ruleList);
            }
        }
    }
}