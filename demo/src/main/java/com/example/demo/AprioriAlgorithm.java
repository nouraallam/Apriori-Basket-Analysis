package com.example.demo;

import java.util.*;

public class AprioriAlgorithm {
    private double minSupport;
    private double minConfidence;
    private List<String[]> transactions;
    private List<AssociationRule> rules = new ArrayList<>();
    private String[] lastTransaction;

    public AprioriAlgorithm(double minSupport, double minConfidence, List<String[]> transactions) {
        this.minSupport = minSupport;
        this.minConfidence = minConfidence;
        this.transactions = transactions;
    }

    public void run() {
        // Conversion des transactions en liste d'ensembles d'éléments
        List<Set<String>> itemsets = new ArrayList<>();
        for (String[] transaction : transactions) {
            itemsets.add(new HashSet<>(Arrays.asList(transaction)));
        }

        // Générer des ensembles fréquents d'éléments
        Map<Set<String>, Integer> frequentItemsets = generateFrequentItemsets(itemsets, minSupport);

        // Générer des règles d'association
        rules = generateAssociationRules(frequentItemsets, minConfidence);
        lastTransaction = transactions.get(transactions.size() - 1);
        System.out.println("Dernière transaction : " + Arrays.toString(lastTransaction));
    }

    private Map<Set<String>, Integer> generateFrequentItemsets(List<Set<String>> itemsets, double minSupport) {
        Map<Set<String>, Integer> allFrequentItemsets = new HashMap<>();
        Map<Set<String>, Integer> currentFrequentItemsets = new HashMap<>();
        // Initialisation et support des items individuels
        // Calculer le support de chaque élément unique
        Map<String, Integer> itemSupport = new HashMap<>();
        for (Set<String> itemset : itemsets) {
            for (String item : itemset) {
                itemSupport.put(item, itemSupport.getOrDefault(item, 0) + 1);
            }
        }
       // Filtrage des items fréquents de taille 1 (L1)
        // Filtrer les éléments en fonction du support minimal
        int minSupportCount = (int) Math.ceil(minSupport * itemsets.size());
        for (Map.Entry<String, Integer> entry : itemSupport.entrySet()) {
            if (entry.getValue() >= minSupportCount) {
                Set<String> itemset = new HashSet<>();
                itemset.add(entry.getKey());
                currentFrequentItemsets.put(itemset, entry.getValue());
            }
        }

        allFrequentItemsets.putAll(currentFrequentItemsets);
            //Génération des candidats et vérification du support pour les tailles supérieures
        // Générer des itemsets fréquents de taille supérieure
        while (!currentFrequentItemsets.isEmpty()) {
            Map<Set<String>, Integer> newFrequentItemsets = new HashMap<>();

            List<Set<String>> itemsetList = new ArrayList<>(currentFrequentItemsets.keySet());
            int size = itemsetList.size();

            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    Set<String> candidate = new HashSet<>(itemsetList.get(i));
                    candidate.addAll(itemsetList.get(j));
                    if (candidate.size() == itemsetList.get(i).size() + 1) {
                        int count = 0;
                        for (Set<String> itemset : itemsets) {
                            if (itemset.containsAll(candidate)) {
                                count++;
                            }
                        }
                        if (count >= minSupportCount) {
                            newFrequentItemsets.put(candidate, count);
                        }
                    }
                }
            }

            allFrequentItemsets.putAll(newFrequentItemsets);
            currentFrequentItemsets = newFrequentItemsets;
        }

        return allFrequentItemsets;
    }

    private List<AssociationRule> generateAssociationRules(Map<Set<String>, Integer> frequentItemsets, double minConfidence) {
        List<AssociationRule> rules = new ArrayList<>();

        for (Set<String> itemset : frequentItemsets.keySet()) {
            if (itemset.size() < 2) {
                continue;
            }

            List<Set<String>> subsets = getSubsets(itemset);
            for (Set<String> subset : subsets) {
                Set<String> remaining = new HashSet<>(itemset);
                remaining.removeAll(subset);

                if (!remaining.isEmpty()) {
                    double supportItemset = (double) frequentItemsets.get(itemset) / transactions.size();
                    double supportSubset = (double) frequentItemsets.get(subset) / transactions.size();
                    double confidence = supportItemset / supportSubset;

                    if (confidence >= minConfidence) {
                        double lift = confidence / ((double) frequentItemsets.get(remaining) / transactions.size());
                        double leverage = supportItemset - (supportSubset * (double) frequentItemsets.get(remaining) / transactions.size());
                        double conviction = (1 - ((double) frequentItemsets.get(remaining) / transactions.size())) / (1 - confidence);

                        rules.add(new AssociationRule(subset, remaining, confidence, lift, leverage, conviction));
                    }
                }
            }
        }

        return rules;
    }

    private List<Set<String>> getSubsets(Set<String> itemset) {
        List<Set<String>> subsets = new ArrayList<>();
        int n = itemset.size();
        String[] items = itemset.toArray(new String[0]);

        for (int i = 1; i < (1 << n); i++) {
            Set<String> subset = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(items[j]);
                }
            }
            subsets.add(subset);
        }

        return subsets;
    }

    public List<AssociationRule> getRules() {
        return rules;
    }

    public String[] getLastTransaction() {
        return lastTransaction;
    }

    public class AssociationRule {
        private Set<String> antecedent;
        private Set<String> consequent;
        private double confidence;
        private double lift;
        private double leverage;
        private double conviction;

        public AssociationRule(Set<String> antecedent, Set<String> consequent, double confidence, double lift, double leverage, double conviction) {
            this.antecedent = antecedent;
            this.consequent = consequent;
            this.confidence = confidence;
            this.lift = lift;
            this.leverage = leverage;
            this.conviction = conviction;
        }

        @Override
        public String toString() {
            return "Rule " + antecedent + " => " + consequent + " | " + confidence + " | " + lift + " | " + conviction + " | " + leverage;
        }

        public Set<String> getAntecedent() {
            return antecedent;
        }

        public Set<String> getConsequent() {
            return consequent;
        }

        public double getConfidence() {
            return confidence;
        }

        public double getLift() {
            return lift;
        }

        public double getLeverage() {
            return leverage;
        }

        public double getConviction() {
            return conviction;
        }
    }
}
