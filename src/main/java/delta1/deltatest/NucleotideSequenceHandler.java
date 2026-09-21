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
//
    public void gcRendezes() {
        for (int i = 0; i < sequences.size(); i++) {
            for (int j = i + 1; j < sequences.size(); j++) {
                NucleotideSequence seq1 = sequences.get(i);
                NucleotideSequence seq2 = sequences.get(j);

                if (seq1.getGcContent() < seq2.getGcContent()) {
                    sequences.set(i, seq2);
                    sequences.set(j, seq1);
                }
            }
        }

        System.out.println();
        System.out.println("--- GC-tartalom csokkeno sorrendben ---");
        for (NucleotideSequence seq : sequences) {
            System.out.println("Nev: " + seq.getHeader() + " | GC-arany: " + seq.getGcContent());
        }
    }

    public void hasitohelyKereso(String site) {
        int totalOccurrences = 0;

        for (NucleotideSequence seq : sequences) {
            String s = seq.getSequence();
            int siteLength = site.length();

            for (int i = 0; i <= s.length() - siteLength; i++) {
                String fragment = s.substring(i, i + siteLength);
                if (fragment.equals(site)) {
                    totalOccurrences++;
                }
            }
        }

        System.out.println();
        System.out.println("--- Endonukleaz ('" + site + "') talalatok ---");
        System.out.println("Osszesen " + totalOccurrences + " alkalommal talalhato meg a fajlban.");
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
        handler.gcRendezes();
        handler.hasitohelyKereso("TTTAAA");
    }
}