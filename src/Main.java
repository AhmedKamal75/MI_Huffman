import Huffman.Compression;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String input = "data/test.txt";
        String outputCompress = "data/test_compressed.txt";
        String outputDecompress = "data/test_decompressed.txt";

        try {
            Compression.compress(input, outputCompress, 1);
            Compression.decompress(outputCompress, outputDecompress);
        } catch (IOException e){
            System.out.println("Error in operation of Huffman" + outputCompress);
        }

    }
}