package com.g15.dsa.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.g15.dsa.dao.FaultDAO;
import com.g15.dsa.model.Fault;

public class FaultService {

    private static final ObservableList<Fault> faults =
            FXCollections.observableArrayList();

    static {
        refresh();
    }

    public static ObservableList<Fault> getFaults() {
        return faults;
    }

    public static void refresh() {
        FaultDAO dao = new FaultDAO();
        faults.clear();
        faults.addAll(dao.getAllFaults());
    }
}