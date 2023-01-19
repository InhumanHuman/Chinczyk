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
import java.util.SortedMap;

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

    private Image redImage = new Image(getClass().getResourceAsStream("player_model_red_49x49.png"));
    private Image greenImage = new Image(getClass().getResourceAsStream("player_model_green_49x49.png"));
    private Image blueImage = new Image(getClass().getResourceAsStream("player_model_blue_49x49.png"));
    private Image yellowImage = new Image(getClass().getResourceAsStream("player_model_yellow_49x49.png"));

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
    private boolean isRolled = false;
    private ObservableList<String> listenerForUpdate = FXCollections.observableArrayList();
    private ArrayList<Field> redBase = new ArrayList<>();
    private ArrayList<Field> greenBase = new ArrayList<>();
    private ArrayList<Field> blueBase = new ArrayList<>();
    private ArrayList<Field> yellowBase = new ArrayList<>();
    private int[] baseStats = {0,0,0,0};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createPawns();
        createFields();
        initializeBases();
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
    private void initializeBases() {
        redBase.add(new Field(5,6));
        redBase.add(new Field(4,6));
        redBase.add(new Field(3,6));
        redBase.add(new Field(2,6));
        greenBase.add(new Field(6,5));
        greenBase.add(new Field(6,4));
        greenBase.add(new Field(6,3));
        greenBase.add(new Field(6,2));
        blueBase.add(new Field(7,6));
        blueBase.add(new Field(8,6));
        blueBase.add(new Field(9,6));
        blueBase.add(new Field(10,6));
        yellowBase.add(new Field(6,7));
        yellowBase.add(new Field(6,8));
        yellowBase.add(new Field(6,9));
        yellowBase.add(new Field(6,10));
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
                System.out.println("LICZBA: " + clientGame.diceRollNumber);
                updateDice(clientGame.diceRollNumber);
            }
        });
        clientGame.PawnsValueList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                System.out.println("LISTENER");
                updatePawns();
            }
        });
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
        random = r.nextInt(1,7);

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

    private void updatePawns()
    {

    }

    @FXML
    private void swapImage() {
        String message;

        turnNumber = clientGame.tourNumber;
        if(turnNumber % 2 == userID) {
            isRolled = true;

            random = diceRoll();
            if(random != 6)
            {
                //if wszystkie pionki w bazie

                //Wysłanie stanu kostki
                message = "diceRoll," + random;
                clientGame.sendToServer(message);

                //Wysłanie informacji o końcu tury
                message = "tourEnd";
                clientGame.sendToServer(message);



                //if jakis pionek (przynajmniej jedne) poza baza
            }
            if(random == 6)
            {
                message = "diceRoll," + random;
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

    private void movePawnOutOfBase() {
        if(isRolled)
        {
            Image pawnToMove = getPawnToMove(clickedNode.getId()).getImage();
            if(clientGame.color.equals("red")) {
                Node startField = getNodeFromGridPane(GameGrid, 1, 5);
                ImageView startingField = (ImageView) startField;
                startingField.setImage(pawnToMove);
                startField.setId(clickedNode.getId());
                ImageView clickedPawn = (ImageView) clickedNode;
                clickedPawn.setImage(null);
                int pawnNumber = Integer.parseInt(clickedNode.getId().substring(clickedNode.getId().length()-1,clickedNode.getId().length()))-1;
                clickedNode.setId(null);
                red.get(pawnNumber).setField(new Field(1,5));
            }
            if(clientGame.color.equals("green")) {
                Node startField = getNodeFromGridPane(GameGrid, 7, 1);
                ImageView startingField = (ImageView) startField;
                startingField.setImage(pawnToMove);
                startField.setId(clickedNode.getId());
                ImageView clickedPawn = (ImageView) clickedNode;
                clickedPawn.setImage(null);
                int pawnNumber = Integer.parseInt(clickedNode.getId().substring(clickedNode.getId().length()-1,clickedNode.getId().length()))-1;
                green.get(pawnNumber).setField(new Field(7,1));
                clickedNode.setId(null);
            }
            if(clientGame.color.equals("blue")) {
                Node startField = getNodeFromGridPane(GameGrid, 11, 7);
                ImageView startingField = (ImageView) startField;
                startingField.setImage(pawnToMove);
                startField.setId(clickedNode.getId());
                ImageView clickedPawn = (ImageView) clickedNode;
                clickedPawn.setImage(null);
                int pawnNumber = Integer.parseInt(clickedNode.getId().substring(clickedNode.getId().length()-1,clickedNode.getId().length()))-1;
                clickedNode.setId(null);
                blue.get(pawnNumber).setField(new Field(11,7));
            }
            if(clientGame.color.equals("yellow")) {
                Node startField = getNodeFromGridPane(GameGrid, 5, 11);
                ImageView startingField = (ImageView) startField;
                startingField.setImage(pawnToMove);
                startField.setId(clickedNode.getId());
                ImageView clickedPawn = (ImageView) clickedNode;
                clickedPawn.setImage(null);
                int pawnNumber = Integer.parseInt(clickedNode.getId().substring(clickedNode.getId().length()-1,clickedNode.getId().length()))-1;
                clickedNode.setId(null);
                yellow.get(pawnNumber).setField(new Field(5,11));
            }
            isRolled = false;
        }
    }

    private int getCurrentField() {
        String id = clickedNode.getId();
        int pawnNumber = Integer.parseInt(clickedNode.getId().substring(id.length()-1,id.length()))-1;
        int fieldCurrentPosX = 0;
        int fieldCurrentPosY = 0;
        switch (clientGame.color) {
            case "red":
                fieldCurrentPosX = red.get(pawnNumber).getField().getX();
                fieldCurrentPosY = red.get(pawnNumber).getField().getY();
                for(int i=0;i< fields.size();i++) {
                    if(fieldCurrentPosX == fields.get(i).getX() && fieldCurrentPosY == fields.get(i).getY()) return i;
                }
            case "green":
                 fieldCurrentPosX = green.get(pawnNumber).getField().getX();
                 fieldCurrentPosY = green.get(pawnNumber).getField().getY();
                for(int i=0;i< fields.size();i++) {
                    if(fieldCurrentPosX == fields.get(i).getX() && fieldCurrentPosY == fields.get(i).getY()) return i;
                }
            case "blue":
                fieldCurrentPosX = blue.get(pawnNumber).getField().getX();
                fieldCurrentPosY = blue.get(pawnNumber).getField().getY();
                for(int i=0;i< fields.size();i++) {
                    if(fieldCurrentPosX == fields.get(i).getX() && fieldCurrentPosY == fields.get(i).getY()) return i;
                }
            case "yellow":
                fieldCurrentPosX = yellow.get(pawnNumber).getField().getX();
                fieldCurrentPosY = yellow.get(pawnNumber).getField().getY();
                for(int i=0;i< fields.size();i++) {
                    if(fieldCurrentPosX == fields.get(i).getX() && fieldCurrentPosY == fields.get(i).getY()) return i;
                }
        }
        return -1;
    }

    private Image getPawnImage() {
        switch(clientGame.color)
        {
            case "red":
                return redImage;
            case "green":
                return greenImage;
            case "blue":
                return blueImage;
            case "yellow":
                return yellowImage;
        }
        return null;
    }

    @FXML
    public void movePawn()
    {

    }

    private void movePawnForward() {
        //TODO: NAPRAWIĆ ID, USTAWIA SIĘ NA NULL
        if(isRolled)
        {
            //Image pawnToMove = getPawnToMove(clickedNode.getId()).getImage();
            System.out.println(clickedNode.getId());

            //Zwracanie Image danego koloru
            Image pawnToMove = getPawnImage();

            //Pobranie aktualnego pola piona ktorego nacisnelismy
            int currField = getCurrentField();
            System.out.println("CURRFIELD: " + currField);


            //Wyliczenie wartosci pola na ktore przeniesiemy piona
            int nextField = currField + random;

            //Utworzenie Field z nowym miejscem piona
            Field newField = new Field (fields.get(nextField).getX(), fields.get(nextField).getY());

            //Pobranie numery piona - 1 (Liczenie od 0)
            int pawnNumber = Integer.parseInt(clickedNode.getId().substring(clickedNode.getId().length()-1,clickedNode.getId().length()))-1;

            // Pobranie Node pola do ktorego chcemy sie przeniesc
            Node next = getNodeFromGridPane(GameGrid, fields.get(nextField).getX(),fields.get(nextField).getY());
            ImageView fieldToMove = (ImageView) next;

            System.out.println("NEXTFIELD: " + nextField);
            switch (clientGame.color) {
                case "red":
                    if (nextField >= 39) {
                        newField = redBase.get(baseStats[0]);
                        red.get(pawnNumber).setField(newField);
                        baseStats[0]++;
                        next.setId(clickedNode.getId());
                        clickedNode.setId(null);

                        // Wyslanie wiadomosci do serwera o przeniesieniu pionka
                        String message = "pawnMoved," + next.getId() + "," + newField ;
                        clientGame.sendToServer(message);
                        isRolled = false;
                        return;
                    }
                case "green":
                    if(currField < 9 && nextField>=9) {
                        newField = greenBase.get(baseStats[1]);
                        green.get(pawnNumber).setField(newField);
                        baseStats[1]++;
                        next.setId(clickedNode.getId());
                        clickedNode.setId(null);

                        // Wyslanie wiadomosci do serwera o przeniesieniu pionka
                        String message = "pawnMoved," + next.getId() + "," + newField ;
                        clientGame.sendToServer(message);
                        isRolled = false;
                        return;
                    }
                    else if(nextField>39) {
                        nextField-=39;
                        newField = new Field (fields.get(nextField).getX(), fields.get(nextField).getY());
                        green.get(pawnNumber).setField(newField);
                        next.setId(clickedNode.getId());
                        clickedNode.setId(null);
                        // Wyslanie wiadomosci do serwera o przeniesieniu pionka
                        String message = "pawnMoved," + next.getId() + "," + newField ;
                        clientGame.sendToServer(message);
                        isRolled = false;
                        return;
                    }
                case "blue":
                    if(currField < 19 && nextField>=19) {
                        newField = blueBase.get(baseStats[2]);
                        blue.get(pawnNumber).setField(newField);
                        baseStats[2]++;
                        next.setId(clickedNode.getId());
                        clickedNode.setId(null);

                        // Wyslanie wiadomosci do serwera o przeniesieniu pionka
                        String message = "pawnMoved," + next.getId() + "," + newField ;
                        clientGame.sendToServer(message);
                        isRolled = false;
                        return;
                    }
                    else if(nextField>39) {
                        nextField-=39;
                        newField = new Field (fields.get(nextField).getX(), fields.get(nextField).getY());
                        blue.get(pawnNumber).setField(newField);
                        next.setId(clickedNode.getId());
                        clickedNode.setId(null);
                        // Wyslanie wiadomosci do serwera o przeniesieniu pionka
                        String message = "pawnMoved," + next.getId() + "," + newField ;
                        clientGame.sendToServer(message);
                        isRolled = false;
                        return;
                    }
                case "yellow":
                    if(currField < 29 && nextField>=29) {
                        newField = yellowBase.get(baseStats[3]);
                        yellow.get(pawnNumber).setField(newField);
                        baseStats[3]++;
                        next.setId(clickedNode.getId());
                        clickedNode.setId(null);

                        // Wyslanie wiadomosci do serwera o przeniesieniu pionka
                        String message = "pawnMoved," + next.getId() + "," + newField ;
                        clientGame.sendToServer(message);
                        isRolled = false;
                        return;
                    }
                    else if(nextField>39) {
                        nextField-=39;
                        newField = new Field (fields.get(nextField).getX(), fields.get(nextField).getY());
                        yellow.get(pawnNumber).setField(newField);
                        next.setId(clickedNode.getId());
                        clickedNode.setId(null);
                        // Wyslanie wiadomosci do serwera o przeniesieniu pionka
                        String message = "pawnMoved," + next.getId() + "," + newField ;
                        clientGame.sendToServer(message);
                        isRolled = false;
                        return;
                    }
            }


            //Ustawienie polu na ktore sie przenosimy
            // Image pionka ktory sie przenosi
            fieldToMove.setImage(pawnToMove);

            //Skasowanie z pola wczesniejszego Image
            //Poniewaz zostal juz przeniesiony
            ImageView clickedPawn = (ImageView) clickedNode;
            clickedPawn.setImage(null);

            //Ustawienie ID polu na ktore sie przenieslismy
            //za pomocą ID piona ktory byl na poprzednim polu
            next.setId(clickedNode.getId());

            switch (clientGame.color) {
                case "red":
                        red.get(pawnNumber).setField(newField);
                case "green":
                        green.get(pawnNumber).setField(newField);
                case "blue":
                        blue.get(pawnNumber).setField(newField);
                case "yellow":
                        yellow.get(pawnNumber).setField(newField);
            }

            clickedNode.setId(null);

            // Wyslanie wiadomosci do serwera o przeniesieniu pionka
            System.out.println("Daje informacje o przeniesieniu pionka do serwera");

            String message = "pawnMoved," + next.getId() + "," + newField ;
            clientGame.sendToServer(message);
            message = "tourEnd";
            clientGame.sendToServer(message);
            isRolled = false;
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

        System.out.println("TUTAJ RANDOM: " + random);
        if(isPawnInBase(pawnToMove.getField(), clientGame.color) && random==6)
        {
           // pawnToMove.setField(getStartingField(clientGame.color));
            System.out.println("TUTAJ WCHODZI PO TYM RANDOMIE: "+random);
            movePawnOutOfBase();
        }
        else if(!isPawnInBase(pawnToMove.getField(), clientGame.color)){
            System.out.println("WCHODZI DO ELSE");
            movePawnForward();
        }
    }

    private ImageView getPawnToMove(String id) {
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