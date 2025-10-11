package com.example;

/**
 * compilez:
 * D:\GithubRepositories\CS-UBB-FMI-INFO\3rd Year\PPD\tema_lab_1\tema_lab_1\src\main\java git:[main]
 * javac --release 8 -d ../../out com/example/*.java
 * <p>
 * rulez:
 * D:\GithubRepositories\CS-UBB-FMI-INFO\3rd Year\PPD\tema_lab_1\tema_lab_1\src\out git:[main]
 * .\com\example\scriptJ.ps1 com.example.Main 1 10
 */

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int N, M, n, p;
        int[][] matrix;
        int[][] convMatrix;

        N = Utils.getN();
        M = Utils.getM();
        n = Utils.getn();


        p = Utils.getp();

        if (args.length > 0) {
            p = Integer.parseInt(args[0]);
        }

        System.out.println("threads: " + p);

        //! doar prima rulare
//        Utils.generateMatrix(N, M, n, p);

        matrix = Utils.getMatrix();
        convMatrix = Utils.getConvMatrix();


//?        SECVENTIAL
//        long startTime = System.nanoTime();
//        Secvential secvential = new Secvential(N, M, n, matrix, convMatrix);
//        int[][] result = secvential.run();
//        long endTime = System.nanoTime();
//
//        Utils.writeToFile(result);



//?         Linii
//        long startTime = System.nanoTime();
//        Linii linii = new Linii(N, M, n, p, matrix, convMatrix);
//        int[][] result = linii.run();
//        long endTime = System.nanoTime();
//
//        Utils.writeToFile(result);
//
//        if (!Utils.compareOutputFiles()) {
//            System.err.println("Outputurile nu coincid");
//        }



//?         Coloane
        long startTime = System.nanoTime();
        Coloane coloane = new Coloane(N, M, n, p, matrix, convMatrix);
        int[][] result = coloane.run();
        long endTime = System.nanoTime();

        Utils.writeToFile(result);

        if (!Utils.compareOutputFiles()) {
            System.err.println("Outputurile nu coincid");
        }


        System.out.println((double) (endTime - startTime) / 1E6);
    }
}