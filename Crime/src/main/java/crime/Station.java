/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crime;

/**
 *
 * @author Nicky
 */
public class Station {

    String Name;
    String County;
    Crime crime;
    float X;
    float Y;

    public Station(){
        
    }

    public Crime getCrime() {
        return crime;
    }

    public void setCrime(Crime crime) {
        this.crime = crime;
    }
    // Parameter constructor
    public Station(String name, float x, float y) {
        Name = name;
        X = x;
        Y = y;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        this.County = county;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getX() {
        return X;
    }

    public void setX(float X) {
        this.X = X;
    }

    public double getY() {
        return Y;
    }

    public void setY(float Y) {
        this.Y = Y;
    }
    
    
}
