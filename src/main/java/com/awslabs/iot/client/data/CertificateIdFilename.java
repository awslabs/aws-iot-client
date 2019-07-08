package com.awslabs.iot.client.data;

public class CertificateIdFilename {
    private final String certificateIdFilename;

    CertificateIdFilename(String certificateIdFilename) {
        this.certificateIdFilename = certificateIdFilename;
    }

    public static CertificateIdFilenameBuilder builder() {
        return new CertificateIdFilenameBuilder();
    }

    public String getCertificateIdFilename() {
        return this.certificateIdFilename;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CertificateIdFilename)) return false;
        final CertificateIdFilename other = (CertificateIdFilename) o;
        if (!other.canEqual(this)) return false;
        final Object this$certificateIdFilename = this.getCertificateIdFilename();
        final Object other$certificateIdFilename = other.getCertificateIdFilename();
        return this$certificateIdFilename == null ? other$certificateIdFilename == null : this$certificateIdFilename.equals(other$certificateIdFilename);
    }

    private boolean canEqual(final Object other) {
        return other instanceof CertificateIdFilename;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $certificateIdFilename = this.getCertificateIdFilename();
        result = result * PRIME + ($certificateIdFilename == null ? 43 : $certificateIdFilename.hashCode());
        return result;
    }

    public String toString() {
        return "CertificateIdFilename(certificateIdFilename=" + this.getCertificateIdFilename() + ")";
    }

    public static class CertificateIdFilenameBuilder {
        private String certificateIdFilename;

        CertificateIdFilenameBuilder() {
        }

        public CertificateIdFilename.CertificateIdFilenameBuilder certificateIdFilename(String certificateIdFilename) {
            this.certificateIdFilename = certificateIdFilename;
            return this;
        }

        public CertificateIdFilename build() {
            return new CertificateIdFilename(certificateIdFilename);
        }

        public String toString() {
            return "CertificateIdFilename.CertificateIdFilenameBuilder(certificateIdFilename=" + this.certificateIdFilename + ")";
        }
    }
}
