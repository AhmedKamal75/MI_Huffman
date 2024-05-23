## Huffman Coding Compression and Decompression

This project implements a Huffman coding system for data compression and decompression in Java. Huffman coding assigns shorter codes to frequently occurring symbols, resulting in efficient compression of data.

**Table of Contents**

* [Introduction](#introduction)
* [Implementation](#implementation)
* [Usage](#usage)
    * [Compress](#compress)
    * [Decompress](#decompress)
    * [Analyze](#analyze)
* [Compilation](#compilation)
* [Examples](#examples)
* [File Structure](#file-structure)
* [Credits](#credits)

## Introduction

Huffman coding is a technique for data compression that assigns variable-length codes to symbols based on their frequency of appearance. This program allows you to explore Huffman coding with various file sizes and block sizes to understand how these factors impact compression and decompression performance.

## Implementation

The system is built using three main classes:

* `State`: Manages nodes in the Huffman tree, including methods for building codes and printing the tree structure.
* `Huffman`: Generates Huffman codes from an input file by calculating symbol frequencies, building the Huffman tree, and creating a code map.
* `Compression`: Handles compressing and decompressing files using the generated Huffman codes.

## Usage

The program supports three main operations: compression, decompression, and analysis. Each operation is specified by a command-line option.

### Compress

To compress a file, use the `c` option followed by the input file path, the output file path (where the compressed file will be saved), and the block size (n).

```sh
java Main c inputFilePath outputFilePath n
```

**Example:**

```sh
java Main c file1.txt file2.huf 2
```

This command compresses `file1.txt` to `file2.huf` using a block size of 2 bytes.

### Decompress

To decompress a file, use the `d` option followed by the input file path (the compressed file) and the output file path (where the decompressed file will be saved).

```sh
java Main d inputFilePath outputFilePath
```

**Example:**

```sh
java Main d file2.huf file2.txt
```

This command decompresses `file2.huf` to `file2.txt`.

### Analyze

To analyze the Huffman coding of a file, use the `a` option followed by the input file path and the block size (n).

```sh
java Main a inputFilePath n
```

**Example:**

```sh
java Main a file1.txt 2
```

This command analyzes `file1.txt` using a block size of 2 bytes.

## Compilation

Compile the Java source code using the following command:

```sh
javac Main.java
```

## Examples

The provided examples demonstrate how to use the program for compression, decompression, and analysis.

## File Structure

The project consists of the following Java files:

* `Main.java`: The program's entry point.
* `State.java`: Manages nodes in the Huffman tree.
* `Huffman.java`: Generates Huffman codes from the input file.
* `Compression.java`: Compresses and decompresses files using Huffman codes.