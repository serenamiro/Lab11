package it.polito.tdp.rivers.db;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RiversDAO {

	public List<River> getAllRivers() {
		
		final String sql = "SELECT id, name FROM river";
		List<River> rivers = new LinkedList<River>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				River r = new River(res.getInt("id"), res.getString("name"));
				rivers.add(r);
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			e.printStackTrace();
			System.out.println("Errore connessione al database.");
			throw new RuntimeException("SQL Error");
		}
		return rivers;
	}

	public List<Flow> getDatiCompletiRiver(River river) {
		String sql = "SELECT * " + 
				"FROM river AS r, flow AS f " + 
				"WHERE r.id = f.river AND r.id=?";
		
		List<Flow> flows = new LinkedList<Flow>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, river.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Flow f = new Flow(res.getDate("day").toLocalDate(), res.getDouble("flow"), river);
				flows.add(f);
			}
			conn.close();
			return flows;
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public List<Flow> getFlows(River river){
		final String sql = "SELECT id, day, flow FROM flow WHERE river=?";
		List<Flow> flows = new LinkedList<Flow>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, river.getId());
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				flows.add(new Flow(res.getDate("day").toLocalDate(), res.getDouble("flow"), river));
			}
			Collections.sort(flows);
			river.setFlows(flows);
			conn.close();
		} catch(SQLException e) {
			throw new RuntimeException("SQL Error");		
		}
		return flows;
	}
	
}
