package com.fabiogouw.hexagonal.ports;

public class SimulationRequest {
    private String clienteId;
    private double value;
    private int installments;

    public SimulationRequest() {

    }

    public SimulationRequest(String clienteId, double value, int installments) {
        this.clienteId = clienteId;
        this.value = value;
        this.installments = installments;
    }

    public String getClienteId() {
        return this.clienteId;
    }
    public void setClienteId(String value) {
        this.clienteId = value;
    }
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public int getInstallments() {
        return this.installments;
    }
    public void setInstallments(int value) {
        this.installments = value;
    }
}
