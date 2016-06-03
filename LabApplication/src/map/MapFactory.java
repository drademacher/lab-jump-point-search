package map;

import exception.InvalidCoordinateException;
import exception.MapInitialisationException;
import util.RandomUtil;

import java.io.*;

/**
 * Created by paloka on 01.06.16.
 */
class MapFactory {

    Map createEmptyMap(int xDim, int yDim) throws MapInitialisationException {
        return new Map(xDim,yDim,true);
    }

    Map createRandomMap(int xDim, int yDim, double pPassable) throws MapInitialisationException {
        Map map = new Map(xDim,yDim,false);
        for (int x = 0; x < xDim; x++) {
            for (int y = 0; y < yDim; y++) {
                //Todo: implement random singelton to obmit global
                if (RandomUtil.getRandomDouble() < pPassable) {
                    try {
                        map.switchPassable(x, y);
                    } catch (InvalidCoordinateException e) {
                        e.printStackTrace();
                        //Todo: MapFactory.createRandomMap - InvalidCoordinateException
                    }
                }
            }
        }
        return map;
    }

    Map loadMap(File file) throws MapInitialisationException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();  //Skip type
            int yDim    = Integer.valueOf(br.readLine().substring(7));  //Read height
            int xDim    = Integer.valueOf(br.readLine().substring(6));  //Read width
            Map map     = new Map(xDim,yDim,false); //init Map without passable fields
            br.readLine();  //Skip map
            String currentLine;
            for(int y=0;(currentLine=br.readLine())!=null;y++){ //Read in MapRow
                for(int x=0;x<currentLine.length();x++){
                    if(currentLine.charAt(x)=='.' || currentLine.charAt(x)=='G' || currentLine.charAt(x)=='S'){
                        map.switchPassable(x,y);    //Mark passable fields
                    }
                }
            }
            return map;
        } catch(Exception e){
            throw new MapInitialisationException();
        }
    }
}
