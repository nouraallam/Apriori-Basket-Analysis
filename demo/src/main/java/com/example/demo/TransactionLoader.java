package com.example.demo;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionLoader {
    public static List<Set<String>> loadTransactions(String filePath, double minSupport, double minConfidence) throws IOException {
        List<Set<String>> transactions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] items = line.split(",");
            Set<String> transaction = new HashSet<>();

            for (String item : items) {
                transaction.add(item.trim());
            }

            transactions.add(transaction);
        }

        reader.close();

        // Filtrer les transactions en fonction du minSupport et du minConfidence si nécessaire
        transactions = filterTransactions(transactions, minSupport, minConfidence);

        return transactions;
    }

    private static List<Set<String>> filterTransactions(List<Set<String>> transactions, double minSupport, double minConfidence) {
        List<Set<String>> filteredTransactions = new ArrayList<>();

        for (Set<String> transaction : transactions) {
            // Implémentez le filtrage des transactions ici en fonction du minSupport et du minConfidence
            // Par exemple, vous pouvez vérifier si la transaction satisfait les conditions de support et de confiance
            // Si elle le fait, ajoutez-la à la liste des transactions filtrées
            // Sinon, ignorez-la
            if (checkSupportAndConfidence(transaction, minSupport, minConfidence)) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }

    private static boolean checkSupportAndConfidence(Set<String> transaction, double minSupport, double minConfidence) {
        // Implémentez la vérification de support et de confiance ici
        // Par exemple, vous pouvez calculer le support de la transaction et le comparer à minSupport
        // De même, vous pouvez calculer la confiance et la comparer à minConfidence
        // Si la transaction satisfait les conditions de support et de confiance, retournez true, sinon false
        return true; // Remplacez true par votre implémentation réelle
    }
}

