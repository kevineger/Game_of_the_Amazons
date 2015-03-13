/**
 * Created by Kevin on 2015-03-12.
 */
public class LocationData {
    int colPos;
    int rowPos;

    public LocationData(int y, int x){
        rowPos = y;
        colPos = x;

    }
    public String toString(){
        return "\ncol: "+colPos+", row: "+rowPos;
    }
}
