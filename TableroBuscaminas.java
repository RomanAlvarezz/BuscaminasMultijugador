package Buscamina;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class TableroBuscaminas {
	Casilla[][] casillas;
	
	int numFilas;
	int numColumnas;
	int numMinas;
	int numCasillasAbiertas;
	boolean partidaGanada;
	
	
	public TableroBuscaminas(int numFilas, int numColumnas, int numMinas) {
		super();
		this.numFilas = numFilas;
		this.numColumnas = numColumnas;
		this.numMinas = numMinas;
		inicializarCasillas(); 
	}
	
	public void inicializarCasillas() {
		casillas = new Casilla[this.numFilas][this.numColumnas];
		
		for(int i = 0; i < this.numFilas; i++) {
			for(int j = 0; j < this.numColumnas; j++) {
				casillas[i][j] = new Casilla(i,j);			}
		}
		generarMinas();
	}
	
	private void generarMinas() {
		int minasGeneradas = 0;
		while(minasGeneradas != numMinas) {
			int posTmpFila  = (int) (Math.random() * this.numFilas);
			int posTmpColumna  = (int) (Math.random() * this.numColumnas);
			if(!casillas[posTmpFila][posTmpColumna].isMina()) {
				casillas[posTmpFila][posTmpColumna].setMina(true);
				minasGeneradas++;
			}
		}
		actualizarNumeroMinasAlrededor();
	}
	
	public void imprimirTablero() {
		for(int i = 0; i < this.numFilas; i++) {
			for(int j = 0; j < this.numColumnas; j++) {
				if(casillas[i][j].isMina()) {
					System.out.print("*");
				} else {
					System.out.print("0");
				}
			}
			System.out.println("");
		}
	}
	
	private void imprimirPistas() { //imprime la cantidad de minas que tiene alrededor cada mina
		for(int i = 0; i < this.numFilas; i++) {
			for(int j = 0; j < this.numColumnas; j++) {
					System.out.print(casillas[i][j].getNumMinasAlrededor());
			}
			System.out.println("");
		}
	}
	
	private void actualizarNumeroMinasAlrededor() {
		for(int i = 0; i < this.numFilas; i++) {
			for(int j = 0; j < this.numColumnas; j++) {
				if (casillas[i][j].isMina()){
					List<Casilla> casillasAlrededor = obtenerCasillasAlrededor(i,j);
					casillasAlrededor.forEach((c)->c.incrementarNumeroMinasAlrededor());
				}	
			}
		}
	}
	
	public List<Casilla> obtenerCasillasAlrededor(int posFila, int posColumna){
		List<Casilla> listaCasillas = new LinkedList<>();
		for (int i = 0; i < 8; i++) {
			int tmpPosFila = posFila;
			int tmpPosColumna = posColumna;
			switch(i){
				case 0: tmpPosFila--; break; //Arriba
				case 1: tmpPosFila--; tmpPosColumna++; break; //Arriba Derecha
				case 2: tmpPosColumna++; break; //Derecha
				case 3: tmpPosFila++; tmpPosColumna++; break; //Derecha Abajo
				case 4: tmpPosFila++; break; //Abajo
				case 5: tmpPosFila++; tmpPosColumna--; break; //Abajo Izquierda
				case 6: tmpPosColumna--; break; //Izquierda
				case 7: tmpPosFila--; tmpPosColumna--; break; //Izqauierda Arriba
			}
			
			if (tmpPosFila >= 0 && tmpPosFila < this.numFilas
				&& tmpPosColumna >= 0 && tmpPosColumna < this.numColumnas) {
					listaCasillas.add(this.casillas[tmpPosFila][tmpPosColumna]);
			    }
			}
		return listaCasillas;
	}
	
	public boolean casillaEsMina(int posFila, int posColumna) {
		return casillas[posFila][posColumna].isMina();
	}
	
	public List<Casilla> obtenerCasillasConMinas() {
	    List<Casilla> casillasConMinas = new LinkedList<>();
	    
	    for (int i = 0; i < numFilas; i++) {
	        for (int j = 0; j < numColumnas; j++) {
	            if (casillas[i][j].isMina()) {
	                casillasConMinas.add(casillas[i][j]);
	            }
	        }
	    }
	    return casillasConMinas;	
	}
	
	public int getCantidadMinasAlrededor(int posFila, int posColumna) {
		return casillas[posFila][posColumna].getNumMinasAlrededor();
	}
	
	public List<Casilla> obtenerCasillasAbiertas(int posFila, int posColumna) {
	    List<Casilla> casillasAbiertas = new LinkedList<>();
	    abrirCasillasRecursivamente(posFila, posColumna, casillasAbiertas);
	    return casillasAbiertas;
	}

	private void abrirCasillasRecursivamente(int posFila, int posColumna, List<Casilla> casillasAbiertas) {
	    //if (posFila < 0 || posFila >= numFilas || posColumna < 0 || posColumna >= numColumnas) {
		//    return;
		//}
	    
	    Casilla casillaActual = casillas[posFila][posColumna];

	    // Si ya está abierta o es una mina, no la procesamos.
	    if (casillaActual.isAbierta() || casillaActual.isMina()) {
	        return;
	    }

	    // Marcamos la casilla como abierta y la añadimos a la lista.
	    casillaActual.setAbierta(true);
	    numCasillasAbiertas++;
	    casillasAbiertas.add(casillaActual);

	    // Si tiene 0 minas alrededor, abrimos sus casillas vecinas.
	    if (casillaActual.getNumMinasAlrededor() == 0) {
	        List<Casilla> vecinos = obtenerCasillasAlrededor(posFila, posColumna);
	        for (Casilla vecino : vecinos) {
	            abrirCasillasRecursivamente(vecino.getPosFila(), vecino.getPosColumna(), casillasAbiertas);
	        }
	    }
	}

	boolean partidaGanada() {
		return numCasillasAbiertas >= (numFilas*numFilas) - numMinas;
	}
	
	public static void main(String[] args) {
		TableroGUI tb = new TableroGUI();
	}

	
}
