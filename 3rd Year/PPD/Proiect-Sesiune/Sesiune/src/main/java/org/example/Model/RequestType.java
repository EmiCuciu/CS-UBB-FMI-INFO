package org.example.Model;

/**
 * Tipuri de cereri de la client
 */
public enum RequestType {
    PURCHASE,              // Cumpără bilete (check + reserve + pay)
    CHECK_AVAILABILITY,    // Doar verifică disponibilitate
    CONFIRM_PAYMENT        // Confirmă plata unei rezervări
}
