package com.mygubbi.game.Reports;

import com.mygubbi.game.proposal.model.Module;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.json.JSONArray;

import java.sql.*;

/**
 * Created by User on 27-06-2017.
 */
public class ModuleCountReport {


    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://ogdemodb.cyn8wqrk6sdc.ap-southeast-1.rds.amazonaws.com/mg_report","admin", "OG$#gubi32");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM proposal_product where id = 27714");


            while (rs.next()) {
                String array = rs.getString("modules");
                int proposalId = rs.getInt("proposalId");
                String version = rs.getString("fromVersion");
                String productTitle = rs.getString("title");
                double productTotal = rs.getDouble("amount");
                String proposalTitle = null;
                String proposalQuoteNo = null;
                String crmId = null;

                ResultSet proposal = stmt.executeQuery("SELECT * FROM proposal where id = " + proposalId);

                if (proposal.next()) {

                    proposalTitle = proposal.getString("title");
                    proposalQuoteNo = proposal.getString("quoteNoNew");
                    crmId = proposal.getString("crmId");
                }


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


                    }
                    /*System.out.println("StdModuleCount :" + stdModuleCount + " | " + "Non std Module Count :" + nStdModuleCount
                            + " | " + "Hike Module Count :" + hikeModuleCount + " | " + "Standard Module Price : " + stdModulePrice
                            + " | " + "N std Module Price :" + nStdModulePrice + " | " + "Hike Module Price :" + hikeModulePrice);*/

                int insert = stmt.executeUpdate("INSERT INTO module_report (proposalId, proposalTitle, quoteNo, crmId, version," +
                        " productTitle, amount, stdModuleCount, stdModulePrice, nStdModuleCount, nStdModulePrice," +
                        " hikeModuleCount, hikeModulePrice) VALUES ("+ proposalId + "," + "'" + proposalTitle + "'" + "," +
                        "'" + proposalQuoteNo + "'" + "," + "'" +crmId + "'" + "," + "'" + version + "'" + "," + "'" +
                        productTitle + "'" + "," + productTotal + "," + stdModuleCount + "," + stdModulePrice + "," +
                        nStdModuleCount + "," + nStdModulePrice + "," + hikeModuleCount + "," + hikeModulePrice + ")");

                    if (insert == 1)
                    {
                        System.out.println("Inserted record for Proposal :" + proposalId);
                    }

                }
        }
        catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
        }
    }
}
