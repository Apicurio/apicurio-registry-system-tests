package io.apicurio.registry.systemtests.framework;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Certificate {
    private CertificateType type;
    private String certificate;

    public Certificate(CertificateType type) {
        this.type = type;
        this.certificate = "";
    }

    public void addLine(String line) {
        if (line.endsWith("\n")) {
            this.certificate = this.certificate + line;
        } else {
            this.certificate = this.certificate + line + "\n";
        }
    }
}
