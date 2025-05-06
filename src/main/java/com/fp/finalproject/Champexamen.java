package com.fp.finalproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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

        VBox root = new VBox();
        MenuBar menuBarMain = buildMenuBar();
        Label labelGrade = new Label("Grade: ");
        HBox hBoxGrade = new HBox(labelGrade);
        hBoxGrade.setAlignment(Pos.CENTER);
        root.getChildren().add(menuBarMain);
        root.getChildren().add(buildBanner());
        root.getChildren().add(hBoxGrade);


        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("ChampExamen (R) application (C)");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}