package it.polito.tdp.rivers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	
	public Map<Integer, River> idMap;
	private RiversDAO dao;
	
	public Model() {
		idMap = new HashMap<Integer, River>();
		dao = new RiversDAO();
		this.dao.getAllRivers(idMap);
	}
	
	public List<Flow> getDatiCompleti(River river) {
		List<Flow> flows = dao.getDatiCompletiRiver(river);
		this.idMap.get(river.getId()).setFlows(flows);
		double media = 0.0;
		for (Flow f : flows) {
			media += f.getFlow();
		}
		double mediaNew = media/(double)flows.size();
		this.idMap.get(river.getId()).setFlowAvg(mediaNew);
		// usa Math.round(doubleValue) per troncare
		return flows;
	}
	
	
}
