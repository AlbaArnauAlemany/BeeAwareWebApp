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

    public byte[] downloadExcelFile(Long beezzerId) throws IOException {
        try {
            Response response = excelTarget
                    .path("download/" + beezzerId)
                    .request("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                InputStream inputStream = response.readEntity(InputStream.class);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                return byteArrayOutputStream.toByteArray();
            } else {
                throw new IOException("Failed to dowmload Excel file. " + response.getStatus());
            }
        } catch (Exception e) {
            throw new IOException("Error downloading the Excel file. " + e.getMessage());

        }
    }
}