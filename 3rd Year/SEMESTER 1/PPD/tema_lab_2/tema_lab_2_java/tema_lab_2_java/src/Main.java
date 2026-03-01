import java.io.IOException;

public class Main {

    static class TestConfig {
        String inputFile;
        String outputSeq;
        String outputPar;

        public TestConfig(String inputFile) {
            this.inputFile = inputFile;

            // Extract the base name from the input file path
            int lastSlash = Math.max(inputFile.lastIndexOf('/'), inputFile.lastIndexOf('\\'));
            String fileName = (lastSlash == -1) ? inputFile : inputFile.substring(lastSlash + 1);

            // Remove "date_" prefix and ".txt" suffix
            String baseName;
            int datePos = fileName.indexOf("date_");
            int txtPos = fileName.lastIndexOf(".txt");

            if (datePos != -1 && txtPos != -1) {
                baseName = fileName.substring(datePos + 5, txtPos);
            } else {
                baseName = fileName;
            }

            // Get directory path from input file
            String dirPath = (lastSlash == -1) ? "" : inputFile.substring(0, lastSlash + 1);

            this.outputSeq = dirPath + "output_" + baseName + "_seq.txt";
            this.outputPar = dirPath + "output_" + baseName + ".txt";
        }
    }

    static double runSequential(String inputFile, String outputFile) throws IOException {
        Utils.DataInput data = Utils.readData(inputFile);

        SecventialDinamic seq = new SecventialDinamic(data.N, data.M, data.n);
        seq.loadData(data.matrix, data.convMatrix);

        long startTime = System.nanoTime();
        seq.run();
        long endTime = System.nanoTime();

//        Utils.writeToFile(outputFile, seq.getMatrix());

        return (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
    }

    static double runParallel(String inputFile, String outputFile, int threads)
            throws IOException, InterruptedException {
        Utils.DataInput data = Utils.readData(inputFile);

        LiniiDinamic par = new LiniiDinamic(data.N, data.M, data.n, threads);
        par.loadData(data.matrix, data.convMatrix);

        long startTime = System.nanoTime();
        par.run();
        long endTime = System.nanoTime();

//        Utils.writeToFile(outputFile, par.getMatrix());

        return (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <threads>");
            System.exit(1);
        }

        String basePath = "src/data/";
        String date_10_10_3_2 = basePath + "date_10_10_3_2.txt";
        String date_1000_1000_3_2 = basePath + "date_1000_1000_3_2.txt";
        String date_10000_10000_3_2 = basePath + "date_10000_10000_3_2.txt";

        int threads = Integer.parseInt(args[0]);

         String inputFile = date_10_10_3_2;
//         String inputFile = date_1000_1000_3_2;
//        String inputFile = date_10000_10000_3_2;

        TestConfig config = new TestConfig(inputFile);

        try {
            double time;
            String outputFile;

            if (threads == 0) {
                outputFile = config.outputSeq;
                time = runSequential(inputFile, outputFile);
                System.out.println(time);
            } else {
                outputFile = config.outputPar;
                time = runParallel(inputFile, outputFile, threads);
                System.out.println(time);

//                Utils.compareFiles(config.outputSeq, config.outputPar);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}