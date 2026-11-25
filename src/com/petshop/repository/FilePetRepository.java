package com.petshop.repository;

import com.petshop.exception.InvalidPetDataException;
import com.petshop.exception.PetNotFoundException;
import com.petshop.exception.PetShopException;
import com.petshop.model.Bird;
import com.petshop.model.Cat;
import com.petshop.model.Dog;
import com.petshop.model.Pet;
import com.petshop.model.PetStatus;
import com.petshop.model.PetType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Pure Java File-Based CSV Repository (Syllabus Unit 5: File Handling & try-with-resources).
 * Uses character streams (FileReader, FileWriter, BufferedReader, BufferedWriter)
 * to persist and load pet records from disk without any external database.
 */
public class FilePetRepository implements Repository<Pet, Integer> {
    private final File dataFile;
    private final Map<Integer, Pet> cache = new LinkedHashMap<>();

    // CSV Header definition
    public static final String CSV_HEADER = "id,name,type,breed,ageMonths,price,status,microchipId,lastVaccine,lastVaccineDate,extra1,extra2";

    public FilePetRepository(String filePath) throws PetShopException {
        this.dataFile = new File(filePath);
        File parentDir = dataFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        loadFromFile();
    }

    public void loadFromFile() throws PetShopException {
        cache.clear();
        if (!dataFile.exists()) {
            return;
        }

        // try-with-resources with BufferedReader (Syllabus Unit 5)
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line = reader.readLine(); // Read CSV header
            int lineNum = 1;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Pet pet = parseCsvLine(line);
                    cache.put(pet.getId(), pet);
                } catch (Exception ex) {
                    System.err.printf("  [Warning] Skipping malformed line #%d in %s: %s (%s)%n",
                            lineNum, dataFile.getName(), line, ex.getMessage());
                }
            }
        } catch (IOException e) {
            throw new PetShopException("Failed to read pet storage file: " + e.getMessage(), "ERR_IO_READ", e);
        }
    }

    public void saveToFile() throws PetShopException {
        // try-with-resources with BufferedWriter (Syllabus Unit 5)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (Pet pet : cache.values()) {
                writer.write(serializePetToCsv(pet));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new PetShopException("Failed to write pet storage file: " + e.getMessage(), "ERR_IO_WRITE", e);
        }
    }

    @Override
    public Pet save(Pet pet) throws PetShopException {
        if (pet == null) throw new PetShopException("Cannot save null pet", "ERR_NULL");
        cache.put(pet.getId(), pet);
        saveToFile();
        return pet;
    }

    @Override
    public Optional<Pet> findById(Integer id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<Pet> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean deleteById(Integer id) throws PetShopException {
        if (!cache.containsKey(id)) {
            throw new PetNotFoundException(id);
        }
        cache.remove(id);
        saveToFile();
        return true;
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && cache.containsKey(id);
    }

    @Override
    public long count() {
        return cache.size();
    }

    public File getDataFile() {
        return dataFile;
    }

    // Helper: deserialize CSV string into polymorphic Pet instances
    private Pet parseCsvLine(String line) throws InvalidPetDataException {
        String[] tokens = line.split(",", -1);
        if (tokens.length < 10) {
            throw new InvalidPetDataException("line", "Insufficient columns in CSV row");
        }

        int id = Integer.parseInt(tokens[0].trim());
        String name = tokens[1].trim();
        PetType type = PetType.valueOf(tokens[2].trim().toUpperCase());
        String breed = tokens[3].trim();
        int age = Integer.parseInt(tokens[4].trim());
        double price = Double.parseDouble(tokens[5].trim());
        PetStatus status = PetStatus.valueOf(tokens[6].trim().toUpperCase());
        String microchip = tokens[7].trim();
        String lastVaccine = tokens[8].trim();
        String vaccineDateStr = tokens[9].trim();

        Pet pet;
        if (type == PetType.DOG) {
            boolean isTrained = tokens.length > 10 && Boolean.parseBoolean(tokens[10].trim());
            String breedGroup = tokens.length > 11 ? tokens[11].trim() : "General";
            pet = new Dog(id, name, breed, age, price, microchip, isTrained, breedGroup);
        } else if (type == PetType.CAT) {
            boolean isIndoor = tokens.length > 10 && Boolean.parseBoolean(tokens[10].trim());
            boolean isDeclawed = tokens.length > 11 && Boolean.parseBoolean(tokens[11].trim());
            pet = new Cat(id, name, breed, age, price, microchip, isIndoor, isDeclawed);
        } else if (type == PetType.BIRD) {
            double wingspan = tokens.length > 10 ? Double.parseDouble(tokens[10].trim()) : 25.0;
            boolean canTalk = tokens.length > 11 && Boolean.parseBoolean(tokens[11].trim());
            pet = new Bird(id, name, breed, age, price, microchip, wingspan, canTalk);
        } else {
            // Default generic concrete subclass
            pet = new Dog(id, name, breed, age, price, microchip, false, "General");
        }

        pet.setStatus(status);
        if (!lastVaccine.isEmpty() && !vaccineDateStr.isEmpty()) {
            pet.recordVaccination(lastVaccine, LocalDate.parse(vaccineDateStr));
        }
        return pet;
    }

    // Helper: serialize Pet object to comma-separated line
    public static String serializePetToCsv(Pet pet) {
        StringBuilder sb = new StringBuilder();
        sb.append(pet.getId()).append(",")
          .append(pet.getName()).append(",")
          .append(pet.getType().name()).append(",")
          .append(pet.getBreed()).append(",")
          .append(pet.getAgeMonths()).append(",")
          .append(String.format("%.2f", pet.getPrice())).append(",")
          .append(pet.getStatus().name()).append(",")
          .append(pet.getMicrochipId()).append(",")
          .append(pet.getLastVaccineName() != null ? pet.getLastVaccineName() : "").append(",")
          .append(pet.getLastVaccinationDate() != null ? pet.getLastVaccinationDate().toString() : "").append(",");

        if (pet instanceof Dog dog) {
            sb.append(dog.isTrained()).append(",").append(dog.getBreedGroup());
        } else if (pet instanceof Cat cat) {
            sb.append(cat.isIndoor()).append(",").append(cat.isDeclawed());
        } else if (pet instanceof Bird bird) {
            sb.append(bird.getWingspanCm()).append(",").append(bird.canTalk());
        } else {
            sb.append("false,General");
        }
        return sb.toString();
    }
}
