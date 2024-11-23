package com.example.demo;
import java.util.Set;

public class AssociationRule {
    private Set<String> antecedent;
    private Set<String> consequent;
    private double support;
    private double confidence;
    private double lift;
    private double leverage;
    private double conviction;

    public AssociationRule(Set<String> antecedent, Set<String> consequent, double support, double confidence, double lift) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.support = support;
        this.confidence = confidence;
        this.lift = lift;
        this.leverage = calculateLeverage();
        this.conviction = calculateConviction();
    }

    private double calculateLeverage() {
        double expectedSupport = (support / confidence) * (1 - confidence);
        return support - expectedSupport;
    }

    private double calculateConviction() {
        double supportConsequent = confidence * support;
        return (1 - supportConsequent) / (1 - confidence);
    }

    @Override
    public String toString() {
        return String.format("Rule: %s -> %s, Support: %.2f, Confidence: %.2f, Lift: %.2f, Leverage: %.2f, Conviction: %.2f",
                antecedent, consequent, support, confidence, lift, leverage, conviction);
    }
}
