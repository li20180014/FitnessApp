/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.List;
import javax.swing.JOptionPane;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.domain.TreningGrupa;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmInsertClan;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class InsertClanController {

    private final FrmInsertClan frmInsertClan;

    public InsertClanController(FrmInsertClan frmInsertClan) {
        this.frmInsertClan = frmInsertClan;
        addActionListeners();
    }

    public void openInsertForm() {
        try {
            setUpForm();
            frmInsertClan.setLocationRelativeTo(null);
            frmInsertClan.setVisible(true);
            

        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(
                    frmInsertClan,
                    ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    frmInsertClan,
                    "Error loading form\n" + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void setUpForm() throws Exception {
        List<TreningGrupa> grupe = Communication.getInstance().getGrupe();
        frmInsertClan.getGrupeCmb().removeAllItems();
        for (TreningGrupa tg : grupe) {
            frmInsertClan.getGrupeCmb().addItem(tg);
        }
    }

    private void addActionListeners() {
        frmInsertClan.btnCancelAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmInsertClan.dispose();
            }
        });

        frmInsertClan.btnDodajddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajClana(e);
            }

            private void dodajClana(ActionEvent e) {
                try {
                    if (frmInsertClan.getImeTextField().getText().isEmpty() || frmInsertClan.getPrezimeTextField().getText().isEmpty()
                            || frmInsertClan.getEmailTextField().getText().isEmpty()
                            || frmInsertClan.getStarostTextField().getText().isEmpty()) {
                        throw new Exception("Sva polja su obavezna!");
                    }

                    String email = frmInsertClan.getEmailTextField().getText().trim();
                    boolean clanarina = frmInsertClan.getClanarina().isSelected();
                    String ime = frmInsertClan.getImeTextField().getText().trim();
                    String prezime = frmInsertClan.getPrezimeTextField().getText().trim();
                    int starost = Integer.valueOf(frmInsertClan.getStarostTextField().getText().trim());
                    TreningGrupa tg = (TreningGrupa) frmInsertClan.getGrupeCmb().getSelectedItem();
                    
                    if (starost<0) {
                       JOptionPane.showMessageDialog(
                            frmInsertClan,
                            "Starost ne moze biti negativna!",
                            "Greska", JOptionPane.INFORMATION_MESSAGE
                    );  
                       return;
                    }
                    
                    Communication.getInstance().addClan(email, clanarina, ime, prezime, starost, tg);

                    JOptionPane.showMessageDialog(
                            frmInsertClan,
                            "Sistem je zapamtio clana!",
                            "Clan", JOptionPane.INFORMATION_MESSAGE
                    );
                    resetForm();
                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmInsertClan,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    //zatvori forme
                    Coordinator.getInstance().closeAllForms((FrmMain) frmInsertClan.getParent(), frmInsertClan, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmInsertClan,
                            "Sistem ne moze da zapamti clana!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            private void resetForm() {
                frmInsertClan.getEmailTextField().setText("");
                frmInsertClan.getImeTextField().setText("");
                frmInsertClan.getPrezimeTextField().setText("");
                frmInsertClan.getStarostTextField().setText("");
            }
        }
        );
    }

}
