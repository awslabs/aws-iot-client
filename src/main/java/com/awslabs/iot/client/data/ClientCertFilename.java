package com.awslabs.iot.client.data;

public class ClientCertFilename {
    private final String clientCertFilename;

    ClientCertFilename(String clientCertFilename) {
        this.clientCertFilename = clientCertFilename;
    }

    public static ClientCertFilenameBuilder builder() {
        return new ClientCertFilenameBuilder();
    }

    public String getClientCertFilename() {
        return this.clientCertFilename;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ClientCertFilename)) return false;
        final ClientCertFilename other = (ClientCertFilename) o;
        if (!other.canEqual(this)) return false;
        final Object this$clientCertFilename = this.getClientCertFilename();
        final Object other$clientCertFilename = other.getClientCertFilename();
        return this$clientCertFilename == null ? other$clientCertFilename == null : this$clientCertFilename.equals(other$clientCertFilename);
    }

    private boolean canEqual(final Object other) {
        return other instanceof ClientCertFilename;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $clientCertFilename = this.getClientCertFilename();
        result = result * PRIME + ($clientCertFilename == null ? 43 : $clientCertFilename.hashCode());
        return result;
    }

    public String toString() {
        return "ClientCertFilename(clientCertFilename=" + this.getClientCertFilename() + ")";
    }

    public static class ClientCertFilenameBuilder {
        private String clientCertFilename;

        ClientCertFilenameBuilder() {
        }

        public ClientCertFilename.ClientCertFilenameBuilder clientCertFilename(String clientCertFilename) {
            this.clientCertFilename = clientCertFilename;
            return this;
        }

        public ClientCertFilename build() {
            return new ClientCertFilename(clientCertFilename);
        }

        public String toString() {
            return "ClientCertFilename.ClientCertFilenameBuilder(clientCertFilename=" + this.clientCertFilename + ")";
        }
    }
}
