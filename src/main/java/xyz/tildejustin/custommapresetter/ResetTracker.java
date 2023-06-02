package xyz.tildejustin.custommapresetter;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ResetTracker {
    public Map<String, Integer> resetCount;
    public File resetCountFile;
    private @Nullable String currentWorld;

    public void setCurrentWorld(@Nullable String currentWorld) {
        System.out.println("new currentworld = " + currentWorld);
        this.currentWorld = currentWorld;
    }

    public @Nullable String getCurrentWorld() {
        return this.currentWorld;
    }

    public ResetTracker(File countFile) {
        this.resetCount = this.readResetCountFile(countFile);
        this.resetCountFile = countFile;
    }

    public int incrementAttemptCount(String name) {
        this.resetCount.putIfAbsent(name, 0);
        this.resetCount.put(name, this.resetCount.get(name) + 1);
        this.writeResetCountFile(this.resetCount, this.resetCountFile);
        return resetCount.get(name);
    }

    private Map<String, Integer> readResetCountFile(File countFile) {
        try {
            countFile.createNewFile();
        } catch (IOException ignored) {}
        Map<String, Integer> newAttempts = new HashMap<>();
        try (Scanner countFileReader = new Scanner(countFile)) {
            if (countFileReader.hasNext()) {
                String firstLine = countFileReader.nextLine().replace("\n", "");
                if (!Objects.equals(firstLine, "")) {
                    this.setCurrentWorld(firstLine);
                } else this.setCurrentWorld(null);
            } else this.setCurrentWorld(null);
            while (countFileReader.hasNext()) {
                String[] line = countFileReader.nextLine().split(" = ");
                newAttempts.put(line[0], Integer.parseInt(line[1]));
            }
        } catch (IOException ignored) {}
        return newAttempts;
    }

    private void writeResetCountFile(Map<String, Integer> currentAttempts, File countFile) {
        try {
            countFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(countFile))) {
            String currWorld = this.getCurrentWorld() == null ? "" : this.getCurrentWorld();
            fileWriter.write(currWorld);
            fileWriter.newLine();
            currentAttempts.forEach((String name, Integer attempts) -> {
                try {
                    fileWriter.write(name);
                    fileWriter.write(" = ");
                    fileWriter.write(String.valueOf(attempts));
                    fileWriter.newLine();
                } catch (IOException ignored) {}
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException ignored) {}
    }
}
