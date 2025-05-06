package com.fp.finalproject;

public class TFQuestion extends Question {
    public TFQuestion() {
        super();
    }

    public TFQuestion(String questionText, String correctAnswer, QuestionType questionType) {
        super(questionText, correctAnswer, questionType);
    }
    @Override
    public String toString() {
        return "TFQuestion{" + this.getQuestionText()+ "\n" + "\nANS:"+this.getCorrectAnswer()+
                '}';
    }
}
