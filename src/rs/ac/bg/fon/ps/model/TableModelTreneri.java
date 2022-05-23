/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import rs.ac.bg.fon.ps.domain.TrenerKluba;

/**
 *
 * @author Korisnik
 */
public class TableModelTreneri extends AbstractTableModel {

    List<TrenerKluba> listaTrenera = new ArrayList<>();
    String[] kolone = new String[]{"Ime", "Prezime", "Starost"};

    @Override
    public int getRowCount() {
        return listaTrenera.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    public List<TrenerKluba> getListaTrenera() {
        return listaTrenera;
    }
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TrenerKluba tk = listaTrenera.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return tk.getIme();
            case 1:
                return tk.getPrezime();
            case 2:
                return tk.getStarost();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column]; //To change body of generated methods, choose Tools | Templates.
    }

    public void dodajTrenera(TrenerKluba t) {
        listaTrenera.add(t);
        fireTableDataChanged();
    }
    
    public void obrisiTrenera(int row){
    listaTrenera.remove(row);
    fireTableDataChanged();
    }

}
