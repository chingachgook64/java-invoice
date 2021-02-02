package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {

	private Map <Product, Integer> products = new LinkedHashMap<>();

    public void addProduct(Product product) {
    	
    	if(product == null ||  product.equals("")) {
    		
    		throw new IllegalArgumentException("Product can't be null or empty");
    	}
    	
        this.products.put(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
    	
    	if(product == null ||  product.equals("") || quantity == null || quantity <= 0) {
    		
    		throw new IllegalArgumentException("Product can't be null or empty. Also quantity cant't be null and must be greather than 0");
    	}
        
    	for (int i = 0; i <= quantity; i++) {
    		
    		this.products.put(product, quantity);
    	}
    }

    public BigDecimal getSubtotal() {
        
    	BigDecimal subtotal = new BigDecimal("0");
    	
    	if (this.products != null) {
    		
    		for (Product product : this.products.keySet()) {
    			
    			Integer quantity = this.products.get(product);
    			
    			subtotal = subtotal.add(product.getPrice().multiply(new BigDecimal(quantity)));	
    		}
    	}
    	
    	return subtotal;
    }

    public BigDecimal getTax() {
        
    	BigDecimal totalTax = new BigDecimal("0");
    	
		if (this.products != null) {
			
			for (Product product : this.products.keySet()) {
				
				BigDecimal taxRate = product.getTaxPercent();
				BigDecimal itemTax = product.getPrice().multiply(taxRate);
			
				totalTax = totalTax.add(itemTax);		
			}
		}
    	
    	return totalTax;
    }

    public BigDecimal getTotal() {
    	
    	BigDecimal total = new BigDecimal("0");
    	
    	if (this.products != null) {
    		
    		for (Product product : this.products.keySet()) {
    			
    			Integer quantity = this.products.get(product);
    			
    			total = total.add(product.getPriceWithTax().multiply(new BigDecimal(quantity))); 				
    		}
    	}
   
    	return total;
    }
}
