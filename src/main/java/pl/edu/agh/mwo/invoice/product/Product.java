package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
    	if(name == null || name.equals("")) {
    		throw new IllegalArgumentException("Name can't be null or empty");
    	}
        this.name = name;
        
        if(price == null || (price.compareTo(BigDecimal.ZERO) < 0)) {
        	
        	throw new IllegalArgumentException("Price can't be null or lower than 0");
        }
        
        this.price = price;
        this.taxPercent = tax;
    }

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getTaxPercent() {
		return taxPercent;
	}


   public BigDecimal getPriceWithTax() {
        return price.add((price.multiply(taxPercent)));
    }
    
    
    
}
