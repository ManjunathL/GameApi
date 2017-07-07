package com.mygubbi.game.Reports;

import com.mygubbi.game.proposal.model.Module;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.json.JSONArray;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 27-06-2017.
 */
public class ModuleCountReport {


    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://ogdemodb.cyn8wqrk6sdc.ap-southeast-1.rds.amazonaws.com/prod_check","admin", "OG$#gubi32");

            ResultSet versions = con.createStatement().executeQuery("select proposalId,version,date from version_master where date >= \"2017-04-01 00:00:41\" group by proposalId,version order by proposalId ");


            while (versions.next()) {
                int version_proposalId = versions.getInt("proposalId");
                String version = versions.getString("version");
                Date version_CreateDate = versions.getDate("date");


            ResultSet products = con.createStatement().executeQuery("select * from proposal_product where proposalId = " + version_proposalId + " and fromVersion = " + "'" + version + "'");


                while (products.next()) {


                    String array = products.getString("modules");
                    int productId = products.getInt("id");
                    String productTitle = products.getString("title");
                    String productCategory = products.getString("productCategoryCode");
                    productTitle = productTitle.replaceAll("[^a-zA-Z]+", " ");
                    double productTotal = products.getDouble("amount");
                    String proposalTitle = null;
                    String proposalQuoteNo = null;
                    String crmId = null;
                    System.out.println("Inserting for product :" + version_proposalId + " : " + version + " :" + productTitle);


                    ResultSet proposal = con.createStatement().executeQuery("SELECT * FROM proposal where id = " + version_proposalId);

                    while (proposal.next()) {

                        proposalTitle = proposal.getString("title");

                        proposalTitle = proposalTitle.replaceAll("[^a-zA-Z]+", " ");

                        proposalQuoteNo = proposal.getString("quoteNoNew");
                        crmId = proposal.getString("crmId");
                        crmId = crmId.replaceAll("[^a-zA-Z]+", " ");

                    }

                    if (array == null) continue;

                    JsonArray jsonArray = new JsonArray(array);
                    int stdModuleCount = 0;
                    int nStdModuleCount = 0;
                    int hikeModuleCount = 0;
                    double stdModulePrice = 0;
                    double nStdModulePrice = 0;
                    double hikeModulePrice = 0;

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject module = jsonArray.getJsonObject(i);
                        String moduleCategory = module.getString("moduleCategory");
                        if (module.getDouble("amount") == null) continue;
                        double moduleAmount = module.getDouble("amount");
                        if (moduleCategory == null) continue;
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
                    }
                    /*System.out.println("StdModuleCount :" + stdModuleCount + " | " + "Non std Module Count :" + nStdModuleCount
                            + " | " + "Hike Module Count :" + hikeModuleCount + " | " + "Standard Module Price : " + stdModulePrice
                            + " | " + "N std Module Price :" + nStdModulePrice + " | " + "Hike Module Price :" + hikeModulePrice);*/

                    int insert = con.createStatement().executeUpdate("INSERT INTO module_report_copy_copy (proposalId, productId, proposalTitle, quoteNo, crmId, version," +
                            " productTitle, productCategory, amount, stdModuleCount, stdModulePrice, nStdModuleCount, nStdModulePrice," +
                            " hikeModuleCount, hikeModulePrice) VALUES (" + version_proposalId + "," + productId + "," + "'" + proposalTitle + "'" + "," +
                            "'" + proposalQuoteNo + "'" + "," + "'" + crmId + "'" + "," + "'" + version + "'" + "," + "'" +
                            productTitle + "'" + "," + "'" + productCategory + "'" + "," + productTotal + "," + stdModuleCount + "," + stdModulePrice + "," +
                            nStdModuleCount + "," + nStdModulePrice + "," + hikeModuleCount + "," + hikeModulePrice + ")");

                    if (insert == 1) {
                        System.out.println("Inserted record for Proposal :" + version_proposalId);
                    }
                }

                }
        }
        catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
        }
    }
}
