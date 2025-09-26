package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.constants.ResponseCode;
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

    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PASSWORD = "123guesswhat";

    public AuthService(ShopRepository shopRepository, JwtUtil jwtUtil) {
        this.shopRepository = shopRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public ResponseUtil<Shop> registerShop(Shop shop) {

        if (shop.getShopId() == null || shop.getShopId().isBlank() || shop.getShopId().length() <8) {
            return new ResponseUtil<>("Shop ID must be at least 8 characters.", ResponseCode.REGISTRATION_FAILED);
        }

        if (shop.getOwnerPassword() == null || shop.getOwnerPassword().length() < 8) {
            return new ResponseUtil<>("Password must be at least 8 characters.", ResponseCode.REGISTRATION_FAILED);
        }

        if (shopRepository.shopIdExists(shop.getShopId())) {
            return new ResponseUtil<>("Shop ID already exists.", ResponseCode.REGISTRATION_FAILED);
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
            return new ResponseUtil<>("Shop ID not found", ResponseCode.LOGIN_FAILED);
        }

        Shop shop = shopOpt.get();
        if (!passwordEncoder.matches(rawPassword, shop.getOwnerPassword())) {
            return new ResponseUtil<>("Incorrect password", ResponseCode.LOGIN_FAILED);
        }

        String token = jwtUtil.generateToken(shopId,"SHOP_OWNER");

        Map<String, Object> data = new HashMap<>();
        data.put("shopId", shop.getShopId());
        data.put("token", token);

        return new ResponseUtil<>(data);
    }

    public ResponseUtil<Map<String, Object>> loginAdmin(String adminId, String adminPassword){

        if (!adminId.equals(ADMIN_ID) || !adminPassword.equals(ADMIN_PASSWORD)){
            return new ResponseUtil<>("Invalid admin credentials", ResponseCode.LOGIN_FAILED);
        }else{
            String token = jwtUtil.generateToken(ADMIN_ID, "ADMIN");

            Map<String, Object> data = new HashMap<>();
            data.put("adminId", ADMIN_ID);
            data.put("token", token);

            return new ResponseUtil<>(data);
        }

    }

}
