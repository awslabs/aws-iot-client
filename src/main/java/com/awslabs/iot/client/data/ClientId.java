package com.awslabs.iot.client.data;

public class ClientId {
    private final String clientId;

    ClientId(String clientId) {
        this.clientId = clientId;
    }

    public static ClientIdBuilder builder() {
        return new ClientIdBuilder();
    }

    public String getClientId() {
        return this.clientId;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ClientId)) return false;
        final ClientId other = (ClientId) o;
        if (!other.canEqual(this)) return false;
        final Object this$clientId = this.getClientId();
        final Object other$clientId = other.getClientId();
        return this$clientId == null ? other$clientId == null : this$clientId.equals(other$clientId);
    }

    private boolean canEqual(final Object other) {
        return other instanceof ClientId;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $clientId = this.getClientId();
        result = result * PRIME + ($clientId == null ? 43 : $clientId.hashCode());
        return result;
    }

    public String toString() {
        return "ClientId(clientId=" + this.getClientId() + ")";
    }

    public static class ClientIdBuilder {
        private String clientId;

        ClientIdBuilder() {
        }

        public ClientId.ClientIdBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ClientId build() {
            return new ClientId(clientId);
        }

        public String toString() {
            return "ClientId.ClientIdBuilder(clientId=" + this.clientId + ")";
        }
    }
}
