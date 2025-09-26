package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.repositories.ShopRepository;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PreAuthorize("#shopId == authentication.principal.shopId or hasRole('ADMIN')")
    public ResponseUtil<Shop> getShop(String shopId) {
        var shopOpt = shopRepository.findByShopId(shopId);
        if (shopOpt.isEmpty()) return new ResponseUtil<>("Shop not found", 501);

        Shop shop = shopOpt.get();
        return new ResponseUtil<>(shop);
    }

    @PreAuthorize("#shopId == authentication.principal.shopId")
    public ResponseUtil<Shop> updateShopName(String shopId, String newName) {
        var shopOpt = shopRepository.findByShopId(shopId);
        if (shopOpt.isEmpty()) return new ResponseUtil<>("Shop not found", 502);

        Shop shop = shopOpt.get();
        shop.setShopName(newName);
        shopRepository.save(shop);

        return new ResponseUtil<>(shop);
    }

    @PreAuthorize("#shopId == authentication.principal.shopId")
    public ResponseUtil<Shop> updatePassword(String shopId, String oldPassword, String newPassword) {
        var shopOpt = shopRepository.findByShopId(shopId);
        if (shopOpt.isEmpty()) return new ResponseUtil<>("Shop not found.", 503);

        Shop shop = shopOpt.get();

        if (!passwordEncoder.matches(oldPassword, shop.getOwnerPassword())) {
            return new ResponseUtil<>("Old password does not match.", 504);
        }
        if (newPassword == null || newPassword.length() < 8) {
            return new ResponseUtil<>("Password must be at least 8 characters.", 505);
        }
        shop.setOwnerPassword(passwordEncoder.encode(newPassword));
        shopRepository.save(shop);

        return new ResponseUtil<>(shop);
    }

    @PreAuthorize("#shopId == authentication.principal.shopId")
    public ResponseUtil<String> deleteOwnShop(String shopId, String password) {
        Optional<Shop> existing = shopRepository.findByShopId(shopId);
        if (existing.isEmpty()) {
            return new ResponseUtil<>("Shop not found.", 506);
        }
        if (!passwordEncoder.matches(password, existing.get().getOwnerPassword())) {
            return new ResponseUtil<>("Old password does not match.", 507);
        }
        shopRepository.delete(shopId);
        return new ResponseUtil<>("Shop deleted successfully.", 200);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseUtil<String> deleteShop(String shopId) {
        Optional<Shop> existing = shopRepository.findByShopId(shopId);
        if (existing.isEmpty()) {
            return new ResponseUtil<>("Shop not found.", 506);
        }
        shopRepository.delete(shopId);
        return new ResponseUtil<>("Shop deleted successfully.", 200);
    }
}
