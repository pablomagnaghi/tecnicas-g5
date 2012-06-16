package main.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.config.Constantes;
import main.states.BolitaGrande;
import main.states.BolitaNormal;
import main.states.BolitaNula;

public class Celda {
	
	private String idCeldaIzquierda;
	private String idCeldaDerecha;
	private String idCeldaArriba;
	private String idCeldaAbajo;
	private Bolita bolita;
	private String id;
	
	public Celda(String val, String id){
		if (Constantes.BOLITA.equals(val)){
			this.bolita = new BolitaNormal();
		} else if (Constantes.BOLON.equals(val)){
			this.bolita = new BolitaGrande();
		} else if ("".equals(val)){
			this.bolita = new BolitaNula();
		} 
		this.setId(id);
	}
	
	public String getSiguienteCelda(String direccion){
		String siguienteCelda = null;
		if (Constantes.DERECHA.equals(direccion)){
			siguienteCelda = idCeldaDerecha;
		} else if (Constantes.IZQUIERDA.equals(direccion)){
			siguienteCelda = idCeldaIzquierda;
		} else if (Constantes.ARRIBA.equals(direccion)){
			siguienteCelda = idCeldaArriba;
		} else if (Constantes.ABAJO.equals(direccion)){
			siguienteCelda = idCeldaAbajo;
		}
		if (siguienteCelda == null){
			return "";
		}
		return siguienteCelda;
	}

//	public String getCeldaIzquierda() {
//		if (this.idCeldaIzquierda == null){
//			return "";
//		}
//		return idCeldaIzquierda;
//	}
//
	public void setCeldaIzquierda(String celdaIzquierda) {
		this.idCeldaIzquierda = celdaIzquierda;
	}
//
//	public String getCeldaDerecha() {
//		if (this.idCeldaDerecha == null){
//			return "";
//		}
//		return idCeldaDerecha;
//	}
//	
	public void setCeldaDerecha(String celdaDerecha) {
		this.idCeldaDerecha = celdaDerecha;
	}
//
//	public String getCeldaArriba() {
//		if (this.idCeldaArriba == null){
//			return "";
//		}
//		return idCeldaArriba;
//	}
//
	public void setCeldaArriba(String celdaArriba) {
		this.idCeldaArriba = celdaArriba;
	}
//
//	public String getCeldaAbajo() {
//		if (this.idCeldaAbajo == null){
//			return "";
//		}
//		return idCeldaAbajo;
//	}

	public void setCeldaAbajo(String celdaAbajo) {
		this.idCeldaAbajo = celdaAbajo;
	}

	public Bolita getBolita() {
		return bolita;
	}

	public void setBolita(Bolita bolita) {
		this.bolita = bolita;
	}

	public String toString() {
		String s = new String();
		s = this.bolita.imprimir();
		return s;
	}

	public void visitarPorPacman() {
		this.bolita.comer();
		this.bolita = new BolitaNula();
	}

	public Boolean esBifurcacion(){
		Boolean celdaAbajoPosible = this.idCeldaAbajo != null; 
		Boolean celdaArribaPosible = this.idCeldaArriba != null;
		Boolean celdaDerechaPosible = this.idCeldaDerecha != null;
		Boolean celdaIzquierdaPosible = this.idCeldaIzquierda != null;
		Boolean bifurcacionPosibleUno = celdaDerechaPosible && celdaIzquierdaPosible && (celdaAbajoPosible || celdaArribaPosible);
		Boolean bifurcacionPosibleDos = celdaAbajoPosible && celdaArribaPosible && (celdaIzquierdaPosible || celdaDerechaPosible);
		return (bifurcacionPosibleUno || bifurcacionPosibleDos);
	}

	public String getContent() {
		return this.bolita.getContent();
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}
	
	private static String getFila(String identificador){
		Pattern p = Pattern.compile("(\\d{2})\\d{2}");
		Matcher m = p.matcher(identificador);
		if (m.find()){
			return m.group(1);
		}
		return null;
	}
	
	private static String getColumna(String identificador){
		Pattern p = Pattern.compile("\\d{2}(\\d{2})");
		Matcher m = p.matcher(identificador);
		if (m.find()){
			return m.group(1);
		}
		return null;
	}
	
	public static Integer getDistanciaEntreCeldas(Celda uno, Celda dos){
		return getDistanciaEntreCeldas(uno.id, dos.id);
	}
	
	public static Integer getDistanciaEntreCeldas(String idUno, String idDos){
		Integer filUno = new Integer(getFila(idUno));
		Integer filDos = new Integer(getFila(idDos));
		Integer colUno = new Integer(getColumna(idUno));
		Integer colDos = new Integer(getColumna(idDos));
		Integer distanciaFila = filUno - filDos;
		if (distanciaFila < 0){
			distanciaFila = distanciaFila * -1;
		}
		Integer distanciaCol = colUno - colDos;
		if (distanciaCol < 0){
			distanciaCol = distanciaCol * -1;
		}
		return distanciaCol + distanciaFila;
	}

	public String getColumna() {
		return getColumna(this.id);
	}
	
	public String getFila() {
		return getFila(this.id);
	}
}
