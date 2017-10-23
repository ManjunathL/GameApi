package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.Handle;
import com.mygubbi.game.proposal.model.HingePack;
import com.mygubbi.game.proposal.model.Module;
import com.mygubbi.game.proposal.model.ShutterFinish;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ModuleSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(ModuleSheetCreator.class);

    private QuoteData quoteData;
    private Sheet sheet;
    private ExcelStyles styles;
    private AssembledProductInQuote product;
    private ExcelSheetProcessor sheetProcessor;
    private String hingeTitle;

    public ModuleSheetCreator(Sheet sheet, QuoteData quoteData, AssembledProductInQuote product, ExcelStyles styles)
    {
        this.sheet = sheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.product = product;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.sheet, this.styles, this);
        this.sheetProcessor.process();
    }

    @Override
    public Object getValueForKey(String key)
    {
        Object value = this.quoteData.getValue(key);
        if (value == null)
        {
            return this.product.getValue(key);
        }
        return value;
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Modules":
                int rownum=this.fillModules(this.product.getModules(), cell.getRow().getRowNum() + 1, "No Modules.");
                rownum=this.fillModules1(this.product.getModules(), rownum + 1, "No Modules.");
                rownum=this.fillModulesCustomCheck(this.product.getModules(), rownum + 1, "No Modules.");
                break;
            default:
                break;
        }
    }
    public String exposedSides(ProductModule module) {
        String exposed="";
        if(module.isLeftExposed()) {exposed+="Left";}
        if(module.isRightExposed()) {exposed+=" Right";}
        if(module.isBottomExposed()) {exposed+=" Bottom";}
        if(module.isTopExposed()) {exposed+=" Top";}
        if(module.isBackExposed()) {exposed+=" Back";}
        if(module.isOpenUnit()) {exposed+=" Open";}

        return exposed;
    }

    private int fillModules(List<ProductModule> modules, int currentRow, String defaultMessage) {
        try {
            if (modules == null || modules.isEmpty()) {
                currentRow++;
                this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
                return currentRow;
            }

            int seq = 1;
            currentRow++;
            this.sheetProcessor.createTitleRowInDataSheet(currentRow, new String[]{"Standard Modules"});
            for (ProductModule module : modules) {
                Module mgModule = ModuleDataService.getInstance().getModule(module.getMGCode());
                LOG.info("module in module sheet creator " +mgModule.toString());
                ShutterFinish finish = ModuleDataService.getInstance().getFinish(module.getFinishCode());
                String glassMandatory=mgModule.getGlassMandatory();
                String glassValue="";
                if(glassMandatory.equals("Yes"))
                {
                    glassValue=this.product.getGlass();
                }
                else
                {
                    glassValue="NA";
                }
                String custom = " ";
                try {
                    if (module.getCustomCheck() == null || module.getCustomCheck().length() == 0 || module.getCustomCheck().equals("General Remarks")) {
                        // if(module.getCustomCheck().equals("Custom Remarks"))
                        custom = "No";
                    } else
                        custom = "Yes";
                } catch (Exception e) {
                    LOG.info(e.getStackTrace());
                }
                if(!module.getMGCode().startsWith("MG-NS") && custom.equals("No"))
                {
                    currentRow++;
                    LOG.info("Standard Module code " +module.getMGCode());
                    for (HingePack hingePack : module.getHingePacks()) {
                        hingeTitle = hingePack.getTYPE();
                    }
                    String handleType = this.product.getProduct().getHandletypeSelection();
                    try
                    {
                        if (handleType == null || handleType.length() == 0)  //      module.getCustomCheck() == null || module.getCustomCheck().length() == 0
                        {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;
                        }
                    }
                    catch (Exception e)
                    {
                        LOG.info(e);
                    }


                    if ("Normal".equals(handleType)) {
                        if ((module.getHandleQuantity() != 0) && (module.getKnobQuantity() == 0)) {
                            Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getHandleCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, handle.getTitle(), handle.getFinish(), handle.getThickness(), module.getHandleQuantity(), "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;
                        } else if ((module.getHandleQuantity() == 0) && (module.getKnobQuantity() != 0)) {
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle,glassValue, "NA", "NA", "NA", "NA", knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else if ((module.getHandleQuantity() != 0) && (module.getKnobQuantity() != 0)) {
                            Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getHandleCode());
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            // LOG.info("&&&" + handle.getTitle() + "" + knob.getTitle());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, handle.getTitle(), handle.getFinish(), handle.getThickness(), module.getHandleQuantity(), knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});

                            seq++;
                        }
                    } else if("Gola Profile".equals(handleType) || "G Profile".equals(handleType) || "J Profile".equals(handleType)) {

                        if (module.getKnobQuantity() != 0) {
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle,glassValue, handleType, "NA", "NA", "NA", "NA", knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle,glassValue, handleType, "NA", "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;

                        }

                    }
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }
        return currentRow;
    }

    private int fillModules1(List<ProductModule> modules, int currentRow, String defaultMessage) {
        try {
            if (modules == null || modules.isEmpty()) {
                currentRow++;
                this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
                return currentRow;
            }

            int seq = 1;
            currentRow++;
            this.sheetProcessor.createTitleRowInDataSheet(currentRow, new String[]{"Non Standard Modules"});
            for (ProductModule module : modules) {
                Module mgModule = ModuleDataService.getInstance().getModule(module.getMGCode());
                ShutterFinish finish = ModuleDataService.getInstance().getFinish(module.getFinishCode());
                String custom = " ";
                String glassMandatory=mgModule.getGlassMandatory();
                String glassValue="";
                if(glassMandatory.equals("Yes"))
                {
                    glassValue=this.product.getGlass();
                }
                else
                {
                    glassValue="NA";
                }
                try {
                    if (module.getCustomCheck() == null || module.getCustomCheck().length() == 0 || module.getCustomCheck().equals("General Remarks")) {
                        // if(module.getCustomCheck().equals("Custom Remarks"))
                        custom = "No";
                    } else
                        custom = "Yes";
                } catch (Exception e) {
                    LOG.info(e.getStackTrace());
                }
                if(module.getMGCode().startsWith("MG-NS") && custom.equals("No"))
                {
                    currentRow++;
                    LOG.info("Non standard Module code " +module.getMGCode());
                    for (HingePack hingePack : module.getHingePacks()) {
                        hingeTitle = hingePack.getTYPE();
                    }
                    /*String custom = " ";
                    try {
                        if (module.getCustomCheck() == null || module.getCustomCheck().length() == 0 || module.getCustomCheck().equals("General Remarks")) {
                            // if(module.getCustomCheck().equals("Custom Remarks"))
                            custom = "No";
                        } else
                            custom = "Yes";
                    } catch (Exception e) {
                        LOG.info(e.getStackTrace());
                    }*/
                    String handleType = this.product.getProduct().getHandletypeSelection();
                    try
                    {
                        if (handleType == null || handleType.length() == 0)  //      module.getCustomCheck() == null || module.getCustomCheck().length() == 0
                        {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;
                        }
                    }
                    catch (Exception e)
                    {
                        LOG.info(e);
                    }


                    if ("Normal".equals(handleType)) {
                        if ((module.getHandleQuantity() != 0) && (module.getKnobQuantity() == 0)) {
                            Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getHandleCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, handle.getTitle(), handle.getFinish(), handle.getThickness(), module.getHandleQuantity(), "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;
                        } else if ((module.getHandleQuantity() == 0) && (module.getKnobQuantity() != 0)) {
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, "NA", "NA", "NA", "NA", knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else if ((module.getHandleQuantity() != 0) && (module.getKnobQuantity() != 0)) {
                            Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getHandleCode());
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            // LOG.info("&&&" + handle.getTitle() + "" + knob.getTitle());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, handle.getTitle(), handle.getFinish(), handle.getThickness(), module.getHandleQuantity(), knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});

                            seq++;
                        }
                    } else if("Gola Profile".equals(handleType) || "G Profile".equals(handleType) || "J Profile".equals(handleType)) {

                        if (module.getKnobQuantity() != 0) {
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, "NA", "NA", "NA", "NA", knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, "NA", "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;

                        }

                    }
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }
        return currentRow;
    }

    private int fillModulesCustomCheck(List<ProductModule> modules, int currentRow, String defaultMessage) {
        try {
            if (modules == null || modules.isEmpty()) {
                currentRow++;
                this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
                return currentRow;
            }

            int seq = 1;
            currentRow++;
            this.sheetProcessor.createTitleRowInDataSheet(currentRow, new String[]{"Customised Modules"});
            for (ProductModule module : modules) {
                Module mgModule = ModuleDataService.getInstance().getModule(module.getMGCode());
                ShutterFinish finish = ModuleDataService.getInstance().getFinish(module.getFinishCode());
                String custom = " ";
                String glassMandatory=mgModule.getGlassMandatory();
                String glassValue="";
                if(glassMandatory.equals("Yes"))
                {
                    glassValue=this.product.getGlass();
                }
                else
                {
                    glassValue="NA";
                }
                try {
                    if (module.getCustomCheck() == null || module.getCustomCheck().length() == 0 || module.getCustomCheck().equals("General Remarks")) {
                        // if(module.getCustomCheck().equals("Custom Remarks"))
                        custom = "No";
                    } else
                        custom = "Yes";
                } catch (Exception e) {
                    LOG.info(e.getStackTrace());
                }
                if(custom.equals("Yes"))
                {
                    currentRow++;
                    LOG.info("Non standard Module code33 " +module.getMGCode());
                    for (HingePack hingePack : module.getHingePacks()) {
                        hingeTitle = hingePack.getTYPE();
                    }
                    /*String custom = " ";
                    try {
                        if (module.getCustomCheck() == null || module.getCustomCheck().length() == 0 || module.getCustomCheck().equals("General Remarks")) {
                            // if(module.getCustomCheck().equals("Custom Remarks"))
                            custom = "No";
                        } else
                            custom = "Yes";
                    } catch (Exception e) {
                        LOG.info(e.getStackTrace());
                    }*/
                    String handleType = this.product.getProduct().getHandletypeSelection();
                    try
                    {
                        if (handleType == null || handleType.length() == 0)  //      module.getCustomCheck() == null || module.getCustomCheck().length() == 0
                        {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;
                        }
                    }
                    catch (Exception e)
                    {
                        LOG.info(e);
                    }


                    if ("Normal".equals(handleType)) {
                        if ((module.getHandleQuantity() != 0) && (module.getKnobQuantity() == 0)) {
                            Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getHandleCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, handle.getTitle(), handle.getFinish(), handle.getThickness(), module.getHandleQuantity(), "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;
                        } else if ((module.getHandleQuantity() == 0) && (module.getKnobQuantity() != 0)) {
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, "NA", "NA", "NA", "NA", knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else if ((module.getHandleQuantity() != 0) && (module.getKnobQuantity() != 0)) {
                            Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getHandleCode());
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            // LOG.info("&&&" + handle.getTitle() + "" + knob.getTitle());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, handle.getTitle(), handle.getFinish(), handle.getThickness(), module.getHandleQuantity(), knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});

                            seq++;
                        }
                    } else if("Gola Profile".equals(handleType) || "G Profile".equals(handleType) || "J Profile".equals(handleType)) {

                        if (module.getKnobQuantity() != 0) {
                            Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, "NA", "NA", "NA", "NA", knob.getTitle(), knob.getFinish(), module.getKnobQuantity(), module.getAccessoryFlag()});
                            seq++;
                        } else {
                            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getCode(), mgModule.getDescription(),
                                    module.getWidth(), module.getDepth(), module.getHeight(), custom, module.getRemarks(), module.getCarcassCode(), finish.getFinishMaterial(), finish.getTitle(), module.getColorCode(), "", exposedSides(module),
                                    hingeTitle, glassValue, handleType, "NA", "NA", "NA", "NA", "NA", "NA", "NA", module.getAccessoryFlag()});
                            seq++;

                        }

                    }
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }
        return currentRow;
    }

}
