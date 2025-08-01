package com.marketmind;

import org.springframework.web.bind.annotation.*;
import smile.classification.RandomForest;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PredictionController {
    private final RandomForest model;

    public PredictionController() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("model.ser"));
        model = (RandomForest) ois.readObject();
        ois.close();
    }

    @PostMapping("/predict")
    public Map<String, Integer> predict(@RequestBody Map<String, Double> input) {
        double[] features = {
            input.get("Open"),
            input.get("High"),
            input.get("Low"),
            input.get("Close"),
            input.get("Volume"),
            input.get("Close") - input.get("Open")
        };
        int prediction = model.predict(features);
        return Collections.singletonMap("prediction", prediction);
    }
}
