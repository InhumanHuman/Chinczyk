package com.example.chinczyk;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
    @FXML
    private ImageView green_1, green_2, green_3, green_4;
    @FXML
    private ImageView blue_1, blue_2, blue_3, blue_4;
    @FXML
    private ImageView yellow_1, yellow_2, yellow_3, yellow_4;
    
    private Double clickedX;
    private Double clickedY;
    private Node clickedNode;

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
        createFields();
        GameGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            clickedX = e.getX();
            clickedY = e.getY();
            clickedNode = getNodeFromGridPane(GameGrid,(int) Math.round(clickedX/50) ,(int) Math.round(clickedY/50));
            String color = clickedNode.getId().substring(0,clickedNode.getId().length()-2);

            if(clickedNode.getId() != null && color.equals(clientGame.color) ) {
                System.out.println("Ruszam do boju");
                moveClickedPawn();
            }
        });
    }

    @FXML
    protected void getClient(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        clientGame = (ClientGame) stage.getUserData();

        userID = clientGame.getID();
        System.out.println("Board : " + userID);

        clientGame.DiceValueList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                System.out.println("LISTENER");
                System.out.println("LICZBA: " + clientGame.diceRollNumber);
                updateDice(clientGame.diceRollNumber);
            }
        });
    }



    @FXML
    protected void onHelloButtonClick() {
        Node czerwony = getNodeFromGridPane(GameGrid, 1, 1);
        ImageView cos = (ImageView) czerwony;

        Node pole = getNodeFromGridPane(GameGrid, 1, 5);
        ImageView pole_image = (ImageView) pole;

        pole_image.setImage(cos.getImage());

        czerwony.setVisible(false);
        System.out.println(czerwony.getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pole = getNodeFromGridPane(GameGrid, fields.get(5).getX(),fields.get(5).getY());
        ImageView pole2 = (ImageView) pole;
        pole2.setImage(pole_image.getImage());
        pole_image.setVisible(false);
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

        temp_list.add(new Pawn(color, color + "_" + 1, new Field(   x  ,      y)));
        temp_list.add(new Pawn(color, color + "_" + 2, new Field( x + 1,    y)));
        temp_list.add(new Pawn(color, color + "_" + 3, new Field(   x,      y + 1)));
        temp_list.add(new Pawn(color, color + "_" + 4, new Field( x + 1,  y + 1)));

        return temp_list;
    }

    private int diceRoll()
    {
        Random r = new Random();
        random = r.nextInt(1,6);

        return random;
    }
    private boolean isPawnInBase(Field field, String color) {
        ArrayList<Pawn> pawnsInBase = new ArrayList<>();

        ArrayList<Field> redStartFields = new ArrayList<>();
        redStartFields.add(new Field(1, 1));
        redStartFields.add(new Field(2, 1));
        redStartFields.add(new Field(1, 2));
        redStartFields.add(new Field(2, 2));

        ArrayList<Field> greenStartFields = new ArrayList<>();
        greenStartFields.add(new Field(10, 1));
        greenStartFields.add(new Field(11, 1));
        greenStartFields.add(new Field(10, 2));
        greenStartFields.add(new Field(11, 2));

        ArrayList<Field> blueStartFields = new ArrayList<>();
        blueStartFields.add(new Field(10, 10));
        blueStartFields.add(new Field(11, 10));
        blueStartFields.add(new Field(10, 11));
        blueStartFields.add(new Field(11, 11));

        ArrayList<Field> yellowStartFields = new ArrayList<>();
        yellowStartFields.add(new Field(1, 10));
        yellowStartFields.add(new Field(2, 10));
        yellowStartFields.add(new Field(1, 11));
        yellowStartFields.add(new Field(2, 11));

        switch (color) {
            case "red":
                for (Field field1 : redStartFields) {
                    if (field1.getX() == field.getX() && field1.getY() == field.getY()) return true;
                }
             case "green":
                for (Field field1 : greenStartFields) {
                    if (field1.getX() == field.getX() && field1.getY() == field.getY()) return true;
                }
            case "blue":
                for (Field field1 : blueStartFields) {
                    if (field1.getX() == field.getX() && field1.getY() == field.getY()) return true;
                }
            case "yellow":
                for (Field field1 : yellowStartFields) {
                    if (field1.getX() == field.getX() && field1.getY() == field.getY()) return true;
                }
    }
        return false;
    }

    @FXML
    private void swapImage() {
        String message;

        turnNumber = clientGame.tourNumber;
        if(turnNumber % 2 == userID) {

            random = diceRoll();
            if(random != 6)
            {
                message = "tourEnd,diceRoll," + random;
                clientGame.sendToServer(message);
            }

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
            case 7:
                image = new Image(getClass().getResourceAsStream("dice_default_80x80.png"));
                dice_roll.setImage(image);
                break;
        }
    }

    private ArrayList<Pawn> getMyPawnList(String color) {
        switch(color) {
            case "red":
                return red;
            case "green":
                return green;
            case "blue":
                return blue;
            case "yellow":
                return yellow;
        }
        return null;
    }
    private Field getStartingField(String color) {
        Field startingField = new Field(0,0);
        switch(color) {
            case "red":
                startingField.setX(fields.get(startingField.RED_START_POINT).getX());
                startingField.setY(fields.get(startingField.RED_START_POINT).getY());
                return startingField;
            case "green":
                startingField.setX(fields.get(startingField.GREEN_START_POINT).getX());
                startingField.setY(fields.get(startingField.GREEN_START_POINT).getY());
                return startingField;
            case "blue":
                startingField.setX(fields.get(startingField.BLUE_START_POINT).getX());
                startingField.setY(fields.get(startingField.BLUE_START_POINT).getY());
                return startingField;
            case "yellow":
                startingField.setX(fields.get(startingField.YELLOW_START_POINT).getX());
                startingField.setY(fields.get(startingField.YELLOW_START_POINT).getY());
                return startingField;
        }
        return null;
    }

    @FXML
    private void movePawn() {

    }

    private void movePawnOutOfBase() {
        Image pawnToMove = getPawnToMove(clickedNode.getId()).getImage();
        if(clientGame.color.equals("red")) {
            Node startField = getNodeFromGridPane(GameGrid, 1, 5);
            ImageView startingField = (ImageView) startField;
            startingField.setImage(pawnToMove);
            ImageView clickedPawn = (ImageView) clickedNode;
            clickedPawn.setImage(null);
        }
        if(clientGame.color.equals("green")) {
            Node startField = getNodeFromGridPane(GameGrid, 7, 1);
            ImageView startingField = (ImageView) startField;
            startingField.setImage(pawnToMove);
            ImageView clickedPawn = (ImageView) clickedNode;
            clickedPawn.setImage(null);
        }
        if(clientGame.color.equals("blue")) {
            Node startField = getNodeFromGridPane(GameGrid, 11, 7);
            ImageView startingField = (ImageView) startField;
            startingField.setImage(pawnToMove);
            ImageView clickedPawn = (ImageView) clickedNode;
            clickedPawn.setImage(null);
        }
        if(clientGame.color.equals("yellow")) {
            Node startField = getNodeFromGridPane(GameGrid, 5, 11);
            ImageView startingField = (ImageView) startField;
            startingField.setImage(pawnToMove);
            ImageView clickedPawn = (ImageView) clickedNode;
            clickedPawn.setImage(null);
        }

    }

    private void moveClickedPawn()
    {
        ArrayList<Pawn> userPawns = getMyPawnList(clientGame.color);
        Pawn pawnToMove = null;
        for(Pawn pawn: userPawns) {
            if(pawn.getName().equals(clickedNode.getId())) pawnToMove = pawn;
        }
        // TODO - dodac z powrotem argument do sprawdzenia clientGame.diceRollNumber

        if(isPawnInBase(pawnToMove.getField(), clientGame.color))
        {
           // pawnToMove.setField(getStartingField(clientGame.color));
            System.out.println("Wychodze z bazy");
            movePawnOutOfBase();
        }
    }

    private ImageView getPawnToMove(String id) {
        System.out.println(id);
        switch (id) {
            case "red_1":
                return red_1;
            case "red_2":
                return red_2;
            case "red_3":
                return red_3;
            case "red_4":
                return red_4;
            case "green_1":
                return green_1;
            case "green_2":
                return green_2;
            case "green_3":
                return green_3;
            case "green_4":
                return green_4;
            case "blue_1":
                return blue_1;
            case "blue_2":
                return blue_2;
            case "blue_3":
                return blue_3;
            case "blue_4":
                return blue_4;
            case "yellow_1":
                return yellow_1;
            case "yellow_2":
                return yellow_2;
            case "yellow_3":
                return yellow_3;
            case "yellow_4":
                return yellow_4;
        }
        return null;
    }

}