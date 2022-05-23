/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.view.coordinator;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import rs.ac.bg.fon.ps.communication.Communication;
import rs.ac.bg.fon.ps.controller.DeleteClanController;
import rs.ac.bg.fon.ps.controller.EditClanController;
import rs.ac.bg.fon.ps.controller.EditTreningGrupeController;
import rs.ac.bg.fon.ps.controller.InsertAktivnostController;
import rs.ac.bg.fon.ps.controller.InsertClanController;
import rs.ac.bg.fon.ps.controller.InsertProgramController;
import rs.ac.bg.fon.ps.controller.InsertTrenerController;
import rs.ac.bg.fon.ps.controller.InsertTreningGrupaController;
import rs.ac.bg.fon.ps.controller.LoginController;
import rs.ac.bg.fon.ps.controller.MainController;
import rs.ac.bg.fon.ps.controller.SearchClanController;
import rs.ac.bg.fon.ps.view.form.FrmDeleteClan;
import rs.ac.bg.fon.ps.view.form.FrmEditClan;
import rs.ac.bg.fon.ps.view.form.FrmEditTreningGrupa;
import rs.ac.bg.fon.ps.view.form.FrmInsertAktivnost;
import rs.ac.bg.fon.ps.view.form.FrmInsertClan;
import rs.ac.bg.fon.ps.view.form.FrmInsertProgramRada;
import rs.ac.bg.fon.ps.view.form.FrmInsertTrener;
import rs.ac.bg.fon.ps.view.form.FrmInsertTreningGrupa;
import rs.ac.bg.fon.ps.view.form.FrmLogin;
import rs.ac.bg.fon.ps.view.form.FrmMain;
import rs.ac.bg.fon.ps.view.form.FrmSearchClan;

/**
 *
 * @author Korisnik
 */
public class Coordinator {

    private static Coordinator instance;
    private final Map<String, Object> params;
    private final MainController mainController;

    public static Coordinator getInstance() {
        if (instance == null) {
            instance = new Coordinator();
        }
        return instance;
    }

    private Coordinator() {
        params = new HashMap<>();
        mainController = new MainController(new FrmMain());
    }

    public void addParam(String name, Object key) {
        params.put(name, key);
    }

    public Object getParam(String name) {
        return params.get(name);
    }

    public void openLoginForm() {
        LoginController loginController = new LoginController(new FrmLogin());
        loginController.openForm();
    }

    public void openMainForm() {
        mainController.openForm();
    }

    public void openInsertTrenerForm(FrmMain form) {
        InsertTrenerController insertTrenerController = new InsertTrenerController(new FrmInsertTrener(form, true));
        insertTrenerController.openCreateForm();
    }

    public void openInsertProgramForm(FrmMain frmMain) {
        InsertProgramController insertProgramController = new InsertProgramController(new FrmInsertProgramRada(frmMain, true));
        insertProgramController.openInsertForm();
    }

    public void openInsertAktivnostForm(FrmMain frmMain, FrmInsertProgramRada frmInsertProgramRada) {
        InsertAktivnostController insertAktivnostController = new InsertAktivnostController(new FrmInsertAktivnost(frmMain, true), frmInsertProgramRada);
        insertAktivnostController.openInsertForm();
    }

    public void openInsertTreningGrupa(FrmMain frmMain) {
        InsertTreningGrupaController insertTreningGrupaController = new InsertTreningGrupaController(new FrmInsertTreningGrupa(frmMain, true));
        insertTreningGrupaController.openInsertForm();
    }

    public void openEditTreningGrupa(FrmMain frmMain) {
        EditTreningGrupeController editTreningGrupeController = new EditTreningGrupeController(new FrmEditTreningGrupa(frmMain, true));
        editTreningGrupeController.openEditForm();
    }

    public void openInsertClanForm(FrmMain frmMain) {
        InsertClanController insertClanController = new InsertClanController(new FrmInsertClan(frmMain, true));
        insertClanController.openInsertForm();
    }

    public void openEditClanForm(FrmMain frmMain) {
        EditClanController editClanController = new EditClanController(new FrmEditClan(frmMain, true));
        editClanController.openSearchForm();
    }

    public void openDeleteClanForm(FrmMain frmMain) {
        DeleteClanController deleteClanController = new DeleteClanController(new FrmDeleteClan(frmMain, true));
        deleteClanController.openDeleteForm();
    }

    public void openSearchClanForm(FrmMain frmMain) {
        SearchClanController searchClanController = new SearchClanController(new FrmSearchClan(frmMain, true));
        searchClanController.openSearchForm();
    }

    public void closeAllForms(FrmMain main, JDialog parent, JDialog current) {

        if (current != null) {
            current.dispose();
        }

        parent.dispose();
        main.dispose();

        try {
            Communication.restart();
        } catch (Exception ex) {
            Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
        }
        openLoginForm();
    }

    public void closeAllForms(FrmMain main, JDialog parent, JDialog parent2, JDialog current) {
        main.dispose();
        parent.dispose();
        parent2.dispose();
        current.dispose();

        Communication.restart();
        openLoginForm();
    }
}
