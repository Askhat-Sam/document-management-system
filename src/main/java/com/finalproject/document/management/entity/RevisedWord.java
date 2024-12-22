package com.finalproject.document.management.entity;

public class RevisedWord {
    private String revisedWord;
    private String comment;

    public RevisedWord(String revisedWord, String comment) {
        this.revisedWord = revisedWord;
        this.comment = comment;
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
