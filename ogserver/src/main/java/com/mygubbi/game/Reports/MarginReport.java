package com.mygubbi.game.Reports;

import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.Module;
import com.mygubbi.game.proposal.price.ModulePriceHolder;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.sql.*;

/**
 * Created by User on 30-06-2017.
 */
public class MarginReport {

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://ogdemodb.cyn8wqrk6sdc.ap-southeast-1.rds.amazonaws.com/mg_report","admin", "OG$#gubi32");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM proposal where id = 4743");


            while (rs.next()) {
                int proposalId = rs.getInt("id");
                String version = rs.getString("version");
                String proposalTitle = rs.getString("title");
                String quoteNo = rs.getString("quoteNoNew");
                String crmId = rs.getString("crmId");
                Date priceDate = rs.getDate("priceDate");
                String city = rs.getString("Pcity");

                System.out.println("Inside 1st : " + proposalId + proposalTitle );


                String productTitle;
                String productVersion;

                ResultSet product = stmt.executeQuery("SELECT * FROM proposal_product where proposalId = " + proposalId);

                System.out.println("Product Module :" +product.toString());


                if (product.next()) {

                    ProductLineItem productLineItem = (ProductLineItem) product;

                    System.out.println("Product Module :" + productLineItem.encodePrettily());

                productTitle = product.getString("title");
                productVersion = product.getString("fromVersion");
                String modules = product.getString("modules");

                JsonArray modules_array = new JsonArray(modules);
                for (int i =0; i < modules_array.size(); i++)
                {
                    JsonObject module = modules_array.getJsonObject(i);
                    ProductModule module1 = new ProductModule(module);
                    ModulePriceHolder priceHolder = new ModulePriceHolder(module1,
                            city, priceDate,productLineItem);
                    priceHolder.prepare();
                    priceHolder.calculateTotalCost();


                }




            }


                int stdModuleCount = 0;
                int nStdModuleCount = 0;
                int hikeModuleCount = 0;
                double stdModulePrice = 0;
                double nStdModulePrice = 0;
                double hikeModulePrice = 0;

                /*for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject module = jsonArray.getJsonObject(i);
                    String moduleCategory = module.getString("moduleCategory");
                    double moduleAmount = module.getDouble("amount");
                    if (moduleCategory.startsWith("S")) {
                        stdModuleCount = stdModuleCount + 1;
                        stdModulePrice = stdModulePrice + moduleAmount;
                    } else if (moduleCategory.startsWith("N")) {
                        nStdModuleCount = nStdModuleCount + 1;
                        nStdModulePrice = nStdModulePrice + moduleAmount;
                    } else if (moduleCategory.startsWith("H")) {
                        hikeModuleCount = hikeModuleCount + 1;
                        hikeModulePrice = hikeModulePrice + moduleAmount;
                    }


                }*/
                    /*System.out.println("StdModuleCount :" + stdModuleCount + " | " + "Non std Module Count :" + nStdModuleCount
                            + " | " + "Hike Module Count :" + hikeModuleCount + " | " + "Standard Module Price : " + stdModulePrice
                            + " | " + "N std Module Price :" + nStdModulePrice + " | " + "Hike Module Price :" + hikeModulePrice);*/

                /*int insert = stmt.executeUpdate("INSERT INTO module_report (proposalId, proposalTitle, quoteNo, crmId, version," +
                        " productTitle, amount, stdModuleCount, stdModulePrice, nStdModuleCount, nStdModulePrice," +
                        " hikeModuleCount, hikeModulePrice) VALUES ("+ proposalId + "," + "'" + proposalTitle + "'" + "," +
                        "'" + proposalQuoteNo + "'" + "," + "'" +crmId + "'" + "," + "'" + version + "'" + "," + "'" +
                        productTitle + "'" + "," + productTotal + "," + stdModuleCount + "," + stdModulePrice + "," +
                        nStdModuleCount + "," + nStdModulePrice + "," + hikeModuleCount + "," + hikeModulePrice + ")");*/
/*

                if (insert == 1)
                {
                    System.out.println("Inserted record for Proposal :" + proposalId);
                }
*/

            }
        }
        catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
        }
    }

}
