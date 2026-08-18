package com.g15.dsa.database;

/**
 * TeamParameters.java
 * Algorithm parameters derived from Michelle Nana Abena Asantewaa Sarfo's
 * index number: 22396802
 *
 * Derivation:
 * - Urgency weight  = 1.0 + (digit sum % 5) * 0.2
 *                     digit sum of 22396802 = 2+2+3+9+6+8+0+2 = 32
 *                     32 % 5 = 2  ->  1.0 + (2 * 0.2) = 1.4
 * - Road penalty    = 1.0 + (last 2 digits % 10) * 0.1
 *                     last 2 digits = 02  ->  02 % 10 = 2  ->  1.0 + (2 * 0.1) = 1.2
 * - Hash capacity   = next prime number after (100 + (index % 50))
 *                     22396802 % 50 = 2  ->  100 + 2 = 102  ->  next prime = 103
 * - Hash seed       = last 4 digits of index number = 6802
 */
public class TeamParameters {

    public static final String STUDENT_INDEX = "22396802";
    public static final String STUDENT_NAME = "Michelle Nana Abena Asantewaa Sarfo";

    public static final double URGENCY_WEIGHT = 1.4;
    public static final double ROAD_PENALTY = 1.2;
    public static final int HASH_CAPACITY = 103;
    public static final int HASH_SEED = 6802;

    public static void printParameters() {
        System.out.println("Team Parameters (derived from index 22396802):");
        System.out.println("Student Reference: " + STUDENT_NAME + " (" + STUDENT_INDEX + ")");
        System.out.println("Urgency Weight   : " + URGENCY_WEIGHT);
        System.out.println("Road Penalty     : " + ROAD_PENALTY);
        System.out.println("Hash Capacity    : " + HASH_CAPACITY);
        System.out.println("Hash Seed        : " + HASH_SEED);
    }

    public static void main(String[] args) {
        printParameters();
    }
}