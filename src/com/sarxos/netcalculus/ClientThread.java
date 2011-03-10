package com.sarxos.netcalculus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Watek klienta chodzi w tle i na rozklaz aplikacji wysyla lub odbiera zorkazy
 * od serwerow obliczeniowych.<br>
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class ClientThread extends Thread {

	Socket socket = null; // soket komunikacji
	DataInputStream input = null; // Strumien wejsciowy
	DataOutputStream output = null; // Strumien wyjsciowy
	boolean isRunning = true; // Czy watek ma dzialac (uzywane do zapetlenia)

	/**
	 * Konstruktor. Generuje IOException jesli serwer o podanym adresie nie ma
	 * ustawionej uslugi obliczeniowej.<br>
	 * 
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public ClientThread(String host, int port) throws IOException {
		try {
			socket = new Socket(host, port);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		super.run();
		while (isRunning) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Wysylanie rozklazu dzia³ania.
	 * 
	 * @param a - liczba a
	 * @param b - liczba b
	 * @param code - kod rozkazu (kody sa w NetCalculusCommands)
	 */
	public synchronized void send(double a, double b, byte code) {
		try {
			output.writeByte(code); // wpierw kod
			output.writeDouble(a); // potem reszta
			output.writeDouble(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Odbieranie odpowiedzi serwra.<br>
	 * 
	 * @return double
	 */
	public synchronized double receive() {
		double wynik = Double.NEGATIVE_INFINITY;
		try {
			wynik = input.readDouble();
			wynik = Math.round(wynik * 1E+9d) / 1E+9d;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wynik;
	}

	/**
	 * Zatrzymywanie watku klienta i zamykanie polaczenia.<br>
	 */
	public void stopThread() {
		isRunning = false;
		if (!interrupted()) {
			try {
				socket.shutdownInput();
				socket.shutdownOutput();
				input.close();
				output.close();
			} catch (IOException e) {
			}
			interrupt();
		}
	}
}
