package vn.com.buscu.bean;

import java.io.Serializable;

/**
 * Product Bean
 *
 * @version 1.0
 * @author ToanNDD
 */
@SuppressWarnings("serial")
public class Product implements Serializable  {

    private String productCode;
    private String branchName;
    private String title;
    private Double price;
    private Double saleRank;
    private String image1;
    private String image2;

    public Product() {
    }

    public Product(String productCode, String branchName, String title, Double price, Double saleRank, String image1, String image2) {
        this.productCode = productCode;
        this.branchName = branchName;
        this.title = title;
        this.price = price;
        this.saleRank = saleRank;
        this.image1 = image1;
        this.image2 = image2;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName the branchName to set
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the saleRank
     */
    public Double getSaleRank() {
        return saleRank;
    }

    /**
     * @param saleRank the saleRank to set
     */
    public void setSaleRank(Double saleRank) {
        this.saleRank = saleRank;
    }

    /**
     * @return the image1
     */
    public String getImage1() {
        return image1;
    }

    /**
     * @param image1 the image1 to set
     */
    public void setImage1(String image1) {
        this.image1 = image1;
    }

    /**
     * @return the image2
     */
    public String getImage2() {
        return image2;
    }

    /**
     * @param image2 the image2 to set
     */
    public void setImage2(String image2) {
        this.image2 = image2;
    }

    @Override
    public String toString() {
        return "product{" + "code=" + productCode + ", Full Name=" + branchName + " " + title + ", image2=" + image2 + '}';
    }
}
