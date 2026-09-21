package delta1.deltatest;

public class NucleotideSequence {

    private String header;
    private String sequence;

    public NucleotideSequence(String header, String sequence) {
        this.header = header;
        this.sequence = sequence.toUpperCase();
    }

    public String getHeader() {
        return header;
    }

    public String getSequence() {
        return sequence;
    }

    public void printSequence() {
        System.out.println(sequence);
    }

    public int getLength() {
        return sequence.length();
    }
//0osztas hiba kilövése, karakterekre bontas char array
    public double getGcContent() {
        if (sequence.isEmpty()) {
            return 0.0;
        }
        int gcCount = 0;
        for (char c : sequence.toCharArray()) {
            if (c == 'G' || c == 'C') {
                gcCount++;
            }
        }
        return (double) gcCount / sequence.length();
    }
//revers komplementer csinalas srtingbuilder vadaszat 
// 
    public String getReverseComplement() {
        StringBuilder revComp = new StringBuilder();
        for (int i = sequence.length() - 1; i >= 0; i--) { 
            char c = sequence.charAt(i);
            switch (c) {
                case 'A': revComp.append('T'); break;
                case 'T': revComp.append('A'); break;
                case 'C': revComp.append('G'); break;
                case 'G': revComp.append('C'); break;
                default: revComp.append(c); break;
            }
        }
        return revComp.toString();
    }

    public boolean isPalindrome() {
        return sequence.equals(getReverseComplement());
    }
}