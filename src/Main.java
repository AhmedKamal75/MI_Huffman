import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

//// checks if two files are the same
////cmp --silent file1.txt file2.txt && echo "Files are identical" || echo "Files are different"

public class Main {
//    public static void main(String[] args) throws IOException {
//        if (args.length < 4) {
//            printUsage();
//            return;
//        }
//
//        String option = args[0];
//        int n = Integer.parseInt(args[1]); // number of bytes to take in one block
//        String inputFilePath = args[2];
//        String outputFilePath = args[3];
//
//        if (option.equals("c")) {
//            compress(n, inputFilePath, outputFilePath);
//        } else if (option.equals("d")) {
//            decompress(inputFilePath, outputFilePath);
//        } else if (option.equals("a")) {
//            analyze(n, inputFilePath);
//        } else {
//            System.out.println("Invalid option: " + option);
//            printUsage();
//        }
//    }

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            printUsage();
            return;
        }

        String option = args[0];
        int n = 1; // Initialize n for analysis

        if (!validateOption(option)) {
            System.out.println("Invalid option: " + option);
            printUsage();
            return;
        }

        // Extract input and output file paths based on option
        String inputFilePath;
        String outputFilePath = null; // Output not required for analysis

        if (option.equals("c")) {
            if (args.length < 4) {
                System.out.println("Missing arguments for compression.");
                printUsage();
                return;
            }
            inputFilePath = args[1];
            outputFilePath = args[2];
            try {
                n = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid n value (must be an integer).");
                return;
            }
            compress(n, inputFilePath, outputFilePath);
        } else if (option.equals("d")) {
            if (args.length > 3) {
                System.out.println("Too many arguments for decompression.");
                printUsage();
                return;
            }
            inputFilePath = args[1];
            outputFilePath = args[2];
            decompress(inputFilePath, outputFilePath);
        } else if (option.equals("a")) {
            if (args.length > 3) {
                System.out.println("Too many arguments for analysis.");
                printUsage();
                return;
            }
            inputFilePath = args[1];
            try {
                n = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid n value (must be an integer).");
                return;
            }
            analyze(n, inputFilePath);
        } else {
            System.out.println("Invalid input.");
            printUsage();
        }
    }

    private static void compress(int n, String inputFilePath, String outputFilePath) throws IOException {
        long startTime = System.currentTimeMillis();
        Compression.compress(inputFilePath, outputFilePath, n);
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;

        // Get the size of the original file
        File inputFile = new File(inputFilePath);
        long decompressedFileSize = inputFile.length();

        // Get the size of the compressed file
        File compressedFile = new File(outputFilePath);
        long compressedFileSize = compressedFile.length();

        // Calculate the compression ratio
        double compressionRatio = (double) decompressedFileSize / compressedFileSize;

        System.out.println("----Compression completed successfully----");
        System.out.println("Time taken: " + timeTaken + " seconds");
        System.out.println("Compression ratio: " + compressionRatio);
    }

    private static void decompress(String inputFilePath, String outputFilePath) throws IOException {
        long startTime = System.currentTimeMillis();
        Compression.decompress(inputFilePath, outputFilePath);
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;

        // Get the size of the original file (compressed)
        File inputFile = new File(inputFilePath);
        long compressedFileSize = inputFile.length();

        // Get the size of the decompressed file
        File decompressedFile = new File(outputFilePath);
        long decompressedFileSize = decompressedFile.length();

        // Calculate the compression ratio
        double compressionRatio = (double) decompressedFileSize / compressedFileSize;
        System.out.println("----Decompression completed successfully----");
        System.out.println("Time taken: " + timeTaken + " seconds");
        System.out.println("Compression ratio: " + compressionRatio);
    }

    private static void analyze(int n, String inputFilePath) throws IOException {
        long startTime = System.currentTimeMillis();
        Map<ArrayList<Byte>, String> huffmanCodes = Huffman.getCode(inputFilePath, n);
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;

        // Get the size of the original file
        File inputFile = new File(inputFilePath);
        long decompressedFileSize = inputFile.length();

        // Create a temporary file for compression
        File tempCompressedFile = File.createTempFile("compressed", null);
        String tempCompressedFilePath = tempCompressedFile.getAbsolutePath();

        // Create a temporary file for decmpression
        File tempDecompressedFile = File.createTempFile("decompressed", null);
        String tempDecompressedFilePath = tempDecompressedFile.getAbsolutePath();


        try {
            // Print analysis results
            System.out.println("Analysis:");
            System.out.println("Time taken to generate Huffman codes: " + timeTaken + " seconds");
            System.out.println("Number of unique symbols encountered: " + huffmanCodes.size());
            compress(n, inputFilePath, tempCompressedFilePath);
            decompress(tempCompressedFilePath, tempDecompressedFilePath);
        } finally {
            // Delete the temporary compressed file
            tempCompressedFile.delete();
            tempDecompressedFile.delete();
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java Main [option] input_file [output_file] [n]");
        System.out.println("  option:");
        System.out.println("    c - Compress");
        System.out.println("    d - Decompress");
        System.out.println("    a - Analyze (code generation)");
        System.out.println("  input_file:");
        System.out.println("    Path to the input file (compressed or decompressed)");
        System.out.println("  output_file (required for compression/decompression):");
        System.out.println("    Path to the output file (compressed or decompressed)");
        System.out.println("  n (optional for analysis):");
        System.out.println("    Number of bytes to take in one block (symbol code)");
    }

    private static boolean validateOption(String option) {
        return option.equals("d") || option.equals("c") || option.equals("a");
    }
}
