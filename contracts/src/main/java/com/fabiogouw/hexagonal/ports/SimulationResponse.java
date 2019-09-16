package com.fabiogouw.hexagonal.ports;

public class SimulationResponse {
    private String clienteId;
    private double interestRate;
    private int installments;
    private double installmentValue;

    public SimulationResponse() {

    }

    public SimulationResponse(String clienteId, double interestRate, int installments, double installmentValue) {
        this.clienteId = clienteId;
        this.interestRate = interestRate;
        this.installments = installments;
        this.installmentValue = installmentValue;
    }

    public String getClienteId() {
        return this.clienteId;
    }
    public void setClienteId(String value) {
        this.clienteId = value;
    }
    public double getInterestRate() {
        return this.interestRate;
    }
    public void setInterestRate(double value) {
        this.interestRate = value;
    }
    public int getInstallments() {
        return this.installments;
    }
    public void setInstallments(int value) {
        this.installments = value;
    }
    public double getInstallmentValue() {
        return this.installmentValue;
    }
    public void setInstallmentValue(double value) {
        this.installmentValue = value;
    }
}
