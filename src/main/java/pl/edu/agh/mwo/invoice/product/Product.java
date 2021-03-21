package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Product {
    private final String name;
    private final BigDecimal price;
    private static final BigDecimal exciseRate = new BigDecimal(5.56);

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
    	if(name == null || name.equals("")) {
    		throw new IllegalArgumentException("Name can't be null or empty");
    	}
        this.name = name;
        
        if(price == null || (price.compareTo(BigDecimal.ZERO) < 0)) {
        	
        	throw new IllegalArgumentException("Price can't be null or lower than 0");
        }
        
       	
        this.taxPercent = tax;
       
        //Cała operacja dzieje się tutaj, ponieważ według wykładni jaką znalazłem akcyza jest częścią ceny,
        // a do ceny doliczamy VAT. 
        GregorianCalendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        String cargoShipFestivalDate = day + "-" + month;
        
        if(this instanceof BottleOfWine) {
        	this.price = (price.add(exciseRate)).setScale(2, RoundingMode.CEILING);
        	return;
        }else if((this instanceof FuelCanister)) {
        	if (!cargoShipFestivalDate.equals("26-3")) {
        		
        		this.price = (price.add(exciseRate)).setScale(2, RoundingMode.CEILING);
            	return;
        	}
        	
        }
        this.price = price;
   
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
        return (price.add((price.multiply(taxPercent))));
    }
    
    
    
}
