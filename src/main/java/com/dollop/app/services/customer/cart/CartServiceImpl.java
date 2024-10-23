package com.dollop.app.services.customer.cart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dollop.app.dto.AddProductInCartDto;
import com.dollop.app.dto.CartItemsDto;
import com.dollop.app.dto.OrderDto;
import com.dollop.app.dto.PlaceOrderDto;
import com.dollop.app.entity.CartItems;
import com.dollop.app.entity.Coupon;
import com.dollop.app.entity.Order;
import com.dollop.app.entity.Product;
import com.dollop.app.entity.User;
import com.dollop.app.enums.OrderStatus;
import com.dollop.app.exceptions.ValidationException;
import com.dollop.app.repository.CartItemsRepository;
import com.dollop.app.repository.CouponRepository;
import com.dollop.app.repository.OrderRepository;
import com.dollop.app.repository.ProductRepository;
import com.dollop.app.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CouponRepository couponRepository;

	@Override
	public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
	    // Find active order for the user
	    Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

	    // If no active order found, create a new order
	    if (activeOrder == null) {
	        activeOrder = new Order();
	        activeOrder.setUser(userRepository.findById(addProductInCartDto.getUserId()).orElse(null));
	        activeOrder.setOrderStatus(OrderStatus.Pending);
	        activeOrder.setTotalAmount(0L);
	        activeOrder.setAmount(0L);
	        activeOrder.setCartItems(new ArrayList<>());
	        activeOrder = orderRepository.save(activeOrder);
	    }

	    // Check if the product is already in the cart
	    Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
	            addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

	    if (optionalCartItems.isPresent()) {
	        CartItems cart = optionalCartItems.get();
	        // Increase the quantity of the existing product in the cart
	        cart.setQuantity(cart.getQuantity() + 1L);
	        cartItemsRepository.save(cart);

	        // Update total amount and amount in the order
	        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
	        activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
	        orderRepository.save(activeOrder);

	        return ResponseEntity.status(HttpStatus.OK).body(cart.getId());
	    } else {
	        // Find the product to add to the cart
	        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
	        if (optionalProduct.isPresent()) {
	            Product product = optionalProduct.get();

	            // Create a new cart item
	            CartItems cart = new CartItems();
	            cart.setProduct(product);
	            cart.setPrice(product.getPrice());
	            cart.setQuantity(1L);
	            cart.setUser(activeOrder.getUser());
	            cart.setOrder(activeOrder);

	            // Update total amount and amount in the order
	            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
	            activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
	            activeOrder.getCartItems().add(cart);

	            // Save cart item and update order
	            @SuppressWarnings("unused")
				CartItems updatedCart = cartItemsRepository.save(cart);
	            orderRepository.save(activeOrder);

	            return ResponseEntity.status(HttpStatus.CREATED).body(cart.getId());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
	        }
	    }
	}


	public OrderDto getCartByUserId(Long userId) {
		Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

		List<CartItemsDto> cartItemsDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto)
				.collect(Collectors.toList());

		OrderDto orderDto = new OrderDto();
		orderDto.setAmount(activeOrder.getAmount());
		orderDto.setId(activeOrder.getId());
		orderDto.setOrderStatus(activeOrder.getOrderStatus());
		orderDto.setDiscount(activeOrder.getDiscount());
		orderDto.setTotalAmount(activeOrder.getTotalAmount());
		orderDto.setCartItems(cartItemsDtoList);
		
		if(activeOrder.getCoupon() != null) {
			orderDto.setCouponName(activeOrder.getCoupon().getName());
		}

		return orderDto;
	}

	@Override
	public OrderDto applyCoupon(Long userId, String code) {
		Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
		
		Coupon coupon = couponRepository.findByCode(code).orElseThrow(()-> new ValidationException("Coupon not found !!"));
		
		if(couponIsExpired(coupon)) {
			throw new ValidationException("Coupon has Expired !!");
		}
		
		double discountAmount = ((coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount());
		double netAmount = activeOrder.getTotalAmount() - discountAmount;
		
		activeOrder.setAmount((long)netAmount);
		activeOrder.setDiscount((long)discountAmount);
		activeOrder.setCoupon(coupon);
		
		orderRepository.save(activeOrder);
		return activeOrder.getOrderDto();
	}
	
	private boolean couponIsExpired(Coupon coupon) {
		Date currentDate = new Date();
		Date expirationDate = coupon.getExpirationDate();
		
		return expirationDate != null && currentDate.after(expirationDate);
	}
	
	public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto)
	{
		Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
		Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
		
		Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
				addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
		
		if(optionalProduct.isPresent() && optionalCartItem.isPresent()) {
			CartItems cartItems = optionalCartItem.get();
			Product product = optionalProduct.get();
			
			activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
			activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());
			
			cartItems.setQuantity(cartItems.getQuantity() + 1);
			
			if(activeOrder.getCoupon() != null) {
				double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());
				double netAmount = activeOrder.getTotalAmount() - discountAmount;
				
				activeOrder.setAmount((long)netAmount);
				activeOrder.setDiscount((long)discountAmount);
			}
			
			cartItemsRepository.save(cartItems);
			orderRepository.save(activeOrder);
			return activeOrder.getOrderDto();
		}
		
		return null;
	}

	@Override
	public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
		
		Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
		Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
		
		Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
				addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
		
		if(optionalProduct.isPresent() && optionalCartItem.isPresent()) {
			CartItems cartItems = optionalCartItem.get();
			Product product = optionalProduct.get();
			
			activeOrder.setAmount(activeOrder.getAmount() - product.getPrice());
			activeOrder.setTotalAmount(activeOrder.getTotalAmount() - product.getPrice());
			
			cartItems.setQuantity(cartItems.getQuantity() - 1);
			
			if(activeOrder.getCoupon() != null) {
				double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());
				double netAmount = activeOrder.getTotalAmount() - discountAmount;
				
				activeOrder.setAmount((long)netAmount);
				activeOrder.setDiscount((long)discountAmount);
			}
			
			cartItemsRepository.save(cartItems);
			orderRepository.save(activeOrder);
			return activeOrder.getOrderDto();
		}
		
		return null;
	}

	@Override
	public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
		
	    Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
	    Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());
	    
	    if (activeOrder != null && optionalUser.isPresent()) {
	        activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
	        activeOrder.setAddress(placeOrderDto.getAddress());
	        activeOrder.setDate(new Date());
	        activeOrder.setOrderStatus(OrderStatus.Placed);
	        activeOrder.setTrackingId(UUID.randomUUID());
	        
	        orderRepository.save(activeOrder);
	        
	        Order newOrder = new Order();
	        newOrder.setAmount(0L);
	        newOrder.setTotalAmount(0L);
	        newOrder.setDiscount(0L);
	        newOrder.setUser(optionalUser.get());
	        newOrder.setOrderStatus(OrderStatus.Pending);
	        orderRepository.save(newOrder);
	        
	        return activeOrder.getOrderDto();
	    }
	    return null;
	}


	@Override
	public List<OrderDto> getAllPlacedOrders(Long userId) {
		// TODO Auto-generated method stub
		return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Shipped,
				OrderStatus.Delivered)).stream().map(Order::getOrderDto).collect(Collectors.toList());
	}


	@Override
	public OrderDto searchOrderByTrackingId(UUID trackingId) {
		// TODO Auto-generated method stub
		Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
		if(optionalOrder.isPresent()) {
			return optionalOrder.get().getOrderDto();
		}
 		return null;
	}

}
