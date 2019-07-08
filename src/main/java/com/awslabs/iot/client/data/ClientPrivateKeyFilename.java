package com.awslabs.iot.client.data;

public class ClientPrivateKeyFilename {
    private final String clientPrivateKeyFilename;

    ClientPrivateKeyFilename(String clientPrivateKeyFilename) {
        this.clientPrivateKeyFilename = clientPrivateKeyFilename;
    }

    public static ClientPrivateKeyFilenameBuilder builder() {
        return new ClientPrivateKeyFilenameBuilder();
    }

    public String getClientPrivateKeyFilename() {
        return this.clientPrivateKeyFilename;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ClientPrivateKeyFilename)) return false;
        final ClientPrivateKeyFilename other = (ClientPrivateKeyFilename) o;
        if (!other.canEqual(this)) return false;
        final Object this$clientPrivateKeyFilename = this.getClientPrivateKeyFilename();
        final Object other$clientPrivateKeyFilename = other.getClientPrivateKeyFilename();
        return this$clientPrivateKeyFilename == null ? other$clientPrivateKeyFilename == null : this$clientPrivateKeyFilename.equals(other$clientPrivateKeyFilename);
    }

    private boolean canEqual(final Object other) {
        return other instanceof ClientPrivateKeyFilename;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $clientPrivateKeyFilename = this.getClientPrivateKeyFilename();
        result = result * PRIME + ($clientPrivateKeyFilename == null ? 43 : $clientPrivateKeyFilename.hashCode());
        return result;
    }

    public String toString() {
        return "ClientPrivateKeyFilename(clientPrivateKeyFilename=" + this.getClientPrivateKeyFilename() + ")";
    }

    public static class ClientPrivateKeyFilenameBuilder {
        private String clientPrivateKeyFilename;

        ClientPrivateKeyFilenameBuilder() {
        }

        public ClientPrivateKeyFilename.ClientPrivateKeyFilenameBuilder clientPrivateKeyFilename(String clientPrivateKeyFilename) {
            this.clientPrivateKeyFilename = clientPrivateKeyFilename;
            return this;
        }

        public ClientPrivateKeyFilename build() {
            return new ClientPrivateKeyFilename(clientPrivateKeyFilename);
        }

        public String toString() {
            return "ClientPrivateKeyFilename.ClientPrivateKeyFilenameBuilder(clientPrivateKeyFilename=" + this.clientPrivateKeyFilename + ")";
        }
    }
}
