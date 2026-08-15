package com.scbs.service;

import java.util.ArrayList;
import java.util.List;

public class BiometricService {

    // Euclidean distance threshold for matching facial feature vectors
    private static final double SIMILARITY_THRESHOLD = 0.60;

    /**
     * Parses comma-separated or JSON array string of float values into double array
     */
    public static double[] parseDescriptor(String descriptorStr) {
        if (descriptorStr == null || descriptorStr.trim().isEmpty()) {
            return new double[0];
        }
        String clean = descriptorStr.replaceAll("[\\[\\]\\s]", "");
        String[] parts = clean.split(",");
        double[] vector = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                vector[i] = Double.parseDouble(parts[i]);
            } catch (NumberFormatException e) {
                vector[i] = 0.0;
            }
        }
        return vector;
    }

    /**
     * Calculates Euclidean distance between two face embedding vectors
     */
    public static double calculateEuclideanDistance(double[] v1, double[] v2) {
        if (v1 == null || v2 == null || v1.length == 0 || v2.length == 0) {
            return Double.MAX_VALUE;
        }
        int minLen = Math.min(v1.length, v2.length);
        double sum = 0.0;
        for (int i = 0; i < minLen; i++) {
            double diff = v1[i] - v2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * Verifies if a live captured face descriptor matches the stored face descriptor
     */
    public static boolean verifyFace(String enrolledDescriptor, String liveDescriptor) {
        if (enrolledDescriptor == null || enrolledDescriptor.trim().isEmpty()) {
            return false;
        }
        if (liveDescriptor == null || liveDescriptor.trim().isEmpty()) {
            return false;
        }
        // If exact string match (e.g. simulated token or identical vector)
        if (enrolledDescriptor.equals(liveDescriptor)) {
            return true;
        }

        double[] enrolledVec = parseDescriptor(enrolledDescriptor);
        double[] liveVec = parseDescriptor(liveDescriptor);

        if (enrolledVec.length == 0 || liveVec.length == 0) {
            return false;
        }

        double distance = calculateEuclideanDistance(enrolledVec, liveVec);
        return distance <= SIMILARITY_THRESHOLD;
    }
}
