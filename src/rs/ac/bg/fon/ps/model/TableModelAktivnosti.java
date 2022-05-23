/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import rs.ac.bg.fon.ps.domain.Aktivnosti;

/**
 *
 * @author Korisnik
 */
public class TableModelAktivnosti extends AbstractTableModel {

    List<Aktivnosti> listaAktivnosti = new ArrayList<>();
    String[] kolone = new String[]{"Naziv Aktivnosti", "Opis", "Trajanje", "Dodatne Napomene"};

    @Override
    public int getRowCount() {
        return listaAktivnosti.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Aktivnosti aktivnost = listaAktivnosti.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return aktivnost.getNazivAktivnosti();
            case 1:
                return aktivnost.getOpis();
            case 2:
                return aktivnost.getTrajanjeAktivnosti();
            case 3:
                return aktivnost.getDodatneNapomene();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column]; //To change body of generated methods, choose Tools | Templates.
    }

    public void dodajAktivnost(Aktivnosti a) {
        listaAktivnosti.add(a);
        fireTableDataChanged();
    }

    public void deleteItem(int row) {
        listaAktivnosti.remove(row);
        fireTableDataChanged();
    }

    public List<Aktivnosti> getListaAktivnosti() {
        return listaAktivnosti;
    }

    
}
