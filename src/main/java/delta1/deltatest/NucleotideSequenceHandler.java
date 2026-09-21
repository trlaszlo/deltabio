package delta1.deltatest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NucleotideSequenceHandler {

    private final List<NucleotideSequence> sequences;

    public NucleotideSequenceHandler() {
        sequences = new ArrayList<>();
    }

    public void readFasta(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentHeader = "";
            StringBuilder currentSequence = new StringBuilder();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith(">")) {
                    if (!currentHeader.isEmpty()) {
                        sequences.add(new NucleotideSequence(currentHeader, currentSequence.toString()));
                        currentSequence.setLength(0);
                    }
                    currentHeader = line.substring(1);
                } else {
                    currentSequence.append(line);
                }
            }
            if (!currentHeader.isEmpty()) {
                sequences.add(new NucleotideSequence(currentHeader, currentSequence.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTotalBases() {
        int total = 0;
        for (NucleotideSequence seq : sequences) {
            total += seq.getLength();
        }
        return total;
    }

    public NucleotideSequence getLongerSequence(NucleotideSequence seq1, NucleotideSequence seq2) {
        if (seq1.getLength() >= seq2.getLength()) {
            return seq1;
        }
        return seq2;
    }

    public void sortByLength() {
        sequences.sort(Comparator.comparingInt(NucleotideSequence::getLength));
    }

    public NucleotideSequence getHigherGcSequence(NucleotideSequence seq1, NucleotideSequence seq2) {
        if (seq1.getGcContent() >= seq2.getGcContent()) {
            return seq1;
        }
        return seq2;
    }

    public void sortByGcContent() {
        sequences.sort(Comparator.comparingDouble(NucleotideSequence::getGcContent));
    }

    public boolean containsSubstring(NucleotideSequence mainSeq, NucleotideSequence subSeq) {
        return mainSeq.getSequence().contains(subSeq.getSequence());
    }

    public List<NucleotideSequence> getSequences() {
        return sequences;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: Missing input FASTA file path.");
            return;
        }

        String filePath = args[0];
        NucleotideSequenceHandler handler = new NucleotideSequenceHandler();
        handler.readFasta(filePath);

        for (NucleotideSequence seq : handler.getSequences()) {
            System.out.println("Name: " + seq.getHeader() + " | Length: " + seq.getLength());
        }
    }
}