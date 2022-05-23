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
import rs.ac.bg.fon.ps.domain.ProgramRada;
import rs.ac.bg.fon.ps.domain.TrenerKluba;
import rs.ac.bg.fon.ps.domain.TreningGrupa;
import rs.ac.bg.fon.ps.domain.Zaduzenje;
import rs.ac.bg.fon.ps.model.TableModelTreneri;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmInsertTreningGrupa;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class InsertTreningGrupaController {

    private final FrmInsertTreningGrupa frmInsertTreningGrupa;
    TableModelTreneri tmt;

    public InsertTreningGrupaController(FrmInsertTreningGrupa frmInsertTreningGrupa) {
        this.frmInsertTreningGrupa = frmInsertTreningGrupa;
        addActionListeners();
    }

    public void openInsertForm() {
        try {
            setUpForm();
            frmInsertTreningGrupa.setLocationRelativeTo(null);
            frmInsertTreningGrupa.setVisible(true);

        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(
                    frmInsertTreningGrupa,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    frmInsertTreningGrupa,
                    "Error loading form\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addActionListeners() {
        frmInsertTreningGrupa.btnDodajGrupuActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (frmInsertTreningGrupa.getNaziv().getText().isEmpty() || frmInsertTreningGrupa.getKapacitet().getText().isEmpty()) {
                        throw new Exception("Sva polja su neophodna!");
                    }
                    int kapacitet=0;
                    try {
                       kapacitet = Integer.parseInt(frmInsertTreningGrupa.getKapacitet().getText()); 
                    } catch (NumberFormatException ne) {
                        JOptionPane.showMessageDialog(
                            frmInsertTreningGrupa,
                            "Kapacitet mora biti broj!",
                            "Greska", JOptionPane.INFORMATION_MESSAGE
                    );
                        return;
                    }
                    if (kapacitet==0) {
                         JOptionPane.showMessageDialog(
                            frmInsertTreningGrupa,
                            "Kapacitet mora biti veci od nule!",
                            "Greska", JOptionPane.INFORMATION_MESSAGE
                    );
                         return;
                    }

                    ProgramRada pr = (ProgramRada) frmInsertTreningGrupa.getProgrami().getSelectedItem();
                    List<TrenerKluba> treneri = tmt.getListaTrenera();
                    if (treneri.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            frmInsertTreningGrupa,
                            "Morate uneti bar jedno zaduzenje!",
                            "Greska", JOptionPane.INFORMATION_MESSAGE
                    );
                        return;
                    }
                    TreningGrupa tg = new TreningGrupa();
                    tg.setNaziv(frmInsertTreningGrupa.getNaziv().getText());
                    tg.setKapacitet(kapacitet);
                    tg.setProgram(pr);
                    tg.setZaduzeniTreneri(treneri);
                    
                    
                    Communication.getInstance().dodajGrupu(tg);
                    JOptionPane.showMessageDialog(
                            frmInsertTreningGrupa,
                            "Uspesno kreirana trening grupa!",
                            "Trening Grupa", JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    setUpForm();
                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(
                            frmInsertTreningGrupa,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                    Coordinator.getInstance().closeAllForms((FrmMain) frmInsertTreningGrupa.getParent(), frmInsertTreningGrupa, null);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            frmInsertTreningGrupa,
                            "Neuspesno kreirana trening grupa!\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        );

        frmInsertTreningGrupa.btnDodajTreneraAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrenerKluba tk = (TrenerKluba) frmInsertTreningGrupa.getCmbTreneri().getSelectedItem();
                List<TrenerKluba> treneri = tmt.getListaTrenera();
                for (TrenerKluba trenerKluba : treneri) {
                    if (trenerKluba.equals(tk)) {
                        JOptionPane.showMessageDialog(
                                frmInsertTreningGrupa,
                                "Trener vec postoji u grupi!\n",
                                "Error", JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
                tmt.dodajTrenera(tk);

            }
        });

        frmInsertTreningGrupa.btnCancelAddActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                frmInsertTreningGrupa.dispose();
            }
        }
        );
    }

    private void setUpForm() throws Exception {
        frmInsertTreningGrupa.getNaziv().setText("");
        frmInsertTreningGrupa.getKapacitet().setText("");
        List<ProgramRada> programi = Communication.getInstance().getProgramiRada();
        frmInsertTreningGrupa.getProgrami().removeAllItems();
        for (ProgramRada pr : programi) {
            frmInsertTreningGrupa.getProgrami().addItem(pr);
        }

        List<TrenerKluba> treneri = Communication.getInstance().getTreneri();
        frmInsertTreningGrupa.getCmbTreneri().removeAllItems();
        for (TrenerKluba trenerKluba : treneri) {
            frmInsertTreningGrupa.getCmbTreneri().addItem(trenerKluba);
        }

        tmt = new TableModelTreneri();
        frmInsertTreningGrupa.getTblTreneri().setModel(tmt);
    }

}
