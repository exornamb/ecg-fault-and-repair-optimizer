package service;

import dao.FaultDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Fault;

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

        FaultDAO dao =
                new FaultDAO();

        faults.setAll(
                dao.getAllFaults()
        );
    }

}