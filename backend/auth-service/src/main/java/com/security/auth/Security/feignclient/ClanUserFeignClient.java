package com.security.auth.Security.feignclient;

import com.security.auth.Security.feigndto.ClanUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value="user-service", url="http://user-service:8080")
public interface ClanUserFeignClient {
    @GetMapping("/clanusers/search")
    ResponseEntity<ClanUserDTO> getClanUserIfExists(@RequestParam Long userId);
}
