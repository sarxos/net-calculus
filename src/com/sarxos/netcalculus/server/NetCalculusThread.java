package com.sarxos.netcalculus.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Watek klienta pracujacy po stronie serwera.<br>
 */
public class NetCalculusThread extends Thread {

	private Socket socket = null;				// Soket komunikacji z aplikacj¹ klienta
	private DataInputStream input = null;		// Strumien wejsciowy
	private DataOutputStream output = null;		// Strumien wyjsciowy
	private NetCalculusServer server = null;	// Serwer aplikacji
	
	/**
	 * Watek klienta pracujacy po stronie serwera.<br>
	 * @param gniazdo - socket komunikacji
	 * @param server - serwer obliczeniowy wyzwalajacy watek
	 */
	public NetCalculusThread(Socket gniazdo, NetCalculusServer server) {
		
		this.socket = gniazdo;
		this.server = server;
		
		try {
			
			/* NOTE!
			 * Otwieramy polaczenie wejsciowe i wyjsciowe.<br>
			 */
			
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(gniazdo.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * Uruchamianie watku.<br>
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while(true) {
				
				/* NOTE!
				 * W pierwszej kolejnosci cvzytamy komende wyslana w postaci jednego bajtu 
				 * a nastepnie odbieramy dwie liczby double (niejawnie odczytywane 8 bajtow
				 * reprezentujacych liczbe typu double).
				 */ 
				
				byte cmd_byte = input.readByte();

				NetCalculusCommands command = null;			
				for(NetCalculusCommands cc : NetCalculusCommands.values()) {
					if(cmd_byte == cc.getCode()) {
						command = cc;
					}
				}
				
				/* NOTE!
				 * Na poczatku wynik rowny POSITIVE_INFINITY i jesli taka liczba zostanie 
				 * przeslana to wiemy ze nastapil problem w komunikacji lub ew. zla komenda. 
				 */
				
				double wynik = Double.POSITIVE_INFINITY;
				double a = a = input.readDouble();
				double b = b = input.readDouble();
				
				switch(command.getCode()) {
					case 1:
						wynik = 1.0;
						break;
					case 2: 	// PLUS
						wynik = a + b;
						break;
					case 3: 	// MINUS
						wynik = a - b;
						break;
					case 4: 	// MNOZENIE
						wynik = a * b;
						break;
					case 5: 	// DZIELENIE
						wynik = a / b;
						break;
					default:
						System.out.println(
								getClass().getSimpleName() + ": " +
								"Nieznana komenda [" + command + "]" 
						);
						break;
				}

				output.writeDouble(wynik);
				
				System.out.println(
						getClass().getSimpleName() + ": " +
						"Odebrano komenda [" + command + 
						"] [" +	a + "] [" + b + "]"
				);
			}
		} catch(EOFException e) {
		} catch(SocketException e){
			System.out.println(
					getClass().getSimpleName() + ": " +
					"Zresetowno polaczenie"
			);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			// Pamietam o wylogowaniu klienta na koniec petli
			server.logoutClient(this);
		}
	}

	/**
	 * Zwraca strumien wejsciowy.<br>
	 * @return DataInputStream
	 */
	public DataInputStream getInput() {
		return input;
	}

	/**
	 * Zwraca strumien wyjsciowy.<br>
	 * @return DataOutputStream
	 */
	public DataOutputStream getOutput() {
		return output;
	}

	/**
	 * Zwraca serwer asygnujacy watek.<br>
	 * @return NetCalculusServer
	 */
	public NetCalculusServer getServer() {
		return server;
	}

	/**
	 * Zwraca socket komunikacji z aplikacja kliencka.<br>
	 * @return Socket
	 */
	public Socket getSocket() {
		return socket;
	}
}
