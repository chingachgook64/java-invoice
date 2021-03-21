package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.hamcrest.Matchers;
import org.hamcrest.integration.JMock1Adapter;
import org.junit.Assert;
import org.junit.Test;

public class ProductTest {
    @Test
    public void testProductNameIsCorrect() {
        Product product = new OtherProduct("buty", new BigDecimal("100.0"));
        Assert.assertEquals("buty", product.getName());
    }

    @Test
    public void testProductPriceAndTaxWithDefaultTax() {
        Product product = new OtherProduct("Ogorki", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.23"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testProductPriceAndTaxWithDairyProduct() {
        Product product = new DairyProduct("Szarlotka", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.08"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testPriceWithTax() {
        Product product = new DairyProduct("Oscypek", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("108"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullName() {
        new OtherProduct(null, new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithEmptyName() {
        new TaxFreeProduct("", new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullPrice() {
        new DairyProduct("Banany", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNegativePrice() {
        new TaxFreeProduct("Mandarynki", new BigDecimal("-1.00"));
    }
    

    @Test
    public void testPriceWithTaxAndExcise() {
        Product product1 = new BottleOfWine("Chateau Bordeaux ", new BigDecimal("26.0"));
        Product product2 = new FuelCanister("5l Petrol ", new BigDecimal("24.50"));
       
        Assert.assertThat(new BigDecimal("38.82"), Matchers.comparesEqualTo(product1.getPriceWithTax()));
        Assert.assertThat(new BigDecimal("36.98"), Matchers.comparesEqualTo(product2.getPriceWithTax()));
    }
  

//    Działa jak zmienię datę, ale poległem na temacie zmockowania daty systemowej :(
//  @Test 
//  public void testInvoiceHasNoExciseForManyProductsOnTransportDay() {
//	  
//	  Product product = new FuelCanister("5l Petrol ", new BigDecimal("24.50"));
//	  Assert.assertThat(new BigDecimal("30.14"), Matchers.comparesEqualTo(product.getPriceWithTax()));
//  }
}
