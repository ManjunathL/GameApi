package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.IModuleComponent;
import com.mygubbi.game.proposal.model.ModuleComponent;

/**
 * Created by test on 09-09-2016.
 */
public class ComponentResolver
{
    public IModuleComponent resolveComponent(IModuleComponent component, ProductModule productModule)
    {

        String type = null;
        String code = null;

        switch(component.getComponentCode())
        {
            case "DRAWER-HW":
                //=+IF(F21<350,"H005",IF(F21<400,"H004",IF(F21<450,"H002",IF(F21<500,"H003",IF(F21<550,"H006")))))
                type = IModuleComponent.HARDWARE_TYPE;
                if (productModule.getDepth() < 350)
                    code = "H005";
                else if (productModule.getDepth() < 400)
                    code = "H004";
                else if (productModule.getDepth() < 450)
                    code = "H002";
                else if (productModule.getDepth() < 500)
                    code = "H003";
                else if (productModule.getDepth() < 550)
                    code = "H006";
                break;
        }

        if (code == null) return null;

        return new ModuleComponent().setComponentCode(code).setQuantity(component.getQuantity())
                .setQuantityFlag(component.getQuantityFlag()).setQuantityFormula(component.getQuantityFormula())
                .setType(type);
    }
}
