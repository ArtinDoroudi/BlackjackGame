package com.example.blackjack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackjackGame extends Application {

    // Constants for the card images
    private static final int NUM_CARDS = 52;
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 150;

    // Variables for the game
    private ImageView[] playerCardViews = new ImageView[5];
    private ImageView[] dealerCardViews = new ImageView[5];
    private List<Integer> playerHand = new ArrayList<>();
    private List<Integer> dealerHand = new ArrayList<>();
    private Label resultLabel;
    private boolean isGameFinished;

    @Override
    public void start(Stage primaryStage) {
        HBox playerCardBox = new HBox(10);
        playerCardBox.setPadding(new Insets(10));

        HBox dealerCardBox = new HBox(10);
        dealerCardBox.setPadding(new Insets(10));

        for (int i = 0; i < 5; i++) {
            playerCardViews[i] = new ImageView();
            playerCardViews[i].setFitWidth(CARD_WIDTH);
            playerCardViews[i].setFitHeight(CARD_HEIGHT);
            playerCardBox.getChildren().add(playerCardViews[i]);

            dealerCardViews[i] = new ImageView();
            dealerCardViews[i].setFitWidth(CARD_WIDTH);
            dealerCardViews[i].setFitHeight(CARD_HEIGHT);
            dealerCardBox.getChildren().add(dealerCardViews[i]);
        }

        Button hitButton = new Button("Hit");
        hitButton.setOnAction(e -> hit());

        Button standButton = new Button("Stand");
        standButton.setOnAction(e -> stand());

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> newGame());

        resultLabel = new Label();

        VBox root = new VBox(10, dealerCardBox, playerCardBox, hitButton, standButton, newGameButton, resultLabel);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Blackjack Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        newGame();
    }

    private void newGame() {
        playerHand.clear();
        dealerHand.clear();
        resultLabel.setText("");
        isGameFinished = false;

        // Clear all card images
        for (ImageView cardView : playerCardViews) {
            cardView.setImage(null);
        }
        for (ImageView cardView : dealerCardViews) {
            cardView.setImage(null);
        }

        dealInitialCards();
    }

    private void dealInitialCards() {
        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            int playerCard = random.nextInt(NUM_CARDS) + 1;
            playerHand.add(playerCard);
            String playerCardImagePath = "/card/" + playerCard + ".png";
            Image playerCardImage = new Image(getClass().getResourceAsStream(playerCardImagePath));
            playerCardViews[i].setImage(playerCardImage);

            int dealerCard = random.nextInt(NUM_CARDS) + 1;
            dealerHand.add(dealerCard);
            String dealerCardImagePath = "/card/" + dealerCard + ".png";
            Image dealerCardImage = new Image(getClass().getResourceAsStream(dealerCardImagePath));
            dealerCardViews[i].setImage(dealerCardImage);
        }
    }

    private void hit() {
        if (isGameFinished) {
            resultLabel.setText("Game finished. Start a new game.");
            return;
        }

        Random random = new Random();
        int cardNumber = random.nextInt(NUM_CARDS) + 1;
        playerHand.add(cardNumber);
        String cardImagePath = "/card/" + cardNumber + ".png";
        Image cardImage = new Image(getClass().getResourceAsStream(cardImagePath));
        playerCardViews[playerHand.size() - 1].setImage(cardImage);

        if (calculateHandValue(playerHand) > 21) {
            resultLabel.setText("Player busts! Dealer wins.");
            isGameFinished = true;
        }
    }

    private void stand() {
        if (isGameFinished) {
            resultLabel.setText("Game finished. Start a new game.");
            return;
        }

        while (calculateHandValue(dealerHand) < 17) {
            Random random = new Random();
            int cardNumber = random.nextInt(NUM_CARDS) + 1;
            dealerHand.add(cardNumber);
            String cardImagePath = "/card/" + cardNumber + ".png";
            Image cardImage = new Image(getClass().getResourceAsStream(cardImagePath));
            dealerCardViews[dealerHand.size() - 1].setImage(cardImage);
        }

        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        if (dealerValue > 21 || playerValue > dealerValue) {
            resultLabel.setText("Player wins!");
        } else if (playerValue < dealerValue) {
            resultLabel.setText("Dealer wins!");
        } else {
            resultLabel.setText("It's a tie!");
        }

        isGameFinished = true;
    }

    private int calculateHandValue(List<Integer> hand) {
        int value = 0;
        int aces = 0;

        for (int card : hand) {
            int cardValue = (card - 1) % 13 + 1;
            if (cardValue > 10) {
                cardValue = 10;
            }
            if (cardValue == 1) {
                aces++;
                cardValue = 11;
            }
            value += cardValue;
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
