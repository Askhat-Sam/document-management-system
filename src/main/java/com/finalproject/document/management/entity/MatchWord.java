package com.finalproject.document.management.entity;

import org.springframework.validation.MapBindingResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatchWord {
    private String oldData;
    private String matchPattern;
    private int startIndex;
    private int endIndex;
    private String newData;
    private String text;
    private static List<String> definedAbb = new ArrayList<>();
    //To change to download from file !!!!!!!!!!!!!!!!!!!!!!!!!!!11
//    Map<String, String> abbMap = new HashMap<>(Map.of(
//            "AOG", "Aircraft on Ground",
//            "NLG", "Nose Landing Gear",
//            "OCC", "Operations Control Center",
//            "MCC", "Maintenance Control Center"
//    ));

    private final Map<String, String> abbMap = mapCSVToMap("src/main/resources/abbreviation.csv");

    public MatchWord(String oldData, String matchPattern, int startIndex, int endIndex, String text) {
        this.oldData = oldData;
        this.matchPattern = matchPattern;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.text = text;
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

    public static Map<String, String> mapCSVToMap(String filePath ) {
        Map<String, String> resultMap = new HashMap<>();
       try {
           Stream<String> lines = Files.lines(Paths.get(filePath));
           resultMap = lines.map(line->line.split(",")).collect(Collectors.toMap(line->line[0], line->line[1]));
       } catch (IOException e) {
           e.printStackTrace();
       }
        System.out.println(resultMap);

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

            return new RevisedWord(sb.toString(), "Changed from [" + oldData + "] to [" + sb.toString() + "].");

        } else if (matchPattern.equals("4")) { // "Day-Month-Year"

            newDataArray = oldData.split("-");
            //Add day
            String day = String.valueOf(newDataArray[0]);
            sb.append(day).append(" ");

            //Add month
            String month = String.valueOf(Months.values()[newDataArray[1].charAt(0) == '0' ? Integer.parseInt(String.valueOf(newDataArray[1].charAt(1)))
                    : Integer.parseInt(newDataArray[1]) - 1]);
            sb.append(month.substring(0, 1).toUpperCase().concat(month.substring(1).toLowerCase())).append(" ");

            //Add year
            String year = String.valueOf(newDataArray[2]);
            sb.append(year);
            return new RevisedWord(sb.toString(), "Changed from [" + oldData + "] to [" + sb.toString() + "].");
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
            return new RevisedWord(sb.toString(), "Changed from [" + oldData + "] to [" + sb.toString() + "].");
        } else if (matchPattern.equals("9")) { // "abbreviation"

            return getRevisedWord(sb);

        } else if (matchPattern.equals("10")) {
            // Check if the word is in abbreviation list
            if (getRevisedWord(sb).getComment().equals("Incorrect definition for abbreviation")) {
                // "abbreviation OCC, NLG"
                if (definedAbb.contains(oldData)) {
                    return new RevisedWord(oldData, "No changes");
                } else {
                    sb.append(oldData);
                    return new RevisedWord(sb.toString(), " [Abbreviation {" + oldData + "} is not defined previously]");
                }
            }else {
                return getRevisedWord(sb);
            }
        }

        return null;
    }

    private RevisedWord getRevisedWord(StringBuilder sb) {
        int abbreviationLength = oldData.length();
        String trimmedOldData = oldData.replace("(", "").replace(")", "");
        String[] letters = trimmedOldData.split("");

        List<String> words = List.of(text.split(" "));

        int n = words.indexOf(oldData);

        StringBuilder wordsBeforeSb = new StringBuilder();
        StringBuilder wordsAfterSb = new StringBuilder();

        //Check if the n words before the abbreviation
        if (n>=3) {
            for (String s : letters) {
                    wordsBeforeSb.insert(0, words.get(n - 1)).insert(0, " ");
                n--;
            }
        }

        n = words.indexOf(oldData);

        //Check if the n words before the abbreviation
        if (words.size()-n >3) {
            for (String s : letters) {
                wordsAfterSb.append(words.get(n + 1)).append(" ");
                n++;
            }
        }

        String abbDefinition = null;


        if ((abbDefinition = abbMap.get(trimmedOldData)) != null) {
            if (abbDefinition.equalsIgnoreCase(wordsBeforeSb.toString().trim())) {
                sb.append(oldData).append(" ").append(abbMap.get(trimmedOldData));
                definedAbb.add(trimmedOldData);
                return new RevisedWord(sb.toString(), "Changed from [" + oldData + " " + wordsBeforeSb.toString().trim() + "] to [" + sb.toString() + "].");
            } else if (abbDefinition.equalsIgnoreCase(wordsAfterSb.toString().trim())) {
                sb.append(oldData).append(" ").append(abbMap.get(trimmedOldData));
                definedAbb.add(trimmedOldData);
                return new RevisedWord(sb.toString(), "Changed from [" + oldData + " " + wordsAfterSb.toString().trim() + "] to [" + sb.toString() + "].");
            } else {
                definedAbb.add(trimmedOldData);
                return new RevisedWord(oldData, "No changes");
            }
        } else {
            return new RevisedWord(oldData, "Incorrect definition for abbreviation");
        }
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
