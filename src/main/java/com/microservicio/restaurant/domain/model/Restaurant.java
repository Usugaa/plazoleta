package com.microservicio.restaurant.domain.model;

public class Restaurant {

    private Long id;
    private String nameRestaurant;
    private String address;
    private String phone;
    private String urlLogo;
    private String nit;
    private Long idOwner;

    public Restaurant(Long id, String nameRestaurant, String address, String phone, String urlLogo, String nit, Long idOwner) {
        this.id = id;
        this.nameRestaurant = nameRestaurant;
        this.address = address;
        this.phone = phone;
        this.urlLogo = urlLogo;
        this.nit = nit;
        this.idOwner = idOwner;
    }

    public Restaurant(){

    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", nameRestaurant='" + nameRestaurant + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", urlLogo='" + urlLogo + '\'' +
                ", nit='" + nit + '\'' +
                ", idOwner=" + idOwner +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }
}
