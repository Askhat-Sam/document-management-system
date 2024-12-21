package com.finalproject.document.management.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchWord {
    private String oldData;
    private String matchPattern;
    private int startIndex;
    private int endIndex;
    private String newData;
    private String text;
    private static List<String> definedAbb = new ArrayList<>();

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
            //To change to download from file !!!!!!!!!!!!!!!!!!!!!!!!!!!11
            Map<String, String> abbMap = new HashMap<>();

            abbMap.put("AOG", "Aircraft on Ground");
            abbMap.put("NLG", "Nose Landing Gear");
            abbMap.put("OCC", "Operations Control Center");
            abbMap.put("MCC", "Maintenance Control Center");

            int abbreviationLength = oldData.length();
            String trimmedOldData = oldData.replace("(", "").replace(")", "");
            String[] letters = trimmedOldData.split("");

            List<String> words = List.of(text.split(" "));

            int n = words.indexOf(oldData);

            StringBuilder wordsBeforeSb = new StringBuilder();
            StringBuilder wordsAfterSb = new StringBuilder();

            //Check if the n words before the abbreviation
            for (String s : letters) {
                wordsBeforeSb.insert(0, words.get(n - 1)).insert(0, " ");
                n--;
            }

            n = words.indexOf(oldData);

            //Check if the n words before the abbreviation
            for (String s : letters) {
                wordsAfterSb.append(words.get(n + 1)).append(" ");
                n++;
            }

            String abbDefinition = null;


            if ((abbDefinition = abbMap.get(trimmedOldData)) != null) {
                if (abbDefinition.equalsIgnoreCase(wordsBeforeSb.toString().trim())) {
                    sb.append(oldData).append(" ").append(abbMap.get(trimmedOldData));
                    definedAbb.add(trimmedOldData);
                } else if (abbDefinition.equalsIgnoreCase(wordsAfterSb.toString().trim())) {
                    sb.append(oldData).append(" ").append(abbMap.get(trimmedOldData));
                    definedAbb.add(trimmedOldData);
                } else {
                    sb.append(oldData);
                    definedAbb.add(trimmedOldData);
                }
            } else {
                System.out.println("abbreviation is not present");
                sb.append(oldData).append(" [Incorrect definition for abbreviation.");
            }

        } else if (matchPattern.equals("10")) { // "abbreviation OCC, NLG"
            if (definedAbb.contains(oldData)) {
                sb.append(oldData);
            } else {
                sb.append(oldData).append(" [Abbreviation {" + oldData + "} is not defined previously]");
            }
        }

        return new RevisedWord(sb.toString(), "Changed from [" + oldData + "] to [" + sb.toString() + "].");
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
