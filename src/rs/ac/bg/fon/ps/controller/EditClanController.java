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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.domain.Clan;
import rs.ac.bg.fon.ps.domain.TreningGrupa;
import rs.ac.bg.fon.ps.model.TableModelClanovi;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmEditClan;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class EditClanController {

    private final FrmEditClan frmEditClan;
    private List<Clan> clanovi;
    private Clan clanZaIzmenu;

    public EditClanController(FrmEditClan frmEditClan) {
        this.frmEditClan = frmEditClan;
    }

    public void openSearchForm() {

        try {
            setUpForm();
            frmEditClan.setLocationRelativeTo(null);
            frmEditClan.setVisible(true);

        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(
                    frmEditClan,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    frmEditClan,
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
        frmEditClan.getTblClanovi().setModel(tmc);
        frmEditClan.getTxtIme().setEditable(false);
        frmEditClan.getTxtPrezime().setEditable(false);
        frmEditClan.getTxtEmail().setEditable(false);
        frmEditClan.getTxtIme().setEditable(false);
        frmEditClan.getBtnIzmeni().setEnabled(false);

        List<TreningGrupa> grupe = Communication.getInstance().getGrupe();
        frmEditClan.getCmbGrupe().removeAllItems();
        for (TreningGrupa treningGrupa : grupe) {
            frmEditClan.getCmbGrupe().addItem(treningGrupa);
        }

        addActionListeners();

    }

    private void addActionListeners() {
        frmEditClan.btnOdustaniActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmEditClan.dispose();
            }
        });

        frmEditClan.btnPronadjiActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String value = frmEditClan.getTxtUslov().getText();
                    if (value.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                frmEditClan,
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
                    frmEditClan.getTblClanovi().setModel(mtc);
                    addTableListener();
                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmEditClan,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmEditClan.getParent(), frmEditClan, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmEditClan,
                            "Neuspesno pronadjeni clanovi!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            private void addTableListener() {
                frmEditClan.tblSelect(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        int row = frmEditClan.getTblClanovi().getSelectedRow();
                        if (row != -1) {
                            TableModelClanovi tmc = (TableModelClanovi) frmEditClan.getTblClanovi().getModel();
                            clanZaIzmenu = tmc.getClan(row);
                            frmEditClan.getTxtIme().setText(clanZaIzmenu.getIme());
                            frmEditClan.getTxtPrezime().setText(clanZaIzmenu.getPrezime());
                            frmEditClan.getTxtEmail().setText(clanZaIzmenu.getEmail());
                            frmEditClan.getTxtStarost().setText(String.valueOf(clanZaIzmenu.getStarost()));
                            frmEditClan.getTxtGrupa().setText(clanZaIzmenu.getGrupa().getNaziv());
                            frmEditClan.getChClanarina().setSelected(clanZaIzmenu.isClanarina());

                            frmEditClan.getTxtIme().setEditable(true);
                            frmEditClan.getTxtPrezime().setEditable(true);
                            frmEditClan.getTxtEmail().setEditable(true);
                            frmEditClan.getTxtIme().setEditable(true);
                            frmEditClan.getBtnIzmeni().setEnabled(true);
                        }

                    }
                });
            }
        });

        frmEditClan.btnIzmeniActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (frmEditClan.getTxtIme().getText().isEmpty() || frmEditClan.getTxtPrezime().getText().isEmpty() || frmEditClan.getTxtEmail().getText().isEmpty()
                            || frmEditClan.getTxtStarost().getText().isEmpty()) {
                        throw new Exception("Sva polja su neophodna!");
                    }

                    int starost =0;
                    try {
                        starost = Integer.parseInt(frmEditClan.getTxtStarost().getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                frmEditClan,
                                "Starost mora biti broj!\n",
                                "Greska", JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    
                    if (starost<0) {
                        JOptionPane.showMessageDialog(
                                frmEditClan,
                                "Starost ne moze biti negativna!\n",
                                "Greska", JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    TreningGrupa tg = (TreningGrupa) frmEditClan.getCmbGrupe().getSelectedItem();
                    clanZaIzmenu.setIme(frmEditClan.getTxtIme().getText());
                    clanZaIzmenu.setPrezime(frmEditClan.getTxtPrezime().getText());
                    clanZaIzmenu.setEmail(frmEditClan.getTxtEmail().getText());
                    clanZaIzmenu.setClanarina(frmEditClan.getChClanarina().isSelected());
                    clanZaIzmenu.setGrupa(tg);
                    clanZaIzmenu.setStarost(starost);
                    
                    Communication.getInstance().updateClan(clanZaIzmenu);

                    JOptionPane.showMessageDialog(
                            frmEditClan,
                            "Uspesno izmenjeni podaci clana!",
                            "Clan", JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmEditClan,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmEditClan.getParent(), frmEditClan, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmEditClan,
                            "Neuspesno izmenjeni podaci clana!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

    }

}
