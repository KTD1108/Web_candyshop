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
// xử lý logic nghiệp vụ liên quan đến giỏ hàng của người dùng.
public class CartService {
	private final CartRepository cartRepo;
	private final CartItemRepository cartItemRepo;
	private final ProductRepository productRepo;

	// Đảm bảo rằng một người dùng có một giỏ hàng. Nếu chưa có, tạo mới.
	public Cart ensureCart(User user) {
		return cartRepo.findByUser(user).orElseGet(() -> {
			Cart c = new Cart();
			c.setUser(user);
			return cartRepo.save(c);
		});
	}

	// Thêm một sản phẩm vào giỏ hàng hoặc tăng số lượng nếu đã tồn tại.
	@Transactional
	public void addItem(User user, AddCartItemRequest req) {
		Cart cart = ensureCart(user);
		Product p = productRepo.findById(req.getProductId())
				.orElseThrow(() -> new RuntimeException("Product not found"));

		// Tìm kiếm sản phẩm trong giỏ hàng.
		CartItem item = cartItemRepo.findByCartAndProduct(cart, p).orElse(null);

		// Nếu sản phẩm chưa có trong giỏ hàng, tạo mới.
		if (item == null) {
			item = new CartItem();
			item.setCart(cart);
			item.setProduct(p);
			item.setQuantity(Math.max(1, req.getQuantity() == null ? 1 : req.getQuantity()));
		} else {
			// Nếu đã có, tăng số lượng.
			item.setQuantity(item.getQuantity() + Math.max(1, req.getQuantity() == null ? 1 : req.getQuantity()));
		}
		cartItemRepo.save(item);
	}

	// Xóa một mục khỏi giỏ hàng.
	@Transactional
	public void deleteItem(Long cartItemId, User user) {
		Cart cart = ensureCart(user);
		CartItem item = cartItemRepo.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));

		// Đảm bảo rằng mục này thuộc về giỏ hàng của người dùng trước khi xóa.
		if (!item.getCart().getId().equals(cart.getId())) {
			throw new SecurityException("Access denied to delete this cart item");
		}

		cartItemRepo.delete(item);
	}

	// Cập nhật số lượng của một mục trong giỏ hàng.
	@Transactional
	public CartItem updateCartItemQuantity(Long cartItemId, int quantity, User user) {
		Cart cart = ensureCart(user);
		CartItem cartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> new RuntimeException("Sản phẩm không tìm thấy trong giỏ hàng"));

		// Đảm bảo rằng mục này thuộc về giỏ hàng của người dùng.
		if (!cartItem.getCart().getId().equals(cart.getId())) {
			throw new SecurityException("Bạn không có quyền thay đổi mục này");
		}

		Product product = cartItem.getProduct();
		// Kiểm tra xem số lượng yêu cầu có vượt quá số lượng tồn kho không.
		if (quantity > product.getStockQty()) {
			throw new IllegalArgumentException("Số lượng yêu cầu vượt quá số lượng tồn kho");
		}

		// Nếu số lượng nhỏ hơn hoặc bằng 0, xóa mục khỏi giỏ hàng.
		if (quantity <= 0) {
			// Do có `orphanRemoval=true` trong danh sách `cartItems` của `Cart`,
			// việc xóa khỏi collection của đối tượng cha là đủ để xóa khỏi DB.
			cart.getCartItems().remove(cartItem);
			return null; // Trả về null để báo hiệu đã xóa
		} else {
			// Nếu không, cập nhật số lượng.
			cartItem.setQuantity(quantity);
			return cartItemRepo.save(cartItem);
		}
	}
}
