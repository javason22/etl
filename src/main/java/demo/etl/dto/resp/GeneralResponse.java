package demo.etl.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "GeneralResponse", description = "General response object")
public class GeneralResponse {

    @Schema(description = "Status of the response", example = "404 NOT FOUND")
    private String status;
    @Schema(description = "Message of the response", example = "No data found")
    private String message;
}
