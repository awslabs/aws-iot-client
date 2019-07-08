package com.awslabs.iot.client.data;

public class CaCertFilename {
    private final String caCertFilename;

    CaCertFilename(String caCertFilename) {
        this.caCertFilename = caCertFilename;
    }

    public static CaCertFilenameBuilder builder() {
        return new CaCertFilenameBuilder();
    }

    public String getCaCertFilename() {
        return this.caCertFilename;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CaCertFilename)) return false;
        final CaCertFilename other = (CaCertFilename) o;
        if (!other.canEqual(this)) return false;
        final Object this$caCertFilename = this.getCaCertFilename();
        final Object other$caCertFilename = other.getCaCertFilename();
        return this$caCertFilename == null ? other$caCertFilename == null : this$caCertFilename.equals(other$caCertFilename);
    }

    private boolean canEqual(final Object other) {
        return other instanceof CaCertFilename;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $caCertFilename = this.getCaCertFilename();
        result = result * PRIME + ($caCertFilename == null ? 43 : $caCertFilename.hashCode());
        return result;
    }

    public String toString() {
        return "CaCertFilename(caCertFilename=" + this.getCaCertFilename() + ")";
    }

    public static class CaCertFilenameBuilder {
        private String caCertFilename;

        CaCertFilenameBuilder() {
        }

        public CaCertFilename.CaCertFilenameBuilder caCertFilename(String caCertFilename) {
            this.caCertFilename = caCertFilename;
            return this;
        }

        public CaCertFilename build() {
            return new CaCertFilename(caCertFilename);
        }

        public String toString() {
            return "CaCertFilename.CaCertFilenameBuilder(caCertFilename=" + this.caCertFilename + ")";
        }
    }
}
