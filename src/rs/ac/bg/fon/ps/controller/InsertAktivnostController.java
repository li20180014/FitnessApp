/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import rs.ac.bg.fon.ps.domain.Aktivnosti;
import rs.ac.bg.fon.ps.model.TableModelAktivnosti;
import rs.ac.bg.fon.ps.view.form.FrmInsertAktivnost;
import rs.ac.bg.fon.ps.view.form.FrmInsertProgramRada;

/**
 *
 * @author Korisnik
 */
public class InsertAktivnostController {

    private final FrmInsertAktivnost frmInsertAktivnost;
    private final FrmInsertProgramRada frmInsertProgramRada;

    public InsertAktivnostController(FrmInsertAktivnost frmInsertAktivnost, FrmInsertProgramRada frmInsertProgramRada) {
        this.frmInsertAktivnost = frmInsertAktivnost;
        this.frmInsertProgramRada = frmInsertProgramRada;
        addActionListeners();
    }

    public void openInsertForm() {
        frmInsertAktivnost.setLocationRelativeTo(null);
        frmInsertAktivnost.setVisible(true);

    }

    private void addActionListeners() {
        frmInsertAktivnost.btnAddAktivnostActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String naziv = frmInsertAktivnost.getNazivAktivnosti().getText();
                String opis = frmInsertAktivnost.getOpis().getText();
                String trajanje = frmInsertAktivnost.getTrajanje().getText();
                String napomene = frmInsertAktivnost.getNapomene().getText();

                if (naziv.isEmpty() || opis.isEmpty() || trajanje.isEmpty() || napomene.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            frmInsertAktivnost,
                            "Sva polja moraju biti popunjena!\n",
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                int tr=0;
                try {
                    tr = Integer.parseInt(trajanje);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            frmInsertAktivnost,
                            "Trajanje mora biti broj!\n",
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                Aktivnosti a = new Aktivnosti();
                a.setNazivAktivnosti(naziv);
                a.setOpis(opis);
                a.setTrajanjeAktivnosti(Integer.valueOf(tr));
                a.setDodatneNapomene(napomene);

                TableModelAktivnosti model = (TableModelAktivnosti) frmInsertProgramRada.getTblTable().getModel();
                model.dodajAktivnost(a);
                JOptionPane.showMessageDialog(
                        frmInsertAktivnost,
                        "Uspesno dodata aktivnost!\n",
                        "Success", JOptionPane.INFORMATION_MESSAGE
                );
                resetForma();
            }

            private void resetForma() {
                frmInsertAktivnost.getNazivAktivnosti().setText("");
                frmInsertAktivnost.getNapomene().setText("");
                frmInsertAktivnost.getTrajanje().setText("");
                frmInsertAktivnost.getOpis().setText("");
            }
        });

        frmInsertAktivnost.btnOdustaniActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmInsertAktivnost.dispose();
            }
        });
    }

}
