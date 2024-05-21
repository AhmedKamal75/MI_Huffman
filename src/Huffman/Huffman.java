package Huffman;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {
    private static HashMap<ArrayList<Byte>, Integer> getFrequencies(String inputFilePath, int n) {
        HashMap<ArrayList<Byte>, Integer> frequencies = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(inputFilePath)) {
            ArrayList<Byte> buffer = new ArrayList<>();
            int byteRead;
            boolean running = true;
            while (running) {
                running = (byteRead = fis.read()) != -1;
                if (running) {
                    buffer.add((byte) byteRead);
                }
                if (!buffer.isEmpty() && (buffer.size() == n || !running)) {
                    ArrayList<Byte> key = new ArrayList<>(buffer);
                    frequencies.put(key, frequencies.getOrDefault(key, 0) + 1);
                    buffer.clear();
                }
            }
        } catch (IOException e) {
            System.out.println("Error in the getFrequencies method");
        }
        return frequencies;
    }

    private static HashMap<ArrayList<Byte>, Double> frequenciesToProbabilities(HashMap<ArrayList<Byte> , Integer> frequencies) {
        double totalFreq = 0;
        for (Map.Entry<ArrayList<Byte>, Integer> entry : frequencies.entrySet()) {
            totalFreq += entry.getValue();
        }
        HashMap<ArrayList<Byte>, Double> probabilities = new HashMap<>();
        for (Map.Entry<ArrayList<Byte>, Integer> entry : frequencies.entrySet()) {
            probabilities.put(entry.getKey(), entry.getValue() / totalFreq);
        }
        return probabilities;
    }

    public static Map<ArrayList<Byte>, String> getCode(String inputFilePath, int n) {
        HashMap<ArrayList<Byte>, Double> probabilities = frequenciesToProbabilities(getFrequencies(inputFilePath,n));


        PriorityQueue<State> queue = new PriorityQueue<>();
        for (Map.Entry<ArrayList<Byte>, Double> entry : probabilities.entrySet()) {
            queue.add(new State(entry.getKey(), entry.getValue()));
        }

        while (queue.size() > 1) {
            State left = queue.poll();
            State right = queue.poll();
            assert right != null;
            queue.add(new State(left, right, new ArrayList<>(), left.getProbability() + right.getProbability()));
        }

        Map<ArrayList<Byte>, String> codes = new HashMap<>();
        if (!queue.isEmpty()) {
            queue.peek().buildCode("", codes);
        }


//        for (Map.Entry<ArrayList<Byte>, String> entry : codes.entrySet()) {
//            StringBuilder sb = new StringBuilder();
//            for (Byte b : entry.getKey()) {
//                sb.append((char) b.byteValue());
//            }
//            System.out.println("Symbol (" + sb.toString() + ") -> Code: " + entry.getValue());
//        }
//
//        assert queue.peek() != null;
//        queue.peek().printTree("",true);


        return codes;
    }
}
