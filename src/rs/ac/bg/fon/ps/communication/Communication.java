/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.ps.communication;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import rs.ac.bg.fon.ps.domain.Aktivnosti;
import rs.ac.bg.fon.ps.domain.Clan;
import rs.ac.bg.fon.ps.domain.ProgramRada;
import rs.ac.bg.fon.ps.domain.TezinaPrograma;
import rs.ac.bg.fon.ps.domain.TrenerKluba;
import rs.ac.bg.fon.ps.domain.TreningGrupa;
import rs.ac.bg.fon.ps.domain.Zaduzenje;
import rs.ac.bg.fon.ps.operations.Operation;
import rs.ac.bg.fon.ps.transfer.Receiver;
import rs.ac.bg.fon.ps.transfer.Request;
import rs.ac.bg.fon.ps.transfer.Response;
import rs.ac.bg.fon.ps.transfer.Sender;

/**
 *
 * @author Korisnik
 */
public class Communication {

    private final Socket socket;
    private final Sender sender;
    private final Receiver receiver;
    private static Communication instance;

    private Communication() throws Exception {
        socket = new Socket("localhost", 9000);
        sender = new Sender(socket);
        receiver = new Receiver(socket);
    }

    public static Communication getInstance() throws Exception {
        if (instance == null) {
            instance = new Communication();
        }
        return instance;
    }

    public static void restart() {
        instance = null;
    }

    public Socket getSocket() {
        return socket;
    }

    public TrenerKluba login(String username, String password) throws Exception {
        TrenerKluba trenerKluba = new TrenerKluba();
        trenerKluba.setUsername(username);
        trenerKluba.setPassword(password);

        Request request = new Request();
        request.setOperation(Operation.LOGIN);
        request.setArgument(trenerKluba);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (TrenerKluba) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public boolean addTrener(String username, String password, String ime, String prezime, int starost) throws Exception {

        TrenerKluba trenerKluba = new TrenerKluba();
        trenerKluba.setUsername(username);
        trenerKluba.setPassword(password);
        trenerKluba.setIme(ime);
        trenerKluba.setPrezime(prezime);
        trenerKluba.setStarost(starost);

        Request request = new Request();
        request.setOperation(Operation.ZAPAMTI_TRENERA_KLUBA);
        request.setArgument(trenerKluba);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (boolean) response.getResult();
        } else {
            throw response.getException();
        }

    }

    public void dodajProgram(String nazivPrograma, TezinaPrograma tezina, List<Aktivnosti> lista) throws Exception {
        ProgramRada pr = new ProgramRada();
        pr.setNaziv(nazivPrograma);
        pr.setTezina(String.valueOf(tezina));
        pr.setListaAktivnosti(lista);

        Request request = new Request();
        request.setOperation(Operation.ZAPAMTI_PROGRAM_RADA);
        request.setArgument(pr);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() != null) {
            throw response.getException();
        }

    }

    public List<ProgramRada> getProgramiRada() throws Exception {
        Request request = new Request();
        request.setOperation(Operation.VRATI_PROGRAME_RADA);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<ProgramRada>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public boolean dodajGrupu(TreningGrupa tg) throws Exception {

        Request request = new Request();
        request.setOperation(Operation.ZAPAMTI_TRENING_GRUPU);
        request.setArgument(tg);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (boolean) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<TreningGrupa> getGrupe() throws Exception {
        Request request = new Request();
        request.setOperation(Operation.UCITAJ_TRENING_GRUPU);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<TreningGrupa>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public boolean updateGrupu(TreningGrupa tg) throws Exception {

        Request request = new Request();
        request.setOperation(Operation.IZMENI_TRENING_GRUPU);
        request.setArgument(tg);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (boolean) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public boolean addClan(String email, boolean clanarina, String ime, String prezime, int starost, TreningGrupa tg) throws Exception {

        Clan clan = new Clan();

        clan.setIme(ime);
        clan.setEmail(email);
        clan.setPrezime(prezime);
        clan.setStarost(starost);
        clan.setClanarina(clanarina);
        clan.setGrupa(tg);

        Request request = new Request();
        request.setOperation(Operation.ZAPAMTI_CLANA);
        request.setArgument(clan);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (boolean) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<Clan> getClanovi() throws Exception {
        Request request = new Request();
        request.setOperation(Operation.NADJI_CLANOVE);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Clan>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public boolean deleteClan(Clan c) throws Exception {
        Request request = new Request();
        request.setOperation(Operation.OBRISI_CLANA);
        request.setArgument(c);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (boolean) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<Clan> nadjiClanaPoUslovu(String value) throws Exception {
        Request request = new Request();
        request.setOperation(Operation.UCITAJ_CLANA);
        request.setArgument(value);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Clan>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<TrenerKluba> getTreneri() throws Exception {
        Request request = new Request();
        request.setOperation(Operation.UCITAJ_TRENERE);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<TrenerKluba>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public boolean updateClan(Clan clanZaIzmenu) throws Exception {

        Request request = new Request();
        request.setOperation(Operation.IZMENI_CLANA);
        request.setArgument(clanZaIzmenu);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (boolean) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<TreningGrupa> getGrupePoUslovu(String uslov) throws Exception {
        Request request = new Request();
        request.setOperation(Operation.NADJI_TRENING_GRUPE);
        request.setArgument(uslov);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<TreningGrupa>) response.getResult();
        } else {
            throw response.getException();
        }
    }

    public List<Zaduzenje> getZaduzenjaZaGrupu(TreningGrupa treningGrupa) throws Exception {
        Request request = new Request();
        request.setOperation(Operation.UCITAJ_ZADUZENJA);
        request.setArgument(treningGrupa);
        sender.send(request);

        Response response = (Response) receiver.receive();
        if (response.getException() == null) {
            return (List<Zaduzenje>) response.getResult();
        } else {
            throw response.getException();
        }
    }

}
