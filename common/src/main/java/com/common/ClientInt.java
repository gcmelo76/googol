package com.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInt extends Remote {
    void adminBoard(ArrayList<Integer> activeBarrels, ArrayList<String> topSearches, ArrayList<BarrelMetrics> barrelMetrics) throws RemoteException;
}
