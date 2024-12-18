package com.finalproject.document.management.entity;

public class MatchWord {
    private String oldData;
    private String matchPattern;
    private int startIndex;
    private int endIndex;
    private String newData;

    public MatchWord(String oldData, String matchPattern, int startIndex, int endIndex) {
        this.oldData = oldData;
        this.matchPattern = matchPattern;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
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

    public String getNewData() {

        String[] newDataArray;
        StringBuilder sb = new StringBuilder();

        if (matchPattern.equals("1")) {// 17 august 2024

            newDataArray =  oldData.replaceAll(",", "").split(" ");

            //Add day
            String day = String.valueOf(newDataArray[1]);
            sb.append(day).append(" ");

            //Add month
             String month = Months.valueOf(newDataArray[0].toUpperCase()).toString();
            sb.append(month.substring(0,1).toUpperCase().concat(month.substring(1).toLowerCase())).append(" ");

            //Add year
            String year = String.valueOf(newDataArray[2]);
            sb.append(year);

        } else if (matchPattern.equals("4")) { // "Day-Month-Year"

            newDataArray =  oldData.split("-");
            //Add day
            String day = String.valueOf(newDataArray[0]);
            sb.append(day).append(" ");

            //Add month
            String month = String.valueOf(Months.values()[newDataArray[1].charAt(0) == '0' ? Integer.parseInt(String.valueOf(newDataArray[1].charAt(1)))
                    : Integer.parseInt(newDataArray[1])-1]);
            sb.append(month.substring(0,1).toUpperCase().concat(month.substring(1).toLowerCase())).append(" ");

            //Add year
            String year = String.valueOf(newDataArray[2]);
            sb.append(year);

        } else if (matchPattern.equals("7")) { // "Month Day"

            newDataArray =  oldData.split(" ");

            // Add day
            String day = String.valueOf(newDataArray[1]);
            sb.append(day).append(" ");

            // Add month
            String month = String.valueOf(newDataArray[0]);
            sb.append(month).append(" ");

            // Add year
            sb.append("XXXX");

        }

        return sb.toString();
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
