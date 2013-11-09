package connection;

import java.util.LinkedList;

import javax.jws.WebService;

@WebService
public class Service {
	
	DatabaseConnection db = new DatabaseConnection();
	

	public String signInMarket(String fname,String password)
	{
		String result=null,pass;
		
		pass = db.SignIn(fname, password);
		//System.out.println(pass);
		
		if(pass!=null)
		{
			//System.out.println("inside signinmarket");
			if(pass.equals(password))
			{
				System.out.print("valid");
				result="valid";
			}
			else
			{
				result="invalid";
			}
		}
		else
		{
			System.out.println("user dont exist");
			result="user dont exist";
		}
		return result;
	}
	
	public String updatetime(String username)
	{
		String result = db.updateTime(username);
		
		return result;
	}
	
	public String signUpMarket(String fname,String lname,String email,String password)
	{
		String result;
		
		result = db.SignUp(fname, lname, email, password);
		return result;
	}
	
	public int getIdFromFirstname(String firstname)
	{
		int id=0;
		id = db.getIdFromFirstname(firstname);
		return id;
	}
	
	public String createAdvertisement(String username,String itemname,String description,int price,int quantity,String sellerinfo)
	{
		String result;
		
		 result = db.createAdvertise(username, itemname, description, price, quantity, sellerinfo);
		 
		 return result;
	}
	
	public AdvertisementBean[] getAdvertisementDetail()
	{
		AdvertisementBean[] advertisementBean = null;
		System.out.println("Inside getAdvertisementdetail");
		advertisementBean = db.getAllAdvertisementArray();
		
		return advertisementBean;
	}
	
	public AdvertisementBean[] getAdvertisementDetailLinkedList()
	{
		//int size;
		//String result="";
		LinkedList<AdvertisementBean> ls = new LinkedList<AdvertisementBean>();
		
		ls = db.getAllAdvertisement();
		AdvertisementBean[] adBean =new AdvertisementBean[ls.size()];
		ls.toArray(adBean);
		
		return adBean;
	}
	
	public int getQuantityFromAdid(int adid)
	{
		int quantity=0;
		quantity = db.getQuantityFromAdid(adid);
		return quantity;
	}

	public UserMarket getuserDetailMarket(String username)
	{
		UserMarket user = new UserMarket();
		String result=db.getUserDetailsMarket(username);
		System.out.print(result);
		String[] temp = result.split(";");
		
		user.setFirstname(temp[0]);
		user.setLastname(temp[1]);
		user.setEmail(temp[2]);
		user.setPwd(temp[3]);
		String date =temp[4] +":" + temp[5];
		//System.out.println(date);
		user.setLastlogtime(date);
		
		return user;
	}
	
	public AdvertisementBean getAdvertisementFromId(int id)
	{
		System.out.println("Inside adfromid");
		AdvertisementBean bean = new AdvertisementBean();
		
		bean = db.getAdvertisementFromId(id);
		return bean;
	}
	
	public String insertTransaction(int adid,int sellerid,int buyerid,int quantity,int total)
	{
		System.out.println("inside server inserttransaction");
		String result = db.insertTransaction(adid, sellerid, buyerid, quantity, total);
		return result;
	}
	
	public TransactionBean[] getTransaction()
	{
		TransactionBean[] transactionBean = null;
		System.out.println("Inside getAdvertisementdetail");
		transactionBean = db.getTransaction();
		
		return transactionBean;
	}
	
	public TransactionBean[] getAllBuyerTransaction(int buyerid)
	{
		TransactionBean[] transactionBeanBuyer =null;
		
		System.out.println("Inside getAllBuyerTransaction");
		transactionBeanBuyer = db.getAllBuyerTransaction(buyerid);
		
		return transactionBeanBuyer;
	}
	
	public TransactionBean[] getAllSellerTransaction(int sellerid)
	{
		TransactionBean[] transactionBeanSeller =null;
		
		System.out.println("Inside getAllSellerTransaction");
		transactionBeanSeller = db.getAllSellerTransaction(sellerid);
		
		return transactionBeanSeller;
	}
	
	public String updateQuantityAdvertisementTransaction(int quantitydeduct,int adid)
	{
		String result = db.updateQuantity(quantitydeduct, adid);
		
		return result;
	}
	
	public int deleteAdvertisement()
	{
		int noofrows=0;
		
		noofrows = db.deleteAdvertisement();
		return noofrows;
	}

	
	public User getuserDetail()
	{
		User user = new User();
		System.out.println("Inside getuserdetail");
		String result="";
		result = db.getUserdetails();
		String[] temp = result.split(":");
		
		user.username = temp[0];
		user.password = temp[1];
		
		return user;
	}
	
	public User[] getuserDetailArray()
	{
		User[] userlist =null;
		System.out.println("Inside getuserdetail");
		userlist = db.getUserdetailsString();
		
		return userlist;
	}
}
