package it.polito.tdp.rivers.model;

public class Simulatore {
	
	// PARAMETRI DI SIMULAZIONE
	private double k; // frazione
	private double Q;
	private double C;
	private double f_out_min;
	
	
	// OUTPUT DA CALCOLARE
	private int giorniNegativi;
	private double C_med;
	
	// STATO DEL SISTEMA
	
	// CODA DEGLI EVENTI
	
	// INIZIALIZZAZIONE
	public void init(double k, double f_med) {
		this.k = k;
		this.Q = k*f_med*30*3600;
		this.C = Q/2;
		this.f_out_min = 0.8*f_med;
		
	}
	
	// ESECUZIONE
	
}
