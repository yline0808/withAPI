package net.ddns.yline.withAPI.controller.success;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/success-controller")
public class SuccessController {

    @GetMapping
    public ResponseEntity<String> success() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
