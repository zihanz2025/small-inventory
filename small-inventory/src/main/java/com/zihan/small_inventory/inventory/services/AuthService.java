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

    public ResponseUtil<Shop> registerShop(Shop shop) {

        if (shop.getShopId() == null || shop.getShopId().isBlank() || shop.getShopId().length() <8) {
            return new ResponseUtil<>("Shop ID must be at least 8 characters.", 401);
        }

        if (shop.getOwnerPassword() == null || shop.getOwnerPassword().length() < 8) {
            return new ResponseUtil<>("Password must be at least 8 characters.", 402);
        }

        if (shopRepository.shopIdExists(shop.getShopId())) {
            return new ResponseUtil<>("Shop ID already exists.", 403);
        }
        // hash the raw password and replace it
        String hashedPassword = passwordEncoder.encode(shop.getOwnerPassword());
        shop.setOwnerPassword(hashedPassword);

        Shop newShop = Shop.newShop(
                shop.getShopId(),
                shop.getShopName(),
                shop.getOwnerEmail(),
                shop.getOwnerPassword()
        );

        Shop savedShop = shopRepository.save(newShop);
        return new ResponseUtil<>(savedShop);
    }

    public ResponseUtil<Map<String, Object>> loginShop(String shopId, String rawPassword) {
        Optional<Shop> shopOpt = shopRepository.findByShopId(shopId);
        if (shopOpt.isEmpty()) {
            return new ResponseUtil<>("Shop ID not found", 404);
        }

        Shop shop = shopOpt.get();
        if (!passwordEncoder.matches(rawPassword, shop.getOwnerPassword())) {
            return new ResponseUtil<>("Incorrect password", 405);
        }

        String token = jwtUtil.generateToken(shopId);

        Map<String, Object> data = new HashMap<>();
        data.put("shopId", shop.getShopId());
        data.put("token", token);

        return new ResponseUtil<>(data);
    }
}
