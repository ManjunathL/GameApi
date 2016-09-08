package com.mygubbi.game.proposal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.ICostComponent;
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

	private static ModuleDataService INSTANCE;
	private AtomicInteger cachingCounter = new AtomicInteger(7);

    private Multimap<String, ModuleComponent> moduleComponentsMap;
    private Multimap<String, AccessoryPackComponent> accessoryPackComponentsMap;
    private Multimap<String, AccessoryPack> moduleAccessoryPacksMap;
    //private Multimap<String, AccHwComponent> accessoryAddonsMap;
    private Multimap<String, AccHwComponent> accessoryPackAddonsMap;
    private Map<String, Module> moduleMap = Collections.EMPTY_MAP;
    private Map<String, CarcassPanel> carcassPanelMap = Collections.EMPTY_MAP;
    private Map<String, ShutterPanel> shutterPanelMap = Collections.EMPTY_MAP;
    private Map<String, AccHwComponent> accessoriesMap = Collections.EMPTY_MAP;
    private Map<String, AccHwComponent> hardwareMap = Collections.EMPTY_MAP;
    private Map<String, ShutterFinish> finishCodeMap = Collections.EMPTY_MAP;
    private Map<String, AccessoryPack> accessoryPackMap = Collections.EMPTY_MAP;

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
        this.cacheModules();
        this.cacheModuleComponents();
        this.cacheCarcassPanels();
        this.cacheShutterPanels();
        this.cacheAccessories();
        this.cacheHardware();
        this.cacheFinishCostCodes();
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
        String query = ("A".equals(type) ? "accessory.select.all" : "hardware.select.all");
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
                            components.put(component.getCode(), component);
                        }
                        if ("A".equals(type))
                        {
                            this.cacheAccessoryPackMaster();
                        }
                        else
                        {
                            markResult("Accessories or Hardware loaded for type " + type, true);
                        }
                    }
                });
    }

    private void cacheModules()
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("module.select.all", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Modules master table is empty.", false);
                    }
                    else
                    {
                        this.moduleMap = new HashMap(selectData.rows.size());
                        for (JsonObject record : selectData.rows)
                        {
                            Module module = new Module(record);
                            this.moduleMap.put(module.getCode(), module);
                        }
                        markResult("Module master is loaded.", true);
                    }
                });
    }

    private void cacheFinishCostCodes()
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY,
                LocalCache.getInstance().store(new QueryData("finish.master.all", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Finish master table is empty.", false);
                    }
                    else
                    {
                        this.finishCodeMap = new HashMap(selectData.rows.size());
                        for (JsonObject record : selectData.rows)
                        {
                            ShutterFinish finish = new ShutterFinish(record);
                            this.finishCodeMap.put(finish.getFinishCode(), finish);
                        }
                        markResult("Finish master is loaded.", true);
                    }
                });
    }

    private void cacheAccessoryPackMaster()
    {
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY,
                LocalCache.getInstance().store(new QueryData("acc_pack_master.select", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Accessory pack master table is empty.", false);
                    }
                    else
                    {
                        this.accessoryPackMap = new HashMap(selectData.rows.size());
                        for (JsonObject record : selectData.rows)
                        {
                            AccessoryPack pack = new AccessoryPack(record);
                            this.accessoryPackMap.put(pack.getCode(), pack);
                        }
                        this.cacheAccessoryPackComponents();
                    }
                });
    }

    private void cacheAccessoryPackComponents()
    {
        this.accessoryPackComponentsMap = ArrayListMultimap.create();
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY,
                LocalCache.getInstance().store(new QueryData("acc_pack_components.select", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Accessory pack components table is empty.", false);
                    }
                    else
                    {
                        for (JsonObject record : selectData.rows)
                        {
                            AccessoryPackComponent component = new AccessoryPackComponent(record);
                            this.accessoryPackComponentsMap.put(component.getAccessoryPackCode(), component);
                            AccessoryPack accessoryPack = this.accessoryPackMap.get(component.getAccessoryPackCode());
                            if (accessoryPack != null) accessoryPack.addAccessory(this.getAccessory(component.getComponentCode()));
                        }
                        this.cacheModuleAccessoryPacks();
                    }
                });
    }

    private void cacheModuleAccessoryPacks()
    {
        this.moduleAccessoryPacksMap = ArrayListMultimap.create();
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY,
                LocalCache.getInstance().store(new QueryData("module_acc_pack.select", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Module Accessory packs table is empty.", false);
                    }
                    else
                    {
                        for (JsonObject record : selectData.rows)
                        {
                            String packCode = record.getString("apcode");
                            AccessoryPack pack = this.accessoryPackMap.get(packCode);
                            if (pack == null)
                            {
                                markResult("Accessory pack not setup for " + packCode, false);
                                break;
                            }
                            this.moduleAccessoryPacksMap.put(record.getString("mgcode"), pack);
                        }
                        this.cacheAccessoryAddons();
                    }
                });
    }

    private void cacheAccessoryAddons()
    {
        this.accessoryPackAddonsMap = ArrayListMultimap.create();
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY,
                LocalCache.getInstance().store(new QueryData("acc_addon_map.select", new JsonObject())),
                (AsyncResult<Message<Integer>> dataResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        markResult("Accessory pack addons table is empty.", false);
                    }
                    else
                    {
                        for (JsonObject record : selectData.rows)
                        {
                            String addonCode = record.getString("addoncode");
                            AccHwComponent accessory = this.accessoriesMap.get(addonCode);
                            if (accessory == null)
                            {
                                markResult("Accessory not setup for " + addonCode, false);
                                break;
                            }
                            this.accessoryPackAddonsMap.put(record.getString("apcode"), accessory);
                        }
//                        this.cacheAccessoryPackAddons();
                        markResult("Accessory Pack addons are loaded.", true);
                    }
                });
    }

/*
    private void cacheAccessoryPackAddons()
    {
        this.accessoryPackAddonsMap = HashMultimap.create();
        for (AccessoryPack pack : this.accessoryPackMap.values())
        {
            for (AccessoryPackComponent component : this.getAccessoryPackComponents(pack.getCode()))
            {
                if (!component.isAccessory()) continue;
                for (AccHwComponent accessory : this.getAccessoryAddons(component.getComponentCode()))
                {
                    this.accessoryPackAddonsMap.put(pack.getCode(), accessory);
                }
            }
        }
        markResult("Accessory Pack addons are loaded.", true);
    }
*/

    private synchronized void markResult(String message, boolean success)
    {
        LOG.info(message);

        if (this.startFuture == null) return;
        if (!success)
        {
            this.startFuture.fail(message);
            this.startFuture = null;
            LOG.error(message);
            return;
        }

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


/*
    public Collection<AccHwComponent> getAccessoryAddons(String accessoryCode)
    {
        return this.accessoryAddonsMap.get(accessoryCode);
    }
*/

    public Collection<AccHwComponent> getAccessoryPackAddons(String accessoryPackCode)
    {
        return this.accessoryPackAddonsMap.get(accessoryPackCode);
    }

    public Collection<AccessoryPack> getAccessoryPacksForModule(String moduleCode)
    {
        return this.moduleAccessoryPacksMap.get(moduleCode);
    }

    public Collection<AccessoryPackComponent> getAccessoryPackComponents(String accessoryPack)
    {
        return this.accessoryPackComponentsMap.get(accessoryPack);
    }

    public Module getModule(String code)
    {
        return this.moduleMap.get(code);
    }

    public ShutterFinish getFinish(String finishCode)
    {
        return this.finishCodeMap.get(finishCode);
    }

    public ShutterFinish getFinish(String carcassCode, String finishCode)
    {
        ShutterFinish shutterFinish = this.getFinish(finishCode);
        if (shutterFinish == null) return null;

        for (ShutterFinish finish : this.finishCodeMap.values())
        {
            if (finish.getFinishType().equals(shutterFinish.getFinishType()) && finish.getShutterMaterial().equals(carcassCode))
            {
                return finish;
            }
        }
        return shutterFinish;
    }

    public CarcassPanel getCarcassPanel(String code)
    {
        return this.carcassPanelMap.get(code);
    }

    public ShutterPanel getShutterPanel(String code)
    {
        return this.shutterPanelMap.get(code);
    }

    public AccHwComponent getAccessory(String code)
    {
        return this.accessoriesMap.get(code);
    }

    public AccHwComponent getHardware(String code)
    {
        return this.hardwareMap.get(code);
    }

    public void setMapping(ProductModule module)
    {
        Module mgModule = this.getModule(module.getExternalCode());
        if (mgModule != null)
        {
            module.setMappedFlag(ProductModule.MODULE_MAPPED);
            module.setMGCode(mgModule.getCode());
            module.setImagePath(mgModule.getImagePath());
        }
        else
        {
            module.setMappedFlag(ProductModule.MODULE_NOT_MAPPED);
        }
    }
}

