import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Compression {
    public static void compress(String inputFilePath, String outputFilePath, int n) throws IOException {
        // Step 0: get the Huffman code
        Map<ArrayList<Byte>, String> huffmanCodes = Huffman.getCode(inputFilePath, n);

        // Step 1: Read the file and encode it.
        StringBuilder encodedData = new StringBuilder();
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
                    String code = huffmanCodes.get(key);
                    if (code != null) {
                        encodedData.append(code);
                    }
                    buffer.clear();
                }
            }
        }

        // Step 2: Write the Huffman codes and the encoded data to a file.
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFilePath))) {
            // Write the number of Huffman codes.
            out.writeInt(huffmanCodes.size());

            // Write each Huffman code.
            for (Map.Entry<ArrayList<Byte>, String> entry : huffmanCodes.entrySet()) {
                out.writeInt(entry.getKey().size());
                for (Byte b : entry.getKey()) {
                    out.writeByte(b);
                }
                out.writeUTF(entry.getValue());
            }

            // Calculate padding.
            int padding = 8 - (encodedData.length() % 8);
            padding = padding == 8 ? 0 : padding;  // No padding if length is already a multiple of 8.

            // Write the number of padding bits.
            out.writeByte(padding);

            // Add padding to the encoded data.
            for (int i = 0; i < padding; i++) {
                encodedData.append('0');
            }

            // Write the encoded data.
            for (int i = 0; i < encodedData.length(); i += 8) {
                String byteStr = encodedData.substring(i, i + 8);
                int byteVal = Integer.parseInt(byteStr, 2);
                out.writeByte(byteVal);
            }
        }
    }


    public static void decompress(String inputFilePath, String outputFilePath) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFilePath))) {
            // Read the number of Huffman codes.
            int numCodes = in.readInt();

            // Read each Huffman code.
            Map<ArrayList<Byte>, String> huffmanCodes = new HashMap<>();
            for (int i = 0; i < numCodes; i++) {
                int keySize = in.readInt();
                ArrayList<Byte> key = new ArrayList<>();
                for (int j = 0; j < keySize; j++) {
                    key.add(in.readByte());
                }
                String code = in.readUTF();
                huffmanCodes.put(key, code);
            }

            // Read the number of padding bits.
            int padding = in.readByte();

            // Read the encoded data.
            StringBuilder encodedData = new StringBuilder();
            while (in.available() > 0) {
                int byteVal = in.readByte();
                String byteStr = Integer.toBinaryString(byteVal & 0xFF);
                while (byteStr.length() < 8) {
                    byteStr = "0" + byteStr;
                }
                encodedData.append(byteStr);
            }

            // Remove padding.
            encodedData.setLength(encodedData.length() - padding);

            // Decode the data using Huffman codes.
            StringBuilder decodedData = new StringBuilder();
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < encodedData.length(); i++) {
                temp.append(encodedData.charAt(i));
                if (huffmanCodes.containsValue(temp.toString())) {
                    for (Map.Entry<ArrayList<Byte>, String> entry : huffmanCodes.entrySet()) {
                        if (entry.getValue().equals(temp.toString())) {
                            for (Byte b : entry.getKey()) {
                                decodedData.append((char) b.byteValue());
                            }
                            temp.setLength(0);
                            break;
                        }
                    }
                }
            }

            // Write the decoded data to the output file.
            try (FileOutputStream out = new FileOutputStream(outputFilePath)) {
                out.write(decodedData.toString().getBytes());
            }
        }
    }
}
