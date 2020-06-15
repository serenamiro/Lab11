package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	
	List<River> rivers;
	
	// gli eventi sono i flow
	private PriorityQueue<Flow> queue;
	
	public Model() {
		RiversDAO dao = new RiversDAO();
		rivers = dao.getAllRivers();
		for(River river : rivers) {
			dao.getFlows(river);
		}
	}

	public List<River> getRivers() {
		return rivers;
	}

	public void setRivers(List<River> rivers) {
		this.rivers = rivers;
	}
	
	public LocalDate getStartDate(River river) {
		if(!river.getFlows().isEmpty()) {
			return river.getFlows().get(0).getDay();
		}
		return null;
	}
	
	public LocalDate getEndDate(River river) {
		if(!river.getFlows().isEmpty()) {
			return river.getFlows().get(river.getFlows().size()-1).getDay();
		}
		return null;
	}
	
	public int getNumMeasurements(River river) {
		return river.getFlows().size();
	}
	
	public double getFMed(River river) {
		double avg = 0.0;
		for(Flow f : river.getFlows()) {
			avg += f.getFlow();
		}
		avg /= river.getFlows().size();
		river.setFlowAvg(avg);
		return avg;
	}
	
	public SimulatorResults simulate(River river, double k) {
		// inizializzo la coda
		this.queue = new PriorityQueue<Flow>();
		this.queue.addAll(river.getFlows());
		
		List<Double> capacity = new ArrayList<Double>();
		
		double Q = k*30*convertM3SecToM3Day(river.getFlowAvg());
		double C = Q/2;
		double fOutMin = convertM3SecToM3Day(0.8*river.getFlowAvg());
		int numberOfDays = 0;
		
		System.out.println("Q: "+Q);
		Flow flow;
		
		while((flow=this.queue.poll())!=null) {
			System.out.println("Date: "+flow.getDay());
			double fOut = fOutMin;
			
			if(Math.random()>0.95) {
				fOut = 10*fOutMin;
				System.out.println("10*fOutMin");
			}
			
			C += convertM3SecToM3Day(flow.getFlow());
			
			if(C>Q) {
				// Tracinazione: la quantità in più esce
				C = Q;
			}
			
			if(C < fOut) {
				// Non riesco a garantire la quantità minima: è un giorno critico
				numberOfDays++;
				C = 0;
			} else {
				// Faccio uscire la quantità giornaliera
				C -= fOut;
			}
			capacity.add(C);
		}
		
		double CAvg = 0;
		for(Double d : capacity) {
			CAvg += d;
		}
		CAvg/=capacity.size();
		return new SimulatorResults(CAvg, numberOfDays);
	}

	private double convertM3SecToM3Day(double flowAvg) {
		// funzione per convertire l'unità di misura
		return flowAvg*60*60*24;
	}
	
	private double convertM3DayToM3Sec(double flowAvg) {
		// funzione per convertire l'unità di misura
		return flowAvg/60/60/24;
	}
	
	
	
	
	
}
