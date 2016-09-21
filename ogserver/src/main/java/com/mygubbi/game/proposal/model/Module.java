package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-05-2016.
 */
public class Module
{
    private static final double SQMM2SQFT = 0.0000107639;

    private static final String CODE = "code";
    private static final String WIDTH = "width";
    private static final String DEPTH = "depth";
    private static final String HEIGHT = "height";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_PATH = "imagePath";
    private static final String MODULE_CATEGORY = "moduleCategory";
    private static final String MODULE_TYPE = "moduleType";
    private static final String UNIT_TYPE = "unitType";
    private static final String MATERIAL = "material";
    private static final String PRODUCT_CATEGORY = "productCategory";

    private static final String STANDARD_MODULE = "S";
    private static final String CUSTOM_MODULE = "N";

    private String code;
    private String description;
    private String imagePath;
    private String moduleCategory;
    private String moduleType;
    private String unitType;
    private String material;
    private String productCategory;
    private int height;
    private int depth;
    private int width;

    public Module()
    {

    }

    public Module(JsonObject json)
    {
        this.setCode(json.getString(CODE));
        this.setHeight(json.getInteger(HEIGHT));
        this.setDepth(json.getInteger(DEPTH));
        this.setWidth(json.getInteger(WIDTH));
        this.setDescription(json.getString(DESCRIPTION));
        this.setImagePath(json.getString(IMAGE_PATH));
        this.setModuleCategory(json.getString(MODULE_CATEGORY));
        this.setModuleType(json.getString(MODULE_TYPE));
        this.setProductCategory(json.getString(PRODUCT_CATEGORY));
        this.setUnitType(json.getString(UNIT_TYPE));
        this.setMaterial(json.getString(MATERIAL));
    }

    public String getModuleCategory() {
        return moduleCategory;
    }

    public void setModuleCategory(String moduleCategory) {
        this.moduleCategory = moduleCategory;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDimension()
    {
        return this.getWidth() + " X " + this.getDepth() + " X " + this.getHeight();
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public double getAreaOfModuleInSft()
    {
        double h = this.getHeight();
        double w = this.getWidth();
        double d = this.getDepth();

        double t1 = 0;
        double t2 = 0;

        t1=h;
        t2=w;

        return t1 * t2 * SQMM2SQFT;
    }


    public boolean isStandard() {
        return this.getModuleType().equals(STANDARD_MODULE);
    }

    public boolean isCustomized() {
        return this.getModuleType().equals(CUSTOM_MODULE);
    }

    public String getUnitType()
    {
        return unitType;
    }

    public void setUnitType(String unitType)
    {
        this.unitType = unitType;
    }

    public String getMaterial()
    {
        return material;
    }

    public void setMaterial(String material)
    {
        this.material = material;
    }
}


