package com.autolog.autolog.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private String fileName;
    private List<ServiceRecord> serviceRecords = new ArrayList<>();

    public ServiceManager() {

    }

    public List<ServiceRecord> loadServices() {
        return serviceRecords;
    }

    public void addService(ServiceRecord record) {
        serviceRecords.add(record);
    }

    public List<ServiceRecord> getServiceRecords() {
        return serviceRecords;
    }

    public ServiceRecord getMostRecentService() {
        if (serviceRecords.isEmpty()) {
            return null;
        }

        return serviceRecords.get(serviceRecords.size() - 1);
    }

    public int getTotalServices() {
        return serviceRecords.size();
    }

    public double getTotalCost() {
        double total = 0.0;

        for (ServiceRecord record : serviceRecords) {
            total += record.getCost();
        }

        return total;
    }

    public int countServicesByType(String type) {
        int count = 0;

        for (ServiceRecord record : serviceRecords) {
            if (record.getType().equalsIgnoreCase(type)) {
                count++;
            }
        }

        return count;
    }

    private ServiceRecord parseServiceRecord(String line) {
        return null;
    }
}