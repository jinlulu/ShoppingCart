package onlineShop.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.entity.Authorities;
import onlineShop.entity.Customer;
import onlineShop.entity.User;

@Repository //主要用于data access
public class CustomerDao {

    @Autowired
    private SessionFactory sessionFactory; //连接数据库 把已经创建好的在config里面的sessionFactory inject进来

    public void addCustomer(Customer customer) {
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER"); //分配权限
        authorities.setEmailId(customer.getUser().getEmailId());
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(customer);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //userName <--> email
    public Customer getCustomerByUserName(String userName) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {

            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("emailId", userName)).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null)
            return user.getCustomer();
        return null;
    }
}
