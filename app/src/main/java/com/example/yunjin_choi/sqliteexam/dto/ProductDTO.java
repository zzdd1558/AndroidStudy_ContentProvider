package com.example.yunjin_choi.sqliteexam.dto;

public class ProductDTO {

    private int productID;
    private String productName;
    private int productQuantity;

    /* 기본 생성자 */
    public ProductDTO() { }

    /* Product Find시 사용할 생성자 */
    public ProductDTO(String productName, int productQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }

    /* Product 등록시 사용할 생성자 */
    public ProductDTO(int productID, String productName, int productQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.productQuantity = productQuantity;
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
