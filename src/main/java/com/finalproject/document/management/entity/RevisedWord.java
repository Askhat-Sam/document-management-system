package com.finalproject.document.management.entity;

public class RevisedWord {
    private String revisedWord;
    private String comment;
    private int offSet;

    public RevisedWord(String revisedWord, String comment, int offSet) {
        this.revisedWord = revisedWord;
        this.comment = comment;
        this.offSet = offSet;
    }

    public String getRevisedWord() {
        return revisedWord;
    }

    public void setRevisedWord(String revisedWord) {
        this.revisedWord = revisedWord;
    }

    public String getComment() {
        return comment;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "RevisedWord{" +
                "revisedWord='" + revisedWord + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
