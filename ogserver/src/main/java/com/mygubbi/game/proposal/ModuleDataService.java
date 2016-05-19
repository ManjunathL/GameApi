package com.mygubbi.game.proposal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.db.QueryDef;
import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.CarcassPanel;
import com.mygubbi.game.proposal.model.ModuleComponent;
import com.mygubbi.game.proposal.model.ShutterPanel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleDataService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(ModuleDataService.class);
	private Map<String, QueryDef> queryMap;
	
	private static ModuleDataService INSTANCE;
	private AtomicInteger cachingCounter = new AtomicInteger(7);

    private Multimap<String, ModuleComponent> moduleComponentsMap;
    private Map<String, CarcassPanel> carcassPanelMap = Collections.EMPTY_MAP;
    private Map<String, ShutterPanel> shutterPanelMap = Collections.EMPTY_MAP;
    private Map<String, String> kdmaxDefaultModuleMap = Collections.EMPTY_MAP;
    private Set<String> kdmaxModulesWithMGMapping = Collections.EMPTY_SET;
    private Map<String, AccHwComponent> accessoriesMap = Collections.EMPTY_MAP;
    private Map<String, AccHwComponent> hardwareMap = Collections.EMPTY_MAP;

	public static ModuleDataService getInstance()
	{
		return INSTANCE;
	}

	private Future<Void> startFuture;

	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		this.startFuture = startFuture;
		this.setupData();
		INSTANCE = this;
	}

	private void setupData()
	{
        this.cacheModuleComponents();
        this.cacheCarcassPanels();
        this.cacheShutterPanels();
        this.cacheKdmaxMapping();
        this.cacheKdmaxDefaultMapping();
        this.cacheAccessories();
        this.cacheHardware();
	}

    private void cacheAccessories()
    {
        this.accessoriesMap = new HashMap<>();
        this.cacheAccHw("A", this.accessoriesMap);
    }

    private void cacheHardware()
    {
        this.hardwareMap = new HashMap<>();
        this.cacheAccHw("H", this.hardwareMap);
    }

    private void cacheModuleComponents()
    {
        this.moduleComponentsMap = ArrayListMultimap.create();
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("module.components.select.all", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Module components table is empty.", false);
                    }
                    else
                    {
                        for (JsonObject record : selectData.rows)
                        {
                            ModuleComponent component = ModuleComponent.fromJson(record);
                            this.moduleComponentsMap.put(component.getModuleCode(), component);
                        }
                        markResult("Module components loaded.", true);
                    }
                });
    }

    private void cacheCarcassPanels()
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("carcass.select.all", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Carcass panels table is empty.", false);
                    }
                    else
                    {
                        this.carcassPanelMap = new HashMap<String, CarcassPanel>(selectData.rows.size());
                        for (JsonObject record : selectData.rows)
                        {
                            CarcassPanel carcass = CarcassPanel.fromJson(record);
                            this.carcassPanelMap.put(carcass.getCode(), carcass);
                        }
                        markResult("Carcass panels loaded.", true);
                    }
                });
    }

    private void cacheShutterPanels()
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("shutter.select.all", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Shutter panels table is empty.", false);
                    }
                    else
                    {
                        this.shutterPanelMap = new HashMap(selectData.rows.size());
                        for (JsonObject record : selectData.rows)
                        {
                            ShutterPanel shutter = ShutterPanel.fromJson(record);
                            this.shutterPanelMap.put(shutter.getCode(), shutter);
                        }
                        markResult("Shutter panels done.", true);
                    }
                });
    }

    private void cacheAccHw(String type, Map<String, AccHwComponent> components)
    {
        String query = type == "A" ? "accessory.select.all" : "hardware.select.all";
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData(query, new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Did not find any accessory or hardware for type " + type, false);
                    }
                    else
                    {
                        for (JsonObject record : selectData.rows)
                        {
                            AccHwComponent component = AccHwComponent.fromJson(record);
                            components.put(component.getCodeAndMakeType(), component);
                        }
                        markResult("Accessories or Hardware loaded for type " + type, true);
                    }
                });
    }

    private void cacheKdmaxMapping()
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("kdmax.mapped.modules", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.kdmaxModulesWithMGMapping = Collections.EMPTY_SET;
                    }
                    else
                    {
                        this.kdmaxModulesWithMGMapping = new HashSet<String>(selectData.rows.size());
                        for (JsonObject record : selectData.rows)
                        {
                            this.kdmaxModulesWithMGMapping.add(record.getString("kdmcode"));
                        }
                    }
                    markResult("KDMax mapping loaded.", true);
                });
    }

    private void cacheKdmaxDefaultMapping()
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("kdmax.default.mapped.modules", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        this.kdmaxDefaultModuleMap = Collections.EMPTY_MAP;
                    }
                    else
                    {
                        this.kdmaxDefaultModuleMap = new HashMap(selectData.rows.size());
                        for (JsonObject record : selectData.rows)
                        {
                            this.kdmaxDefaultModuleMap.put(record.getString("kdmcode"), record.getString("kdmdefcode"));
                        }
                    }
                    markResult("KDMax default mapping loaded.", true);
                });
    }

    private synchronized void markResult(String message, boolean success)
    {
        if (this.startFuture == null) return;
/*        if (!success)
        {
            this.startFuture.fail(message);
            this.startFuture = null;
            LOG.error(message);
            return;
        }
*/ //TODO: Uncomment this block

        int counter = this.cachingCounter.decrementAndGet();
        if (counter == 0)
        {
            this.startFuture.complete();
            this.startFuture = null;
        }
    }

    public Collection<ModuleComponent> getModuleComponents(String mgCode)
    {
        return this.moduleComponentsMap.get(mgCode);
    }

    public CarcassPanel getCarcassPanel(String code)
    {
        return this.carcassPanelMap.get(code);
    }

    public ShutterPanel getShutterPanel(String code)
    {
        return this.shutterPanelMap.get(code);
    }

    public AccHwComponent getAccessory(String code, String makeType)
    {
        return this.accessoriesMap.get(AccHwComponent.getCodeAndMakeType(code, makeType));
    }

    public AccHwComponent getHardware(String code, String makeType)
    {
        return this.hardwareMap.get(AccHwComponent.getCodeAndMakeType(code, makeType));
    }


    public void setMapping(ProductModule module)
    {
        if (this.kdmaxModulesWithMGMapping.contains(module.getKDMCode()))
        {
            module.setMappedFlag(ProductModule.MAPPED_AT_MODULE);
        }
        else if (this.kdmaxDefaultModuleMap.containsKey(module.getKDMCode()))
        {
            module.setMappedFlag(ProductModule.MAPPED_AT_DEFAULT);
            module.setDefaultModule(this.kdmaxDefaultModuleMap.get(module.getKDMCode()));
        }
        else
        {
            module.setMappedFlag(ProductModule.NOT_MAPPED);
        }
    }
}
