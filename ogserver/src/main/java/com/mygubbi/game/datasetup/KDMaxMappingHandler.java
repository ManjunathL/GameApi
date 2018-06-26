package com.mygubbi.game.datasetup;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.si.excel.ExcelRowHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunil on 05-05-2016.
 */
public class KDMaxMappingHandler implements ExcelRowHandler
{

    private List<String[]> mgMapping = new ArrayList<>();
    private List<String[]> defaultMapping = new ArrayList<>();

    @Override
    public void handle(Object[] data)
    {
        String kdmaxCode = (String) data[0];
        String defaultCode = (String) data[1];
        if (StringUtils.isEmpty(kdmaxCode)) return;
        if (kdmaxCode.equals("MG-")) return;

        for (int i= 0; i< 6; i++)
        {
            String mgCode = (String) data[i+2];
            if (StringUtils.isNonEmpty(mgCode))
            {
                this.mgMapping.add(new String[]{kdmaxCode, mgCode});
            }
        }

        if (StringUtils.isNonEmpty(defaultCode))
        {
            this.defaultMapping.add(new String[]{kdmaxCode, defaultCode});
        }

        //if (StringUtils.isNonEmpty(defaultCode)) this.makeDefaultMapping(kdmaxCode, defaultCode);
        //if (!blankMGCode) this.makeMGMapping(kdmaxCode, mgCodes);
    }

    @Override
    public void done()
    {
        this.printCodes(this.mgMapping, "Printing MG Mapping. ------------ ");
        this.printCodes(this.defaultMapping, "Printing Default Mapping. ------------ ");
    }

    private void printCodes(List<String[]> codesMap, String title)
    {
        System.out.println(title);
        for (String[] codes : codesMap)
        {
            System.out.println(codes[0] + "," + codes[1]);
        }
    }

    private void makeDefaultMapping(String kdmaxCode, String defaultCode)
    {
        JsonObject kdmaxData = new JsonObject().put("kdmcode", kdmaxCode).put("kdmdefcode", defaultCode);
        Integer deleteId = LocalCache.getInstance().store(new QueryData("kdm.def.delete", kdmaxData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, deleteId,
                (AsyncResult<Message<Integer>> deleteResult) -> {
                    Integer insertId = LocalCache.getInstance().store(new QueryData("kdm.def.insert", kdmaxData));
                    VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, insertId,
                            (AsyncResult<Message<Integer>> res) -> {
                                QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                                if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                                {
                                    System.out.println("KDM default code not created. " + kdmaxData.encodePrettily());
                                }
                            });
                });
    }

    private void makeMGMapping(String kdmaxCode, String[] mgCodes)
    {
        JsonObject kdmaxData = new JsonObject().put("kdmcode", kdmaxCode);
        Integer deleteId = LocalCache.getInstance().store(new QueryData("kdm.map.delete", kdmaxData));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, deleteId,
                (AsyncResult<Message<Integer>> deleteResult) -> {

                    for (String mgCode : mgCodes)
                    {
                        JsonObject mappingData = new JsonObject().put("kdmcode", kdmaxCode).put("mgcode", mgCode);
                        Integer insertId = LocalCache.getInstance().store(new QueryData("kdm.map.insert", mappingData));
                        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, insertId,
                                (AsyncResult<Message<Integer>> res) -> {
                                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
                                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                                    {
                                        System.out.println("KDM mapping data not created. " + mappingData.encodePrettily());
                                    }
                                });
                    }
                });
    }
}