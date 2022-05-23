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
import rs.ac.bg.fon.ps.domain.Clan;
import rs.ac.bg.fon.ps.model.TableModelClanovi;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmDeleteClan;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class DeleteClanController {

    private final FrmDeleteClan frmDeleteClan;
    private TableModelClanovi tmc;

    public DeleteClanController(FrmDeleteClan frmDeleteClan) {
        this.frmDeleteClan = frmDeleteClan;
        addActionListeners();
    }

    public void openDeleteForm() {
        try {
            setUpForm();
            frmDeleteClan.setLocationRelativeTo(null);
            frmDeleteClan.setVisible(true);
            
        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(
                    frmDeleteClan,
                    ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    frmDeleteClan,
                    "Error loading form\n" + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void setUpForm() throws Exception {
        List<Clan> clanovi = Communication.getInstance().getClanovi();
        tmc = new TableModelClanovi();
        for (Clan clan : clanovi) {
            tmc.dodajClanove(clan);
        }
        frmDeleteClan.getTblClanovi().setModel(tmc);
    }

    private void addActionListeners() {
        frmDeleteClan.btnCancelAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmDeleteClan.dispose();
            }
        });
        
        frmDeleteClan.btnPronadjiAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 try {
                    String value = frmDeleteClan.getTxtUslov().getText();
                    if (value.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                frmDeleteClan,
                                "Unesite vrednost za pretrazivanje!",
                                "Greska", JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    List<Clan> pronadjeniClanovi = Communication.getInstance().nadjiClanaPoUslovu(value);
                    if (pronadjeniClanovi.isEmpty()) {
                        throw new Exception("Nema clanova po zadatoj vrednosti!");
                    }

                    tmc = new TableModelClanovi();
                    for (Clan clan : pronadjeniClanovi) {
                        tmc.dodajClanove(clan);
                    }
                    frmDeleteClan.getTblClanovi().setModel(tmc);

                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmDeleteClan,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmDeleteClan.getParent(), frmDeleteClan, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmDeleteClan,
                            "Neuspesno pronadjeni clanovi!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            
        });
        
        frmDeleteClan.btnDeleteddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = frmDeleteClan.getTblClanovi().getSelectedRow();
                    if (row==-1) {
                        JOptionPane.showMessageDialog(
                            frmDeleteClan,
                            "Sistem ne moze da obrise clana. \nNiste selektovli clana za brisanje!",
                            "Clan", JOptionPane.INFORMATION_MESSAGE
                    );
                        return;
                    }
                    Clan c = tmc.getClan(row);
                    Communication.getInstance().deleteClan(c);

                    JOptionPane.showMessageDialog(
                            frmDeleteClan,
                            "Sistem je obrisao clana!",
                            "Clan", JOptionPane.INFORMATION_MESSAGE
                    );
                    setUpForm();
                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmDeleteClan,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    
                    Coordinator.getInstance().closeAllForms((FrmMain) frmDeleteClan.getParent(), frmDeleteClan, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmDeleteClan,
                            "Sistem ne moze da obrise clana!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
                
            }
        });
    }

}
