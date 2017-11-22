package com.mygubbi.game.proposal;


import com.mygubbi.common.DateUtil;
import com.mygubbi.game.proposal.model.Module;
import com.mygubbi.game.proposal.price.ModulePriceHolder;
import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by User on 21-11-2017.
 */
public class PriceHikeModuleTest {

    private static final double SQMMFT = 1000;
    private static final double HEIGHT = 71146;
    private static final double WIDTH = 10;
    private static final double DEPTH = 1;

    public static double modulePrice(double height, double width, double depth)
    {
        double carcassRate = 1;
        double finalRate = height * width * depth * SQMMFT * carcassRate;
        return finalRate;
    }


    static JsonObject jsonObject = new JsonObject()
            .put(  "seq" , 0).put(  "moduleSequence" , 0).put(  "unitType" , "Base Unit").put(  "extCode" , "null").put(  "extText" , "null").put(  "mgCode" , "MG-NS-H-001").put(  "carcass" , "default (MDF)").put(  "wallcasscode" , "null").put(  "carcassCode" , "MDF").put(  "fixedCarcassCode" , "null").put(  "finishType" , "default (Laminate)").put(  "finishTypeCode" , "LAMINATE").put(  "finish" , "default (MDF - Matt Woodgrain(S1))").put(  "finishCode" , "D32").put(  "colorCode" , "Auburn Oak 14142 SF").put(  "colorName" , "null").put(  "colorImagePath" , "null").put(  "amount" , 0.0).put(  "remarks" , "").put(  "importStatus" , "m").put(  "description" , "").put(  "dimension" , "null").put(  "imagePath" , "image.jpg").put(  "exposedRight" , false).put(  "exposedLeft" , false).put(  "exposedTop" , false).put(  "exposedBottom" , false).put(  "exposedBack" , false).put(  "exposedOpen" , false).put(  "area" , 0.0).put(  "amountWOAccessories" , 0.0).put(  "width" , WIDTH).put(  "depth" , DEPTH).put(  "height" , HEIGHT).put(  "moduleCategory" , "E - Panel").put(  "moduleType" , "hike").put(  "productCategory" , "Wardrobe").put(  "moduleSource" , "button").put(  "expSides" , "null").put(  "expBottom" , "null").put(  "accessoryPackDefault" , "No").put(  "woodworkCost" , 0.0).put(  "hardwareCost" , 0.0).put(  "shutterCost" , 0.0).put(  "carcassCost" , 0.0).put(  "accessoryCost" , 0.0).put(  "labourCost" , 0.0).put(  "accessoryflag" , "null").put(  "shutterDesign" , "null").put(  "handleType" , "Tunes/CH 713").put(  "handleFinish" , "Chrome Plated (CP)").put(  "handleThickness" , "160").put(  "knobType" , "Tunes/KN 05").put(  "knobFinish" , "Chrome Plated (CP)").put(  "knobThickness" , "null").put(  "handlePresent" , "No").put(  "knobPresent" , "No").put(  "handleCode" , "HANDLE537").put(  "knobCode" , "KNOB34").put(  "customText" , "null").put(  "customCheck" , "null").put(  "handleQuantity" , 0).put(  "knobQuantity" , 0).put(  "newModuleFlag" , "null").put(  "glassType" , "Plain").put(  "hingeType" , "Soft Close").put(  "hingePresent" , "null").put(  "hingeCode" , "null").put(  "hingeQuantity" , 0).put(  "handleTypeSelection" , "null").put(  "handleChangedFlag" , false).put(  "knobChangedFlag" , false).put(  "golaProfileFlag" , "null").put(  "handleOverrideFlag" , "null").put(  "finishSetId" , "null");

    public static ProductModule module = new ProductModule(jsonObject);
//    public static Date currentDate = DateUtil.convertDate(String.valueOf(System.currentTimeMillis()));

    static Timestamp stamp = new Timestamp(System.currentTimeMillis());
    static Date date = new Date(stamp.getTime());
    static String city = "Bangalore";
    static ProductLineItem product = new ProductLineItem();




//    public static Module module = new Module(jsonObject);


    public static void main(String args[])
    {
        double price = modulePrice(HEIGHT,WIDTH,DEPTH);
        ModulePriceHolder modulePriceHolder = new ModulePriceHolder(module,city,date,product);
        modulePriceHolder.prepare();
        modulePriceHolder.calculateTotalCost();
        System.out.println("The Module Price is : " + price );
        System.out.println("Module : " + module.getModuleCategory());
        System.out.println("DATE : " + date);
        System.out.println("total Module Cost :" + modulePriceHolder.getTotalCost());

}
}
