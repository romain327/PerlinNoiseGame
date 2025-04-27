package com.exemple.game;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

import java.awt.image.BufferedImage;

public class GameController {
    private int gameRedValue, gameGreenValue, gameBlueValue;
    private int gameFrequency, gameThreshold;
    private final Random rand = new Random();
    private boolean musicState = false;

    private final BackgroundMusic backgroundMusic = new BackgroundMusic("src/main/resources/com/exemple/game/perlinnoise_withdrums_loop.mp3");

    @FXML private TabPane root;

    @FXML private Label redValueLabel;
    @FXML private Label greenValueLabel;
    @FXML private Label blueValueLabel;
    @FXML private Label frequencyLabel;
    @FXML private Label thresholdLabel;

    @FXML private Label hintRedValueLabel;
    @FXML private Label hintGreenValueLabel;
    @FXML private Label hintBlueValueLabel;
    @FXML private Label hintFrequencyLabel;
    @FXML private Label hintThresholdLabel;

    @FXML private Label errorLabel;
    @FXML private Label winLabel;
    @FXML private Label adviceLabel;

    @FXML private Button newGameButton;

    @FXML private TextField redValueTextField;
    @FXML private TextField greenValueTextField;
    @FXML private TextField blueValueTextField;
    @FXML private TextField frequencyTextField;
    @FXML private TextField thresholdTextField;

    @FXML private ImageView playerGeneratedImage;
    @FXML private ImageView gameGeneratedImage;

    @FXML private Button hint1Button;
    @FXML private Button hint2Button;

    @FXML private ProgressBar hint1ProgressBar;
    @FXML private Label hint1PercentageLabel;

    @FXML private Label rageQuitLabel;
    @FXML private Button rageQuitButton;

    @FXML private ImageView perlinImage1;
    @FXML private ImageView perlinImage2;
    @FXML private ImageView perlinImage3;
    @FXML private ImageView perlinImage4;

    private void updateErrorMessage(byte errorCode) {
        errorLabel.setText("");
        switch (errorCode) {
            case 2 -> errorLabel.setText("La valeur de rouge doit être comprise entre 0 et 255 !");
            case 3 -> errorLabel.setText("La valeur de vert doit être comprise entre 0 et 255 !");
            case 4 -> errorLabel.setText("La valeur de bleu doit être comprise entre 0 et 255 !");
            case 5 -> errorLabel.setText("Le temps doit être supérieur à 0 !");
            case 6 -> errorLabel.setText("La fréquence doit être comprise entre 1 et 100 !");
            case 7 -> errorLabel.setText("Le seuil doit être compris entre 40 et 80 !");
            default -> errorLabel.setText("");
        }
        errorLabel.setVisible(true);
        errorLabel.setTextFill(Color.color(1, 0, 0));
    }

    private void updateHintLabels(byte hintCode, byte hintValue) {
        switch (hintCode) {
            case 0 -> {
                if (hintValue == 0) {
                    hintRedValueLabel.setText("La valeur de rouge est correcte !");
                    hintRedValueLabel.setTextFill(Color.GREEN);
                }
                else if (hintValue == 1) {
                    hintRedValueLabel.setText("La valeur de rouge est plus élevée !");
                    hintRedValueLabel.setTextFill(Color.RED);
                }
                else {
                    hintRedValueLabel.setText("La valeur de rouge est plus basse !");
                    hintRedValueLabel.setTextFill(Color.BLUE);
                }
            }
            case 1 -> {
                if (hintValue == 0) {
                    hintGreenValueLabel.setText("La valeur de vert est correcte !");
                    hintGreenValueLabel.setTextFill(Color.GREEN);
                }
                else if (hintValue == 1) {
                    hintGreenValueLabel.setText("La valeur de vert est plus élevée !");
                    hintGreenValueLabel.setTextFill(Color.RED);
                }
                else {
                    hintGreenValueLabel.setText("La valeur de vert est plus basse !");
                    hintGreenValueLabel.setTextFill(Color.BLUE);
                }
            }
            case 2 -> {
                if (hintValue == 0) {
                    hintBlueValueLabel.setText("La valeur de bleu est correcte !");
                    hintBlueValueLabel.setTextFill(Color.GREEN);
                }
                else if (hintValue == 1) {
                    hintBlueValueLabel.setText("La valeur de bleu est plus élevée !");
                    hintBlueValueLabel.setTextFill(Color.RED);
                }
                else {
                    hintBlueValueLabel.setText("La valeur de bleu est plus basse !");
                    hintBlueValueLabel.setTextFill(Color.LIGHTBLUE);
                }
            }
            case 3 -> {
                if (hintValue == 0) {
                    hintFrequencyLabel.setText("La fréquence est correct !");
                    hintFrequencyLabel.setTextFill(Color.GREEN);
                }
                else if (hintValue == 1) {
                    hintFrequencyLabel.setText("La fréquence est plus élevé !");
                    hintFrequencyLabel.setTextFill(Color.RED);
                }
                else {
                    hintFrequencyLabel.setText("La fréquence est plus bas !");
                    hintFrequencyLabel.setTextFill(Color.BLUE);
                }
            }
            case 4 -> {
                if (hintValue == 0) {
                    hintThresholdLabel.setText("Le seuil est correct !");
                    hintThresholdLabel.setTextFill(Color.GREEN);
                }
                else if (hintValue == 1) {
                    hintThresholdLabel.setText("Le seuil est plus élevé !");
                    hintThresholdLabel.setTextFill(Color.RED);
                }
                else {
                    hintThresholdLabel.setText("Le seuil est plus bas !");
                    hintThresholdLabel.setTextFill(Color.BLUE);
                }
            }
            default -> errorLabel.setText("");
        }
    }

    private boolean compare(int redValue, int greenValue, int blueValue, int frequency, int threshold) {
        boolean red, green, blue, frequencyB, thresholdB;
        if (redValue < gameRedValue - 5) {
            red = false;
            updateHintLabels((byte) 0, (byte) 1);
        }
        else if (redValue > gameRedValue + 5) {
            red = false;
            updateHintLabels((byte) 0, (byte) -1);
        }
        else {
            red = true;
            updateHintLabels((byte) 0, (byte) 0);
        }
        if (greenValue < gameGreenValue - 5) {
            green = false;
            updateHintLabels((byte) 1, (byte) 1);
        }
        else if (greenValue > gameGreenValue + 5) {
            green = false;
            updateHintLabels((byte) 1, (byte) -1);
        }
        else {
            green = true;
            updateHintLabels((byte) 1, (byte) 0);
        }
        if (blueValue < gameBlueValue - 5) {
            blue = false;
            updateHintLabels((byte) 2, (byte) 1);
        }
        else if (blueValue  > gameBlueValue + 5) {
            blue = false;
            updateHintLabels((byte) 2, (byte) -1);
        }
        else {
            blue = true;
            updateHintLabels((byte) 2, (byte) 0);
        }
        if (frequency < gameFrequency - 4) {
            frequencyB = false;
            updateHintLabels((byte) 3, (byte) 1);
        }
        else if (frequency > gameFrequency + 4) {
            frequencyB = false;
            updateHintLabels((byte) 3, (byte) -1);
        }
        else {
            frequencyB = true;
            updateHintLabels((byte) 3, (byte) 0);
        }
        if ( threshold < gameThreshold - 2) {
            thresholdB = false;
            updateHintLabels((byte) 4, (byte) 1);
        }
        else if (threshold > gameThreshold + 2) {
            thresholdB = false;
            updateHintLabels((byte) 4, (byte) -1);
        }
        else {
            thresholdB = true;
            updateHintLabels((byte) 4, (byte) 0);
        }
        return red && green && blue && frequencyB && thresholdB;
    }

    private double calculateProgression(int redValue, int greenValue, int blueValue, int frequency, int threshold) {
        double distanceRed = 0, distanceGreen = 0, distanceBlue = 0, distanceFrequency = 0, distanceThreshold = 0;

        int distanceRedValue = Math.abs(redValue - gameRedValue);
        if (distanceRedValue > 5)
            distanceRed = distanceRedValue / 255.0;

        int distanceGreenValue = Math.abs(greenValue - gameGreenValue);
        if (distanceGreenValue > 5)
            distanceGreen = distanceGreenValue / 255.0;

        int distanceBlueValue = Math.abs(blueValue - gameBlueValue);
        if (distanceBlueValue > 5)
            distanceBlue = distanceBlueValue / 255.0;

        int distanceFrequencyValue = Math.abs(frequency - gameFrequency);
        if (distanceFrequencyValue > 4)
            distanceFrequency = (double) distanceFrequencyValue / 200.0;

        int distanceThresholdValue = Math.abs(threshold - gameThreshold);
        if (distanceThresholdValue > 2)
            distanceThreshold = (double) distanceThresholdValue / 255.0;

        return 1 - ((distanceRed + distanceGreen + distanceBlue + distanceFrequency + distanceThreshold) / 5);
    }

    @FXML
    public void initialize() {
        backgroundMusic.play();
        musicState = true;

        redValueTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updatePlayerGeneratedImage(null);
            }
        });

        greenValueTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updatePlayerGeneratedImage(null);
            }
        });

        blueValueTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updatePlayerGeneratedImage(null);
            }
        });

        frequencyTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updatePlayerGeneratedImage(null);
            }
        });

        thresholdTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updatePlayerGeneratedImage(null);
            }
        });
    }

    @FXML public void onRageQuitButtonClick(ActionEvent event) {
        rageQuitLabel.setText("Les valeurs exactes sont :\n" +
                "rouge : " + gameRedValue + "\n" +
                "vert : " + gameGreenValue + "\n" +
                "bleu : " + gameBlueValue + "\n" +
                "fréquence : " + gameFrequency + "\n" +
                "seuil : " + gameThreshold);
        rageQuitLabel.setVisible(true);
    }

    @FXML public void onHint1ButtonClick(ActionEvent event) {
        hint1ProgressBar.setVisible(true);
        hint1PercentageLabel.setVisible(true);
    }

    @FXML public void onHint2ButtonClick(ActionEvent event) {
        hintRedValueLabel.setVisible(true);
        hintGreenValueLabel.setVisible(true);
        hintBlueValueLabel.setVisible(true);
        hintThresholdLabel.setVisible(true);
        hintFrequencyLabel.setVisible(true);
    }

    @FXML public void onMusicButtonClick(ActionEvent event) {
        if (musicState) {
            backgroundMusic.stop();
            musicState = false;
        }
        else {
            backgroundMusic.play();
            musicState = true;
        }
    }

    @FXML public void onQuit(Event event) {
        backgroundMusic.stop();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML public void onPerlinNoiseTabClick(Event event) {
        perlinImage1.setImage(SwingFXUtils.toFXImage(PerlinNoise.getNoiseImage(200, 200, 200.0f/255, 96.0f/255, 168.0f/255, 5, 50), null));
        perlinImage2.setImage(SwingFXUtils.toFXImage(PerlinNoise.getNoiseImage(200, 200, 200.0f/255, 96.0f/255, 168.0f/255, 50, 50), null));
        perlinImage3.setImage(SwingFXUtils.toFXImage(PerlinNoise.getNoiseImage(200, 200, 200.0f/255, 96.0f/255, 168.0f/255, 50, 70), null));
        perlinImage4.setImage(SwingFXUtils.toFXImage(PerlinNoise.getNoiseImage(200, 200, 50.0f/255, 190.0f/255, 220.0f/255, 5, 50), null));
        perlinImage1.setVisible(true);
        perlinImage2.setVisible(true);
        perlinImage3.setVisible(true);
        perlinImage4.setVisible(true);
    }

    @FXML
    public void onGameTabClick(Event event) {
        winLabel.setText("");
        winLabel.setVisible(true);

        rageQuitLabel.setVisible(false);
        rageQuitButton.setVisible(false);

        adviceLabel.setVisible(false);

        hint1ProgressBar.setVisible(false);
        hint1ProgressBar.setProgress(0.0);
        hint1PercentageLabel.setVisible(false);

        playerGeneratedImage.setVisible(false);
        gameGeneratedImage.setVisible(false);

        redValueLabel.setVisible(false);
        greenValueLabel.setVisible(false);
        blueValueLabel.setVisible(false);
        frequencyLabel.setVisible(false);
        thresholdLabel.setVisible(false);

        hintRedValueLabel.setVisible(false);
        hintGreenValueLabel.setVisible(false);
        hintBlueValueLabel.setVisible(false);
        hintFrequencyLabel.setVisible(false);
        hintThresholdLabel.setVisible(false);

        redValueTextField.setVisible(false);
        greenValueTextField.setVisible(false);
        blueValueTextField.setVisible(false);
        frequencyTextField.setVisible(false);
        thresholdTextField.setVisible(false);

        errorLabel.setVisible(false);

        hint1Button.setVisible(false);
        hint2Button.setVisible(false);
    }

    private void colorGenerator() {
        int colorNumber = rand.nextInt(3);
        switch (colorNumber) {
            case 0:
                gameRedValue = rand.nextInt(255);
                gameGreenValue = rand.nextInt(255);
                if(Math.abs(gameRedValue - gameGreenValue) < 80) {
                    if (gameRedValue < 100 || gameGreenValue < 100)
                        gameBlueValue = rand.nextInt(55) + 200;
                    else
                        gameBlueValue = rand.nextInt(55);
                }
            case 1:
                gameGreenValue = rand.nextInt(255);
                gameBlueValue = rand.nextInt(255);
                if(Math.abs(gameGreenValue - gameBlueValue) < 80) {
                    if (gameGreenValue < 100 || gameBlueValue < 100)
                        gameRedValue = rand.nextInt(55) + 200;
                    else
                        gameRedValue = rand.nextInt(55);
                }
            case 2:
                gameBlueValue = rand.nextInt(255);
                gameRedValue = rand.nextInt(255);
                if(Math.abs(gameBlueValue - gameRedValue) < 80) {
                    if (gameBlueValue < 100 || gameRedValue < 100)
                        gameGreenValue = rand.nextInt(55) + 200;
                    else
                        gameGreenValue = rand.nextInt(55);
                }
        }
    }

    @FXML
    public void onNewGameButtonClick(ActionEvent event) {
        float gameRedFactor, gameGreenFactor, gameBlueFactor;
        colorGenerator();

        gameRedFactor = (float) gameRedValue/255;
        gameGreenFactor = (float) gameGreenValue/255;
        gameBlueFactor = (float) gameBlueValue/255;

        gameFrequency = rand.nextInt(100) + 1;
        gameThreshold = rand.nextInt(41) + 40;

        BufferedImage image = PerlinNoise.getNoiseImage(512, 300, gameRedFactor, gameGreenFactor, gameBlueFactor, gameFrequency, gameThreshold);
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        gameGeneratedImage.setImage(fxImage);

        playerGeneratedImage.setVisible(true);
        gameGeneratedImage.setVisible(true);

        adviceLabel.setVisible(true);

        redValueLabel.setVisible(true);
        greenValueLabel.setVisible(true);
        blueValueLabel.setVisible(true);
        frequencyLabel.setVisible(true);
        thresholdLabel.setVisible(true);

        redValueTextField.setVisible(true);
        greenValueTextField.setVisible(true);
        blueValueTextField.setVisible(true);
        frequencyTextField.setVisible(true);
        thresholdTextField.setVisible(true);

        hint1Button.setVisible(true);
        hint2Button.setVisible(true);

        rageQuitButton.setVisible(true);

        hint1PercentageLabel.setText("0%");

        hintRedValueLabel.setText("");
        hintGreenValueLabel.setText("");
        hintBlueValueLabel.setText("");
        hintFrequencyLabel.setText("");
        hintThresholdLabel.setText("");
    }

    @FXML public void updatePlayerGeneratedImage(InputMethodEvent event) {
        int redValue, greenValue, blueValue;
        int frequency, threshold;

        float redFactor, greenFactor, blueFactor;
        try {
            redValue = Integer.parseInt(redValueTextField.getText());
            greenValue = Integer.parseInt(greenValueTextField.getText());
            blueValue = Integer.parseInt(blueValueTextField.getText());

            frequency = Integer.parseInt(frequencyTextField.getText());
            threshold = Integer.parseInt(thresholdTextField.getText());
        }
        catch (NumberFormatException e) {
            return;
        }

        updateErrorMessage((byte) 0);

        if (redValue < 0 || redValue > 255) {
            updateErrorMessage((byte) 2);
            return;
        }

        if (greenValue < 0 || greenValue > 255) {
            updateErrorMessage((byte) 3);
            return;
        }

        if (blueValue < 0 || blueValue > 255) {
            updateErrorMessage((byte) 4);
            return;
        }

        if (frequency < 1 || frequency > 100) {
            updateErrorMessage((byte) 6);
            return;
        }

        if (threshold < 40 || threshold > 80) {
            updateErrorMessage((byte) 7);
            return;
        }

        redFactor = (float) redValue /255;
        greenFactor = (float) greenValue /255;
        blueFactor = (float) blueValue /255;
        BufferedImage image = PerlinNoise.getNoiseImage(512, 300, redFactor, greenFactor, blueFactor, frequency, threshold);
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        playerGeneratedImage.setImage(fxImage);
        double progress = calculateProgression(redValue, greenValue, blueValue, frequency, threshold);
        hint1ProgressBar.setProgress(progress);
        hint1PercentageLabel.setText(Math.round(progress * 100) + "%");
        boolean winCondition = compare(redValue, greenValue, blueValue, frequency, threshold);
        if (winCondition) {
            winLabel.setText("Bravo, vous avez gagné !\n" +
                    "Les valeurs exactes sont :\n" +
                    "rouge : " + gameRedValue + "\n" +
                    "vert : " + gameGreenValue + "\n" +
                    "bleu : " + gameBlueValue + "\n" +
                    "fréquence : " + gameFrequency + "\n" +
                    "seuil : " + gameThreshold);
        }
    }
}