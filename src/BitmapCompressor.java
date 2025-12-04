/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Jacob Lowe
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {

        // Start with 0 as the initial bit
        boolean b = false;
        int count = 0;

        // Go through the bitmap
        while (!BinaryStdIn.isEmpty()) {
            boolean bit = BinaryStdIn.readBoolean();

            // Check if the current bit is the same as the current digit's bit
            if (bit == b) {

                // Extend the current digit and use the remainder if the maximum length is reached
                if (count == 31) {
                    BinaryStdOut.write(count, 5);

                    // Start the opposite bit with length 0
                    count = 0;
                    BinaryStdOut.write(count, 5);
                }
                count++;
            }

            // Different bit; write out the current digit and start a new current digit
            else {

                // Write current digit and switch bits
                BinaryStdOut.write(count, 5);
                b = !b;
                count = 1;
            }
        }
        BinaryStdOut.write(count, 5);

        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {

        // Start with 0 as the initial bit
        boolean b = false;

        // Keep going until no more encoded parts to read
        while (!BinaryStdIn.isEmpty()) {

            // Read one sequence as a 5 bit integer
            int sequence = BinaryStdIn.readInt(5);
            for (int i = 0; i < sequence; i++) {
                BinaryStdOut.write(b);
            }

            // Switch the bit
            b = !b;
        }
        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}