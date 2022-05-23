/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import rs.ac.bg.fon.ps.domain.TreningGrupa;


/**
 *
 * @author Korisnik
 */
public class TableModelGrupe extends AbstractTableModel{
    
     List<TreningGrupa> listaGrupa = new ArrayList<>();
    String[] kolone = new String[]{"Naziv Grupe", "Kapacitet", "Program"};

    @Override
    public int getRowCount() {
        return listaGrupa.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    public List<TreningGrupa> getListaGrupa() {
        return listaGrupa;
    }
    
    public TreningGrupa getGrupa(int row){
    return listaGrupa.get(row);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TreningGrupa tg = listaGrupa.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return tg.getNaziv();
            case 1:
                return tg.getKapacitet();
            case 2:
                return tg.getProgram().getNaziv();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column]; //To change body of generated methods, choose Tools | Templates.
    }

    public void dodajGrupu(TreningGrupa t) {
        listaGrupa.add(t);
        fireTableDataChanged();
    }
}
