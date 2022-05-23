/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.domain.Clan;
import rs.ac.bg.fon.ps.model.TableModelClanovi;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmMain;
import rs.ac.bg.fon.ps.view.form.FrmSearchClan;

/**
 *
 * @author Korisnik
 */
public class SearchClanController {

    private final FrmSearchClan frmSearchClan;
    List<Clan> clanovi;

    public SearchClanController(FrmSearchClan frmSearchClan) {
        this.frmSearchClan = frmSearchClan;

    }

    public void openSearchForm() {
        try {
            setUpForm();
            frmSearchClan.setLocationRelativeTo(null);
            frmSearchClan.setVisible(true);

        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(
                    frmSearchClan,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    frmSearchClan,
                    "Error loading form\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }

    }

    private void setUpForm() throws Exception {
        clanovi = Communication.getInstance().getClanovi();
        TableModelClanovi tmc = new TableModelClanovi();
        for (Clan clan : clanovi) {
            tmc.dodajClanove(clan);
        }
        frmSearchClan.getTblTable().setModel(tmc);
        addActionListeners();
    }

    private void addActionListeners() {
        frmSearchClan.btnOdustaniActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmSearchClan.dispose();
            }
        });

        frmSearchClan.btnPronadjiActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String value = frmSearchClan.getUslovZaPretragu().getText();
                    if (value.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                frmSearchClan,
                                "Unesite vrednost za pretrazivanje!",
                                "Greska", JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    List<Clan> pronadjeniClanovi = Communication.getInstance().nadjiClanaPoUslovu(value);
                    if (pronadjeniClanovi.isEmpty()) {
                        throw new Exception("Nema clanova po zadatoj vrednosti!");
                    }

                    TableModelClanovi mtc = new TableModelClanovi();
                    for (Clan clan : pronadjeniClanovi) {
                        mtc.dodajClanove(clan);
                    }
                    frmSearchClan.getTblTable().setModel(mtc);

                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmSearchClan,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmSearchClan.getParent(), frmSearchClan, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmSearchClan,
                            "Neuspesno pronadjeni clanovi!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        frmSearchClan.btnResetActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableModelClanovi tmc = new TableModelClanovi();
                for (Clan clan : clanovi) {
                    tmc.dodajClanove(clan);
                }
                frmSearchClan.getTblTable().setModel(tmc);
            }
        });
    }

}
