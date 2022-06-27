package com.epam.rd.autotasks;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Words {

    private static final Pattern wordPattern = Pattern.compile("[\\p{L}|\\p{N}]{4,}");

    public String countWords(List<String> lines){
        List<String> preprocessed = Words.preprocess(lines);
        Set<String> unique = new HashSet<>(preprocessed);

        List<Entry> entries = new ArrayList<>();
        for(String s : unique){
            entries.add(Words.mapToEntry(s.toLowerCase(), preprocessed));}

        entries.sort(Comparator.comparing(Entry::getCount).reversed().thenComparing(Entry::getWord));
        int borderIndex = Words.findBorderIndex(entries);
        
        return Words.formatEntries(entries.subList(0,borderIndex));
    }

    private static int findBorderIndex(List<Entry> entries) {
        int result = 0;
        for(Entry e : entries){
            if(e.count>9) result++;
            else return result;
        }
        return result;
    }

    private static String formatEntries(List<Entry> entries) {
        StringBuilder sb = new StringBuilder();
        for(Entry e : entries){
            if(entries.indexOf(e)!=entries.size()-1)
            sb.append(e.word).append(" - ").append(e.count).append("\n");
            else sb.append(e.word).append(" - ").append(e.count);
        }
        return sb.toString();
    }

    private static Entry mapToEntry(String input, List<String> toCountFrom){
        return Entry.constructEntry(input,Collections.frequency(toCountFrom, input));
    }

    private static List<String> preprocess(List<String> lines){
        List<String> result = new ArrayList<>();
        for(String s : lines){
            result.addAll(Words.captureValues(s));
        }
        return result;
    }

    private static List<String> captureValues(String input){
        Matcher matcher = wordPattern.matcher(input);
        List<String> result = new ArrayList<>(10);
        while(matcher.find()){
            result.add(matcher.group().toLowerCase());
        }
        return result;
    }


    private static class Entry {
        private final String word;
        private final int count;

        private Entry(String word, int count) {
            this.word = word;
            this.count = count;
        }

        static Entry constructEntry(String word, int count){
            return new Entry(word, count);
        }

        @Override
        public String toString() {
            return word + " - " + count;
        }

        String getWord() {
            return word;
        }

        public int getCount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return count == entry.count && word.equals(entry.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(word, count);
        }
    }
}

