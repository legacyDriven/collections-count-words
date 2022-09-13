package com.epam.rd.autotasks;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Words {

    private static final Comparator<Map.Entry<String, Integer>> valueComparator = (o1, o2) -> o2.getValue() - o1.getValue();

    private static final Comparator<Map.Entry<String, Integer>> entryComparator = valueComparator.thenComparing(Map.Entry::getKey);

    private static final Pattern wordPattern = Pattern.compile("[\\p{L}|\\p{N}]{4,}");

    private final Map<String, Integer> capturedValues;

    public Words() {
        capturedValues = new ConcurrentHashMap<>();
    }

    public String countWords(List<String> lines) {
        capturedValues.clear();
        for (String line : lines){
            addToMap(captureValues(line));
        }
        removeObsoleteEntries(capturedValues);
        List<Map.Entry<String, Integer>> result = new ArrayList<>(capturedValues.entrySet());
        result.sort(entryComparator);
        return entryListToString(result);
    }

    private void removeObsoleteEntries(Map<String, Integer> argument){
        for (Map.Entry<String, Integer> entry : argument.entrySet()){
            if(entry.getValue()<10) argument.remove(entry.getKey());
        }
    }

    private void addToMap(List<String> values){
        for(String s : values){
            if(capturedValues.containsKey(s)) capturedValues.put(s, capturedValues.get(s) +1);
            else capturedValues.put(s, 1);
        }
    }

    private static List<String> captureValues(String input){
        Matcher matcher = wordPattern.matcher(input);
        List<String> result = new ArrayList<>(10);
        while(matcher.find()){
            result.add(matcher.group().toLowerCase());
        }
        return result;
    }

    private String entryListToString(List<Map.Entry<String, Integer>> argument){
        StringJoiner sj = new StringJoiner("\n");
        for(Map.Entry<String, Integer> entry : argument){
            sj.add(entry.getKey() + " - " + entry.getValue());
        }
        return sj.toString();
    }
}