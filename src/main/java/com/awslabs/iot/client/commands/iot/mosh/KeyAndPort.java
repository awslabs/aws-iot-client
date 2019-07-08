package com.awslabs.iot.client.commands.iot.mosh;

public class KeyAndPort {
    private String key;

    private int port;

    KeyAndPort(String key, int port) {
        this.key = key;
        this.port = port;
    }

    public static KeyAndPortBuilder builder() {
        return new KeyAndPortBuilder();
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof KeyAndPort)) return false;
        final KeyAndPort other = (KeyAndPort) o;
        if (!other.canEqual(this)) return false;
        final Object this$key = this.getKey();
        final Object other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) return false;
        return this.getPort() == other.getPort();
    }

    private boolean canEqual(final Object other) {
        return other instanceof KeyAndPort;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $key = this.getKey();
        result = result * PRIME + ($key == null ? 43 : $key.hashCode());
        result = result * PRIME + this.getPort();
        return result;
    }

    public String toString() {
        return "KeyAndPort(key=" + this.getKey() + ", port=" + this.getPort() + ")";
    }

    public static class KeyAndPortBuilder {
        private String key;
        private int port;

        KeyAndPortBuilder() {
        }

        public KeyAndPort.KeyAndPortBuilder key(String key) {
            this.key = key;
            return this;
        }

        public KeyAndPort.KeyAndPortBuilder port(int port) {
            this.port = port;
            return this;
        }

        public KeyAndPort build() {
            return new KeyAndPort(key, port);
        }

        public String toString() {
            return "KeyAndPort.KeyAndPortBuilder(key=" + this.key + ", port=" + this.port + ")";
        }
    }
}
