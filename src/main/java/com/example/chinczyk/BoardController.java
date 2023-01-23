package com.example.chinczyk;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    private Label turnInfo;
    private int clickedPositionX;
    private int clickedPositionY;
    private Node clickedNode;

    private ArrayList<Field> fields = new ArrayList<>();
    private ArrayList<Field> myBaseFields = new ArrayList<>();
    private ArrayList<Field> myStartBaseFields = new ArrayList<>();
    private ArrayList<Field> startBaseFieldsRed = new ArrayList<>();
    private ArrayList<Field> startBaseFieldsGreen = new ArrayList<>();
    private ArrayList<Field> startBaseFieldsBlue = new ArrayList<>();
    private ArrayList<Field> startBaseFieldsYellow = new ArrayList<>();
    private ArrayList<Pawn> red = new ArrayList<Pawn>();
    private ArrayList<Pawn> green = new ArrayList<Pawn>();
    private ArrayList<Pawn> blue = new ArrayList<Pawn>();
    private ArrayList<Pawn> yellow = new ArrayList<Pawn>();
    private String[] colorNames = {"RED", "GREEN", "BLUE", "YELLOW"};
    private int random = 0;

    private ClientGame clientGame;
    private int userID;
    private int turnNumber = 1;
    private boolean isRolled = false;
    private ArrayList<Pawn> listOfAllPawns = new ArrayList<>();
    private ArrayList<Pawn> listOfSpecificColor = new ArrayList<>();
    private Field newField;
    private Node newPositionNode;
    private boolean movingOutOfBase;
    private boolean endGame = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createPawns();
        createFields();
        GameGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            String color;
            clickedPositionX = (int) Math.floor(e.getX()/46);
            clickedPositionY = (int) Math.floor(e.getY()/46);


            System.out.println("X: " + clickedPositionX + " | Y: " + clickedPositionY);
            System.out.println("X REAL: " + e.getX() + " | Y REAL: " + e.getY());
            clickedNode = getNodeFromGridPane(GameGrid, clickedPositionX, clickedPositionY);

            if((clickedNode != null) && isRolled)
            {
                System.out.println("CLICKED NODE: " + clickedNode);
                color = clickedNode.getId().substring(0,clickedNode.getId().length()-2);

                if(color.equals(clientGame.color) ) {
                    // Jeżeli nacisnięte pole nie jest puste + pionek jest
                    // koloru który posiada gracz + kostka została zakręcona

                    movePawn(clickedNode.getId());
                }
            }
        });
    }

    private void myFieldsOfBase() {
        if(clientGame.color.equals("red"))
        {
            for(int i = 2; i <= 5; i++)
            {
                myBaseFields.add(new Field(i, 6));
            }
        }
        if(clientGame.color.equals("green"))
        {
            for(int i = 2; i <= 5; i++)
            {
                myBaseFields.add(new Field(6, i));
            }
        }
        if(clientGame.color.equals("blue"))
        {
            for(int i = 7; i <= 10; i++)
            {
                myBaseFields.add(new Field(i, 6));
            }
        }
        if(clientGame.color.equals("yellow"))
        {
            for(int i = 7; i <= 10; i++)
            {
                myBaseFields.add(new Field(6, i));
            }
        }
    }

    private void createStartBase(int v1, int v2, String color)
    {
        ArrayList<Field> tempFields = new ArrayList<>();
        for(int i = v2; i <= v2 + 1; i++)
        {
            for(int j = v1; j <= v1 + 1; j++)
            {
                tempFields.add(new Field(j,i));
            }
        }
        if(color.equals("red")) startBaseFieldsRed = tempFields;
        if(color.equals("green")) startBaseFieldsGreen = tempFields;
        if(color.equals("blue")) startBaseFieldsBlue = tempFields;
        if(color.equals("yellow")) startBaseFieldsYellow = tempFields;
    }

    private Pawn getPawnByID (String pawnID)
    {
        for(Pawn item : listOfSpecificColor)
        {
            if(item.getName().equals(pawnID)) return item;
        }
        return null;
    }

    private void movePawn(String pawnID)
    {
        // Naciśnięty pionek
        Pawn clickedPawn = getPawnByID(pawnID);
        // Image pionka
        Image imageOfPawn = ((ImageView) clickedNode).getImage();

        int newFieldNumber = 0;

        // Usuwanie pionka z pola na którym był (Image + ID)


        if(clickedPawn.isInGame() || !clickedPawn.isInGame() && random == 6)
        {
            if(clickedPawn.isInGame())
            {
                System.out.println("JESTEM W GRZE, ID: " + clickedPawn.getName());
                //Rusz go o tyle pul ile zostało wylosowane
                int fieldNumber = 0;
                for(Field field : fields)
                {
                    if(field.getX() == clickedPositionX && field.getY() == clickedPositionY)
                    {
                        break;
                    }
                    else
                    {
                        fieldNumber++;
                    }
                }
                int passedFields = clickedPawn.getPassedFields();
                // Jeśli dokonał już okrążenia całej planszy
                // Wstaw go do bazy
                if(passedFields + random >= 40)
                {
                    newField = myBaseFields.get(myBaseFields.size() - 1);
                    myBaseFields.remove(myBaseFields.size() - 1);
                    if(myBaseFields.size() == 0)
                    {
                        endGame = true;
                    }

                    int removePawn = 0;
                    for(Pawn item : listOfSpecificColor)
                    {
                        if(clickedPawn.getName().equals(item.getName()))
                        {
                            listOfSpecificColor.remove(removePawn);
                            break;
                        }
                        else
                        {
                            removePawn++;
                        }
                    }
                }
                // W innym wypadku przesuń go o ilość
                // wylosowanych oczek
                else
                {
                    clickedPawn.setPassedFields(passedFields + random);
                    newFieldNumber = fieldNumber + random;
                    if(newFieldNumber >= 40)
                    {
                        newFieldNumber = newFieldNumber - 40;
                    }
                    newField = fields.get(newFieldNumber);
                }
                movingOutOfBase = false;
            }

            // Wychodzenie z pól startowych
            else if(!clickedPawn.isInGame() && random == 6)
            {
                //Wyciągnij go z bazy
                clickedPawn.setInGame(true);
                if(clientGame.color.equals("red"))
                {
                    newField = fields.get(Field.RED_START_POINT);
                }
                if(clientGame.color.equals("green"))
                {
                    newField = fields.get(Field.GREEN_START_POINT);
                }
                if(clientGame.color.equals("blue"))
                {
                    newField = fields.get(Field.BLUE_START_POINT);
                }
                if(clientGame.color.equals("yellow"))
                {
                    newField = fields.get(Field.YELLOW_START_POINT);
                }
                movingOutOfBase = true;
            }
            newPositionNode = getNodeFromGridPane(GameGrid, newField.getX(), newField.getY());
            String color = clientGame.color;
            if(newPositionNode.getId() != null)
            {
                color = newPositionNode.getId().substring(0, newPositionNode.getId().length() - 2);
            }
            // Dopuszczenie do ruchu, jeśli na nowej pozycji
            // nikogo nie ma lub osoba znajdująca się
            // na polu jest innego koloru niż nasz
            if(newPositionNode.getId() == null || !clientGame.color.equals(color))
            {
                clickedPawn.setField(newField);
                clickedPawn.setFieldNumber(fieldToFieldNumber(newField));

                System.out.println("NEWFIELD X: " + newField.getX() + " | NEWFIELD Y: " + newField.getY());

                System.out.println("NEW NODE: " + newPositionNode);
                System.out.println("NEW NODE ID: " + newPositionNode.getId());

                // Wyżej sprawdziliśmy, czy osoba, która jest zbijana
                // posiada inny kolor, tak więc sprawdzamy wyłącznie
                // warunek czy przypadkiem pole nie jest puste
                if(newPositionNode.getId() != null)
                {
                    beatPawn(color, newField.getX(), newField.getY());

                }
                clickedNode.setId(null);
                ((ImageView) clickedNode).setImage(null);

                newPositionNode.setId(pawnID);
                ((ImageView) newPositionNode).setImage(imageOfPawn);

                //Wysłanie informacji o końcu tury
                String message;

                message = "pawnMoved," + clickedPositionX + "," + clickedPositionY + "," + newField.getX() + "," + newField.getY() + "," + clientGame.getID();
                clientGame.sendToServer(message);

                message = "turnInfo," + clientGame.color;
                clientGame.sendToServer(message);

                if(endGame)
                {
                    message = "gameEnded";
                    clientGame.sendToServer(message);
                }


                message = "tourEnd";
                clientGame.sendToServer(message);

                isRolled = false;
            }
            else
            {
                if(movingOutOfBase) clickedPawn.setInGame(false);
                else clickedPawn.setPassedFields(clickedPawn.getPassedFields() - random);

            }
        }
    }

    private void beatPawn(String color, int X_1, int Y_1) {
        Pawn beatenPawn = null;
        String beatenPawnID = newPositionNode.getId();
        Image beatenPawnImage = ((ImageView) newPositionNode).getImage();

        for(Pawn beaten : listOfAllPawns)
        {
            if(newPositionNode.getId().equals(beaten.getName()))
            {
                beatenPawn = beaten;
                break;
            }
        }
        //Przywrócenie wartości startowych
        System.out.println("POROWNANIE: BEATEN PAWN ID: " + beatenPawn.getName() + " NEWPOSNODE ID: " + newPositionNode.getId());
        System.out.println("PIONEK: " + beatenPawn.getName() + " CZY W GRZE: " + beatenPawn.isInGame() + " ILE PÓL: " + beatenPawn.getPassedFields());

        int numberOfEmptyField = 0;
        ArrayList<Field> tempStartBaseFields = new ArrayList<>();

        if(color.equals("red")) tempStartBaseFields = startBaseFieldsRed;
        if(color.equals("green")) tempStartBaseFields = startBaseFieldsGreen;
        if(color.equals("blue")) tempStartBaseFields = startBaseFieldsBlue;
        if(color.equals("yellow")) tempStartBaseFields = startBaseFieldsYellow;
        for(Field item : tempStartBaseFields)
        {
            Node tempNode = getNodeFromGridPane(GameGrid, item.getX(), item.getY());
            if(tempNode.getId() != null) numberOfEmptyField++; else break;
        }
        Field emptyField = tempStartBaseFields.get(numberOfEmptyField);

        Node emptyFieldNode = getNodeFromGridPane(GameGrid, emptyField.getX(), emptyField.getY());

        emptyFieldNode.setId(beatenPawnID);
        ((ImageView) emptyFieldNode).setImage(beatenPawnImage);

        // X_1 i Y_1 - STARE KOORDYNATY PIONKA
        // emptyField.getX() i emptyField.getY() - NOWE KOORDYNATY PIONKA

        String message = "pawnBeaten," + beatenPawnID + "," + beatenPawn.getColor();
        clientGame.sendToServer(message);

        message = "pawnMoved," + X_1 + "," + Y_1 + "," + emptyField.getX() + "," + emptyField.getY() + "," + clientGame.getID();
        clientGame.sendToServer(message);
    }

    public int fieldToFieldNumber(Field field)
    {
        int fieldNumber = 0;
        int field_X = field.getX();
        int field_Y = field.getY();
        for(Field item: fields)
        {
            if(!(item.getX() == field_X && item.getY() == field_Y)) fieldNumber++; else break;
        }
        return fieldNumber;
    }

    private boolean isSomeoneInGame()
    {
        for (Pawn item : listOfSpecificColor)
        {
            if(item.isInGame()) return true;
        }
        return false;
    }

    @FXML
    private void swapImage() {
        String message;
        if(!isRolled)
        {
            turnNumber = clientGame.tourNumber;
            if(turnNumber % 2 == userID) {
                isRolled = true;

                random = 6;

                // Jeżeli wylosowana liczba != 6 i żadnego
                // piona nie ma w grze - STAN KOSTKI + KONIEC TURY
                if(random != 6 && !isSomeoneInGame())
                {
                    //Wysłanie stanu kostki do wszystkich graczy
                    message = "diceRoll," + random;
                    clientGame.sendToServer(message);

                    //Wysłanie informacji o końcu tury
                    message = "tourEnd";
                    clientGame.sendToServer(message);

                    isRolled = false;

                }

                // Jeżeli wylosowana liczba != 6 i pionek/pionki
                // jest w grze - STAN KOSTKI
                // Jeżeli wylosowana liczba == 6 - STAN KOSTKI
                else if((random != 6 && isSomeoneInGame()) || random == 6)
                {
                    //Wysłanie stanu kostki do wszystkich graczy
                    message = "diceRoll," + random;
                    clientGame.sendToServer(message);

                    // Tura zostanie zakończona po wybraniu pionka z planszy
                }
            }
        }
    }

    public void listsOfPawns()
    {
        // Lista wszystkich pionków

        listOfAllPawns = new ArrayList<>();
        listOfAllPawns.addAll(red);
        listOfAllPawns.addAll(green);
        listOfAllPawns.addAll(blue);
        listOfAllPawns.addAll(yellow);

        listOfSpecificColor = new ArrayList<>();

        // Lista pionków gracza o danym kolorze
        for(Pawn item : listOfAllPawns)
        {
            if(item.getColor().equals(clientGame.color))
            {
                listOfSpecificColor.add(item);
            }
        }

    }

    @FXML
    protected void getClient(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        clientGame = (ClientGame) stage.getUserData();
        // Stworzenie listy pionków
        // + z podziałem na kolory
        listsOfPawns();

        // Stworzenie listy pól
        // zawierającej nasze pola bazy
        myFieldsOfBase();

        // Stworzenie list pól
        // zawierających pola startowe (baz) każdego koloru
        createStartBase(1,1,"red");
        createStartBase(10,1,"green");
        createStartBase(10,10,"blue");
        createStartBase(1,10,"yellow");


        userID = clientGame.getID();
        System.out.println("Board : " + userID);

        clientGame.DiceValueList.addListener((ListChangeListener<String>) change -> updateDice(clientGame.diceRollNumber));
        clientGame.PawnsValueList.addListener((ListChangeListener<String>) change -> updatePawns());
        clientGame.PawnsBeaten.addListener((ListChangeListener<String>) change -> updateBeaten());
        clientGame.turnEnded.addListener((ListChangeListener<String>) change -> Platform.runLater(() -> turnInfo.setText("Turn: " + colorNames[clientGame.turnInfo])));
    }

    private void updateBeaten() {
        for (Pawn item : listOfAllPawns)
        {
            if(item.getName().equals(clientGame.beatenPawn))
            {
                item.setInGame(false);
                item.setPassedFields(0);
                item.setFieldNumber(-1);
                break;
            }
        }
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

    private void updatePawns()
    {
        if(clientGame.getID() != clientGame.fromWho)
        {
            Node oldPosition = getNodeFromGridPane(GameGrid, clientGame.oldPosition.getX(), clientGame.oldPosition.getY());
            Node newPosition = getNodeFromGridPane(GameGrid, clientGame.newPosition.getX(), clientGame.newPosition.getY());

            String oldPositionID = oldPosition.getId();
            Image oldPositionImage = ((ImageView) oldPosition).getImage();

            newPosition.setId(oldPositionID);
            ((ImageView) newPosition).setImage(oldPositionImage);

            oldPosition.setId(null);
            ((ImageView) oldPosition).setImage(null);
        }
        System.out.println(clientGame.turnInfo);
    }

    @FXML
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

}