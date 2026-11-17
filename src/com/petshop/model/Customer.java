package com.petshop.model;

import com.petshop.exception.InvalidPetDataException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Customer domain model.
 * Demonstrates string validation, regex matching, and Comparable interface (Unit 1 & 2).
 */
public class Customer implements Comparable<Customer> {
    private static int nextCustomerId = 2001;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{10,14}$");

    private final int customerId;
    private String fullName;
    private String phone;
    private String email;
    private int adoptedCount;

    public Customer(int customerId, String fullName, String phone, String email, int adoptedCount)
            throws InvalidPetDataException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidPetDataException("fullName", "Customer name cannot be empty.");
        }
        validatePhone(phone);
        validateEmail(email);

        this.customerId = (customerId > 0) ? customerId : nextCustomerId++;
        this.fullName = fullName.trim();
        this.phone = phone.trim();
        this.email = email.trim().toLowerCase();
        this.adoptedCount = Math.max(0, adoptedCount);
    }

    public Customer(String fullName, String phone, String email) throws InvalidPetDataException {
        this(nextCustomerId++, fullName, phone, email, 0);
    }

    private static void validateEmail(String email) throws InvalidPetDataException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidPetDataException("email", "Invalid email address format (" + email + ").");
        }
    }

    private static void validatePhone(String phone) throws InvalidPetDataException {
        if (phone == null || !PHONE_PATTERN.matcher(phone.trim().replaceAll("[\\s-]", "")).matches()) {
            throw new InvalidPetDataException("phone", "Invalid phone number format (" + phone + "). Expected 10-14 digits.");
        }
    }

    public void incrementAdoptionCount() {
        this.adoptedCount++;
    }

    public int getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public int getAdoptedCount() { return adoptedCount; }

    @Override
    public int compareTo(Customer other) {
        if (other == null) return 1;
        // Priority to higher adoption counts for loyalty waitlists
        int countComp = Integer.compare(other.adoptedCount, this.adoptedCount);
        if (countComp != 0) return countComp;
        return Integer.compare(this.customerId, other.customerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return customerId == customer.customerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return String.format("[CUST #%d] %-18s | Phone: %-12s | Email: %-22s | Adoptions: %d",
                customerId, fullName, phone, email, adoptedCount);
    }
}
