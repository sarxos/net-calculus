package com.sarxos.netcalculus.server;

/**
 * Komendy sterujace serwera. Ich kod wyslany w pierwszej kolejnosci okresla co
 * serwer ma zrobic z dwoma liczbami ktore otrzyma pozniej.<br>
 */
public enum NetCalculusCommands {

	HELLO(1),		// logowanie
	PLUS(2),		// +
	MINUS(3),		// -
	MNOZENIE(4),	// *
	DZIELENIE(5),	// /
	POTEGA(6),		// ^
	MODULUS(7);		// %
	
	/**
	 * Kod polecenia.<br>
	 */
	private byte code = 0;
	
	/**
	 * Konstruktor polecenia - tylko prywatny ze wzgledu na enumeracje.<br>
	 * @param code
	 */
	private NetCalculusCommands(int code) {
		this.code = (byte) code;
	}
	
	/**
	 * Zwraca kod polecenia.<br>
	 * @return byte
	 */
	public byte getCode() {
		return code;
	}
}
