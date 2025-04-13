package com.ecommerce.shop.service;


import com.ecommerce.shop.exceptions.APIException;
import com.ecommerce.shop.exceptions.ResourceNotFoundException;
import com.ecommerce.shop.models.Category;
import com.ecommerce.shop.models.Product;
import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.payload.ProductResponse;
import com.ecommerce.shop.repository.CategoryRepository;
import com.ecommerce.shop.repository.ProductRepository;
import java.io.IOException;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductServiceI {


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;


    @Override
    public ProductDto addProduct(ProductDto productDtoReq, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean ifProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductName().equalsIgnoreCase(productDtoReq.getProductName())) {
                ifProductNotPresent = false;
                break;
            }
        }

        if (ifProductNotPresent) {

            Product product = modelMapper.map(productDtoReq, Product.class);

            product.setCategory(category);
            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setImage("Default.png");
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);

            ProductDto productDto = modelMapper.map(savedProduct, ProductDto.class);

            return productDto;
        } else {
            throw new APIException("Product already exists!");
        }
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();

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

    @Override
    public ProductDto uploadProductImage(Long productId, MultipartFile image) throws IOException {
//        Get product
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//        Upload image
//        Get file name of uploaded image

        String fileName = fileService.uploadImage(path, image);
//        Update new file name to the product
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDB);
//        return DTO
        return modelMapper.map(updatedProduct, ProductDto.class);

    }


}
