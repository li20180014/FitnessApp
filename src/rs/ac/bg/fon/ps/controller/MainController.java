/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmMain;

/**
 *
 * @author Korisnik
 */
public class MainController {

    private final FrmMain frmMain;

    public MainController(FrmMain frmMain) {
        this.frmMain = frmMain;
        addActionListener();
    }

    private void addActionListener() {
        
        frmMain.addButtonLogoutActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Communication.getInstance().getSocket().close();
                    Communication.getInstance().restart();   
                } catch (Exception ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
                frmMain.dispose();
                Coordinator.getInstance().openLoginForm();
            }
        });

        frmMain.itemInsertTrenerActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openInsertTrenerForm(frmMain);
            }
        });

        frmMain.itemInsertClanAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openInsertClanForm(frmMain);
            }
        });

        frmMain.itemEditClanAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openEditClanForm(frmMain);
            }
        });

        frmMain.itemDeleteClanCreateAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openDeleteClanForm(frmMain);
            }
        });

        frmMain.itemSearchClanAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openSearchClanForm(frmMain);
            }
        });

        frmMain.itemInsertProgramRadaDeleteAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openInsertProgramForm(frmMain);
            }
        });

        frmMain.itemInsertTreningGrupaAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openInsertTreningGrupa(frmMain);
            }
        });

        frmMain.itemEditTreningGrupaAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinator.getInstance().openEditTreningGrupa(frmMain);
            }
        });
    }

    public void openForm() {
        frmMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
       // frmMain.setUndecorated(true);
        frmMain.setVisible(true);
        frmMain.setLblTrener();
    }

}
