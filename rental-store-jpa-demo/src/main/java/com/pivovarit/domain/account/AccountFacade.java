package com.pivovarit.domain.account;

import com.pivovarit.domain.rental.RentalFacade;
import com.pivovarit.domain.warehouse.WarehouseFacade;

public class AccountFacade {

    private final RentalFacade rentalFacade;
    private final WarehouseFacade warehouseService;

    AccountFacade(RentalFacade rentalFacade, WarehouseFacade warehouseService) {
        this.rentalFacade = rentalFacade;
        this.warehouseService = warehouseService;
    }

    public boolean canUserRent(int id) {
        return true;
    }
}
