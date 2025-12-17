package com.pivovarit.rental.service;

public class AccountService {

    private final RentalService rentalFacade;
    private final WarehouseService warehouseService;

    public AccountService(RentalService rentalFacade, WarehouseService warehouseService) {
        this.rentalFacade = rentalFacade;
        this.warehouseService = warehouseService;
    }

    public boolean canUserRent(int id) {
        return true;
    }
}
