package com.example.demo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Set;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/apriori")
public class AprioriController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("minSupport") double minSupport,
                                        @RequestParam("minConfidence") double minConfidence) {
        try {
            List<String[]> transactions = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(line.split(","));
            }

            AprioriAlgorithm apriori = new AprioriAlgorithm(minSupport, minConfidence, transactions);
            apriori.run();

            List<AprioriRuleDTO> rulesDTO = new ArrayList<>();
            for (AprioriAlgorithm.AssociationRule rule : apriori.getRules()) {
                rulesDTO.add(new AprioriRuleDTO(rule.getAntecedent(), rule.getConsequent(), rule.getConfidence(), rule.getLift(), rule.getLeverage(), rule.getConviction()));
            }

            return ResponseEntity.ok().body(rulesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File processing failed: " + e.getMessage());
        }
    }

    public static class AprioriRuleDTO {
        private Set<String> antecedent;
        private Set<String> consequent;
        private double confidence;
        private double lift;
        private double leverage;
        private double conviction;

        public AprioriRuleDTO(Set<String> antecedent, Set<String> consequent, double confidence, double lift, double leverage, double conviction) {
            this.antecedent = antecedent;
            this.consequent = consequent;
            this.confidence = confidence;
            this.lift = lift;
            this.leverage = leverage;
            this.conviction = conviction;
        }

        public Set<String> getAntecedent() {
            return antecedent;
        }

        public void setAntecedent(Set<String> antecedent) {
            this.antecedent = antecedent;
        }

        public Set<String> getConsequent() {
            return consequent;
        }

        public void setConsequent(Set<String> consequent) {
            this.consequent = consequent;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public double getLift() {
            return lift;
        }

        public void setLift(double lift) {
            this.lift = lift;
        }

        public double getLeverage() {
            return leverage;
        }

        public void setLeverage(double leverage) {
            this.leverage = leverage;
        }

        public double getConviction() {
            return conviction;
        }

        public void setConviction(double conviction) {
            this.conviction = conviction;
        }
    }

}
