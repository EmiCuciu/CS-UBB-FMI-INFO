package com.example;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Runs both implementations (sequential and parallel) on the SAME input files
 * and asserts that the produced results contain the same (id -> totalGrade) mapping,
 * regardless of output ordering.
 */
public class ResultsComparisonTest {

    private static final Path DATA_DIR = Path.of("src", "main", "java", "com", "example", "data");

    @Test
    void sequentialAndParallelProduceSameResults() throws Exception {
        // Run sequential
        Secvential.main(new String[0]);

        // Run parallel (p=4, p_r=2) - tweak if you want other configurations as well.
        Parallel.main(new String[]{"4", "2"});

        Map<Integer, Integer> sequential = readResults(DATA_DIR.resolve("rezultate.txt"));
        Map<Integer, Integer> parallel = readResults(DATA_DIR.resolve("rezultate_paralel.txt"));

        assertEquals(sequential, parallel,
                "Sequential and parallel outputs differ. Compare rezultate.txt vs rezultate_paralel.txt");
    }

    @Test
    void parallelMatchesSequentialForMultipleThreadConfigs() throws Exception {
        // Baseline
        Secvential.main(new String[0]);
        Map<Integer, Integer> sequential = readResults(DATA_DIR.resolve("rezultate.txt"));

        // A few configurations asked in the assignment.
        // (No executors; just calling your existing main entry points.)
        String[][] configs = {
                {"4", "1"},
                {"4", "2"},
                {"8", "1"},
                {"8", "2"},
                {"16", "1"},
                {"16", "2"}
        };

        for (String[] cfg : configs) {
            Parallel.main(cfg);
            Map<Integer, Integer> parallel = readResults(DATA_DIR.resolve("rezultate_paralel.txt"));
            assertEquals(sequential, parallel,
                    "Mismatch for config p=" + cfg[0] + ", p_r=" + cfg[1]);
        }
    }

    private static Map<Integer, Integer> readResults(Path file) {
        // TreeMap => stable ordering for easier diffs when debugging.
        Map<Integer, Integer> map = new TreeMap<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid line in " + file + ": '" + line + "'");
                }

                int id = Integer.parseInt(parts[0].trim());
                int nota = Integer.parseInt(parts[1].trim());

                Integer old = map.put(id, nota);
                if (old != null) {
                    throw new IllegalStateException("Duplicate ID in result file " + file + ": " + id);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return map;
    }
}

