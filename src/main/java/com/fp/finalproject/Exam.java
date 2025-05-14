package com.fp.finalproject;

import java.util.HashMap;
import java.util.LinkedList;

public class Exam {
    HashMap<Integer, Question> questions;
    HashMap<Integer, String> submittedAnswers;

    public Exam(){
        this.questions = new HashMap<>();
        this.submittedAnswers = new HashMap<>();
    }

    public Exam(HashMap<Integer, Question> questions, HashMap<Integer, String> submittedAnswers){
        this.questions = new HashMap<>();
        this.questions.putAll(questions);
        this.submittedAnswers = new HashMap<>();
        this.submittedAnswers.putAll(submittedAnswers);
    }
    public Exam(LinkedList<Question> questionList){
        int i=1;
        this.questions = new HashMap<>();
        for (Question question: questionList){
            this.questions.put( i ,question); i++;
        }
    }
    public void clearQuestions(){
        this.questions.clear();
        this.submittedAnswers.clear();
    }
    public Question getQuestion(int i){
        return this.questions.get(i);
    }
    public String getSubmittedAnswers(int i){
        return this.submittedAnswers.get(i);
    }
    public void printAllQuestions(){
        for (int key: this.questions.keySet()){
            System.out.println(this.questions.get(key));
        }
    }

    public HashMap<Integer, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<Integer, Question> questions) {
        this.submittedAnswers = new HashMap<>();
        this.questions.putAll(questions);
    }
    public void addSubmittedAnswer(int questionNumber, String answer) {
        if (submittedAnswers == null) {
            submittedAnswers = new HashMap<>();
        }
        submittedAnswers.put(questionNumber, answer);
    }

}
