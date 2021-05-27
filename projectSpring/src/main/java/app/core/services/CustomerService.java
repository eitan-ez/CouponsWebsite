package app.core.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import app.core.exceptions.ServiceException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;

@Service("customerService")
@Transactional
@Scope("prototype")
public class CustomerService extends ClientService {

	/**
	 * An empty constructor.
	 */
	public CustomerService() {
	}

	/**
	 * Logs in a customer user.
	 */
	@Override
	public boolean login(String email, String password) {

		if (custRep.existsCustomerByEmailAndPassword(email, password))
			return true;

		return false;
	}

	/**
	 * Adds a new purchase to the database
	 */
	public void purchaseCoupon(int id, int couponId) throws ServiceException {

		Optional<Coupon> optCoupon = couRep.findById(couponId);

		if (optCoupon.isEmpty())
			throw new ServiceException("A coupon with this id does not exist. ");
		Coupon coupon = optCoupon.get();
		List<Integer> couponsId = couRep.findAllByCustomers(id);

		if (coupon.getAmount() < 1) {
			throw new ServiceException("The coupon you are trying to buy has run out of stock. ");
		}

		if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
			throw new ServiceException("The coupon you are trying to buy has expired. ");
		}

		if (couponsId.contains(couponId)) {
			throw new ServiceException("You already owns this coupon. ");
		}

		Customer customer = getCustomer(id);

		coupon.setAmount(coupon.getAmount() - 1);
		customer.addCoupon(coupon);

		custRep.flush();
	}

	/**
	 * @return the coupons of the customer. will return empty list in case of no
	 *         coupons.
	 */
	public ArrayList<Coupon> getCoupons(int id) {

		ArrayList<Integer> couponsID = new ArrayList<Integer>(couRep.findAllByCustomers(id));
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();

		for (Integer couponId : couponsID) {

			Optional<Coupon> opt = couRep.findById(couponId);
			Coupon coupon = opt.get();
			coupons.add(coupon);
		}
		return coupons;
	}

	/**
	 * @return the coupons of the customer that match the given category. will
	 *         return empty list in case of no coupons.
	 */
	public ArrayList<Coupon> getCouponsByCategory(int id, Category category) {

		ArrayList<Coupon> coupons = getCoupons(id);
		Iterator<Coupon> it = coupons.iterator();

		while (it.hasNext())
			if (it.next().getCategory() != category)
				it.remove();

		return coupons;
	}

	/**
	 * @return the coupons of the customer that under maxPrice. will return empty
	 *         list in case of no coupons.
	 */
	public ArrayList<Coupon> getCouponsByMaxPrice(int id, double maxPrice) {

		ArrayList<Coupon> coupons = getCoupons(id);
		Iterator<Coupon> it = coupons.iterator();

		while (it.hasNext())
			if (it.next().getPrice() >= maxPrice)
				it.remove();

		return coupons;
	}

	/**
	 * @return all details about this customer from the database.
	 */
	public Customer getCustomerDetails(int id) throws ServiceException {
		return getCustomer(id);
	}

	public int getCustomerIdFromDB(String email, String password) {
		return custRep.findCustomerByEmailAndPassword(email, password).getId();
	}

	/**
	 * @return the customer as an object using it's id.
	 */
	private Customer getCustomer(int id) throws ServiceException {

		Optional<Customer> opt = custRep.findById(id);
		if (opt.isEmpty())
			throw new ServiceException("A customer with this id does not exist. ");
		return opt.get();
	}

}
