package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import pl.edu.agh.mwo.invoice.product.BottleOfWine;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class Invoice {

	private final int number = Math.abs(new Random().nextInt());
	private Map <Product, Integer> products = new LinkedHashMap<>();

   public static void main(String[] args) {
	
	  Invoice invoice = new Invoice();
	  invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
	  invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
      invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
      invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
      invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")), 4);
      invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")), 3);
      invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
      invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")));
      invoice.addProduct(new BottleOfWine("Winiacz", new BigDecimal("26.55"))); 
	   System.out.println(invoice.getProductListDetails());
   }

	public void addProduct(Product product) {
    	
    	if(product == null ||  product.equals("")) {
    		
    		throw new IllegalArgumentException("Product can't be null or empty");
    	}
    	
    	
		for (Map.Entry<Product, Integer> entry: products.entrySet()) {
								
			if(entry.getKey().getName().equalsIgnoreCase(product.getName())) {
				
				entry.setValue((entry.getValue() + 1));
				
				return;
			}										            	
		}
    	
        this.products.put(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
    	
    	if(product == null ||  product.equals("") || quantity == null || quantity <= 0) {
    		
    		throw new IllegalArgumentException("Product can't be null or empty. Also quantity cant't be null and must be greather than 0");
    	}
    	
    	for (Map.Entry<Product, Integer> entry: products.entrySet()) {
			
			if(entry.getKey().getName().equalsIgnoreCase(product.getName())) {
				
				entry.setValue((entry.getValue() + quantity));
				
				return;
			}										            	
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

	public int getNumber() {
		
		return number;
	}
	
	 public Map<Product, Integer> getProducts() {
			return products;
		}
	
	public String getProductListDetails () {
		
		String productListDetails = "Faktura numer: ";
		int totalNumberOfProducts = 0;
		
		productListDetails +=  String.valueOf(this.getNumber()) + "\n";
		
		for (Map.Entry<Product, Integer> entry: products.entrySet()) {
			
			productListDetails += entry.getKey().getName() + " " + entry.getValue() + " sztuk " 
			+ (entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue()))) + "\n";
			
			totalNumberOfProducts += entry.getValue();
		}
		
		productListDetails += "Liczba pozycji: " + String.valueOf(totalNumberOfProducts);
		
		return productListDetails;
	}

}
