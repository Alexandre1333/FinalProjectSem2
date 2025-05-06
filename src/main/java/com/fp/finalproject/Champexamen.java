package com.fp.finalproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Champexamen extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readMCQ("src/main/resources/tfq.txt");

        int[] indices = {11, 0, 5};

        LinkedList<Question> examQuestionList = myBank.selectRandQuestions(indices);
        Exam myExam = new Exam(examQuestionList);
        myExam.printAllQuestions();

        System.out.println();
        Label hello = new Label("Hello World");
        Scene scene = new Scene(hello, 320,240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}