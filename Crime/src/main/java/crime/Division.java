/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crime;

import java.util.ArrayList;

/**
 *
 * @author Nicky
 */
public class Division {
    
    String Name;
    ArrayList<Station> stations;
    
    public Division(){
        this.stations = new ArrayList<>();
    }

    public String getName() {
        return Name;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
    
    public void addStation(Station station){
        this.stations.add(station);
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    
    
}
