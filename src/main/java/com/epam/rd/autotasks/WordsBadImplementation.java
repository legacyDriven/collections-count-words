package com.epam.rd.autotasks;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//an example of how not to do it (;
public class WordsBadImplementation {

    private static final Pattern wordPattern = Pattern.compile("[\\p{L}|\\p{N}]{4,}");

    public String countWords(List<String> lines){
        List<String> preprocessed = preprocess(lines);
        Set<String> unique = new HashSet<>(preprocessed);

        List<Entry> entries = new ArrayList<>();
        for(String s : unique){
            entries.add(mapToEntry(s.toLowerCase(), preprocessed));}

        entries.sort((o1, o2) -> {
            int countCompare = o2.count - o1.count;
            if (countCompare != 0)
                return o2.count - o1.count;

            return o1.word.compareTo(o2.word);
        }) ;

        int borderIndex = findBorderIndex(entries);

        return formatEntries(entries.subList(0,borderIndex));
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
            result.addAll(captureValues(s));
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
        final String word;
        final int count;

        private Entry(String word, int count) {
            this.word = word;
            this.count = count;
        }

        static Entry constructEntry(String word, int count){
            return new Entry(word, count);
        }
    }
}
