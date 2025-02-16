package com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response;

import com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response.MajorNoticeResponseDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FastAPIEmploymentNoticeResponseListDTO {
    private List<EmploymentNoticeResponseDTO> notices;
}
