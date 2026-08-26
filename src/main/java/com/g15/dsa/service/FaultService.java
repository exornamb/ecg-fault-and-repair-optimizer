package com.g15.dsa.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.g15.dsa.dao.FaultDAO;
import com.g15.dsa.model.Fault;

/**
 * FaultService — In-memory singleton cache of all fault records.
 *
 * All mutating operations (add, update, delete) go through this service
 * so that the ObservableList (bound to every UI table) stays in sync with
 * the database and the CSV file at all times.
 */
public class FaultService {

    private static final ObservableList<Fault> faults =
            FXCollections.observableArrayList();

    static {
        refresh();
    }

    /** Returns the live observable list of all faults (bound to UI tables). */
    public static ObservableList<Fault> getFaults() {
        return faults;
    }

    /**
     * Reloads all faults from the database (or CSV fallback).
     * Call after any external change to resync the in-memory list.
     */
    public static void refresh() {
        FaultDAO dao = new FaultDAO();
        faults.clear();
        faults.addAll(dao.getAllFaults());
    }

    /**
     * Adds a new fault to the DB/CSV and the in-memory list.
     * @return true if the insert was successful
     */
    public static boolean addFault(Fault fault) {
        FaultDAO dao = new FaultDAO();
        boolean ok = dao.insertFault(fault);
        refresh();   // reload so assigned DB id / fault_id is picked up
        return ok;
    }

    /**
     * Updates an existing fault in the DB/CSV and refreshes the in-memory list.
     * @return true if the update was successful
     */
    public static boolean updateFault(Fault fault) {
        FaultDAO dao = new FaultDAO();
        boolean ok = dao.updateFault(fault);
        // Update in-memory entry directly so bound tables refresh immediately
        for (int i = 0; i < faults.size(); i++) {
            if (faults.get(i).getFaultId() != null
                    && faults.get(i).getFaultId().equals(fault.getFaultId())) {
                faults.set(i, fault);
                break;
            }
        }
        return ok;
    }

    /**
     * Deletes a fault from the DB/CSV and removes it from the in-memory list.
     * @return true if the deletion was successful
     */
    public static boolean deleteFault(Fault fault) {
        FaultDAO dao = new FaultDAO();
        boolean ok = dao.deleteFault(fault);
        faults.removeIf(f -> f.getFaultId() != null
                && f.getFaultId().equals(fault.getFaultId()));
        return ok;
    }
}