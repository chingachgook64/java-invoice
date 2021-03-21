package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }
    
    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getTotal(), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
    	invoice.addProduct(new DairyProduct("Twaróg chudy", new BigDecimal("3.59")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }
    
    @Test
    public void testInvoiceHasNumber() {
    	int number = invoice.getNumber();
    	
    	Assert.assertTrue(number > 0);
   
    }
    
    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
    	int number1 = invoice.getNumber();
    	int number2 =  new Invoice().getNumber();
    	
    	Assert.assertNotEquals(number1, number2);
    }
    
    @Test
    public void testTheSameInvoiceHasTheSameNumber() {
    	Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }
    
    
    @Test 
    public void testInvoiceHasProductListDetails() {
    	
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        
        Assert.assertTrue(invoice.getProductListDetails().length() > 0);
    	
    }
    
    @Test 
    public void testInvoiceHasNumberInHeaderOfProductListDetail() {
    	
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        String number = String.valueOf(invoice.getNumber());
        
        Assert.assertTrue(invoice.getProductListDetails().contains(number));
    	
    }
    
    @Test 
    public void testInvoiceHasRightNumberOfLinesOfProductListDetail() {
    	
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        int numberOfProducts = invoice.getProducts().size();
        String[] lines = invoice.getProductListDetails().split("\n");
        
        //Two lines are reserved for header and summary 
        Assert.assertEquals(numberOfProducts, lines.length-2);
    	
    }
    
    @Test 
    public void testInvoiceProductListDetailHasProductsNames() {
    	
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        Map <Product, Integer> products = invoice.getProducts();
        
        for (Map.Entry<Product, Integer> entry: products.entrySet()) {
			
          	 Assert.assertTrue(invoice.getProductListDetails()
          			 .contains(entry.getKey().getName()));
   		
   		}
    	
    }
    
    @Test 
    public void testInvoiceProductListDetailHasProductsQuantities() {
    	
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        Map <Product, Integer> products = invoice.getProducts();

        
        for (Map.Entry<Product, Integer> entry: products.entrySet()) {
			
       	 Assert.assertTrue(invoice.getProductListDetails()
       			 .contains(entry.getValue() + " sztuk"));
		
		}
    	
    }
    
    @Test 
    public void testInvoiceProductListDetailHasProductsPrices() {
    	
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        Map <Product, Integer> products = invoice.getProducts();
        
        
        for (Map.Entry<Product, Integer> entry: products.entrySet()) {
			
        	 Assert.assertTrue(invoice.getProductListDetails()
        			 .contains(String.valueOf(entry.getKey().getPrice()
        					 .multiply(BigDecimal.valueOf(entry.getValue())))));
		
		}
    	
    }
    
    @Test 
    public void testInvoiceProductListDetailHasProperSummary() {
    	
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        Map <Product, Integer> products = invoice.getProducts();
        
        int totalNumberOfProducts = 0;
        
        for (Map.Entry<Product, Integer> entry: products.entrySet()) {
			
        	totalNumberOfProducts += entry.getValue();
		}
        
        Assert.assertTrue(invoice.getProductListDetails().contains("Liczba pozycji: " + totalNumberOfProducts));
        
        
     
    	
    }
    
    
    
    
//    
//    @Test 
//    public void testInvoiceHasNoDuplicatedProductsInProductList() {
//    	
//    	
//    }
//    
//    @Test 
//    public void testInvoiceHasProperValueOfExciseForManyProducts() {
//    	
//    }
//    
//    @Test 
//    public void testInvoiceHasNoExciseForManyProductsOnTransportDay() {
//    	
//    }
    
    
    
    

}
