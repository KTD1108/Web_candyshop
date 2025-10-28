package com.candyshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.candyshop.dto.AddCartItemRequest;
import com.candyshop.entity.Cart;
import com.candyshop.entity.CartItem;
import com.candyshop.entity.Product;
import com.candyshop.entity.User;
import com.candyshop.repository.CartItemRepository;
import com.candyshop.repository.CartRepository;
import com.candyshop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;

    public Cart ensureCart(User user){
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepo.save(c);
        });
    }

    @Transactional
    public void addItem(User user, AddCartItemRequest req){
        Cart cart = ensureCart(user);
        Product p = productRepo.findById(req.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Optimized item lookup
        CartItem item = cartItemRepo.findByCartAndProduct(cart, p).orElse(null);
        
        if(item == null){
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(p);
            item.setQuantity(Math.max(1, req.getQuantity()==null?1:req.getQuantity()));
        } else {
            item.setQuantity(item.getQuantity() + Math.max(1, req.getQuantity()==null?1:req.getQuantity()));
        }
        cartItemRepo.save(item);
    }

    @Transactional
    public void deleteItem(Long cartItemId, User user) {
        Cart cart = ensureCart(user);
        CartItem item = cartItemRepo.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Ensure the item belongs to the user's cart before deleting
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("Access denied to delete this cart item");
        }

        cartItemRepo.delete(item);
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity, User user) {
        Cart cart = ensureCart(user);
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tìm thấy trong giỏ hàng"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("Bạn không có quyền thay đổi mục này");
        }

        Product product = cartItem.getProduct();
        if (quantity > product.getStockQty()) {
            throw new IllegalArgumentException("Số lượng yêu cầu vượt quá số lượng tồn kho");
        }

        if (quantity <= 0) {
            // Because of orphanRemoval=true on the Cart's cartItems list,
            // simply removing the item from the parent's collection is enough to delete it.
            cart.getCartItems().remove(cartItem);
            return null; // Return null to indicate deletion
        } else {
            cartItem.setQuantity(quantity);
            return cartItemRepo.save(cartItem);
        }
    }
}
