package com.example.chinczyk;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayersInRooms {
    static ObservableList<ObservableList<String>> playersInRooms = FXCollections.observableArrayList();

    void addLists()
    {
        for (int i = 0; i < 10; i++) {
            ObservableList<String> roomList = FXCollections.observableArrayList();
            playersInRooms.add(roomList);
        }
    }

}
