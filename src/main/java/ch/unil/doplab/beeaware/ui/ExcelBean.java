package ch.unil.doplab.beeaware.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

@ViewScoped
@Named
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelBean implements Serializable {

    @Inject
    BeezzerData beezzerData;

    private Long beezzerId;
    private StreamedContent file;

    @PostConstruct
    public void init() {
        System.out.println("PostConstruct called for ExcelBean");

        // Check for not null injects
        if (beezzerData == null) {
            throw new RuntimeException("BeezzerData is not injected!");
        }
        if (beezzerData.getTheService() == null) {
            throw new RuntimeException("ApplicationServiceManagement is not injected into BeezzerData!");
        }

        // Retrieve beezzer id from beezzerData
        beezzerId = beezzerData.getId();

        // Check correct population of id and date
        System.out.println("Beezzer ID: " + beezzerId);
    }

    public void reset() {
        this.beezzerId = null;
        this.file = null;
    }

    public void download() {
        try {
            byte[] fileContent = beezzerData.getTheService().getExcelService().downloadExcelFile(beezzerId);

            file = DefaultStreamedContent.builder()
                    .name("mySymptoms.xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> new ByteArrayInputStream(fileContent))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error preparing file for download", e);
        }
    }
}
