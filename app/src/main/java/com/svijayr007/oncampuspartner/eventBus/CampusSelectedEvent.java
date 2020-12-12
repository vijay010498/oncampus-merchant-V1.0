package com.svijayr007.oncampuspartner.eventBus;

import com.svijayr007.oncampuspartner.model.CampusModel;

public class CampusSelectedEvent {
    private CampusModel selectedCampus;

    public CampusSelectedEvent(CampusModel selectedCampus) {
        this.selectedCampus = selectedCampus;
    }

    public CampusModel getSelectedCampus() {
        return selectedCampus;
    }

    public void setSelectedCampus(CampusModel selectedCampus) {
        this.selectedCampus = selectedCampus;
    }
}
