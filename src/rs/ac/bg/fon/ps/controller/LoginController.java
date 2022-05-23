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
import rs.ac.bg.fon.ps.constants.Constants;
import rs.ac.bg.fon.ps.domain.TrenerKluba;
import rs.ac.bg.fon.ps.main.Main;
import rs.ac.bg.fon.ps.view.coordinator.Coordinator;
import rs.ac.bg.fon.ps.view.form.FrmLogin;

/**
 *
 * @author Korisnik
 */
public class LoginController {

    private final FrmLogin frmLogin;

    public LoginController(FrmLogin frmLogin) {
        this.frmLogin = frmLogin;
        addActionListener();
    }

    private void addActionListener() {
        frmLogin.loginAddActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser(e);
            }
            
            private void loginUser(ActionEvent actionEvent) {
                
                try {
                    String username = frmLogin.getTxtUsername().getText().trim();
                    String password = String.copyValueOf(frmLogin.getTxtPassword().getPassword());

                    validateForm(username, password);

                    TrenerKluba trenerKluba= Communication.getInstance().login(username,password);
                    JOptionPane.showMessageDialog(
                            frmLogin,
                            "Dobrodosli " + trenerKluba.getIme()+ " " + trenerKluba.getPrezime(),
                            "Uspesno prijavljivanje", JOptionPane.INFORMATION_MESSAGE
                    );
                    frmLogin.dispose();
                    Coordinator.getInstance().addParam(Constants.CURRENT_USER, trenerKluba);
                    Coordinator.getInstance().openMainForm();
                }catch(SocketException e){
                    JOptionPane.showMessageDialog(
                            frmLogin,
                            "Connection error. Please try again later.",
                            "Greska", JOptionPane.ERROR_MESSAGE
                    );
                    
                    //zatvoriti sve forme??
                }catch (Exception e) {
                    JOptionPane.showMessageDialog(frmLogin, e.getMessage(), "Neuspesno prijavljivanje", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void validateForm(String username, String password) throws Exception {
                String errorMessage = "";
                if (username.isEmpty()) {
                    errorMessage += "Username ne moze biti prazan!\n";
                }
                if (password.isEmpty()) {                   
                    errorMessage += "Password ne moze biti prazan!\n";
                }
                if (!errorMessage.isEmpty()) {
                    throw new Exception(errorMessage);
                }
            }
        });
    }
    

     public void openForm() {
        frmLogin.setVisible(true);
    }
     
     
}
