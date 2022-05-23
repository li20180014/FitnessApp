/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import javax.swing.JOptionPane;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.domain.TrenerKluba;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmInsertTrener;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class InsertTrenerController {

    private final FrmInsertTrener frmInsertTrener;

    public InsertTrenerController(FrmInsertTrener frmInsertTrener) {
        this.frmInsertTrener = frmInsertTrener;
        addActionListeners();
    }

    private void addActionListeners() {
        frmInsertTrener.btnCancelAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmInsertTrener.dispose();
            }
        });

        frmInsertTrener.btnDodajddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajTrenera(e);
            }

            private void dodajTrenera(ActionEvent e) {
                try {
                    if (frmInsertTrener.getUsernameTextField().getText().isEmpty() || frmInsertTrener.getPasswordTextField().getText().isEmpty()
                            || frmInsertTrener.getImeTextField().getText().isEmpty() || frmInsertTrener.getPrezimeTextField().getText().isEmpty()
                            || frmInsertTrener.getStarostTextField().getText().isEmpty()) {
                        throw new Exception("All fields are required!");
                    }

                    String username = frmInsertTrener.getUsernameTextField().getText().trim();
                    String password = frmInsertTrener.getPasswordTextField().getText().trim();
                    String ime = frmInsertTrener.getImeTextField().getText().trim();
                    String prezime = frmInsertTrener.getPrezimeTextField().getText().trim();
                    int starost = 0;
                    try {
                        starost = Integer.parseInt(frmInsertTrener.getStarostTextField().getText().trim());
                    } catch (NumberFormatException ne) {
                         JOptionPane.showMessageDialog(
                            frmInsertTrener,
                            "Starost mora biti broj!",
                            "Greska", JOptionPane.INFORMATION_MESSAGE
                    ); 
                       return;
                    }
                    
                    
                    if (starost<=0) {
                       JOptionPane.showMessageDialog(
                            frmInsertTrener,
                            "Starost ne moze biti negativna niti nula!",
                            "Greska", JOptionPane.INFORMATION_MESSAGE
                    ); 
                       return;
                    }
                    
                  

                    Communication.getInstance().addTrener(username, password, ime, prezime, starost);

                    JOptionPane.showMessageDialog(
                            frmInsertTrener,
                            "Sistem je zapamtio trenera!",
                            "Trener Kluba", JOptionPane.INFORMATION_MESSAGE
                    );
                    resetForm();
                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmInsertTrener,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    //zatvori forme
                    Coordinator.getInstance().closeAllForms((FrmMain) frmInsertTrener.getParent(), frmInsertTrener, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmInsertTrener,
                            "Sistem ne moze da zapamti trenera!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            private void resetForm() {
                frmInsertTrener.getUsernameTextField().setText("");
                frmInsertTrener.getPasswordTextField().setText("");
                frmInsertTrener.getImeTextField().setText("");
                frmInsertTrener.getPrezimeTextField().setText("");
                frmInsertTrener.getStarostTextField().setText("");
            }
        }
        );
    }

    public void openCreateForm() {
        frmInsertTrener.setLocationRelativeTo(null);
        frmInsertTrener.setVisible(true);
        
    }
}
