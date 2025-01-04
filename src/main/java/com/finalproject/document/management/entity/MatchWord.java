package com.finalproject.document.management.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatchWord {
    private final String oldData;
    private final String matchPattern;
    private final int startIndex;
    private final int endIndex;
    private String newData;
    private final String text;
    private static final List<String> definedAbb = new ArrayList<>();
    private final int matchedWordLength;

    private final Map<String, String> abbMap = mapCSVToMap("src/main/resources/abbreviation.csv");

    public MatchWord(String oldData, String matchPattern, int startIndex, int endIndex, String text, int matchedWordLength) {
        this.oldData = oldData;
        this.matchPattern = matchPattern;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.text = text;
        this.matchedWordLength = matchedWordLength;

    }

    public String getOldData() {
        return oldData;
    }

    public String getMatchPattern() {
        return matchPattern;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public String getText() {
        return text;
    }

    public int getMatchedWordLength() {
        return abbMap.get(oldData.replace("(", "").replace(")", "")).length();
    }

    public Map<String, String> getAbbMap() {
        return abbMap;
    }

    public static Map<String, String> mapCSVToMap(String filePath) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            Stream<String> lines = Files.lines(Paths.get(filePath));
            resultMap = lines.map(line -> line.split(",")).collect(Collectors.toMap(line -> line[0], line -> line[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public RevisedWord getNewData() {

        String[] newDataArray;
        StringBuilder sb = new StringBuilder();

        if (matchPattern.equals("1")) {// 17 august 2024

            newDataArray = oldData.replaceAll(",", "").split(" ");

            //Add day
            String day = String.valueOf(newDataArray[1]);
            sb.append(day).append(" ");

            //Add month
            String month = Months.valueOf(newDataArray[0].toUpperCase()).toString();
            sb.append(month.substring(0, 1).toUpperCase().concat(month.substring(1).toLowerCase())).append(" ");

            //Add year
            String year = String.valueOf(newDataArray[2]);
            sb.append(year);

            return new RevisedWord(sb.toString(), "Need to be changed from [" + oldData + "] to [" + sb.toString() + "].", 0);

        } else if (matchPattern.equals("4")) { // "Day-Month-Year"

            newDataArray = oldData.split("-");
            //Add day
            String day = String.valueOf(newDataArray[0]);
            sb.append(day).append(" ");

            //Add month
            String month = String.valueOf(Months.values()[newDataArray[1].charAt(0) == '0' ? Integer.parseInt(String.valueOf(newDataArray[1].charAt(1))) - 1
                    : Integer.parseInt(newDataArray[1]) - 1]);
            sb.append(month.substring(0, 1).toUpperCase().concat(month.substring(1).toLowerCase())).append(" ");

            //Add year
            String year = String.valueOf(newDataArray[2]);
            sb.append(year);
            return new RevisedWord(sb.toString(), " Day-Month-Year Need to be changed from [" + oldData + "] to [" + sb.toString() + "].", 0);
        } else if (matchPattern.equals("7")) { // "Month Day"

            newDataArray = oldData.split(" ");

            // Add day
            String day = String.valueOf(newDataArray[1]);
            sb.append(day).append(" ");

            // Add month
            String month = String.valueOf(newDataArray[0]);
            sb.append(month).append(" ");

            // Add year
            sb.append("XXXX");
            return new RevisedWord(sb.toString(), "Need to be changed from [" + oldData + "] to [" + sb.toString() + "].", 0);
        } else if (matchPattern.equals("9")) { // "abbreviation"

            return getRevisedWord(sb);

        } else if (matchPattern.equals("10")) {
            RevisedWord revisedWord = null;
            // Get revised word
            if (definedAbb.contains(oldData)) {
                return new RevisedWord(oldData, "Not changed", 0);
            } else {
                revisedWord = getRevisedWord(sb);
            }


            // Check if the word is in abbreviation list
            System.out.println("revised word " + revisedWord);
            if (revisedWord.getComment().equals("No definition found")) {
//                 "abbreviation OCC, NLG"
                if (definedAbb.contains(oldData)) {
                    return new RevisedWord(oldData, "Not changed", 0);
                } else {
                    definedAbb.add(oldData);
                    return new RevisedWord(revisedWord.getRevisedWord(), "Abbreviation [" + oldData + "] is not defined previously. " +
                            "Probably need to be corrected to [" + revisedWord.getRevisedWord() + "]", 0);
                }
            } else {
                System.out.println("Wrong branch");
                return revisedWord;
            }
        }

        return null;
    }

    private RevisedWord getRevisedWord(StringBuilder sb) {
        int abbreviationLength = oldData.length();
        String trimmedOldData = oldData.replace("(", "").replace(")", "");
        String[] letters = trimmedOldData.split("");

        List<String> words = new ArrayList<>(List.of(text.split(" ")));
        words.replaceAll(w -> w.replace(";", "").replace(".", "")
                .replace("(", "").replace(")", "").replace(",", ""));

        int n = words.indexOf(trimmedOldData);

        StringBuilder wordsBeforeSb = new StringBuilder();
        StringBuilder wordsAfterSb = new StringBuilder();

        //Check if the n words before the abbreviation
        if (n >= letters.length) {
            for (String s : letters) {
                wordsBeforeSb.insert(0, words.get(n - 1)).insert(0, " ");
                n--;
            }
        }
//        System.out.println("Words: " + words);
        n = words.indexOf(trimmedOldData);
//        System.out.println("n is : " + n);
//        System.out.println(words.size());
//        System.out.println(Arrays.toString(letters));

        //Check if the n words after the abbreviation
        if (words.size() - n > letters.length) {
            for (String s : letters) {
                wordsAfterSb.append(words.get(n + 1)).append(" ");
                n++;
            }
        }
        String abbDefinition = null;


        if ((abbDefinition = abbMap.get(trimmedOldData)) != null) {
            if (abbDefinition.equalsIgnoreCase(wordsBeforeSb.toString().trim())) {
                if (abbDefinition.equals(wordsBeforeSb.toString().trim())) {
                    definedAbb.add(trimmedOldData);
                    return new RevisedWord(oldData, "Not changed", 0);
                } else {
                    sb.append(abbMap.get(trimmedOldData)).append(" ").append(oldData);
                    definedAbb.add(trimmedOldData);
                    return new RevisedWord(sb.toString(), "Need to be changed from [" + wordsBeforeSb.toString().trim() + " " + oldData + "] to [" + sb.toString() + "].", wordsBeforeSb.toString().length());
                }
            } else if (abbDefinition.equalsIgnoreCase(wordsAfterSb.toString().trim())) {
                if (abbDefinition.equals(wordsAfterSb.toString().trim())) {
                    return new RevisedWord(oldData, "Not changed", 0);
                } else {
                    sb.append(oldData).append(" ").append(abbMap.get(trimmedOldData));
                    definedAbb.add(trimmedOldData);
                    return new RevisedWord(sb.toString(), "Need to be changed from [" + oldData + " " + wordsAfterSb.toString().trim() + "] to [" + sb.toString() + "].", wordsAfterSb.toString().length());
                }
            } else {
                sb.append(oldData).append(" (").append(abbMap.get(trimmedOldData)).append(")");
                System.out.println("SB contex: " + sb.toString());
                definedAbb.add(trimmedOldData);
                return new RevisedWord(sb.toString(), "Need to be changed from [" + wordsBeforeSb.toString().trim() + " " + oldData + "] to [" + sb.toString() + "].", wordsBeforeSb.toString().length());
            }
        }
        return new RevisedWord(oldData, "No definition found", 0);
//        } else {
//            return new RevisedWord(oldData, "No definition found in the map",0);
//        }
    }

    public enum Months {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER
    }


    @Override
    public String toString() {
        return "MatchWord{" +
                "oldData='" + oldData + '\'' +
                ", matchPattern='" + matchPattern + '\'' +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", newData='" + newData + '\'' +
                '}';
    }
}
