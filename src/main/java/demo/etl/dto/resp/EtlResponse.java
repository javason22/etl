package demo.etl.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EtlResponse {

    private String status;
    private String message;
}
