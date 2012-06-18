package main.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.config.Constantes;
import main.states.ComportamientoZonzo;

public class Laberinto {

	private Map<String, Celda> mapa;
	private List<Fantasma> fantasmas;
	private String posicionInicioFantasma;
	private String posicionInicioPacman;
	private Integer cantFil;
	private Integer cantCol;
	private Integer nodoAncho;
	private Integer nodoAlto;

	public Laberinto(Integer filas, Integer columnas){
		this.cantFil = filas;
		this.cantCol = columnas;
		this.mapa = new HashMap<String, Celda>();
		this.fantasmas = new ArrayList<Fantasma>();
	}

	public Map<String, Celda> getMapa() {
		return mapa;
	}

	public void imprimirLaberintoAXml(){
		System.out.println("<laberinto ancho=\"" + this.cantCol + "\" alto=\"" + this.cantCol +"\" nodoAncho=\"30\" " +
				"nodoAlto=\"30\" inicioPacman=\""+getPosicionInicioPacman()+"\" inicioFantasmas=\""+getPosicionInicioFantasma()+"\">");
		Integer fila;
		for(fila = 0; fila < this.cantFil; fila++){
			Integer columna;
			for(columna = 0; columna < this.cantCol; columna++){
				Celda celda = this.mapa.get(construirId(fila, columna));
				if (celda == null){
					System.out.println("\t<nodo id=\""+construirId(fila, columna)+"\" fila=\""+formatearNro(fila)+"\" columna=\""+formatearNro(columna)+"\" " +
							"contiene=\"\" " +
							"izquierda=\"\" derecha=\"\" arriba=\"\" abajo=\"\"/>");
				} else {
					String content = celda.getContent();
					String izquierda = celda.getSiguienteCelda(Constantes.IZQUIERDA);
					String derecha = celda.getSiguienteCelda(Constantes.DERECHA);
					String arriba = celda.getSiguienteCelda(Constantes.ARRIBA);
					String abajo = celda.getSiguienteCelda(Constantes.ABAJO);
					System.out.println("\t<nodo id=\""+celda.getId()+"\" fila=\""+formatearNro(fila)+"\" columna=\""+formatearNro(columna)+"\" contiene=\""+content+"\" " +
							"izquierda=\"" +izquierda+ "\" derecha=\""+derecha+"\" arriba=\""+arriba+"\" abajo=\""+abajo+"\"/>");
				}
			}
		}
		System.out.println("</laberinto>");
	}

	private String construirId(Integer fila, Integer columna) {
		return String.format("%02d%02d", fila, columna);
	}

	private String formatearNro(Integer nro){
		return String.format("%02d", nro);
	}

	public Boolean hayMasBolitas(){
		Collection<Celda> values = this.mapa.values();
		for (Celda celda : values) {
			if (!celda.getBolita().fueComida()){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public List<Fantasma> getFantasmas() {
		return fantasmas;
	}

	public Integer getCantFil() {
		return cantFil;
	}

	public void setCantFil(Integer cantFil) {
		this.cantFil = cantFil;
	}

	public Integer getCantCol() {
		return cantCol;
	}

	public void setCantCol(Integer cantCol) {
		this.cantCol = cantCol;
	}

	public Integer getNodoAncho() {
		return nodoAncho;
	}

	public void setNodoAncho(Integer nodoAncho) {
		this.nodoAncho = nodoAncho;
	}

	public Integer getNodoAlto() {
		return nodoAlto;
	}

	public void setNodoAlto(Integer nodoAlto) {
		this.nodoAlto = nodoAlto;
	}

	public void setInicioPacman(String inicioPacman) {
		this.setPosicionInicioPacman(inicioPacman);
	}

	public void setInicioFantasma(String inicioFantasma) {
		this.setPosicionInicioFantasma(inicioFantasma);
	}

	public void parsearNodo(String line) {
		String id, izq, der, arr, aba, contiene;
		id = izq = arr = der = aba = null; 
		contiene = "";
		Pattern idPat = Pattern.compile("id=\"(\\d+)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = idPat.matcher(line);
		if (m.find()){
			id = m.group(1);
		}
		Pattern contienePat = Pattern.compile("contiene=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		m = contienePat.matcher(line);
		if (m.find()){
			contiene = m.group(1);
		}
		Pattern izquierdaPat = Pattern.compile("izquierda=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		m = izquierdaPat.matcher(line);
		if (m.find()){
			izq = m.group(1);
		}
		Pattern derechaPat = Pattern.compile("derecha=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		m = derechaPat.matcher(line);
		if (m.find()){
			der = m.group(1);
		}
		Pattern arribaPat = Pattern.compile("arriba=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		m = arribaPat.matcher(line);
		if (m.find()){
			arr = m.group(1);
		}
		Pattern abajoPat = Pattern.compile("abajo=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		m = abajoPat.matcher(line);
		if (m.find()){
			aba = m.group(1);
		}
		if (!(aba == null && arr == null && izq == null && der == null)){
			Celda celda = new Celda(contiene, id);
			celda.setCeldaAbajo(aba);
			celda.setCeldaArriba(arr);
			celda.setCeldaDerecha(der);
			celda.setCeldaIzquierda(izq);
			this.mapa.put(id, celda);
		} 
	}

	public String getPosicionInicioFantasma() {
		return posicionInicioFantasma;
	}

	public void setPosicionInicioFantasma(String posicionInicioFantasma) {
		this.posicionInicioFantasma = posicionInicioFantasma;
	}

	public String getPosicionInicioPacman() {
		return posicionInicioPacman;
	}

	public void setPosicionInicioPacman(String posicionInicioPacman) {
		this.posicionInicioPacman = posicionInicioPacman;
	}

	public Celda getCelda(String id) {
		return this.mapa.get(id);
	}

	public void imprimirActoresAXml() {
		System.out.println("<juego posicionPacman=\"" + Pacman.getInstance().getCeldaActual().getId() +"\" fila=\""+Pacman.getInstance().getCeldaActual().getFila() + "\" " +
				" columna=\""+Pacman.getInstance().getCeldaActual().getColumna()+"\" sentido=\""+Pacman.getInstance().getSentido()+"\" puntaje=\"122\" finJuego=\"false\">");
		for (Fantasma f : this.fantasmas) {
			System.out.println("<fantasma id=\""+f.getId()+"\" nodo=\""+f.getCeldaActual().getId()+"\" " + 
					"fila=\""+f.getCeldaActual().getFila()+"\" columna=\""+f.getCeldaActual().getColumna()+"\" sentido=\""+f.getSentido()+"\" personalidad=\"" + f.getPersonalidad() + "\" estado=\""+f.getEstado().getNombre()+"\"/>");
		}
		System.out.println("</juego>");
	}

	public void inicializarActores() {
		Pacman.getInstance().setCeldaActual(this.getCelda(getPosicionInicioPacman()));
		Celda celda = this.getCelda(getPosicionInicioFantasma());
		Integer i = 0;
		this.getFantasmas().add(new Fantasma(Constantes.COLOR_AMARILLO, celda, ComportamientoZonzo.getInstance(), i.toString()));
		i++;
		this.getFantasmas().add(new Fantasma(Constantes.COLOR_ROJO, celda, ComportamientoZonzo.getInstance(), i.toString()));
		i++;
		this.getFantasmas().add(new Fantasma(Constantes.COLOR_VERDE, celda, ComportamientoZonzo.getInstance(), i.toString()));
		Pacman.getInstance().getCeldaActual().visitarPorPacman();
	}

}
