package com.marketmind;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import smile.classification.RandomForest;
import smile.data.*;
import smile.data.formula.Formula;
import smile.io.Read;
import smile.math.MathEx;
import smile.data.type.DataTypes;

public class StockPredictor {
    private static final String DATA_PATH = "src/main/resources/sp500_sample.csv";
    private static final String MODEL_PATH = "model.ser";

    public static void main(String[] args) {
        try {
            // Load and parse CSV
            DataFrame data = Read.csv(DATA_PATH, CSVFormat.DEFAULT.withHeader());

            // Feature engineering
            double[] priceChange = data.doubleVector("Close").toDoubleArray();
            for (int i = 0; i < priceChange.length; i++) {
                priceChange[i] = data.getDouble(i, "Close") - data.getDouble(i, "Open");
            }
            data = data.merge(DoubleColumn.of("Price_Change", priceChange));

            int[] target = new int[data.nrows() - 1];
            for (int i = 0; i < data.nrows() - 1; i++) {
                target[i] = data.getDouble(i + 1, "Close") > data.getDouble(i, "Close") ? 1 : 0;
            }

            DataFrame features = data.select("Open", "High", "Low", "Close", "Volume", "Price_Change").slice(0, data.nrows() - 1);

            // Train model
            RandomForest model = RandomForest.fit(Formula.lhs(""), features.toArray(new double[0][]), target);
            System.out.println("Model trained.");

            // Save model
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MODEL_PATH));
            oos.writeObject(model);
            oos.close();
            System.out.println("Model saved to " + MODEL_PATH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
