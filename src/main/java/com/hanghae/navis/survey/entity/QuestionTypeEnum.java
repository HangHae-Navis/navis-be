package com.hanghae.navis.survey.entity;

public enum QuestionTypeEnum {
    SHORT_ANSWER(QuestionType.SHORT_ANSWER),
    MULTIPLE_CHOICE(QuestionType.MULTIPLE_CHOICE),
    CHECKBOX(QuestionType.CHECKBOX);

    private final String type;

    QuestionTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static class QuestionType {
        public static final String SHORT_ANSWER = "SHORT_ANSWER";
        public static final String MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
        public static final String CHECKBOX = "CHECKBOX";
    }
}
