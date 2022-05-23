/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import rs.ac.bg.fon.ps.domain.Clan;
import rs.ac.bg.fon.ps.domain.TrenerKluba;

/**
 *
 * @author Korisnik
 */
public class TableModelClanovi extends AbstractTableModel {

    List<Clan> listaClanova = new ArrayList<>();
    String[] kolone = new String[]{"Ime", "Prezime", "Email", "Clanarina", "Starost", "Grupa"};

    @Override
    public int getRowCount() {
        return listaClanova.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Clan c = listaClanova.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return c.getIme();
            case 1:
                return c.getPrezime();
            case 2:
                return c.getEmail();
            case 3:
                return proveriClanarinu(c.isClanarina());
            case 4:
                return c.getStarost();
            case 5:
                return c.getGrupa().getNaziv();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column]; //To change body of generated methods, choose Tools | Templates.
    }

    public void dodajClanove(Clan c) {
        listaClanova.add(c);
        fireTableDataChanged();
    }

    private String proveriClanarinu(boolean clanarina) {
        if (clanarina) {
            return "Uplacena";
        }
        return "Neuplacena";
    }

    public Clan getClan(int row) {
        return listaClanova.get(row);
    }
}
