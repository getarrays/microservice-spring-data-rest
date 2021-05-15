package io.getarrays.webservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@SpringBootApplication
public class WebserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}

	@Bean
	CommandLineRunner run (ProductRepository productReposiroty, RepositoryRestConfiguration repositoryRestConfiguration) {
		return args -> {
			repositoryRestConfiguration.exposeIdsFor(Product.class);
			productReposiroty.save(new Product(null, "Lenovo", 100.00, true));
			productReposiroty.save(new Product(null, "Dell Latitude", 92.00, true));
			productReposiroty.save(new Product(null, "HP Pavilion", 150.00, true));
			productReposiroty.save(new Product(null, "Dell Precision", 500.90, false));
			productReposiroty.save(new Product(null, "Dell Inspiron", 110.40, true));
			productReposiroty.save(new Product(null, "MacBook Pro", 1000.99, true));
			productReposiroty.save(new Product(null, "iMac", 600.00, true));
			productReposiroty.findAll().forEach(product -> {
				System.out.println(product.getName());
			});
		};
	}

}

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
class Product {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private double price;
	private boolean isInStock;
}

@RepositoryRestResource
interface ProductRepository extends JpaRepository<Product, Long> {
	@RestResource(path = "/findByName")
	Page<Product> findByNameContains(@Param("productName") String name, Pageable pageable);
}

@Projection(name = "web", types = Product.class)
interface WebProjection {
	Long getId();
	String getName();
	double getPrice();
}

@Projection(name = "mobile", types = Product.class)
interface MobileProjection {
	Long getId();
	String getName();
}


/*@RestController
@AllArgsConstructor
class ProductResource {
	private final ProductRepository productRepository;

	@GetMapping(path = "/products")
	public List<Product> list(){
		return productRepository.findAll();
	}

	@GetMapping(path = "/products/{id}")
	public Product getProduct(@PathVariable Long id){
		return productRepository.findById(id).get();
	}

	@PostMapping(path = "/products")
	public Product getProduct(@RequestBody Product product){
		return productRepository.save(product);
	}

	@PutMapping(path = "/products/{id}")
	public Product updateProduct(@PathVariable Long id, @RequestBody Product product){
		product.setId(id);
		return productRepository.save(product);
	}

	@DeleteMapping(path = "/products/{id}")
	public void deleteProduct(@PathVariable Long id){
		productRepository.deleteById(id);
	}

}*/
