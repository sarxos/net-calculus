package com.sarxos.netcalculus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Serwer obliczeniowy dla aplikacji NetCalculus.<br>
 */
public class NetCalculusServer extends ServerSocket {

	/**
	 * Czy dzia³a (zmienna uzywana w zapetlaniu serwera przy akceptowaniu klientow).<br>
	 */
	private boolean isRunning;
	/**
	 * Port komunikacji.<br>
	 */
	private int port = -1;
	
	/**
	 * Klienci podlaczeni do danej instancji serwera.<br> 
	 */
	private List <NetCalculusThread> klienci = new ArrayList<NetCalculusThread>();
	
	/**
	 * Konstruktor serwera obliczeniowego. Jako argument podajemy port na ktorym serwer 
	 * ma sie komunikowac. Port ro liczba z zakresu 1 .. 65535
	 * @param port - port komunikacji
	 * @throws IOException
	 */
	public NetCalculusServer(int port) throws IOException {
		super(port);
		this.port = port;
	}
	
	/**
	 * Uruchamianie serwera.<br>
	 */
	public void runServer() {
		
		isRunning = true;
		
		System.out.println("Calculus server started at port " + port + " on " + (new Date()));
		
		try {
			
			/* NOTE!
			 * Metoda ta jest zapetlona i oczekuje na kolejnych klientow. Kazdy
			 * klient ovblusgiwany jest w osobnym watku klienckim. Metoda accept 
			 * blokuje postep metody uruchomieniowej i czeka na polaczenie na
			 * danym w konstruktorze porcie.<br>
			 */
			
			while(isRunning) {
				Socket gniazdo_klienta = this.accept();
				NetCalculusThread watek_klienta = new NetCalculusThread(gniazdo_klienta, this);
				klienci.add(watek_klienta);
				watek_klienta.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally { }
	}

	/**
	 * Wylogowywuje klienta.<br> 
	 * @param klient
	 */
	public synchronized void logoutClient(NetCalculusThread klient) {
		
		klienci.remove(klient);
		
		try {
			
			// Najpierw KONIECZNIE pozamykac komunikacje, a dopiero potem strumienie
			klient.getSocket().shutdownInput();		
			klient.getSocket().shutdownOutput();
			klient.getInput().close();
			klient.getOutput().close();
			
			// Na koncu dopiero zamknac socketa
			klient.getSocket().close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Uruchamianie serwera. W konsoli podajemy argument -port [portNum]<br>
	 * @param args
	 */
	public static void main(String[] args) {
		
		int port_number = -1;
		if(args.length == 2) {
			if(args[0].indexOf("-port") != -1) {
				String port = args[1];
				port_number = Integer.parseInt(port);
			}
		}
		
		if(port_number == -1) {
			System.out.println("Wpisz: NetCalculusServer -port [portnum]\n");
			System.exit(1);
		}
		
		try {
			NetCalculusServer cs = new NetCalculusServer(port_number);
			cs.runServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
