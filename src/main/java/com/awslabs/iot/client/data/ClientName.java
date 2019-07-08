package com.awslabs.iot.client.data;

public class ClientName {
    public final String clientName;

    ClientName(String clientName) {
        this.clientName = clientName;
    }

    public static ClientNameBuilder builder() {
        return new ClientNameBuilder();
    }

    public static class ClientNameBuilder {
        private String clientName;

        ClientNameBuilder() {
        }

        public ClientNameBuilder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public ClientName build() {
            return new ClientName(clientName);
        }

        public String toString() {
            return "ClientName.ClientNameBuilder(clientName=" + this.clientName + ")";
        }
    }
}
