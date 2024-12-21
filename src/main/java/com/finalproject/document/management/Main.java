package com.finalproject.document.management;

import com.finalproject.document.management.entity.MatchWord;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        // Define the regex pattern to search for dates like "August 17, 2024"
        String regex = "\\b([A-Za-z]+) (\\d{1,2}), (\\d{4})\\b" // Matches "Month Day, Year"
                + "|\\b(\\d{1,2})-(\\d{1,2})-(\\d{4})\\b";  // Matches "Day-Month-Year"
        String runText = "On August 17, 2024 new aircraft was found during maintenance 12-09-2024";
        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(runText);
        List<MatchWord> listDate = new ArrayList<>();
        String matcherPattern = null;

        // Create a Matcher object
        while (matcher.find()) {

            System.out.println("Match found: " + matcher.group());

            System.out.println("Start index: " + matcher.start());
            System.out.println("End index: " + matcher.end());

            // Determine which part of the regex matched
            if (matcher.group(1) != null) {
                matcherPattern="1";
                System.out.println("Matched format: 'Month Day, Year'");
            } else if (matcher.group(4) != null) {
                System.out.println("Matched format: 'Day-Month-Year'");
                matcherPattern="4";
            }
            listDate.add(new MatchWord(matcher.group(), matcherPattern,  matcher.start(), matcher.end(), runText));

        }

        listDate.forEach(s-> System.out.println(s.getNewData()));
    }
}
