package agh.oop.project.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter implements AutoCloseable {
    private final BufferedWriter writer;

    public CsvWriter(String fileName) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(fileName));
    }

    public void writeHeader() throws IOException {
        writer.write("\"Dzień\",\"Liczba zwierzaków\",\"Liczba roślin\",\"Liczba wolnych pól\",\"Najpopularniejszy genotyp\",\"Średni poziom energii\",\"Średnia długość życia\",\"Średnia liczba dzieci\"");
        writer.newLine();
        writer.flush();
    }

    public void write(String s) throws IOException {
        writer.write(s);
        writer.newLine();
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
