package app.core.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "coupons")
public class Coupon {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn
	private Company company;
	private int amount;
	private String title, description, image;
	private LocalDateTime startDate, endDate;
	private double price;
	@ManyToMany (mappedBy = "coupons", cascade = CascadeType.REMOVE)
	private List<Customer> customers = new ArrayList<Customer>();
	private Category category;

	public enum Category {

		FOOD, ELECTRICITY, RESTAURANT, VACATION, GARMENT, BOOK;
	}


//	CTORS
	/**
	 * @param id
	 * @param company
	 * @param amount
	 * @param title
	 * @param description
	 * @param image
	 * @param startDate
	 * @param endDate
	 * @param price
	 * @param category
	 */
	public Coupon(int id, Company company, int amount, String title,String description, String image, LocalDateTime startDate,
			LocalDateTime endDate, double price, Category category) {
		this.id = id;
		this.company = company;
		this.amount = amount;
		this.title = title;
		this.description = description;
		this.image = image;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.category = category;
	}
	
	public Coupon(int id) {
		this.id = id;
	}

	public Coupon() {}

	//getters and setters
	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setiD(int iD) {
		this.id = iD;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Company getCompany() {
		return company;
	}

	public int getAmount() {
		return amount;
	}

	public String getTitle() {
		return title;
	}

	public String getImage() {
		return image;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public double getPrice() {
		return price;
	}

	public Category getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return "Coupon{" +
				"id=" + id +
				", company=" + company +
				", amount=" + amount +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", image='" + image + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", price=" + price +
				", category=" + category +
				'}';
	}


}
