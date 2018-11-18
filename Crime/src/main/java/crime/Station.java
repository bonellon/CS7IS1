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
    String X;
    double Y;

    public Station(){
        
    }
    // Parameter constructor
    public Station(String name, String x, double y) {
        Name = name;
        X = x;
        Y = y;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getX() {
        return X;
    }

    public void setX(String X) {
        this.X = X;
    }

    public double getY() {
        return Y;
    }

    public void setY(double Y) {
        this.Y = Y;
    }
    
    
}
