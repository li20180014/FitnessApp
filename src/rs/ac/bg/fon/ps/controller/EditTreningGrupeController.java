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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.domain.ProgramRada;
import rs.ac.bg.fon.ps.domain.TrenerKluba;
import rs.ac.bg.fon.ps.domain.TreningGrupa;
import rs.ac.bg.fon.ps.domain.Zaduzenje;
import rs.ac.bg.fon.ps.model.TableModelGrupe;
import rs.ac.bg.fon.ps.model.TableModelTreneri;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmEditTreningGrupa;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class EditTreningGrupeController {

    private final FrmEditTreningGrupa frmEditTreningGrupa;
    private List<ProgramRada> programi;
    private TreningGrupa treningGrupa;
     TableModelTreneri tmt;

    public EditTreningGrupeController(FrmEditTreningGrupa frmEditTreningGrupa) {
        this.frmEditTreningGrupa = frmEditTreningGrupa;

    }

    public void openEditForm() {
        try {
            setUpForm();
            frmEditTreningGrupa.setLocationRelativeTo(null);
            frmEditTreningGrupa.setVisible(true);

        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(
                    frmEditTreningGrupa,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    frmEditTreningGrupa,
                    "Error loading form\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void setUpForm() throws Exception {
        frmEditTreningGrupa.getBtnSacuvaj().setEnabled(false);
        frmEditTreningGrupa.getNaziv().setEditable(false);
        frmEditTreningGrupa.getKapacitet().setEditable(false);
        frmEditTreningGrupa.getProgram().setEditable(false);
                                
        programi = Communication.getInstance().getProgramiRada();
        frmEditTreningGrupa.getProgrami().removeAllItems();
        for (ProgramRada pr : programi) {
            frmEditTreningGrupa.getProgrami().addItem(pr);
        }
        List<TrenerKluba> treneri = Communication.getInstance().getTreneri();
        frmEditTreningGrupa.getCmbTreneri().removeAllItems();
        for (TrenerKluba trenerKluba : treneri) {
            frmEditTreningGrupa.getCmbTreneri().addItem(trenerKluba);
        }
        TableModelGrupe tmg = new TableModelGrupe();
        List<TreningGrupa> grupe = Communication.getInstance().getGrupe();
        for (TreningGrupa treningGrupa : grupe) {
            tmg.dodajGrupu(treningGrupa);
        }
        frmEditTreningGrupa.getTblGrupe().setModel(tmg);
        tmt = new TableModelTreneri();
        frmEditTreningGrupa.getTblTreneri().setModel(tmt);
        addActionListeners();
    }

    private void addActionListeners() {

        frmEditTreningGrupa.btnPronadjiAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String uslov = frmEditTreningGrupa.getTxtUslov().getText();
                    if (uslov.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                frmEditTreningGrupa,
                                "Nije unet uslov za pretragu!",
                                "Greska", JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    List<TreningGrupa> grupe = Communication.getInstance().getGrupePoUslovu(uslov);

                    if (grupe.isEmpty()) {
                        throw new Exception("Nema grupa po zadatoj vrednosti!");
                    }
                    TableModelGrupe tmg = new TableModelGrupe();

                    for (TreningGrupa treningGrupa : grupe) {
                        tmg.dodajGrupu(treningGrupa);
                    }
                    frmEditTreningGrupa.getTblGrupe().setModel(tmg);
                    addTableListener();

                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmEditTreningGrupa,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmEditTreningGrupa.getParent(), frmEditTreningGrupa, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmEditTreningGrupa,
                            "Neuspesno pronadjena trening grupa!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            private void addTableListener() {
                frmEditTreningGrupa.tblSelect(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        int row = frmEditTreningGrupa.getTblGrupe().getSelectedRow();

                        if (row != -1) {
                            TableModelGrupe tmg = (TableModelGrupe) frmEditTreningGrupa.getTblGrupe().getModel();
                            treningGrupa = tmg.getGrupa(row);
                            treningGrupa.setStatus("NEPROMENJEN");
                            try {
                                List<Zaduzenje> zaduzenja = Communication.getInstance().getZaduzenjaZaGrupu(treningGrupa);
                                tmt = new TableModelTreneri();
                                if (!zaduzenja.isEmpty()) {
                                    for (Zaduzenje zaduzenje : zaduzenja) {
                                        TrenerKluba tk = zaduzenje.getTrenerKluba();
                                        tmt.dodajTrenera(tk);
                                    }
                                }

                                frmEditTreningGrupa.getTblTreneri().setModel(tmt);
                                frmEditTreningGrupa.getBtnSacuvaj().setEnabled(true);
                                frmEditTreningGrupa.getNaziv().setEditable(true);
                                frmEditTreningGrupa.getKapacitet().setEditable(true);
                                frmEditTreningGrupa.getNaziv().setText(treningGrupa.getNaziv());
                                frmEditTreningGrupa.getKapacitet().setText(String.valueOf(treningGrupa.getKapacitet()));
                                frmEditTreningGrupa.getProgram().setText(treningGrupa.getProgram().getNaziv());
                                frmEditTreningGrupa.getProgram().setEditable(false);

                            } catch (Exception ex) {
                                Logger.getLogger(EditTreningGrupeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                });
            }
        });

        frmEditTreningGrupa.btnIzmeniGrupuActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (frmEditTreningGrupa.getNaziv().getText().isEmpty() || frmEditTreningGrupa.getKapacitet().getText().isEmpty()) {
                        throw new Exception("Sva polja su neophodna!");
                    }

                    int kapacitet = 0;
                    try {
                        kapacitet = Integer.parseInt(frmEditTreningGrupa.getKapacitet().getText());
                    } catch (NumberFormatException ne) {
                        JOptionPane.showMessageDialog(
                                frmEditTreningGrupa,
                                "Kapacitet mora biti broj!",
                                "Greska", JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }
                    if (kapacitet == 0) {
                        JOptionPane.showMessageDialog(
                                frmEditTreningGrupa,
                                "Kapacitet mora biti veci od nule!",
                                "Greska", JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    ProgramRada pr = (ProgramRada) frmEditTreningGrupa.getProgrami().getSelectedItem();
                    treningGrupa.setNaziv(frmEditTreningGrupa.getNaziv().getText());
                    treningGrupa.setKapacitet(kapacitet);
                    treningGrupa.setProgram(pr);
                    TableModelTreneri tmt = (TableModelTreneri) frmEditTreningGrupa.getTblTreneri().getModel();
                    List<TrenerKluba> treneri = tmt.getListaTrenera();
                    if (treneri.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            frmEditTreningGrupa,
                            "Grupa mora imati barem jednog zaduzenog trenera!",
                            "Trening Grupa", JOptionPane.INFORMATION_MESSAGE
                    );
                        return;
                    }
                    treningGrupa.setZaduzeniTreneri(treneri);
                   
                    
                    
                    Communication.getInstance().updateGrupu(treningGrupa);

                    JOptionPane.showMessageDialog(
                            frmEditTreningGrupa,
                            "Uspesno izmenjena trening grupa!",
                            "Trening Grupa", JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmEditTreningGrupa,
                            ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmEditTreningGrupa.getParent(), frmEditTreningGrupa, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmEditTreningGrupa,
                            "Neuspesno izmenjena trening grupa!\n" + ex.getMessage(),
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        );

        frmEditTreningGrupa.btnCancelAddActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                frmEditTreningGrupa.dispose();
            }
        }
        );

        frmEditTreningGrupa.btnDodajTreneraAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrenerKluba tk = (TrenerKluba) frmEditTreningGrupa.getCmbTreneri().getSelectedItem();
                List<TrenerKluba> treneri = tmt.getListaTrenera();
                for (TrenerKluba trenerKluba : treneri) {
                    if (trenerKluba.equals(tk)) {
                        JOptionPane.showMessageDialog(
                                frmEditTreningGrupa,
                                "Trener je vec angazovan u grupi!\n",
                                "Error", JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
                tmt.dodajTrenera(tk);
                treningGrupa.setStatus("PROMENJEN");
            }
        });

        frmEditTreningGrupa.btnObrisiTreneraAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = frmEditTreningGrupa.getTblTreneri().getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(
                            frmEditTreningGrupa,
                            "Niste selektovali angazovanje za brisanje!\n",
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                TableModelTreneri tmt = (TableModelTreneri) frmEditTreningGrupa.getTblTreneri().getModel();
                tmt.obrisiTrenera(row);
                treningGrupa.setStatus("PROMENJEN");
            }
        });
    }

}
