/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.domain.Aktivnosti;
import rs.ac.bg.fon.ps.domain.TezinaPrograma;
import rs.ac.bg.fon.ps.model.TableModelAktivnosti;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmInsertProgramRada;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class InsertProgramController {

    private final FrmInsertProgramRada frmInsertProgramRada;

    public InsertProgramController(FrmInsertProgramRada frmInsertProgramRada) {
        this.frmInsertProgramRada = frmInsertProgramRada;
        addActionListeners();
    }

    public void openInsertForm() {
        frmInsertProgramRada.setLocationRelativeTo(null);
        frmInsertProgramRada.setVisible(true);

    }

    private void addActionListeners() {
        frmInsertProgramRada.btnAddAktivnostActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openInsertAktivnostForm((FrmMain) frmInsertProgramRada.getParent(), frmInsertProgramRada);
            }
        });

        frmInsertProgramRada.btnObrisiAktivnostActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = frmInsertProgramRada.getTblTable().getSelectedRow();
                if (row != -1) {
                    TableModelAktivnosti model = (TableModelAktivnosti) frmInsertProgramRada.getTblTable().getModel();
                    model.deleteItem(row);
                } else {
                    JOptionPane.showMessageDialog(
                            frmInsertProgramRada,
                            "No rows selected!\n",
                            "Error!", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        frmInsertProgramRada.btnDodajProgramActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nazivPrograma = frmInsertProgramRada.getNazivPrograma().getText();
                    TezinaPrograma tezina = (TezinaPrograma) frmInsertProgramRada.getCmbTezinaPrograma().getSelectedItem();
                    TableModelAktivnosti model = (TableModelAktivnosti) frmInsertProgramRada.getTblTable().getModel();
                    List<Aktivnosti> lista = model.getListaAktivnosti();

                    if (nazivPrograma.isEmpty()) {
                        throw new Exception("Sva polja su neophodna!");
                    }
                    
                    if(lista.isEmpty()){
                      JOptionPane.showMessageDialog(
                            frmInsertProgramRada,
                            "Program mora imati neku aktivnost!",
                            "Greska", JOptionPane.INFORMATION_MESSAGE
                    );
                      return;
                    }

                    Communication.getInstance().dodajProgram(nazivPrograma, tezina, lista);

                    JOptionPane.showMessageDialog(
                            frmInsertProgramRada,
                            "Sistem je zapamtio program rada!",
                            "Program Rada", JOptionPane.INFORMATION_MESSAGE
                    );
                    resetForm();

                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmInsertProgramRada,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmInsertProgramRada.getParent(), frmInsertProgramRada, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmInsertProgramRada,
                            "Sistem ne moze da zapamti program rada!\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }

            }

            private void resetForm() {
                frmInsertProgramRada.getNazivPrograma().setText("");
                frmInsertProgramRada.getTblTable().setModel(new TableModelAktivnosti());
            }
        });

        frmInsertProgramRada.btnOdustaniActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmInsertProgramRada.dispose();
            }
        });

    }

}
