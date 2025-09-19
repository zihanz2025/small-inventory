package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.repositories.ShopRepository;
import com.zihan.small_inventory.utils.JwtUtil;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final ShopRepository shopRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(ShopRepository shopRepository, JwtUtil jwtUtil) {
        this.shopRepository = shopRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public ResponseUtil<Map<String, Object>> login(String shopId, String rawPassword) {
        Optional<Shop> shopOpt = shopRepository.findByShopId(shopId);
        if (shopOpt.isEmpty()) {
            return new ResponseUtil<>("Shop ID not found");
        }

        Shop shop = shopOpt.get();
        if (!passwordEncoder.matches(rawPassword, shop.getOwnerPassword())) {
            return new ResponseUtil<>("Incorrect password");
        }

        String token = jwtUtil.generateToken(shopId);

        Map<String, Object> data = new HashMap<>();
        data.put("shop", shop);
        data.put("token", token);

        return new ResponseUtil<>(data);
    }
}
