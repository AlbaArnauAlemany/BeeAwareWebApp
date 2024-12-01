package ch.unil.doplab.beeaware.service;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;

@NoArgsConstructor
@AllArgsConstructor
public class ExcelService {

    private WebTarget excelTarget;

    public boolean downloadExcelFile(Long beezzerId, String destinationPath) throws IOException {
        try {
            Response response = excelTarget
                    .path("download/" + beezzerId)
                    .request(MediaType.APPLICATION_OCTET_STREAM)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                InputStream inputStream = response.readEntity(InputStream.class);

                File file = new File(destinationPath);
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                System.out.println("Excel file downloaded successfully to " + destinationPath);
                return true;
            } else {
                System.out.println("Failed to download Excel file. HTTP status code: " + response.getStatus());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error downloading Excel file. " + e.getMessage());
            return false;
        }
    }
}