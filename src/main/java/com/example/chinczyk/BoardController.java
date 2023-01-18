package com.example.chinczyk;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    @FXML
    GridPane GameGrid;
    @FXML
    private ImageView dice_roll;
    @FXML
    private ImageView red_1, red_2, red_3, red_4;
    private ImageView green_1, green_2, green_3, green_4;
    private ImageView blue_1, blue_2, blue_3, blue_4;
    private ImageView yellow_1, yellow_2, yellow_3, yellow_4;

    private ArrayList<Field> fields = new ArrayList<Field>();
    private ArrayList<Pawn> red = new ArrayList<Pawn>();
    private ArrayList<Pawn> green = new ArrayList<Pawn>();
    private ArrayList<Pawn> blue = new ArrayList<Pawn>();
    private ArrayList<Pawn> yellow = new ArrayList<Pawn>();
    private int random = 0;

    private ClientGame clientGame;
    private Stage myStage;
    private int userID;
    private int turnNumber = 1;
    private ObservableList<String> listenerForUpdate = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createPawns();
        System.out.println(red.get(2).getField().toString());

        EventHandler<MouseEvent> mouseEventHandler = mouseEvent ->
        {
        };
        red_1.setOnMouseClicked(mouseEventHandler);



        red_1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(random == 6)
            {

            }
            event.consume();
        });
    }

    @FXML
    protected void getClient(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        clientGame = (ClientGame) stage.getUserData();

        userID = clientGame.getID();
        System.out.println("Board : " + userID);
    }



    @FXML
    protected void onHelloButtonClick() {
        Node czerwony = getNodeFromGridPane(GameGrid, 1, 1);
        czerwony.setVisible(false);
        System.out.println(czerwony.getId());

    }


    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private ArrayList<Field> createFields()
    {
        for(int i = 1; i < 5; i++)
        {
            fields.add(new Field(i,5));
        }
        for(int i = 5; i >= 1; i--)
        {
            fields.add(new Field(5,i));
        }

        fields.add(new Field(6,1));

        for(int i = 1; i < 5; i++)
        {
            fields.add(new Field(7,i));
        }
        for(int i = 7; i <= 11; i++)
        {
            fields.add(new Field(i,5));
        }

        fields.add(new Field(11,6));

        for(int i = 11; i > 7; i--)
        {
            fields.add(new Field(i,7));
        }
        for(int i = 7; i <= 11; i++)
        {
            fields.add(new Field(7,i));
        }

        fields.add(new Field(6,11));

        for(int i = 11; i > 7; i--)
        {
            fields.add(new Field(5,i));
        }
        for(int i = 5; i >= 1; i--)
        {
            fields.add(new Field(i,7));
        }

        fields.add(new Field(1,6));

        return fields;
    }

    private void createPawns()
    {
        red = createColorPawns("red", 1,1);
        green = createColorPawns("green", 10,1);
        blue = createColorPawns("blue", 10,10);
        yellow = createColorPawns("yellow", 1,10);
    }

    private ArrayList<Pawn> createColorPawns(String color, int x, int y)
    {
        ArrayList<Pawn> temp_list = new ArrayList<>();

        temp_list.add(new Pawn(color, color + "_" + 1, new Field(x,y)));
        temp_list.add(new Pawn(color, color + "_" + 2, new Field(x + 1,y)));
        temp_list.add(new Pawn(color, color + "_" + 3, new Field(x,y + 1)));
        temp_list.add(new Pawn(color, color + "_" + 4, new Field(x + 1,y + 1)));

        return temp_list;
    }

    private int diceRoll()
    {
        Random r = new Random();
        random = r.nextInt(1,6);

        return random;
    }

    @FXML
    private void swapImage() {
        if(turnNumber == userID)
        {
            clientGame.listenerList.addListener(new ListChangeListener<String>() {
                @Override
                public void onChanged(Change<? extends String> change) {
                    updateDice(clientGame.diceRollNumber);
                }
            });
        }
        String message;

        turnNumber = clientGame.tourNumber;
        System.out.println(turnNumber);
        // TODO - naprawa ID Board zeby byly dobre ID,
        //  Wysylanie tury, Wysylanie kostki
        //  Zablokowanie kostki po nacisnieciu
        if(turnNumber % 2 == userID) {

            int random = diceRoll();
            if(random != 6)
            {
                System.out.println("Nacisnalem kostke");
                message = "tourEnd,diceRoll," + random;
                clientGame.sendToServer(message);
            }
            Image image;

//            switch (random) {
//                case 1:
//                    image = new Image(getClass().getResourceAsStream("dice_1_80x80.png"));
//                    dice_roll.setImage(image);
//                    break;
//                case 2:
//                    image = new Image(getClass().getResourceAsStream("dice_2_80x80.png"));
//                    dice_roll.setImage(image);
//                    break;
//                case 3:
//                    image = new Image(getClass().getResourceAsStream("dice_3_80x80.png"));
//                    dice_roll.setImage(image);
//                    break;
//                case 4:
//                    image = new Image(getClass().getResourceAsStream("dice_4_80x80.png"));
//                    dice_roll.setImage(image);
//                    break;
//                case 5:
//                    image = new Image(getClass().getResourceAsStream("dice_5_80x80.png"));
//                    dice_roll.setImage(image);
//                    break;
//                case 6:
//                    image = new Image(getClass().getResourceAsStream("dice_6_80x80.png"));
//                    dice_roll.setImage(image);
//                    break;
//            }
        }
    }

    private void updateDice(int rollNumber) {
        Image image;
        switch(rollNumber) {
            case 1:
                image = new Image(getClass().getResourceAsStream("dice_1_80x80.png"));
                dice_roll.setImage(image);
                break;
            case 2:
                image = new Image(getClass().getResourceAsStream("dice_2_80x80.png"));
                dice_roll.setImage(image);
                break;
            case 3:
                image = new Image(getClass().getResourceAsStream("dice_3_80x80.png"));
                dice_roll.setImage(image);
                break;
            case 4:
                image = new Image(getClass().getResourceAsStream("dice_4_80x80.png"));
                dice_roll.setImage(image);
                break;
            case 5:
                image = new Image(getClass().getResourceAsStream("dice_5_80x80.png"));
                dice_roll.setImage(image);
                break;
            case 6:
                image = new Image(getClass().getResourceAsStream("dice_6_80x80.png"));
                dice_roll.setImage(image);
                break;
        }
    }

/*
    private boolean outOfBase(String color)
    {
        if(random == 6 && checkBase("red_1"))
        {
            return true;
        }

    }

    private boolean checkBase(String color)
    {

    }
*/
    @FXML
    private void movePawn()
    {
    }


}