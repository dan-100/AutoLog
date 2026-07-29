package com.autolog.autolog.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Manages all vehicle service records by loading, storing, updating,
 * and calculating maintenance information from the service history data.
 *
 * @author Daniel Hernandez
 */
public class ServiceManager {

    private static final String HEADER = "serviceType,date,mileage,cost,notes";
    private static final Path DEFAULT_FILE =
            Path.of("src", "main", "resources", "data", "service_history.csv");

    private final Path filePath;
    private final List<ServiceRecord> serviceRecords;

    public ServiceManager() {
        this(DEFAULT_FILE);
    }

    public ServiceManager(Path filePath) {
        this.filePath = filePath;
        this.serviceRecords = new ArrayList<>();
    }

    /**
     * Loads every service record from the CSV file.
     * Existing in-memory records are cleared first to prevent duplicates.
     */
    public List<ServiceRecord> loadServices() throws IOException {
        serviceRecords.clear();
        ensureFileExists();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    if (line.trim().equalsIgnoreCase(HEADER)) {
                        continue;
                    }
                }

                if (!line.isBlank()) {
                    serviceRecords.add(parseServiceRecord(line));
                }
            }
        }

        return getServiceRecords();
    }

    /**
     * Adds a record in memory and appends it to the CSV file.
     */
    public void addService(ServiceRecord record) throws IOException {
        if (record == null) {
            throw new IllegalArgumentException("Service record cannot be null.");
        }

        ensureFileExists();

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)) {
            writer.write(toCsv(record));
            writer.newLine();
        }

        serviceRecords.add(record);
    }

    /**
     * Rewrites the CSV file using the current in-memory list.
     */
    public void saveServices() throws IOException {
        ensureParentDirectoryExists();

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(HEADER);
            writer.newLine();

            for (ServiceRecord record : serviceRecords) {
                writer.write(toCsv(record));
                writer.newLine();
            }
        }
    }

    public List<ServiceRecord> getServiceRecords() {
        return List.copyOf(serviceRecords);
    }

    /**
     * The project's "recent service" is the final data row in the CSV file.
     */
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

    public double getAverageCost() {
        if (serviceRecords.isEmpty()) {
            return 0.0;
        }
        return getTotalCost() / serviceRecords.size();
    }

    public int countServicesByType(String type) {
        if (type == null || type.isBlank()) {
            return 0;
        }

        int count = 0;
        for (ServiceRecord record : serviceRecords) {
            if (record.getServiceType().equalsIgnoreCase(type.trim())) {
                count++;
            }
        }
        return count;
    }

    public Map<String, Integer> getServiceBreakdown() {
        Map<String, Integer> breakdown = new LinkedHashMap<>();

        for (ServiceRecord record : serviceRecords) {
            String type = record.getServiceType();
            breakdown.merge(type, 1, Integer::sum);
        }

        return breakdown;
    }

    private void ensureFileExists() throws IOException {
        ensureParentDirectoryExists();

        if (Files.notExists(filePath)) {
            Files.writeString(
                    filePath,
                    HEADER + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW);
        }
    }

    private void ensureParentDirectoryExists() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private ServiceRecord parseServiceRecord(String line) {
        List<String> data = parseCsvLine(line);

        if (data.size() != 5) {
            throw new IllegalArgumentException("Invalid service-history row: " + line);
        }

        try {
            return new ServiceRecord(
                    data.get(0).trim(),
                    data.get(1).trim(),
                    Integer.parseInt(data.get(2).trim()),
                    Double.parseDouble(data.get(3).trim()),
                    data.get(4).trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid mileage or cost in row: " + line, exception);
        }
    }

    private String toCsv(ServiceRecord record) {
        return escapeCsv(record.getServiceType()) + "," +
                escapeCsv(record.getDate()) + "," +
                record.getMileage() + "," +
                record.getCost() + "," +
                escapeCsv(record.getNotes());
    }

    private String escapeCsv(String value) {
        String safeValue = value == null ? "" : value;
        if (safeValue.contains(",") || safeValue.contains("\"") || safeValue.contains("\n")) {
            return "\"" + safeValue.replace("\"", "\"\"") + "\"";
        }
        return safeValue;
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);

            if (character == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (character == ',' && !insideQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }

        values.add(current.toString());
        return values;
    }
}