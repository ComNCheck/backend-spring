package com.ComNCheck.ComNCheck.domain.testController;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @GetMapping("/my")
    @Operation(summary = "CORS 에러 체크", description = "CORS 여부를 따지기 위한 API 였으므로, 신경 안써도됨 삭제 예정")
    @ResponseBody
    public String myAPI() {
        return "my route";
    }


}
