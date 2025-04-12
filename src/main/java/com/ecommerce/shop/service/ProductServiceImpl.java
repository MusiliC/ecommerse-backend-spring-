package com.ecommerce.shop.service;


import com.ecommerce.shop.exceptions.APIException;
import com.ecommerce.shop.exceptions.ResourceNotFoundException;
import com.ecommerce.shop.models.Category;
import com.ecommerce.shop.models.Product;
import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.payload.ProductResponse;
import com.ecommerce.shop.repository.CategoryRepository;
import com.ecommerce.shop.repository.ProductRepository;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductServiceI {


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;


    @Override
    public ProductDto addProduct(ProductDto productDtoReq, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = modelMapper.map(productDtoReq, Product.class);

        product.setCategory(category);
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setImage("Default.png");
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);

        ProductDto productDto = modelMapper.map(savedProduct, ProductDto.class);

        return productDto;
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            throw new APIException("No Products found");
        }

        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));


        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        if (products.isEmpty()) {
            throw new APIException("No Products found");
        }

        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');

        if (products.isEmpty()) {
            throw new APIException("No Products found");
        }

        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDtoReq) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", id));

        existingProduct.setProductName(productDtoReq.getProductName());
        existingProduct.setDescription(productDtoReq.getDescription());
        existingProduct.setQuantity(productDtoReq.getQuantity());
        existingProduct.setDiscount(productDtoReq.getDiscount());
        existingProduct.setPrice(productDtoReq.getPrice());
        existingProduct.setSpecialPrice(productDtoReq.getPrice() - ((productDtoReq.getDiscount() * 0.01) * productDtoReq.getPrice()));

        Product updatedProduct = productRepository.save(existingProduct);
        ProductDto productDto = modelMapper.map(updatedProduct, ProductDto.class);
        return productDto;
    }

    @Override
    public String deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", id));

        productRepository.delete(existingProduct);
       String message = "Product deleted successfully";
        return message;
    }
}
